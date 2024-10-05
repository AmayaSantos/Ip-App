package meli.ipApp.services.impl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import meli.ipApp.dtos.AverageDistanceDto;
import meli.ipApp.dtos.CountryInfoDto;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.dtos.StatisticCountryInfoDto;
import meli.ipApp.dtos.StatisticDto;
import meli.ipApp.exepctions.AppException;
import meli.ipApp.exepctions.errors.StatisticError;
import meli.ipApp.services.CountryService;
import meli.ipApp.services.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

  private final Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);
  private final Map<String, StatisticCountryInfoDto> statisticCountryInfoDtoMap;
  private final Lock lock = new ReentrantLock();


  public StatisticServiceImpl(CountryService countryService) {
    Map<String, CountryInfoDto> countries = countryService.getCountriesInfo();

    this.statisticCountryInfoDtoMap = countries.values()
        .stream()
        .map(StatisticCountryInfoDto::new)
        .collect(Collectors.toMap(StatisticCountryInfoDto::getCountryCode, s -> s));
    logger.debug("created");
  }

  @Override
  @Async
  public void updateStatisticsWith(IpInfoDto ipInfoDto) {
    logger.info("try updateStatistic");
    lock.lock();
    try {
      unlockedUpdateStatisticsWith(ipInfoDto);
    } finally {
      lock.unlock();
    }
  }

  private void unlockedUpdateStatisticsWith(IpInfoDto ipInfoDto) {
    statisticCountryInfoDtoMap.get(ipInfoDto.getCountryCode())
        .incrementCant();
    logger.info("statistic updated");
  }

  @Override
  public StatisticDto getAllStatistics() {
    logger.info("try to get all statistics");
    return new StatisticDto(
        getNearestCountry(),
        getFurthestCountry(),
        getAverageDist()
    );
  }

  @Override
  public BigDecimal getAverageDist() {
    BigDecimal average = statisticCountryInfoDtoMap.values()
        .stream()
        .filter(StatisticCountryInfoDto::hasBeenCalled)
        .map(AverageDistanceDto::new)
        .reduce(AverageDistanceDto::reduce)
        .orElseThrow(
            () -> new AppException(StatisticError.WITHOUT_STATISTICS, HttpStatus.NO_CONTENT))
        .getAverage();
    logger.info("average dist got");
    return average;
  }

  @Override
  public StatisticCountryInfoDto getFurthestCountry() {
    StatisticCountryInfoDto statisticCountryInfoDto1 = statisticCountryInfoDtoMap.values()
        .stream()
        .filter(StatisticCountryInfoDto::hasBeenCalled)
        .reduce(StatisticCountryInfoDto::getFurthest)
        .orElseThrow(
            () -> new AppException(StatisticError.WITHOUT_STATISTICS, HttpStatus.NO_CONTENT));
    logger.info("Furthest country got");
    return statisticCountryInfoDto1;
  }

  @Override
  public StatisticCountryInfoDto getNearestCountry() {
    StatisticCountryInfoDto statisticCountryInfoDto1 = statisticCountryInfoDtoMap.values()
        .stream()
        .filter(StatisticCountryInfoDto::hasBeenCalled)
        .reduce(StatisticCountryInfoDto::getNearest)
        .orElseThrow(
            () -> new AppException(StatisticError.WITHOUT_STATISTICS, HttpStatus.NO_CONTENT));
    logger.info("Nearest country got");
    return statisticCountryInfoDto1;
  }

}
