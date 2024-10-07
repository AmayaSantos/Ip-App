package meli.ipApp.services.impl;

import jakarta.annotation.PostConstruct;
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
import meli.ipApp.utils.HaversineCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

  private final Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);
  private final CountryService countryService;
  private final Lock lock = new ReentrantLock();
  private Map<String, StatisticCountryInfoDto> statisticCountryInfoDtoMap;


  public StatisticServiceImpl(CountryService countryService) {
    this.countryService = countryService;
    logger.debug("created");
  }

  @PostConstruct
  public void init() {
    this.statisticCountryInfoDtoMap =
        countryService.getCountriesInfo()
            .values()
            .stream()
            .map(StatisticCountryInfoDto::new)
            .collect(Collectors.toMap(StatisticCountryInfoDto::getCountryCode, s -> s));
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
    if (ipInfoDto.getCountryCode().equals(CountryInfoDto.OUT_COUNTRY().getAlpha2Code())) {
      updateOutCountryDist(ipInfoDto);
    }
    logger.info("statistic updated {}", statisticCountryInfoDtoMap.get(ipInfoDto.getCountryCode()));
  }

  private void updateOutCountryDist(IpInfoDto ipInfoDto) {
    CountryInfoDto baseCountry = countryService.getBaseCountry();
    Double distance = HaversineCalculator.haversine(baseCountry, ipInfoDto);
    StatisticCountryInfoDto statistic =
        statisticCountryInfoDtoMap.get(ipInfoDto.getCountryCode());
    statistic.setCantCalled(BigDecimal.ONE);
    statistic.setTotalDistance(statistic.getTotalDistance().add(BigDecimal.valueOf(distance)));
  }

  @Override
  public StatisticDto getAllStatistics() {
    logger.info("try to get all statistics");
    lock.lock();
    try {
      StatisticDto statisticDto = new StatisticDto(
          getNearestCountry(),
          getFurthestCountry(),
          getAverageDist()
      );
      return statisticDto;
    } catch (Exception e) {
      logger.error("Error in get statistics {}", e.getMessage());
      throw e;
    } finally {
      lock.unlock();
    }
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
