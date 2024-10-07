package meli.ipApp.exepctions.errors;


import meli.ipApp.exepctions.AppError;

public enum CountryError implements AppError {

  EXTERNAL_COUNTRY_APP_ERROR("External country app error", "15"),
  COUNTRY_BASE_NOT_FOUND("Base country location not found", "16"),
  INTERNAL_ERROR_IN_GET_COUNTRY_INFO("Error interno al obtener country info", "17"),

  ;

  private final String message;
  private final String id;

  CountryError(String message, String id) {
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
