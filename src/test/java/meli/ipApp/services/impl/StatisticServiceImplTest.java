package meli.ipApp.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import meli.ipApp.dtos.AverageDataDto;
import meli.ipApp.dtos.IpInfoDto;
import meli.ipApp.dtos.StatisticCountryInfoDto;
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
  private IpServiceImpl ipService;

  @Test
  void updateStatisticsWith() {
    IpInfoDto ipInfoAR = ipService.getIpInfo("13.44.196.93");
    StatisticDto initialStatistics = statisticService.getAllStatistics();

    statisticService.updateStatisticsWith(ipInfoAR);
    StatisticDto finalStatistics = statisticService.getAllStatistics();

    assertNotEquals(initialStatistics, finalStatistics);

  }

  @Test
  void getAverageData() {

    ipService.getIpInfo("13.44.196.93");
    AverageDataDto average = statisticService.getAverageData();

    assertNotNull(average);
    assertEquals(average.getTotalDist().divide(average.getTotalCalls(), 0, RoundingMode.HALF_UP),
        average.getAverage());
  }

  @Test
  void getAverageDataUpdate() {

    IpInfoDto ipInfoAR = ipService.getIpInfo("13.44.196.93");
    AverageDataDto average = statisticService.getAverageData();

    ipService.getIpInfo("13.44.196.93");
    AverageDataDto averageUpdated = statisticService.getAverageData();

    BigDecimal newTotalDist = average.getTotalDist()
        .add(BigDecimal.valueOf(ipInfoAR.getCountryInfoDto().getDistBsAs()));
    BigDecimal newTotalCalls = average.getTotalCalls().add(BigDecimal.ONE);
    BigDecimal newAverage = newTotalDist.divide(newTotalCalls, 0, RoundingMode.HALF_UP);

    assertNotNull(average);
    assertEquals(averageUpdated.getAverage(), newAverage);
    assertEquals(averageUpdated.getTotalDist(), newTotalDist);
    assertEquals(averageUpdated.getTotalCalls(), newTotalCalls);
  }


  @Test
  void getFurthestCountry() {
    IpInfoDto ipInfoES = ipService.getIpInfo("3.44.196.93");
    StatisticCountryInfoDto furthestCountry = statisticService.getFurthestCountry();

    assertNotNull(furthestCountry);
    assertEquals(ipInfoES.getCountryCode(), furthestCountry.getCountryCode());
  }

  @Test
  void getNearestCountry() {
    IpInfoDto ipInfoAR = ipService.getIpInfo("13.44.196.93");
    StatisticCountryInfoDto nearestCountry = statisticService.getNearestCountry();

    assertNotNull(nearestCountry);
    assertEquals(ipInfoAR.getCountryCode(), nearestCountry.getCountryCode());

  }

  @Test
  void nearestCountryNotEqualsFurthestCountry() {
    IpInfoDto near = ipService.getIpInfo("13.44.196.93");
    StatisticCountryInfoDto nearestCountry = statisticService.getNearestCountry();

    IpInfoDto furtest = ipService.getIpInfo("3.44.196.93");
    StatisticCountryInfoDto furthestCountry = statisticService.getFurthestCountry();

    assertTrue(furthestCountry.getDistBsAs().compareTo(nearestCountry.getDistBsAs()) > 0);
    assertTrue(
        furtest.getCountryInfoDto().getDistBsAs().compareTo(near.getCountryInfoDto().getDistBsAs())
            > 0);
  }

  @Test
  void updateStatisticsWithIp() {
    AverageDataDto oldAverage = statisticService.getAverageData();

    IpInfoDto near = ipService.getIpInfo("13.44.196.93");
    statisticService.updateStatisticsWith(near);

    AverageDataDto newAverage = statisticService.getAverageData();

    assertNotEquals(oldAverage, newAverage);
  }

  @Test
  void updateStatisticsWithIpWithoutCountry() {
    assertNotNull(ipService.getIpInfo("23.44.196.93"));
  }
}