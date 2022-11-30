package com.jerry.imagemanager.common.error;

import com.jerry.imagemanager.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.jerry.imagemanager.common.error.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return createErrorResponseEntity(e, INTERNAL_SERVER_ERROR);
    }

    // 405 : Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return createErrorResponseEntity(e, METHOD_NOT_ALLOWED);
    }

    // 400 : Wrong Method Argument Type
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return createErrorResponseEntity(e, BAD_REQUEST);
    }

    // 400 : Invalid RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return createErrorResponseEntity(e, BAD_REQUEST);
    }

    // 400 : Invalid ModelAttribute
    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        return createErrorResponseEntity(e, BAD_REQUEST);
    }

    // 400 : Missing controller required param (@RequestParam)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return createErrorResponseEntity(e, BAD_REQUEST);
    }

    // 400 : Missing controller required file param (@RequestPart)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return createErrorResponseEntity(e, BAD_REQUEST);
    }

    // 400 : Thrown by HttpMessageConverter implementations when the HttpMessageConverter.read() method fails.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return createErrorResponseEntity(e, BAD_REQUEST);
    }

    // 400 : Can not find api
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return createErrorResponseEntity(e, BAD_REQUEST);
    }

//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
//
//    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(Exception e, ErrorCode errorCode) {
        if(errorCode.isSerious()) {
            log.error(e.getMessage(), e);
        }else{
            log.warn(e.getMessage(), e);
        }

        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }
}
