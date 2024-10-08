package meli.ipApp.services.impl;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import meli.ipApp.dtos.AverageDataDto;
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
  private CountryInfoDto baseCountry;

  public StatisticServiceImpl(CountryService countryService) {
    this.countryService = countryService;
    setCountryInfo();
    logger.debug("created");
  }

  public void setCountryInfo() {
    this.statisticCountryInfoDtoMap =
        countryService.getCountriesInfo()
            .values()
            .stream()
            .map(StatisticCountryInfoDto::new)
            .collect(Collectors.toMap(StatisticCountryInfoDto::getCountryCode, s -> s));
    baseCountry = countryService.getBaseCountry();

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
    if (ipInfoDto.isOutCountry()) {
      updateOutCountryDist(ipInfoDto);
    }
    logger.info("statistic updated {}", statisticCountryInfoDtoMap.get(ipInfoDto.getCountryCode()));
  }

  private void updateOutCountryDist(IpInfoDto ipInfoDto) {
    Double distance = HaversineCalculator.haversine(baseCountry, ipInfoDto);
    StatisticCountryInfoDto statistic =
        statisticCountryInfoDtoMap.get(ipInfoDto.getCountryCode());
    statistic.updateOutCountryDistance(distance);
  }

  @Override
  public StatisticDto getAllStatistics() {
    logger.info("try to get all statistics");
    lock.lock();
    try {
      StatisticDto statisticDto = new StatisticDto(
          getUnlockNearestCountry(),
          getUnlockFurthestCountry(),
          getUnlockAverageData()
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
  public AverageDataDto getAverageData() {
    logger.info("try to get average data");
    lock.lock();
    try {
      return getUnlockAverageData();
    } catch (Exception e) {
      logger.error("Error in get average data {}", e.getMessage());
      throw e;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public StatisticCountryInfoDto getFurthestCountry() {
    logger.info("try to get furthest country");
    lock.lock();
    try {
      return getUnlockFurthestCountry();
    } catch (Exception e) {
      logger.error("Error in get furthest country {}", e.getMessage());
      throw e;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public StatisticCountryInfoDto getNearestCountry() {
    logger.info("try to get nearest country");
    lock.lock();
    try {
      return getUnlockNearestCountry();
    } catch (Exception e) {
      logger.error("Error in get nearest country {}", e.getMessage());
      throw e;
    } finally {
      lock.unlock();
    }
  }

  private AverageDataDto getUnlockAverageData() {
    AverageDataDto average = statisticCountryInfoDtoMap.values()
        .stream()
        .filter(StatisticCountryInfoDto::hasBeenCalled)
        .map(AverageDataDto::new)
        .reduce(AverageDataDto::reduce)
        .orElseThrow(
            () -> new AppException(StatisticError.WITHOUT_STATISTICS, HttpStatus.NO_CONTENT))
        .setAverage()
        .copy();
    logger.info("average dist got");
    return average;
  }

  private StatisticCountryInfoDto getUnlockFurthestCountry() {
    StatisticCountryInfoDto statisticCountryInfoDto1 = statisticCountryInfoDtoMap.values()
        .stream()
        .filter(StatisticCountryInfoDto::hasBeenCalled)
        .reduce(StatisticCountryInfoDto::getFurthest)
        .orElseThrow(
            () -> new AppException(StatisticError.WITHOUT_STATISTICS, HttpStatus.NO_CONTENT));
    logger.info("Furthest country got");
    return statisticCountryInfoDto1;
  }

  private StatisticCountryInfoDto getUnlockNearestCountry() {
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
