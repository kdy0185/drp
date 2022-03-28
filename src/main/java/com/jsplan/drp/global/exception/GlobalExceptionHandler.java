package com.jsplan.drp.global.exception;

import com.jsplan.drp.global.exception.entity.ErrorResponse;
import com.jsplan.drp.global.exception.entity.ErrorStatus;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Class : GlobalExceptionHandler
 * @Author : KDW
 * @Date : 2022-03-28
 * @Description : 예외 처리 Handler
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * <p>유효하지 않은 입력 데이터 예외 처리</p>
     *
     * @param ex (예외 정보)
     * @return ResponseEntity (응답 데이터)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        ErrorResponse response = new ErrorResponse(ErrorStatus.MESSAGE_NOT_READABLE);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * <p>데이터 검증 시 예외 처리</p>
     *
     * @param ex (예외 정보)
     * @return ResponseEntity (응답 데이터)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        ErrorResponse response = new ErrorResponse(ErrorStatus.METHOD_NOT_VALID, getAllErrors(ex));
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * <p>오류 내역 조회</p>
     *
     * @param ex (예외 정보)
     * @return Map (필드 + 메시지 형태의 오류 정보)
     */
    private Map<String, String> getAllErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
            .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return errors;
    }

    /**
     * <p>중복 키 예외 처리</p>
     *
     * @param ex (예외 정보)
     * @return ResponseEntity (응답 데이터)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
        DataIntegrityViolationException ex) {
        log.error(ex.getMessage());
        ErrorResponse response = new ErrorResponse(ErrorStatus.DUPLICATED_KEY);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * <p>통합 예외 처리</p>
     *
     * @param ex (예외 정보)
     * @return ResponseEntity (응답 데이터)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage());
        ErrorResponse response = new ErrorResponse(ErrorStatus.ROOT_EXCEPTION);
        return ResponseEntity.badRequest().body(response);
    }
}
