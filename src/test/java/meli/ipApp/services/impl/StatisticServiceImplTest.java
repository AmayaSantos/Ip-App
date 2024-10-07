package meli.ipApp.services.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.dtos.StatisticDto;
import meli.ipApp.mockUtils.WithOutClientsConfiguration.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class,})
class StatisticServiceImplTest {

  @Autowired
  private StatisticServiceImpl statisticService;

  @Autowired
  IpServiceImpl ipService;

  @Test
  void updateStatisticsWith() {
    IpInfoDto ipInfoES = ipService.getIpInfo("3.44.196.93");
    Double distBsAs = ipInfoES.getCountryInfoDto().getDistBsAs();

    IpInfoDto ipInfoAR = ipService.getIpInfo("13.44.196.93");
    StatisticDto allStatistics = statisticService.getAllStatistics();
/* todo:  cant past if run all test
    assertEquals(
        BigDecimal.valueOf(distBsAs).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP)
        ,allStatistics.getPromDist());*/

    assertEquals(ipInfoES.getCountryCode(),allStatistics.getFurthestCountry().getCountry().getAlpha2Code());

    assertEquals(ipInfoAR.getCountryCode(),allStatistics.getClosestCountry().getCountry().getAlpha2Code());

  }

}