package meli.ipApp.clients.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoinClientImplTest {

  @Autowired
  CoinClientImpl coinClient;

  @Test
  void getCoinsInfo() {
    assertNotNull(coinClient.getCoinsInfo());
  }

  @Test
  void getSupportedSymbols() {
    assertNotNull(coinClient.getSupportedSymbols());
  }
}