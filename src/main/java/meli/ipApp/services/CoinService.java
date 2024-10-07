package meli.ipApp.services;

import java.util.Map;
import java.util.Set;

public interface CoinService {

  Map<String, Double> getCoinsEquivalentDollar();

  Set<String> getSupportedSymbols();

}
