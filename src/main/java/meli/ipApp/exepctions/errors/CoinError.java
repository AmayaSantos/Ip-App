package meli.ipApp.exepctions.errors;


import meli.ipApp.exepctions.AppError;

public enum CoinError implements AppError {

  EXTERNAL_COIN_APP_ERROR("External coin app error", "20"),

  ;

  private final String message;
  private final String id;

  CoinError(String message, String id) {
    this.message = message;
    this.id = id;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public String getId() {
    return id;
  }
}
