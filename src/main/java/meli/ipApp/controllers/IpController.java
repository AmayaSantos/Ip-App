package meli.ipApp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.dtos.StatisticDto;
import meli.ipApp.services.IpService;
import meli.ipApp.services.StatisticService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ip")
public class IpController {

  private final IpService ipService;
  private final StatisticService statisticService;

  public IpController(IpService ipService, StatisticService statisticService) {
    this.ipService = ipService;
    this.statisticService = statisticService;
  }

  @Operation(summary = "Get ip info")
  @GetMapping(path = "/{ip}")
  public ResponseEntity<IpInfoDto> getIpInfo(@PathVariable String ip) {
    return new ResponseEntity<>(ipService.getIpInfo(ip), HttpStatus.OK);
  }

  @Operation(summary = "Get all statistics")
  @GetMapping(path = "/allStatistics")
  public ResponseEntity<StatisticDto> getAllStatitics() {
    return new ResponseEntity<>(statisticService.getAllStatistics(), HttpStatus.OK);
  }
}
