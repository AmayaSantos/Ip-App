package meli.ipApp.clients.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IpClientImplTest {

  @Autowired
  private IpClientImpl ipClient;

  @Test
  void getIpInfo() {
    assertNotNull(ipClient.getIpInfo("8.8.8.8"));
  }
}