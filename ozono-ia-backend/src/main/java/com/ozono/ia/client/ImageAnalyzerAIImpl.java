package com.ozono.ia.client;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozono.ia.dto.AnalysisDto;
import com.ozono.ia.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageAnalyzerAIImpl implements ImageAnalyzerAI {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api-key}")
    private String openAiApiKey;

    @Value("${openai.base-url}")
    private String OPENAI_API_URL;

    @Override
    public AnalysisDto analyze(String imageUrl) {
        try {
            HttpEntity<Map<String, Object>> request = buildRequest(imageUrl);
            ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, request, String.class);
            return parseResponse(response);

        } catch (HttpStatusCodeException httpEx) {
            log.error("Error HTTP desde OpenAI: {}", httpEx.getResponseBodyAsString(), httpEx);
            String openAiErrorMessage = extractOpenAiErrorMessage(httpEx.getResponseBodyAsString());
            throw new ServiceException(HttpStatus.valueOf(httpEx.getStatusCode().value()), openAiErrorMessage);

        } catch (ServiceException e) {
            throw e;

        } catch (Exception e) {
            log.error("Error inesperado al analizar la imagen", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno al procesar la imagen");
        }
    }

    private HttpEntity<Map<String, Object>> buildRequest(String imageUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(buildSystemMessage(), buildUserMessage(imageUrl))
        );

        return new HttpEntity<>(requestBody, headers);
    }

    private Map<String, Object> buildSystemMessage() {
        return Map.of(
                "role", "system",
                "content", """
                            Responde *únicamente* con un objeto JSON válido. Sin explicaciones adicionales,
                            sin formato markdown, solamente un objeto json plano válido, esto es para la respuesta
                            correcta y para el mensaje de error, no quiero ```json ``` en el json.
                            Eres un asistente útil que describe objetos en imágenes accesibles por URL.
                            Solamente tienes exito si es un objeto de la vida real que puede ser reciclado o no reciclado.
                            Si la imagen no es de un material reciclable o no reciclable de la vida real da el mensaje 
                            de error describiendo que es y explica porque es invalido, no mas de 100 caracateres.
                            Responde en formato JSON con las siguientes claves:
                            {
                                "material_type": "...",
                                "material_description": "...",
                                "difficulty_of_recycle": "...",
                                "disintegration_time": "...",
                                "contamination_level": "..."
                            }
                            Si no puedes analizar la imagen, responde:
                            {
                                "error": "No se pudo procesar la imagen"
                            }
                            Todas las respuestas deben estar en español.
                        """
        );
    }

    private Map<String, Object> buildUserMessage(String imageUrl) {
        return Map.of(
                "role", "user",
                "content", List.of(
                        Map.of("type", "text", "text", "Describe el objeto en la imagen con las propiedades solicitadas."),
                        Map.of("type", "image_url", "image_url", Map.of("url", imageUrl, "detail", "auto"))
                )
        );
    }

    private AnalysisDto parseResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Respuesta inválida desde OpenAI (sin cuerpo)");
        }
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String contentText = root.path("choices").get(0).path("message").path("content").asText();

            log.debug("Contenido recibido de OpenAI: {}", contentText);

            JsonNode parsedContent = objectMapper.readTree(contentText);
            if (parsedContent.has("error")) {
                String errorMsg = parsedContent.path("error").asText();
                throw new ServiceException(HttpStatus.UNPROCESSABLE_ENTITY, "Ozono AI respondió con error: " + errorMsg);
            }
            return objectMapper.treeToValue(parsedContent, AnalysisDto.class);

        } catch (ServiceException e) {
            log.error("Error de servicio al procesar respuesta de OpenAI: {}", e.getMessage());
            throw e;
        } catch (JsonParseException e) {
            log.error("Error al parsear JSON de respuesta: {}", response.getBody(), e);
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Ozono AI no pudo interpretar la respuesta");
        } catch (Exception e) {
            log.error("Error general al procesar respuesta de OpenAI", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo interpretar la respuesta de Ozono AI");
        }
    }

    private String extractOpenAiErrorMessage(String responseBody) {
        try {
            JsonNode errorNode = objectMapper.readTree(responseBody).path("error").path("message");
            if (!errorNode.isMissingNode()) {
                return "Error desde Ozono AI: " + errorNode.asText();
            }
        } catch (Exception e) {
            log.warn("No se pudo extraer mensaje de error desde OpenAI", e);
        }
        return "No se pudo procesar la imagen desde Ozono AI";
    }
}
