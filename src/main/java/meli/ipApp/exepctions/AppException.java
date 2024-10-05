package meli.ipApp.exepctions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class AppException extends RuntimeException {

  private final AppError error;
  private HttpStatus httpStatus;

  public AppException(AppError error, HttpStatus httpStatus) {
    super(error.getMessage());
    this.httpStatus = httpStatus;
    this.error = error;
  }

  public AppException(AppError error) {
    super(error.getMessage());
    this.error = error;
  }


  public String getCode() {
    return error.getId();
  }
}
