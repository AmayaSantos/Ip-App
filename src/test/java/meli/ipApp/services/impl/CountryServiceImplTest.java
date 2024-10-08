package meli.ipApp.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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
    CountryInfoDto countryInfo = countryService.getCountryInfo(Mocks.mockIpAR);
    assertEquals(countryInfo, Mocks.mockCountryAR);
  }

  @Test
  void getCountriesInfo() {
    Map<String, CountryInfoDto> countriesInfo = countryService.getCountriesInfo();
    assertEquals(countriesInfo, Mocks.mockCountriesInService);
  }

  @Test
  void getBaseCountry() {
    CountryInfoDto baseCountry = countryService.getBaseCountry();
    assertNotNull(baseCountry.getAlpha2Code());
  }


  @Test
  void getCountriesInfoNotFoundCountry() {
    CountryInfoDto countryInfo = countryService.getCountryInfo(Mocks.mockIpNotCountry);
    assertEquals(countryInfo.getAlpha2Code(), CountryInfoDto.OUT_COUNTRY().getAlpha2Code());
  }

  @Test
  void dontChangeCountriesInServiceWhenChangeCountryGot() {
    CountryInfoDto countryInfo = countryService.getCountryInfo(Mocks.mockIpAR);
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
    return countryService.getCountryInfo(Mocks.mockIpAR)
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

  @Test
  void testGetCountryInfo() {
    Map<String, CountryInfoDto> countriesInfo = countryService.getCountriesInfo();
    assertEquals(countriesInfo, Mocks.mockCountriesInService);
    assertNotNull(countriesInfo);

  }
}