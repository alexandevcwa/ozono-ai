```markdown
# Ozono IA Backend

Ozono IA Backend is a Java Spring Boot API that identifies recyclable and non-recyclable objects in photos. It supports user registration, email confirmation, and login.

## 1\. How to compile the project

Use Maven to compile:

```sh
mvn clean install
```

## 2\. How to start the project with Spring Boot

Run with Maven:

```sh
mvn spring-boot:run
```

Or with the generated JAR:

```sh
java -jar target/ozono-ai-1.0.1-beta.jar
```

### Required environment variables

Set these variables before starting:

- `DB_URL`
- `DB_USER`
- `DB_PASS`
- `MAIL_HOST`
- `MAIL_PORT`
- `MAIL_USER`
- `MAIL_PASS`
- `OPENAI_API_KEY`
- `OPENAI_ORGANIZATION`
- `OPENAI_PROYECT`
- `OPENAI_BASE_URL`
- `FTP_HOST`
- `FTP_PORT`
- `FTP_USER`
- `FTP_PASS`
- `JWT_SECRET`
- `URL_DOMAIN`

Example:

```sh
export DB_URL=jdbc:postgresql://localhost:5432/ozono
export DB_USER=youruser
export DB_PASS=yourpass
# ...and so on
```

## 3\. How to build the Docker container with Maven

### Prerequisites

- Install [Docker](https://docs.docker.com/get-docker/)
- Create a [Docker Hub](https://hub.docker.com/) account

### Build the image

```sh
mvn compile jib:dockerBuild
```

### Tag and push the image

```sh
docker tag ozono-ai:1.0.1-beta docker.io/yourdockerhubuser/ozono-ai:1.0.1-beta
docker login
docker push yourdockerhubuser/ozono-ai:1.0.1-beta
```

## 4\. Docker container environment variables

The container needs the same environment variables as the local run (see above).

## 5\. How to store environment variables in a \.env file

Create a `.env` file (do not commit it):

```
POSTGRES_DB=ozono
POSTGRES_USER=youruser
POSTGRES_PASSWORD=yourpass
FTP_USER_NAME=user
FTP_USER_PASS=user123
JWT_SECRET=yourjwtsecret
MAIL_HOST=smtp.example.com
MAIL_PORT=587
MAIL_USER=mailuser
MAIL_PASS=mailpass
OPENAI_API_KEY=yourkey
OPENAI_ORGANIZATION=yourorg
OPENAI_PROYECT=yourproject
URL_DOMAIN=https://yourdomain.com
CLOUDFLARED_TOKEN=yourtoken
```

## 6\. How to run with docker-compose

From the `docker` folder:

```sh
docker compose --env-file ../.env up
```

This will start the backend with all required services.

---

**Best practices:**

- Never commit sensitive data or `.env` files to version control.
- Use strong, unique secrets for production.
- Review and restrict Docker image permissions.
- Keep dependencies up to date.
```