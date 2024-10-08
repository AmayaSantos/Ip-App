package meli.ipApp.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Map;
import meli.ipApp.clients.CoinClient;
import meli.ipApp.exepctions.AppException;
import meli.ipApp.exepctions.errors.CoinError;
import meli.ipApp.mockUtils.Mocks;
import meli.ipApp.mockUtils.WithOutClientsConfiguration.TestConfig;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class,})
class CoinServiceImplTest {

  private static final String USD = "USD";

  @Autowired
  private CoinServiceImpl coinService;

  @Autowired
  private CoinClient coinClient;

  @Test
  void getCoinsEquivalentDollar() {
    Map<String, Double> coinsEquivalentDollar = coinService.getCoinsEquivalentDollar();
    assertNotNull(coinsEquivalentDollar.size());
  }

  @Test
  void oneToOneInDollar() {
    Map<String, Double> coinsEquivalentDollar = coinService.getCoinsEquivalentDollar();
    assertEquals(coinsEquivalentDollar.get(USD), NumberUtils.DOUBLE_ONE);
  }

  @Test
  void errorWhenDontHaveUSD() {

    when(coinClient.getCoinsInfo()).thenReturn(Mocks.mockCoinsClientWithOutUSD);

    AppException exception = Assertions.assertThrows(AppException.class, () ->
        coinService.getCoinsEquivalentDollar());

    Assertions.assertEquals(CoinError.BASE_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  void haveBaseUSD() {

    when(coinClient.getCoinsInfo()).thenReturn(Mocks.mockCoinsClientBaseUSD);

    Map<String, Double> coinsEquivalentDollar = coinService.getCoinsEquivalentDollar();

    Assertions.assertEquals(coinsEquivalentDollar.get(USD), NumberUtils.DOUBLE_ONE);
  }
}