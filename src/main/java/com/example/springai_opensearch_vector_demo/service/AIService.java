package com.example.springai_opensearch_vector_demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    @Autowired
    VectorStore vectorStore;

    @Value("classpath:product.json")
    Resource resource;

    public void getDocuments() {
        logger.info("Loading products from {}", resource.getFilename());
        List<Document> documents = readAndPrintJsonFile();
        TextSplitter textSplitter = new TokenTextSplitter();
        documents.forEach(document -> {
            List<Document> splitDocuments = textSplitter.split(document);
            logger.info("Split document into {} parts", splitDocuments.size());
            vectorStore.add(splitDocuments);
            logger.info("Added document to vector store {}", document.toString());
        });
        logger.info("Finished loading products");
    }

    private List<Document> readAndPrintJsonFile() {
        List<Document> documents = new ArrayList<>();
        try(InputStream inputStream= resource.getInputStream()){
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            for (JsonNode node : jsonNode) {
                if(node.has("description")) {
                    System.out.println(node.get("description").toString());
                    documents.add(new Document(node.get("description").toString()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    public List<Document> searchDocuments(String query) {
        return vectorStore.similaritySearch(SearchRequest.query(query).withTopK(3).withSimilarityThreshold(0.7));
    }
}
