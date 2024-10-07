package meli.ipApp.clients.impl;

import static org.junit.Assert.assertNotNull;

import meli.ipApp.mockUtils.WithOutClientsConfiguration.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class,})
class CountryClientImplTest {

  @Autowired
  private CountryClientImpl countryClient;

  @Test
  void getCountriesInfos() {
    assertNotNull(countryClient.getCountriesInfos());
  }
}