package com.ozono.ia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.ozono.ia.dto.AnalysisDto;
import com.ozono.ia.services.AnalyzerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/ozono/api/v1/analyzer")
public class AnalyzerController {

    private final OpenAIClient openAIClient;

    private final ObjectMapper objectMapper;

    private final AnalyzerService analyzerService;

    //@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> analyzeImage(
            @Parameter(description = "Imagen a describir", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("image") MultipartFile image) throws IOException {


        final String promp = """
                    [ {
                        "type": "Describe el objeto con las siguientes propiedades, (tipo de material, descripcion de material,
                                dificultad de reciclaje, tiempo de desintegracion del material, nivel de conteminacion del material)
                                en la imagen en idioma español, la respuesta debe de estar con el siguiente formato json (material_type,
                                material_description,difficulty_of_recycle,disintegration_time, contamination_level)"}"
                      {
                        "type": "image_url",
                        "image_url": {
                            "url":"https://www.recoenvases.com/wp-content/uploads/2016/03/tambos-plastico-200-litros-10.png"
                            }
                      }
                    ]
                """;

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.CHATGPT_4O_LATEST)
                .addSystemMessage("""
                        Eres un asistente útil que describe imágenes en español que contienen objetos reciclables y
                        no reciclables en base a sus propiedades. Siempre retornas un JSON con la información de la imagen.
                        El JSON debe de tener la siguiente estructura:
                        {
                            "material_type": "tipo de material",
                            "material_description": "descripcion de material",
                            "difficulty_of_recycle": "dificultad de reciclaje",
                            "disintegration_time": "tiempo de desintegracion del material",
                            "contamination_level": "nivel de conteminacion del material"
                        }
                        
                        material_type = no mas de 50 caracteres
                        material_description = no mas de 250 caracteres
                        difficulty_of_recycle = no mas de 50 caracteres
                        disintegration_time = no mas de 50 caracteres
                        contamination_level = no mas de 150 caracteres
                        
                        Si no puedes describir la imagen, debes de retornar un JSON con el siguiente formato:
                        {
                            "error": "No se pudo procesar la imagen"
                        }
                        Todas las respuestas deben de estar en español.
                        Todas las respuestas inician com mayuscula.
                        """)
                .addUserMessage(promp)
                .build();

        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);

        Optional<String> optionalResponseJson = chatCompletion.choices().get(0).message()._content().asString();

        if (optionalResponseJson.isPresent()) {
            AnalysisDto analysis = objectMapper.readValue(optionalResponseJson.get(), AnalysisDto.class);

            return ResponseEntity.ok(Map.of("response", optionalResponseJson.get()));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "No se pudo procesar la imagen"));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnalysisDto> analyzeImageWithAI(
            @Parameter(description = "Image to analyze", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("image") MultipartFile image
    ) {
        return ResponseEntity.ok(analyzerService.analyzeImage(image));
    }


}
