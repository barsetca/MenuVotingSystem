package ru.cherniak.menuvotingsystem.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.cherniak.menuvotingsystem.util.ValidationUtil;
import ru.cherniak.menuvotingsystem.util.exception.ErrorInfo;
import ru.cherniak.menuvotingsystem.util.exception.NotFoundException;
import ru.cherniak.menuvotingsystem.util.exception.OutsideTimeException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";

    //  http://stackoverflow.com/a/22358422/548473

    @ResponseStatus(value = UNPROCESSABLE_ENTITY) //422
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false,
                UNPROCESSABLE_ENTITY.value() + " - " + UNPROCESSABLE_ENTITY.getReasonPhrase());
    }

    @ResponseStatus(value = REQUESTED_RANGE_NOT_SATISFIABLE)//416
    @ExceptionHandler(OutsideTimeException.class)
    public ErrorInfo handleError(HttpServletRequest req, OutsideTimeException e) {
        return logAndGetErrorInfo(req, e, false,
                REQUESTED_RANGE_NOT_SATISFIABLE.value() + " - " + REQUESTED_RANGE_NOT_SATISFIABLE.getReasonPhrase());
    }

    @ResponseStatus(value = CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true,
                CONFLICT.value() + " - " + CONFLICT.getReasonPhrase());
    }

    @ResponseStatus(value = UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, IllegalArgumentException.class,
            HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, UNPROCESSABLE_ENTITY.value() + " - VALIDATION_ERROR");
    }

    @ResponseStatus(value = UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo illegalRequestRestDataError(HttpServletRequest req, MethodArgumentNotValidException e) {
        List<FieldError> fe = e.getBindingResult().getFieldErrors();
        String[] detail = getStrings(fe);
        return logAndGetErrorInfo(req, e, false, UNPROCESSABLE_ENTITY.value() + " - VALIDATION_ERROR", detail);
    }

    @ResponseStatus(value = UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler(BindException.class)
    public ErrorInfo illegalRequestUIDataError(HttpServletRequest req, BindException e) {
        List<FieldError> fe = e.getBindingResult().getFieldErrors();
        String[] detail = getStrings(fe);
        return logAndGetErrorInfo(req, e, false, UNPROCESSABLE_ENTITY.value() + " - VALIDATION_ERROR", detail);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, "APP_ERROR");
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, String errorType, String... detail) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }

        String rootMsg = rootCause.getLocalizedMessage();
        if (detail.length == 0) {
            if (rootMsg.toLowerCase().contains("restaurants_unique_name_idx")) {
                rootMsg = "Restaurant with this name already exists";
            }
            if (rootMsg.toLowerCase().contains("users_unique_email_idx")) {
                rootMsg = "User with this email already exists";
            }
            if (rootMsg.toLowerCase().contains("votes_unique_date_user_id_idx")) {
                rootMsg = "Vote with this date already exists";
            }
            if (rootMsg.toLowerCase().contains("dishes_unique_date_name_restaurant_id_idx")) {
                rootMsg = "Dish with this name on the date already exists";
            }

            detail = new String[]{rootMsg};
        }

        return new ErrorInfo(req.getRequestURL(), errorType, detail);
    }

    private static String[] getStrings(List<FieldError> fe) {
        String[] detail = new String[fe.size()];
        for (int i = 0; i < fe.size(); i++) {
            detail[i] = "\"" + fe.get(i).getField() + "\": " + fe.get(i).getDefaultMessage();
        }
        return detail;
    }
}
