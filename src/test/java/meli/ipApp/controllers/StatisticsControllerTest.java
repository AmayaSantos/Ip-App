package meli.ipApp.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import meli.ipApp.dtos.AverageDataDto;
import meli.ipApp.dtos.StatisticCountryInfoDto;
import meli.ipApp.dtos.StatisticDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class StatisticsControllerTest {


  @Autowired
  private StatisticsController statisticsController;

  @BeforeEach
  void setUp(@Autowired IpController ipController) {
    ipController.getIpInfo("8.8.8.8");
  }


  @Test
  void getAllStatitics() {
    ResponseEntity<StatisticDto> allStatitics = statisticsController.getAllStatitics();
    assertEquals(allStatitics.getStatusCode(), HttpStatus.OK);
    assertNotNull(allStatitics.getBody());
  }

  @Test
  void getNearestCountry() {
    ResponseEntity<StatisticCountryInfoDto> nearestCountry = statisticsController.getNearestCountry();
    assertEquals(nearestCountry.getStatusCode(), HttpStatus.OK);
    assertNotNull(nearestCountry.getBody());
  }

  @Test
  void getFurthestCountry() {
    ResponseEntity<StatisticCountryInfoDto> furthestCountry = statisticsController.getFurthestCountry();
    assertEquals(furthestCountry.getStatusCode(), HttpStatus.OK);
    assertNotNull(furthestCountry.getBody());
  }

  @Test
  void getAverageData() {
    ResponseEntity<AverageDataDto> averageData = statisticsController.getAverageData();
    assertEquals(averageData.getStatusCode(), HttpStatus.OK);
    assertNotNull(averageData.getBody());
  }
}