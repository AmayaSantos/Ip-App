package meli.ipApp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.dtos.StatisticDto;
import meli.ipApp.services.IpService;
import meli.ipApp.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ip")
public class IpController {

  @Autowired
  private IpService ipService;

  @Autowired
  private StatisticService statisticService;

  @GetMapping(path = "{ip}")
  @Operation(summary = "Get ip info")
  public ResponseEntity<IpInfoDto> getIpInfo(@PathVariable String ip) {
    return new ResponseEntity<>(ipService.getIpInfo(ip), HttpStatus.OK);
  }

  @GetMapping(path = "/allStatistics")
  @Operation(summary = "Get all statistics")
  public ResponseEntity<StatisticDto> getAllStatitics() {
    return new ResponseEntity<>(statisticService.getAllStatistics(), HttpStatus.OK);
  }
}
