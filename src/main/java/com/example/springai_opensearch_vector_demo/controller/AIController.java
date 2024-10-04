package com.example.springai_opensearch_vector_demo.controller;

import com.example.springai_opensearch_vector_demo.service.AIService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AIController {

    @Autowired
    AIService aiService;

    @GetMapping("/load")
    public String loadDocuments() {
        aiService.getDocuments();
        return "Documents loaded";
    }

    @GetMapping("/search")
    public List<String> searchDocuments(@RequestParam(value = "query", defaultValue = "The world is big") String query) {
        List<Document> results = aiService.searchDocuments(query);
        return results.stream()
                .map(Document::getContent)
                .collect(Collectors.toList());
    }
}
