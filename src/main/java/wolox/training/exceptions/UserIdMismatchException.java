package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when user path param is different from user body id
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User Id mismatch")
public class UserIdMismatchException extends Exception {

}
