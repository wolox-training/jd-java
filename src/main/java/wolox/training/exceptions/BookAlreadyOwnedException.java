package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Book already owned to the user
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Book already owned")
public class BookAlreadyOwnedException extends Exception {

}
