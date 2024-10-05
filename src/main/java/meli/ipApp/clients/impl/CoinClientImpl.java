package meli.ipApp.clients.impl;

import meli.ipApp.clients.CoinClient;
import meli.ipApp.dtos.CoinsInfoDto;
import meli.ipApp.exepctions.AppException;
import meli.ipApp.exepctions.errors.CoinError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CoinClientImpl implements CoinClient {

  private final static String ACCESS_KEY = "access_key";
  private final Logger logger = LoggerFactory.getLogger(CoinClientImpl.class);
  private final String coinHost;
  private final String accessKey;

  public CoinClientImpl(
      @Value("${external.coin.host}") String coinHost,
      @Value("${external.coin.accessKey}") String accessKey) {
    this.coinHost = coinHost;
    this.accessKey = accessKey;
    logger.debug("created");
  }

  @Override
  public CoinsInfoDto getCoinsInfo() {
    logger.info("try to get info");
    try {
      return callGet(createCoinUrl());
    } catch (Exception e) {
      logger.error("can`t get info");
      throw new AppException(CoinError.EXTERNAL_COIN_APP_ERROR, HttpStatus.FAILED_DEPENDENCY);
    }
  }

  private String createCoinUrl() {
    return UriComponentsBuilder.fromUriString(coinHost)
        .queryParam(ACCESS_KEY, accessKey)
        .build().toUriString();
  }

  private CoinsInfoDto callGet(String apiUrl) {
    logger.info("called to get info");

    ResponseEntity<CoinsInfoDto> responseEntity =
        new RestTemplate().exchange(
            apiUrl,
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            CoinsInfoDto.class);

    if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
      return responseEntity.getBody();
    } else {
      throw new AppException(CoinError.EXTERNAL_COIN_APP_ERROR, HttpStatus.FAILED_DEPENDENCY);
    }
  }

}
