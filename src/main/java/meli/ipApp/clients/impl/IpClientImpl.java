package meli.ipApp.clients.impl;

import meli.ipApp.clients.IpClient;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.exepctions.AppException;
import meli.ipApp.exepctions.errors.IpError;
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
public class IpClientImpl implements IpClient {

  private final static String ACCESS_KEY = "access_key";
  private final Logger logger = LoggerFactory.getLogger(IpClientImpl.class);
  private final String ipHost;
  private final String accessKey;

  public IpClientImpl(
      @Value("${external.ip.host}") String ipHost,
      @Value("${external.ip.accessKey}") String accessKey) {
    this.ipHost = ipHost;
    this.accessKey = accessKey;
    logger.debug("created");
  }

  @Override
  public IpInfoDto getIpInfo(String ip) {
    logger.info("try to get info");
    try {
      return callGet(createIpUrl(ip));
    } catch (Exception e) {
      logger.error("can`t get info");
      throw new AppException(IpError.EXTERNAL_IP_APP_ERROR, HttpStatus.FAILED_DEPENDENCY);
    }
  }

  private String createIpUrl(String ip) {
    return UriComponentsBuilder.fromUriString(ipHost)
        .path(ip)
        .queryParam(ACCESS_KEY, accessKey)
        .build().toUriString();
  }

  private IpInfoDto callGet(String apiUrl) {
    logger.info("called to get info");

    ResponseEntity<IpInfoDto> responseEntity =
        new RestTemplate().exchange(
            apiUrl,
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            IpInfoDto.class);

    if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
      return responseEntity.getBody();
    } else {
      throw new AppException(IpError.EXTERNAL_IP_APP_ERROR, HttpStatus.FAILED_DEPENDENCY);
    }
  }

}
