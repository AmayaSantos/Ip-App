package meli.ipApp.services;

import meli.ipApp.dtos.AverageDataDto;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.dtos.StatisticCountryInfoDto;
import meli.ipApp.dtos.StatisticDto;

public interface StatisticService {

  void updateStatisticsWith(IpInfoDto ipInfoDto);

  StatisticDto getAllStatistics();

  AverageDataDto getAverageData();

  StatisticCountryInfoDto getFurthestCountry();

  StatisticCountryInfoDto getNearestCountry();
}
