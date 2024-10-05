package meli.ipApp.exepctions.errors;


import meli.ipApp.exepctions.AppError;

public enum StatisticError implements AppError {

  WITHOUT_STATISTICS("Sin Estadisticas ", "5"),

  ;

  private final String message;
  private final String id;

  StatisticError(String message, String id) {
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
