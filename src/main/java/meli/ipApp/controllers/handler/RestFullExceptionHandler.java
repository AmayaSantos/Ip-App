package meli.ipApp.controllers.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import meli.ipApp.dtos.ProcessMensage;
import meli.ipApp.exepctions.AppException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Hidden
@ControllerAdvice
public class RestFullExceptionHandler {

  public RestFullExceptionHandler() {

  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  @ResponseBody
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ProcessMensage handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, HttpServletRequest req) {

    String error = ex.getName() + " debe ser de tipo " + ex.getRequiredType().getName();
    ProcessMensage apiError = new ProcessMensage(HttpStatus.BAD_REQUEST.getReasonPhrase(), error);
    return apiError;
  }


  @ExceptionHandler({MissingServletRequestParameterException.class})
  @ResponseBody
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ProcessMensage handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
      HttpServletRequest req) {

    String error = ex.getParameterName() + " parameter is required";
    return new ProcessMensage(HttpStatus.BAD_REQUEST.getReasonPhrase(), error);

  }

  @ExceptionHandler({Exception.class, Throwable.class})
  @ResponseBody
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ProcessMensage handleInesperadas(Throwable ex, HttpServletRequest req) {

    String error = "Error in operation - " + req.toString();

    return new ProcessMensage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), error);
  }

  @ExceptionHandler({AppException.class})
  public ResponseEntity<ProcessMensage> handleConflict(AppException be, HttpServletRequest req) {

    ProcessMensage apiError = new ProcessMensage(be.getCode(), be.getMessage());

    return new ResponseEntity<>(apiError, be.getHttpStatus());
  }
}




