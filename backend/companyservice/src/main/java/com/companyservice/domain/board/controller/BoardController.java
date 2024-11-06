package com.companyservice.domain.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final WebClient webClient;

    @Autowired
    public BoardController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://k11a606.p.ssafy.io:4041").build();
    }

    @GetMapping
    public ResponseEntity<?> getBoard(){
        return ResponseEntity.ok("Get Request");
    }

    @PostMapping
    public ResponseEntity<?> createBoard(){
        return ResponseEntity.ok("Post Request");
    }

    @GetMapping("/external-data/get")
    public Mono<String> getExternalData() {
        return webClient.get()
                .uri("/api/get")
                .retrieve()
                .bodyToMono(String.class);
    }

    @PostMapping("/external-data/post")
    public Mono<String> postExternalData() {
        return webClient.get()
                .uri("/api/post")
                .retrieve()
                .bodyToMono(String.class);
    }

}