package meli.ipApp.exepctions.errors;


import meli.ipApp.exepctions.AppError;

public enum IpError implements AppError {

  EXTERNAL_IP_APP_ERROR("External app error", "1"),

  ;

  private final String message;
  private final String id;

  IpError(String message, String id) {
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
