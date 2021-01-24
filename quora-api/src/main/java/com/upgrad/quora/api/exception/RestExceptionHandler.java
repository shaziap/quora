package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(SignUpRestrictedException.class)
  public ResponseEntity<ErrorResponse> signUpRestrictionException(SignUpRestrictedException exception, WebRequest request) {
    return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()).rootCause(getClassName(exception.toString())),
            HttpStatus.CONFLICT);
  }

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ErrorResponse> authenticationFailedException(
          AuthenticationFailedException exception, WebRequest request) {
    return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()).rootCause(getClassName(exception.toString())),
            HttpStatus.UNAUTHORIZED);
  }


  /**
   * This method will extract class name from the exception string to avoid exposing entire package name
   * in rest api response
   *
   * @param classname
   * @return
   */
  private String getClassName(String classname){
    String[] path = classname.split("[.]");
    return path[path.length-1];
  }
}
