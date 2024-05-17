package com.echomap.cherryblossomclean.map.service;

import com.echomap.cherryblossomclean.map.entity.GarbageCan;
import com.echomap.cherryblossomclean.map.repository.GarbageCanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GarbageCanServiceTest {

  @Autowired GarbageCanService garbageCanService;

  @Autowired GarbageCanRepository garbageCanRepository;

  @Test
  @Transactional
  @Rollback(value = false)
  @DisplayName("csv파일을 DB에 저장하는 코드임")
  void saveData() throws IOException {
    // given
    String csvFilePath = "/Users/yongseopkim/Desktop/용산.csv";

    // when
    garbageCanService.saveDataFromCsv(csvFilePath);

    List<GarbageCan> garbageCans = garbageCanRepository.findAll();
    GarbageCan garbageCan = garbageCans.get(0);

    System.out.println(garbageCan);
    // then
  }

  @Test
  @Rollback(false)
  @Transactional
  @DisplayName("DB에 위도 경도값 추가하는 코드임")
  void addLatLng() {
    garbageCanService.updateLocationsWithCoordinates();
  }

  @Test
  @DisplayName("webclient 테스트")
  void testWebClient() {
      //given

      //when

      //then
  }


}
