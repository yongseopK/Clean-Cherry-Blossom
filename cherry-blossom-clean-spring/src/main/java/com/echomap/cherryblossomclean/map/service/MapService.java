package com.echomap.cherryblossomclean.map.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class MapService {

  private void getTrashcanLocation() {

    String url = "";

    WebClient client = WebClient.create("https://api.odcloud.kr/api");
    Map<String, Object> res =
        client
            .get()
            .uri("/15038054/v1/uuid:bd7d78ba-ef6a-4865-8959-a14836e95ede")
            .retrieve()
            .bodyToMono(Map.class)
            .block();
    System.out.println(res);
  }
}
