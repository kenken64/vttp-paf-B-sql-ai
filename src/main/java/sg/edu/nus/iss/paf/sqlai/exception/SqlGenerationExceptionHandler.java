package sg.edu.nus.iss.paf.sqlai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SqlGenerationExceptionHandler {
    
    @ExceptionHandler(SqlGenerationException.class)
    public ProblemDetail handle(SqlGenerationException e) {
        return ProblemDetail
            .forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, e.getMessage());
    }
}
