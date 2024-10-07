package meli.ipApp.services.impl;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import meli.ipApp.clients.CountryClient;
import meli.ipApp.dtos.CountryCoinInfoDto;
import meli.ipApp.dtos.CountryInfoDto;
import meli.ipApp.exepctions.AppException;
import meli.ipApp.exepctions.errors.CountryError;
import meli.ipApp.services.CoinService;
import meli.ipApp.services.CountryService;
import meli.ipApp.utils.HaversineCalculator;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl implements CountryService {

  public static final String ARGENTINA_CODE = "AR";
  private final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);
  private final CoinService coinService;
  private final CountryClient countryClient;
  private final Lock lock = new ReentrantLock();
  private Map<String, CountryInfoDto> countryInfoMap;
  private CountryInfoDto baseCountry;

  public CountryServiceImpl(CountryClient countryClient, CoinService coinService) {
    this.coinService = coinService;
    this.countryClient = countryClient;
    logger.debug("created");
  }

  private static CountryInfoDto findBaseCountryToDist(Set<CountryInfoDto> countries, String code) {
    return countries
        .stream()
        .filter(countryInfoDto -> countryInfoDto.getAlpha2Code().equals(code))
        .findFirst()
        .orElseThrow(() -> new AppException(CountryError.COUNTRY_BASE_NOT_FOUND));
  }

  @PostConstruct
  private void setup() {
    Set<CountryInfoDto> countries = countryClient.getCountriesInfos();
    countryInfoMap = new HashMap<>(countries.size() + NumberUtils.INTEGER_ONE);
    addCountry(CountryInfoDto.OUT_COUNTRY());
    addCountriesByService(countries);
    setSupportedCoinsInCountries();
    updateCoins();
  }

  private void setSupportedCoinsInCountries() {
    Set<String> supportedSymbols = coinService.getSupportedSymbols();
    countryInfoMap.values()
        .forEach(countryInfoDto -> countryInfoDto.setSupportCoins(supportedSymbols));
  }

  private void addCountry(CountryInfoDto countryInfoDto) {
    countryInfoMap.put(countryInfoDto.getAlpha2Code(), countryInfoDto);
  }

  private void addCountriesByService(final Set<CountryInfoDto> countries) {
    setBaseCountry(countries);
    countries
        .stream()
        .peek(CountryInfoDto::setCustomLatlng)
        .peek(this::setGeoDist)
        .forEach(this::addCountry);
  }

  private void setGeoDist(CountryInfoDto countryInfoDto) {
    if (countryInfoDto.hasLocalization()) {
      countryInfoDto.setDistBsAs(this.calculateDist(countryInfoDto));
      logger.debug(
          "country {} calculated dist  {}",
          countryInfoDto.getAlpha2Code(),
          countryInfoDto.getDistBsAs());
    }
  }

  private Double calculateDist(CountryInfoDto countryInfoDto) {
    if (countryInfoDto.equals(CountryInfoDto.OUT_COUNTRY())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    CountryInfoDto countryBase = this.countryInfoMap.get(ARGENTINA_CODE);
    try {
      return HaversineCalculator.haversine(countryInfoDto, countryBase);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return NumberUtils.DOUBLE_ZERO;
  }

  @Override
  public void updateCoins() {
    Map<String, Double> coins = coinService.getCoinsEquivalentDollar();
    Map<String, CountryInfoDto> safeCopy = getSafeCopy();

    lock.lock();
    try {
      updateCoinsInCountries(coins);
      logger.info("country coins updated");
    } catch (Exception e) {
      logger.error("not update coins");
      this.countryInfoMap = safeCopy;
      throw e;
    } finally {
      lock.unlock();
    }
  }

  private Map<String, CountryInfoDto> getSafeCopy() {
    return this.countryInfoMap.values().stream()
        .map(CountryInfoDto::copy)
        .collect(Collectors.toMap(CountryInfoDto::getAlpha2Code, CountryInfoDto::copy));
  }

  private void updateCoinsInCountries(Map<String, Double> coins) {
    countryInfoMap.values()
        .forEach(countryInfoDto ->
            countryInfoDto.updateCoinsInCountry(coins));
  }

  @Override
  public CountryInfoDto getCountryInfo(String countryCode) {
    lock.lock();
    try {
      CountryInfoDto copy = countryInfoMap
          .getOrDefault(
              countryCode,
              CountryInfoDto.OUT_COUNTRY())
          .copy();

      logger.info("country copy of {}", copy.getAlpha2Code());
      return copy;

    } catch (Exception e) {
      logger.error("error al obtener contry info");
      throw new AppException(CountryError.INTERNAL_ERROR_IN_GET_COUNTRY_INFO,
          HttpStatus.INTERNAL_SERVER_ERROR);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Map<String, CountryInfoDto> getCountriesInfo() {
    logger.info("get all countries info");
    return countryInfoMap;
  }

  @Override
  public CountryInfoDto getBaseCountry() {
    return baseCountry;
  }

  private void setBaseCountry(Set<CountryInfoDto> countries) {
    baseCountry = findBaseCountryToDist(countries, ARGENTINA_CODE);
    this.addCountry(baseCountry);
    baseCountry.setCustomLatlng();
  }


}
