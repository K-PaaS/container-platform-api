package org.paasta.container.platform.api.exception;

import org.paasta.container.platform.api.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({HttpClientErrorException.class})
    @ResponseBody
    public ErrorMessage handleException(HttpClientErrorException ex) {
        LOGGER.info("HttpClientErrorException >>> " + ex.getStatusText());
        return new ErrorMessage(Constants.RESULT_STATUS_FAIL, ex.getStatusText(), ex.getRawStatusCode(), ex.getResponseBodyAsString());
    }

    @ExceptionHandler({ContainerPlatformException.class})
    @ResponseBody
    public ErrorMessage handleException(ContainerPlatformException ex) {
        LOGGER.info("ContainerPlatformException >>> " + ex.getErrorMessage());
        return new ErrorMessage(ex.getErrorCode(), ex.getErrorMessage(), ex.getStatusCode(), ex.getDetailMessage());
    }

    @ExceptionHandler({CpCommonAPIException.class})
    @ResponseBody
    public ErrorMessage handleException(CpCommonAPIException ex) {
        LOGGER.info("CpCommonAPIException >>> " + ex.getErrorMessage());
        return new ErrorMessage(ex.getErrorCode(), ex.getErrorMessage(), ex.getStatusCode(), ex.getDetailMessage());
    }


    @ExceptionHandler({Exception.class})
    public ErrorMessage handleAll(final Exception ex) {
        if(ex.getMessage().contains("404")) {
            return new ErrorMessage(Constants.RESULT_STATUS_FAIL, ex.getMessage(), HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage());
        }
        LOGGER.info("Exception >>> {}", ex.getLocalizedMessage());
        return new ErrorMessage(Constants.RESULT_STATUS_FAIL, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getLocalizedMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public ErrorMessage handleException(HttpMessageNotReadableException ex) {

        String message = "Required request body is missing";
        if(ex.getMessage().contains(message)){
            return new ErrorMessage(Constants.RESULT_STATUS_FAIL,message, HttpStatus.UNPROCESSABLE_ENTITY.value(), message);
        }
        return new ErrorMessage(Constants.RESULT_STATUS_FAIL, ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getLocalizedMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ErrorMessage handleException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String message = "Missing required fields:";

        FieldError error;
        for(Iterator var5 = result.getFieldErrors().iterator(); var5.hasNext(); message = message + " " + error.getField()) {
            error = (FieldError)var5.next();
        }
        LOGGER.info("MethodArgumentNotValidException >>> " + message);

        return new ErrorMessage(Constants.RESULT_STATUS_FAIL, message);
    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseBody
    public ErrorMessage nullException(NullPointerException ex) {
        LOGGER.info("NullPointerException >>> " + ex);
        return new ErrorMessage(Constants.RESULT_STATUS_FAIL, ex.getMessage());
    }

    @ExceptionHandler({IndexOutOfBoundsException.class})
    @ResponseBody
    public ErrorMessage indexOutOfBoundsException(IndexOutOfBoundsException ex) {
        LOGGER.info("indexOutOfBoundsException >>> " + ex.getMessage());
        return new ErrorMessage(Constants.RESULT_STATUS_FAIL, ex.getMessage());
    }

}
