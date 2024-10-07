package meli.ipApp.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import meli.ipApp.clients.CoinClient;
import meli.ipApp.dtos.CountryCoinInfoDto;
import meli.ipApp.dtos.CountryInfoDto;
import meli.ipApp.exepctions.AppException;
import meli.ipApp.exepctions.errors.CoinError;
import meli.ipApp.mockUtils.Mocks;
import meli.ipApp.mockUtils.WithOutClientsConfiguration.TestConfig;
import meli.ipApp.services.CountryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class,})
class CountryServiceImplTest {

  public static final String AR = "AR";
  public static final String NOT_COUNTRY_NAME = "NOT COUNTRY NAME";

  @Autowired
  private CoinClient coinClient;

  @Autowired
  private CountryService countryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getCountryInfo() {
    CountryInfoDto countryInfo = countryService.getCountryInfo(AR);
    assertEquals(countryInfo, Mocks.mockCountryAR);
  }

  @Test
  void getCountriesInfo() {
    Map<String, CountryInfoDto> countriesInfo = countryService.getCountriesInfo();
    assertEquals(countriesInfo, Mocks.mockCountriesInService);
  }

  @Test
  void getCountriesInfoNotFoundCountry() {
    CountryInfoDto countryInfo = countryService.getCountryInfo(NOT_COUNTRY_NAME);
    assertEquals(countryInfo, CountryInfoDto.OUT_COUNTRY());
  }

  @Test
  void getCountriesInfoNullCountry() {
    CountryInfoDto countryInfo = countryService.getCountryInfo(null);
    assertEquals(countryInfo, CountryInfoDto.OUT_COUNTRY());
  }

  @Test
  void dontChangeCountriesInServiceWhenChangeCountryGot() {
    CountryInfoDto countryInfo = countryService.getCountryInfo(AR);
    countryInfo.setTime();
    assertNotEquals(countryInfo, Mocks.mockCountryAR);
  }


  @Test
  void updateCoins() {
    CountryCoinInfoDto countryCoinInfoDto = getCountryCoinInfoAR();
    if (Mocks.mockCountryAR.getCurrencies().contains(countryCoinInfoDto)) {
      when(coinClient.getCoinsInfo()).thenReturn(Mocks.mockCoinsUpdated);
    } else {
      when(coinClient.getCoinsInfo()).thenReturn(Mocks.mockCoins);
    }

    countryService.updateCoins();
    CountryCoinInfoDto countryCoinInfoDto2 = getCountryCoinInfoAR();

    assertNotEquals(countryCoinInfoDto.getDollarEquivalent(),
        countryCoinInfoDto2.getDollarEquivalent());
  }

  private CountryCoinInfoDto getCountryCoinInfoAR() {
    return countryService.getCountryInfo(AR)
        .getCurrencies()
        .stream()
        .filter(countryCoinInfoDto -> countryCoinInfoDto.getCode().equals("ARS"))
        .findFirst().get();
  }


  @Test
  void errorWhenDontHaveUSD() {
    when(coinClient.getCoinsInfo()).thenReturn(Mocks.mockCoinsClientWithOutUSD);

    AppException exception = Assertions.assertThrows(AppException.class, () ->
        countryService.updateCoins());

    Assertions.assertEquals(CoinError.BASE_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  void errorWhenUpdate() {
    when(coinClient.getCoinsInfo()).thenReturn(Mocks.mockCoinsIncorrect);

    AppException exception = Assertions.assertThrows(AppException.class, () ->
        countryService.updateCoins());

    Assertions.assertEquals(CoinError.REQUIRED_COIN_DOLLAR_EQUIVALENT.getMessage(),
        exception.getMessage());
  }

  @Test
  void notLockInError() {
    errorWhenUpdate();
    updateCoins();
  }
}