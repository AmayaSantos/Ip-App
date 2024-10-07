package meli.ipApp.mockUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import meli.ipApp.dtos.CoinsInfoDto;
import meli.ipApp.dtos.CountryCoinInfoDto;
import meli.ipApp.dtos.CountryInfoDto;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.dtos.SymbolsDto;

public class Mocks {

  public static CountryInfoDto mockCountryAR = CountryInfoDto.builder()
      .alpha2Code("AR")
      .latlng(List.of(-34.0, -64.0))
      .languages(new HashSet<>())
      .timezones(List.of("UTC-03:00"))
      .timezonesCustom(new ArrayList<>())
      .currencies(new HashSet<>())
      .currencies(Set.of(new CountryCoinInfoDto("ARS", 1000.0)))
      .build();

  public static CountryInfoDto mockCountryES = CountryInfoDto.builder()
      .alpha2Code("ES")
      .latlng(List.of(40.0, -4.0))
      .languages(new HashSet<>())
      .timezones(List.of("UTC","UTC+01:00"))
      .timezonesCustom(new ArrayList<>())
      .currencies(new HashSet<>())
      .currencies(Set.of(new CountryCoinInfoDto("EUR", null)))
      .build();

  public static Set<CountryInfoDto> mockCountries =
      Set.of(Mocks.mockCountryAR,mockCountryES);

  public static Map<String, CountryInfoDto> mockCountriesInService =
      Map.of(
          CountryInfoDto.OUT_COUNTRY().getAlpha2Code(), CountryInfoDto.OUT_COUNTRY(),
          Mocks.mockCountryAR.getAlpha2Code(), Mocks.mockCountryAR,
          Mocks.mockCountryES.getAlpha2Code(),Mocks.mockCountryES);

  public static CoinsInfoDto mockCoins = CoinsInfoDto.builder()
      .base("EUR")
      .rates(Map.of("EUR", 1.0, "USD", 1.1, "ARS", 1000.0))
      .build();

  public static SymbolsDto mockSymbols = new SymbolsDto(
      Map.of("EUR", "Euro","USD","Dollar", "ARS","Arge"));

  public static CoinsInfoDto mockCoinsClientWithOutUSD = CoinsInfoDto.builder()
      .base("EUR")
      .rates(Map.of("EUR", 1.0, "ARS", 1000.0))
      .build();

  public static CoinsInfoDto mockCoinsUpdated = CoinsInfoDto.builder()
      .base("EUR")
      .rates(Map.of("EUR", 1.0, "USD", 2.0, "ARS", 1100.0))
      .build();

  public static CoinsInfoDto mockCoinsIncorrect = CoinsInfoDto.builder()
      .base("EUR")
      .rates(Map.of("EUR", 1.0, "USD", 2.0))
      .build();

  public static IpInfoDto mockIpSpain = IpInfoDto.builder()
      .ip("3.44.196.93")
      .countryCode("ES")
      .build();

  public static IpInfoDto mockIpAR = IpInfoDto.builder()
      .ip("13.44.196.93")
      .countryCode("AR")
      .build();
}
