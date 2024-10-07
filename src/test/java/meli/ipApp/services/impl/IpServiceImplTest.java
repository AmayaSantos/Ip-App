package meli.ipApp.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import meli.ipApp.clients.CoinClient;
import meli.ipApp.mockUtils.WithOutClientsConfiguration.TestConfig;
import meli.ipApp.services.CountryService;
import meli.ipApp.services.IpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class,})
class IpServiceImplTest {

  @Autowired
  private CoinClient coinClient;

  @Autowired
  private CountryService countryService;

  @Autowired
  private IpService service;

  @Test
  void getIpInfo() {
    assertNotNull(service.getIpInfo("3.44.196.93"));
  }
}