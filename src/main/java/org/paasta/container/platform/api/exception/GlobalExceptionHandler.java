package org.paasta.container.platform.api.exception;

import org.paasta.container.platform.api.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Iterator;

/**
 * GlobalException Handler 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.08.25
 **/
@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({HttpClientErrorException.class})
    @ResponseBody
    public ErrorMessage handleException(HttpClientErrorException ex) {
        logger.info("HttpClientErrorException >>> " + ex.getStatusText());
        return new ErrorMessage(Constants.RESULT_STATUS_FAIL, ex.getStatusText(), ex.getRawStatusCode(), ex.getResponseBodyAsString());
    }

    @ExceptionHandler({ContainerPlatformException.class})
    @ResponseBody
    public ErrorMessage handleException(ContainerPlatformException ex) {
        logger.info("ContainerPlatformException >>> " + ex.getErrorMessage());
        return new ErrorMessage(ex.getErrorCode(), ex.getErrorMessage(), ex.getStatusCode(), ex.getDetailMessage());
    }

    @ExceptionHandler({CpCommonAPIException.class})
    @ResponseBody
    public ErrorMessage handleException(CpCommonAPIException ex) {
        logger.info("CpCommonAPIException >>> " + ex.getErrorMessage());
        return new ErrorMessage(ex.getErrorCode(), ex.getErrorMessage(), ex.getStatusCode(), ex.getDetailMessage());
    }

    // 500
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex) {
        if(ex.getMessage().contains("404")) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleException(HttpMessageNotReadableException ex) {
        return this.getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String message = "Missing required fields:";

        FieldError error;
        for(Iterator var5 = result.getFieldErrors().iterator(); var5.hasNext(); message = message + " " + error.getField()) {
            error = (FieldError)var5.next();
        }

        return this.getErrorResponse(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessage> nullException(NullPointerException ex) {
        logger.info("NullPointerException >>> " + ex);
        return getErrorResponse(ex.toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IndexOutOfBoundsException.class})
    @ResponseBody
    public String indexOutOfBoundsException(IndexOutOfBoundsException ex) {
        logger.info("indexOutOfBoundsException >>> " + ex.getMessage());
        return ex.getMessage();
    }

    public ResponseEntity<ErrorMessage> getErrorResponse(String message, HttpStatus status) {
        return new ResponseEntity(new ErrorMessage(status.value(), message), status);
    }
}
