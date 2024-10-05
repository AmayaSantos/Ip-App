package meli.ipApp.clients;

import java.util.Set;
import meli.ipApp.dtos.CountryInfoDto;

public interface CountryClient {

  Set<CountryInfoDto> getCountriesInfos();
}
