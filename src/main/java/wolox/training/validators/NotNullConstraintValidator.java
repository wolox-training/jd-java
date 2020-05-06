package wolox.training.validators;

import static wolox.training.config.annotations.NotNullConstraint.MESSAGE;

import com.google.common.base.Preconditions;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import wolox.training.annotations.NotNullConstraint;

public class NotNullConstraintValidator implements ConstraintValidator<NotNullConstraint, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Preconditions.checkNotNull(value, MESSAGE);
        return true;
    }
}
