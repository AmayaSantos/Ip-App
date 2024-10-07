package meli.ipApp.crons;

import meli.ipApp.services.impl.CountryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
  private final Logger logger = LoggerFactory.getLogger(Scheduler.class);

  @Autowired
  private CountryServiceImpl countryService;

  @Scheduled(fixedRate = 1000/*seg*/*60/*min*/*60/*hrs*/)
  public void reportCurrentTime() {
    countryService.updateCoins();
  }
}
