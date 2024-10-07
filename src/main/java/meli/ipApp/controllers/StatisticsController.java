package meli.ipApp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import meli.ipApp.dtos.AverageDataDto;
import meli.ipApp.dtos.StatisticCountryInfoDto;
import meli.ipApp.dtos.StatisticDto;
import meli.ipApp.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

  @Autowired
  private StatisticService statisticService;

  @GetMapping
  @Operation(summary = "Get all statistics")
  public ResponseEntity<StatisticDto> getAllStatitics() {
    return new ResponseEntity<>(statisticService.getAllStatistics(), HttpStatus.OK);
  }

  @GetMapping("/nearest")
  @Operation(summary = "Get nearest country")
  public ResponseEntity<StatisticCountryInfoDto> getNearestCountry() {
    return new ResponseEntity<>(statisticService.getNearestCountry(), HttpStatus.OK);
  }

  @GetMapping("/furthest")
  @Operation(summary = "Get furthest country")
  public ResponseEntity<StatisticCountryInfoDto> getFurthestCountry() {
    return new ResponseEntity<>(statisticService.getFurthestCountry(), HttpStatus.OK);
  }

  @GetMapping("/average")
  @Operation(summary = "Get average Data")
  public ResponseEntity<AverageDataDto> getAverageData() {
    return new ResponseEntity<>(statisticService.getAverageData(), HttpStatus.OK);
  }


}
