package tr.edu.duzce.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AppExceptionHandler {
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {NullPointerException.class, ArithmeticException.class})
    public void handleException(HttpServletRequest request, Exception ex) {
        System.err.println(request.getRequestURL() + " istegi gerceklestirilirken bir"+
                "hata olustu. Hata mesaji: " + ex.getMessage());
    }
}