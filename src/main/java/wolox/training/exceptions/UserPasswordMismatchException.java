package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User password mismatch
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Old password mismatch")
public class UserPasswordMismatchException extends Exception {

}
