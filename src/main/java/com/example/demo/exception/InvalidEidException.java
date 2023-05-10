package com.example.demo.exception;

public class InvalidEidException extends RuntimeException {
  public InvalidEidException() {
    super();
  }

  public InvalidEidException(String message) {
    super(message);
  }
}
