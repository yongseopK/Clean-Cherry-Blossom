package com.echomap.cherryblossomclean.map.service;

import com.echomap.cherryblossomclean.map.entity.GarbageCan;
import com.echomap.cherryblossomclean.map.repository.GarbageCanRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GarbageCanService {
  private final GarbageCanRepository garbageCanRepository;
  private final WebClient webClient;

  @Value("${naver-map.client-id}")
  private String mapClientId;

  @Value("${naver-map.secret-id}")
  private String mapSecretId;

  @Value("${naver-search.client-id}")
  private String searchClientId;

  @Value("${naver-search.secret-id}")
  private String searchSecretId;

  @Transactional
  public void saveDataFromCsv(String csvFilePath) throws IOException {
    try (CSVReader reader =
        new CSVReaderBuilder(new FileReader(csvFilePath, Charset.forName("EUC-KR")))
            .withSkipLines(1) // 헤더 행 건너뛰기
            .build()) {
      String[] line;
      while ((line = reader.readNext()) != null) {
        GarbageCan garbageCan = new GarbageCan();
        garbageCan.setDistrict(line[2]);
        garbageCan.setLoadName(line[4]);
        garbageCan.setLocation(line[5]);
        garbageCan.setPoint(line[5]);
        garbageCan.setType(line[3]);

        garbageCanRepository.save(garbageCan);
      }
    } catch (CsvValidationException e) {

      throw new RuntimeException(e);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public List<GarbageCan> getGarbageCanLocations(String district) {
    List<GarbageCan> garbageCans =
        switch (district) {
          case "trash-can-gwanak" -> garbageCanRepository.findByDistrict("관악구");
          case "trash-can-dongjak" -> garbageCanRepository.findByDistrict("동작구");
          case "trash-can-yeongdeungpo" -> garbageCanRepository.findByDistrict("영등포구");
          case "trash-can-yongsan" -> garbageCanRepository.findByDistrict("용산구");
          default -> throw new IllegalArgumentException("Invalid district: " + district);
        };
    return garbageCans;
  }

  public void updateLocationsWithCoordinates() {
    List<GarbageCan> garbageCans = garbageCanRepository.findAll();

    for (GarbageCan garbageCan : garbageCans) {
      if (!garbageCan.getDistrict().equals("용산구")) {
        continue;
      }

      String address = "";

        address = garbageCan.getLoadName() ;
                //+ " " + garbageCan.getLocation();
        System.out.println("address : " + address);

        try {
        String finalAddress = address;
        String responseBody;

        if (garbageCan.getDistrict().equals("용산구")) {
          // Naver Map Geocoding API 요청
          responseBody =
              webClient
                  .get()
                  .uri(
                      builder ->
                          builder
                              .scheme("https")
                              .host("naveropenapi.apigw.ntruss.com")
                              .path("/map-geocode/v2/geocode")
                              .queryParam("query", finalAddress)
                              .build())
                  .header("X-NCP-APIGW-API-KEY-ID", mapClientId)
                  .header("X-NCP-APIGW-API-KEY", mapSecretId)
                  .retrieve()
                  .bodyToMono(String.class)
                  .block();
        } else {
          // Naver Search API 요청
          responseBody =
              webClient
                  .get()
                  .uri(
                      builder -> {
                        String encodedAddress =
                            Base64.getUrlEncoder().encodeToString(finalAddress.getBytes());
                        return builder
                            .scheme("https")
                            .host("openapi.naver.com")
                            .path("/v1/search/local.json")
                            .queryParam("query", encodedAddress)
                            .queryParam("display", 1)
                            .build();
                      })
                  .header("X-Naver-Client-Id", searchClientId)
                  .header("X-Naver-Client-Secret", searchSecretId)
                  .retrieve()
                  .bodyToMono(String.class)
                  .block();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);

        // 응답 데이터에서 위도, 경도 추출
        JsonNode itemNode;
        if (garbageCan.getDistrict().equals("용산구")) {
          itemNode = rootNode.path("addresses").get(0);
        } else {
          itemNode = rootNode.path("items").get(0);
        }

        if (itemNode != null) {
          double latitude =
              Double.parseDouble(
                  itemNode.path(garbageCan.getDistrict().equals("용산구") ? "y" : "mapy").asText());
          double longitude =
              Double.parseDouble(
                  itemNode.path(garbageCan.getDistrict().equals("용산구") ? "x" : "mapx").asText());

          garbageCan.setLatitude(latitude);
          garbageCan.setLongitude(longitude);

          log.info("Updated coordinates for GarbageCan: {}", garbageCan);
        } else {
          log.warn("No Coordinates found for GarbageCan: {}", garbageCan);
        }

        Thread.sleep(50); // 50밀리초 대기
      } catch (JsonProcessingException | InterruptedException e) {
        log.error("Error updating coordinates for GarbageCan: {}", garbageCan, e);
      }
    }

    garbageCanRepository.saveAll(garbageCans);
  }
}
