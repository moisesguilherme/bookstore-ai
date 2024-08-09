package com.bookstore.ai.controller;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/bookstore")
public class BookstoreAssistantController {

    private final OpenAiChatClient chatClient;

    public BookstoreAssistantController(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }


    // Exemplo recebendo uma string
    @GetMapping("/informations")
    public String bookstoreChat(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message) {
            return chatClient.call(message);
    }


/*    // Exemplo recebendo um prompt e a resposta em JSON com info sobre a busca
    @GetMapping("/informations")
    public ChatResponse bookstoreChat(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message) {
        return chatClient.call(new Prompt(message));
    }*/

    @GetMapping("/reviews")
    public String bookstoreReview(@RequestParam(value = "book", defaultValue = "Dom Quixote") String book) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                Por favor, me forneça
                um breve resumo do livro {book}
                e também a biografia de seu autor. 
                """);

        promptTemplate.add("book", book);

        /*
        // Retorno do Result como lista
        List<Generation> results = this.chatClient.call(promptTemplate.create()).getResults();
        for (Generation result : results) {
            System.out.println(">>>" + result);
        }*/

        return this.chatClient.call(promptTemplate.create()).getResult().getOutput().getContent();
    }

    // Flux de String
    @GetMapping("/stream/informations")
    public Flux<ChatResponse> bookstoreChatStream(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message) {
        return chatClient.stream(new Prompt(message));
    }


}
