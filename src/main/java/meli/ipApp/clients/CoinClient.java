package meli.ipApp.clients;

import meli.ipApp.dtos.CoinsInfoDto;
import meli.ipApp.dtos.SymbolsDto;

public interface CoinClient {

  CoinsInfoDto getCoinsInfo();

  SymbolsDto getSupportedSymbols();
}
