package meli.ipApp.clients.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CountryClientImplTest {

  @Autowired
  private CountryClientImpl countryClient;

  @Test
  void getCountriesInfos() {
    assertNotNull(countryClient.getCountriesInfos());
  }
}