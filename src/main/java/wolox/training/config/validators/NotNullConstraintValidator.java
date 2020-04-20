package wolox.training.config.validators;

import com.google.common.base.Preconditions;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import wolox.training.config.annotations.NotNullConstraint;

public class NotNullConstraintValidator implements ConstraintValidator<NotNullConstraint, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Preconditions.checkNotNull(value, "Value can not be null");
        return true;
    }
}
