package meli.ipApp.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import meli.ipApp.dtos.IpInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class IpControllerTest {

  @Autowired
  private IpController ipController;

  @Test
  public void testGetIpInfo() throws Exception {

    ResponseEntity<IpInfoDto> ipInfo = ipController.getIpInfo("8.8.8.8");

    assertEquals(ipInfo.getStatusCode(), HttpStatus.OK);
    assertNotNull(ipInfo.getBody());
  }
}