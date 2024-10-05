package meli.ipApp.services.impl;

import java.util.HashMap;
import java.util.Map;
import meli.ipApp.clients.CoinClient;
import meli.ipApp.dtos.CoinsInfoDto;
import meli.ipApp.services.CoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CoinServiceImpl implements CoinService {

  public static final String USD = "USD";
  private final Logger logger = LoggerFactory.getLogger(CoinServiceImpl.class);
  private final CoinClient coinClient;

  public CoinServiceImpl(CoinClient coinClient) {
    this.coinClient = coinClient;
    logger.debug("created");
  }

  @Override
  public Map<String, Double> getCoinsEquivalentDollar() {

    CoinsInfoDto coinsInfo = coinClient.getCoinsInfo();

    if (coinsInfo.getBase().equals(USD)) {
      return coinsInfo.getRates();
    }

    //estas acciones es por q mi cuenta free no puedo tener la base en dolar
    Map<String, Double> coinsOtherBase = coinsInfo.getRates();
    Double usdDivBase = coinsOtherBase.get(USD);
    Map<String, Double> coins = new HashMap<>(coinsOtherBase.size());
    coinsOtherBase.forEach((s, value) ->
        coins.put(s, usdDivBase / value));
    logger.debug("coins in USD base");
    return coins;
  }
}
