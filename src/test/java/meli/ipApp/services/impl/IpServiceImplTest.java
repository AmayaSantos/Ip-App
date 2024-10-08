package meli.ipApp.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import meli.ipApp.dtos.CountryInfoDto;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.mockUtils.WithOutClientsConfiguration.TestConfig;
import meli.ipApp.services.IpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class,})
class IpServiceImplTest {

  @Autowired
  private IpService service;

  @Test
  void getIpInfo() {
    assertNotNull(service.getIpInfo("3.44.196.93"));
  }

  @Test
  void getIpInfoWhenIpNotCountry() {
    IpInfoDto ipInfo = service.getIpInfo("23.44.196.93");
    assertNotNull(ipInfo.getCountryCode(), CountryInfoDto.OUT_COUNTRY().getAlpha2Code());
  }
}