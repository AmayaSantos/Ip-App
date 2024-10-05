package meli.ipApp.services;

import java.math.BigDecimal;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.dtos.StatisticCountryInfoDto;
import meli.ipApp.dtos.StatisticDto;

public interface StatisticService {

  void updateStatisticsWith(IpInfoDto ipInfoDto);

  StatisticDto getAllStatistics();

  BigDecimal getAverageDist();

  StatisticCountryInfoDto getFurthestCountry();

  StatisticCountryInfoDto getNearestCountry();
}
