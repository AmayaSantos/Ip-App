package meli.ipApp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.services.IpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IpController {

  private final IpService ipService;

  public IpController(IpService ipService) {
    this.ipService = ipService;
  }

  @Operation(summary = "Get a Hi")
  @GetMapping(path = "/saludar")
  public ResponseEntity<String> saludar() {
    return new ResponseEntity<>("hola assss", HttpStatus.OK);
  }

  @Operation(summary = "Get ip info")
  @GetMapping(path = "/ip/{ip}")
  public ResponseEntity<IpInfoDto> getIpInfo(@PathVariable String ip) {
    return new ResponseEntity<>(ipService.getIpInfo(ip), HttpStatus.OK);
  }
}
