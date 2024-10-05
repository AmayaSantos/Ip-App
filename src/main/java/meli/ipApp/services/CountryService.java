package meli.ipApp.services;

import java.util.Map;
import meli.ipApp.dtos.CountryInfoDto;

public interface CountryService {

  void updateCoins();

  CountryInfoDto getCountryInfo(String countryCode);

  Map<String, CountryInfoDto> getCountriesInfo();
}
