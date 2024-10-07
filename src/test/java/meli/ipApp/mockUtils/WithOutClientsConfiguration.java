package meli.ipApp.mockUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import meli.ipApp.clients.CoinClient;
import meli.ipApp.clients.CountryClient;
import meli.ipApp.clients.IpClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class WithOutClientsConfiguration {

  @TestConfiguration
  public static class TestConfig {

    @Bean
    @Primary
    public static IpClient mockIpClient() {
      IpClient mock = mock(IpClient.class);
      return mock;
    }

    @Bean
    @Primary
    public static CountryClient mockCountryClient() {
      CountryClient mock = mock(CountryClient.class);
      when(mock.getCountriesInfos()).thenReturn(Mocks.mockCountries);
      return mock;
    }

    @Bean
    @Primary
    public static CoinClient mockCoinClient() {
      CoinClient mock = mock(CoinClient.class);
      when(mock.getCoinsInfo()).thenReturn(Mocks.mockCoins);
      return mock;
    }

  }
}
