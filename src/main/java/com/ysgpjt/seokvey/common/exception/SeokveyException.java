package com.ysgpjt.seokvey.common.exception;

import com.ysgpjt.seokvey.type.ErrorType;
import lombok.Getter;

@Getter
public class SeokveyException extends RuntimeException {

  private final ErrorType errorType;

  public SeokveyException(ErrorType errorType) {
      super(errorType.getMessage());
      this.errorType = errorType;
  }
}
