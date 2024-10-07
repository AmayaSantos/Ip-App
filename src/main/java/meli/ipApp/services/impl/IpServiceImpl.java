package meli.ipApp.services.impl;

import meli.ipApp.clients.IpClient;
import meli.ipApp.dtos.CountryInfoDto;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.services.CountryService;
import meli.ipApp.services.IpService;
import meli.ipApp.services.StatisticService;
import org.springframework.stereotype.Service;

@Service
public class IpServiceImpl implements IpService {

  private final IpClient ipClient;
  private final CountryService countryService;
  private final StatisticService statisticService;

  public IpServiceImpl(IpClient ipClient,
      CountryService countryService,
      StatisticService statisticService) {
    this.ipClient = ipClient;
    this.countryService = countryService;
    this.statisticService = statisticService;
  }

  @Override
  public IpInfoDto getIpInfo(String ip) {
    IpInfoDto ipInfo = ipClient.getIpInfo(ip);
    ipInfo.setCountryInfoDto(countryService.getCountryInfo(ipInfo.getCountryCode()));

    if (ipInfo.isOutCountry())
      ipInfo.setOutCountryCode();

    ipInfo.setTime();
    statisticService.updateStatisticsWith(ipInfo);
    return ipInfo;
  }
}
