package meli.ipApp.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import meli.ipApp.clients.CountryClient;
import meli.ipApp.dtos.CountryInfoDto;
import meli.ipApp.exepctions.AppException;
import meli.ipApp.exepctions.errors.CountryError;
import meli.ipApp.services.CoinService;
import meli.ipApp.services.CountryService;
import meli.ipApp.utils.HaversineCalculator;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl implements CountryService {

  public static final String ARGENTINA_CODE = "AR";
  private final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);
  private final Map<String, CountryInfoDto> countryInfoMap;
  private final CoinService coinService;
  private final Lock lock = new ReentrantLock();

  public CountryServiceImpl(CountryClient countryClient, CoinService coinService) {
    this.coinService = coinService;
    Set<CountryInfoDto> countries = countryClient.getCountriesInfos();
    countryInfoMap= new HashMap<>(countries.size()+NumberUtils.INTEGER_ONE);

    addCountry(CountryInfoDto.OUT_COUNTRY());
    addCountriesByService(countries);
    updateCoins();

    logger.debug("created");
  }

  private void addCountry(CountryInfoDto countryInfoDto) {
    countryInfoMap.put(countryInfoDto.getAlpha2Code(),countryInfoDto);
  }

  private void addCountriesByService(final Set<CountryInfoDto> countries) {
    setBaseCountry(findBaseCountryToDist(countries,ARGENTINA_CODE));
    countries
        .stream()
        .peek(CountryInfoDto::setCustomLatlng)
        .peek(this::setGeoDist)
        .forEach(this::addCountry);
  }

  private static CountryInfoDto findBaseCountryToDist(Set<CountryInfoDto> countries, String code) {
    return countries
        .stream()
        .filter(countryInfoDto -> countryInfoDto.getAlpha2Code().equals(code))
        .findFirst()
        .orElseThrow(() -> new AppException(CountryError.COUNTRY_BASE_NOT_FOUND));
  }

  private void setBaseCountry(CountryInfoDto countryBase) {
    this.addCountry(countryBase);
    countryInfoMap.get(ARGENTINA_CODE).setCustomLatlng();
  }

  private void setGeoDist(CountryInfoDto countryInfoDto) {
    if (countryInfoDto.hasLocalization()){
      countryInfoDto.setDistBsAs(this.calculateDist(countryInfoDto));
      logger.debug(
          "country {} calculated dist  {}",
          countryInfoDto.getAlpha2Code(),
          countryInfoDto.getDistBsAs());
    }
  }

  private Double calculateDist(CountryInfoDto countryInfoDto) {
    if (countryInfoDto.equals(CountryInfoDto.OUT_COUNTRY()))
      return NumberUtils.DOUBLE_ZERO;
    CountryInfoDto countryBase = this.countryInfoMap.get(ARGENTINA_CODE);
    try {
      return HaversineCalculator.haversine(
          countryInfoDto.getLatitude(),
          countryInfoDto.getLongitude(),
          countryBase.getLatitude(),
          countryBase.getLongitude()
      );
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return NumberUtils.DOUBLE_ZERO;
  }

  @Override
  public void updateCoins() {
    Map<String, Double> coins = coinService.getCoinsEquivalentDollar();
    lock.lock();
    try {
      updateCoinsInCountries(coins);
      logger.info("country coins updated");
    } finally {
      lock.unlock();
    }
  }

  private void updateCoinsInCountries(Map<String, Double> coins) {
    countryInfoMap.values()
        .forEach(countryInfoDto -> countryInfoDto.updateCoinsInCountry(coins));
  }


  @Override
  public CountryInfoDto getCountryInfo(String countryCode) {
    lock.lock();
    try {
      CountryInfoDto copy = countryInfoMap
          .getOrDefault(
              countryCode,
              CountryInfoDto.OUT_COUNTRY())
          .toBuilder().build();
      logger.info("country copy of {}", copy.getAlpha2Code());
      return copy;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Map<String, CountryInfoDto> getCountriesInfo() {
    logger.info("get all countries info");
    return countryInfoMap;
  }
}
