# SpringAI OpenSearch Vector Demo

This project demonstrates the integration of OpenSearch, a powerful search engine with vector search capabilities, with Spring Boot to build a vector store service. The application provides endpoints for loading documents into the OpenSearch vector store and searching for similar documents using vector embeddings.

## Prerequisites

Before running the project, ensure you have the following installed:

- **Java 21**: Required to build and run the Spring Boot application.
- **Maven**: Used to build the project.
- **Docker**: Required to run OpenSearch in a container.
- **Spring Boot**: A Java-based framework to create microservices.
- **Spring AI**: Used for managing AI interactions and embeddings.
- **OpenSearch**: Vector store for storing and querying vectorized data.

## Project Setup

### 1. Clone the Repository

Clone the repository from GitHub:

```bash
git clone https://github.com/LegPro/springai-opensearch-vector-demo.git
cd springai-opensearch-vector-demo
```

### 2. Configure OpenAI API Key

This project uses OpenAI embeddings for vector generation. Ensure that you have an OpenAI API key and set it in the `application.properties` file:

```properties
spring.ai.openai.api-key=your-openai-api-key
```

Alternatively, you can set the API key as an environment variable:

```bash
export spring.ai.openai.api-key=your-openai-api-key
```

### 3. Setup OpenSearch with Docker

Use the following Docker command to run OpenSearch:

```bash
docker run -it -p 9200:9200 -p 9600:9600 \
-e "discovery.type=single-node" \
-e "OPENSEARCH_INITIAL_ADMIN_PASSWORD=strongPass123#" \
opensearchproject/opensearch:latest
```

### 4. Verify OpenSearch is Running

Once OpenSearch is running, you can verify it by running:

```bash
curl https://localhost:9200 -ku admin:strongPass123#
```

### 5. Install Certificates in Your JDK Folder

If you encounter SSL certificate issues, you may need to install the certificates in your JDK.

1. Create a temporary folder, e.g., `C:\temp`
2. Use the following command in Git Bash to create the certificate:

```bash
openssl s_client -showcerts -connect localhost:9200 </dev/null | sed -n -e '/-.BEGIN/,/-.END/ p' > certifs.cer
```

3. Add the certificate into your JDK:

Navigate to your JDK's `bin` folder and run the following command as administrator:

```bash
keytool -import -alias opensearchcert -file "C:\temp\certifs.cer" -keystore "pathToJDK\lib\security\cacerts"
```

## Endpoints

### 1. Load Documents

Load vectorized documents into the OpenSearch vector store.

```http
GET http://localhost:8080/load
```

### 2. Search Documents

Search for similar documents based on a vector query.

```http
GET http://localhost:8080/search?query=<your-search-query>
```