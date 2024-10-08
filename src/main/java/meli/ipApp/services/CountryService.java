package meli.ipApp.services;

import java.util.Map;
import meli.ipApp.dtos.CountryInfoDto;
import meli.ipApp.dtos.IpInfoDto;

public interface CountryService {

  CountryInfoDto getBaseCountry();

  void updateCoins();

  CountryInfoDto getCountryInfo(IpInfoDto countryCode);

  Map<String, CountryInfoDto> getCountriesInfo();
}
