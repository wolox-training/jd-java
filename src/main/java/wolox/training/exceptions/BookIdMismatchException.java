package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when book path param is different from book body id
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Book Id mismatch")
public class BookIdMismatchException extends Exception {

}
