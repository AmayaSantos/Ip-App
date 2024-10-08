package meli.ipApp.clients.impl;

import java.util.Set;
import meli.ipApp.clients.CountryClient;
import meli.ipApp.dtos.CountryInfoDto;
import meli.ipApp.exepctions.AppException;
import meli.ipApp.exepctions.errors.CountryError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CountryClientImpl implements CountryClient {

  private final static String ACCESS_KEY = "access_key";
  private final Logger logger = LoggerFactory.getLogger(CountryClientImpl.class);
  private final String countryHost;
  private final String accessKey;

  public CountryClientImpl(
      @Value("${external.country.host}") String countryHost,
      @Value("${external.country.accessKey}") String accessKey) {
    this.countryHost = countryHost;
    this.accessKey = accessKey;

    logger.debug("created");
  }

  @Override
  public Set<CountryInfoDto> getCountriesInfos() {
    logger.info("try to get info");
    try {
      return callGet(createCountryUrl());
    } catch (Exception e) {
      logger.error("can`t get info");
      throw new AppException(CountryError.EXTERNAL_COUNTRY_APP_ERROR, HttpStatus.FAILED_DEPENDENCY);
    }
  }

  private String createCountryUrl() {
    return UriComponentsBuilder.fromUriString(countryHost)
        .queryParam(ACCESS_KEY, accessKey)
        .build().toUriString();
  }

  private Set<CountryInfoDto> callGet(String apiUrl) {
    logger.info("called get info");
    ResponseEntity<Set<CountryInfoDto>> responseEntity =
        new RestTemplate().exchange(
            apiUrl,
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            new ParameterizedTypeReference<Set<CountryInfoDto>>() {
            });

     if (HttpStatus.OK.equals(responseEntity.getStatusCode()) &&
          responseEntity.hasBody() && !responseEntity.getBody().isEmpty()) {
       return responseEntity.getBody();
    } else {
      throw new AppException(CountryError.EXTERNAL_COUNTRY_APP_ERROR, HttpStatus.FAILED_DEPENDENCY);
    }
  }

}
