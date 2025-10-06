package com.ysgpjt.seokvey.common.exception;

import com.ysgpjt.seokvey.type.ErrorType;
import lombok.Builder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

@Builder
public class SeokveyErrorResponse implements ErrorResponse {

    private ErrorType errorType;


    @Override
    public HttpStatusCode getStatusCode() {
        return errorType.getHttpStatus();
    }

    @Override
    public ProblemDetail getBody() {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(errorType.getHttpStatus(), errorType.getMessage());
        pd.setTitle(errorType.name());
        return pd;
    }
}
