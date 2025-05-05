package ttlpta.ntq.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ttlpta.ntq.user.exception.ApplicationException;
import ttlpta.ntq.user.repository.UserRepository;
import org.springframework.http.HttpStatus;

public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {

    @Autowired
    private UserRepository userRepository;

    private String field;

    @Override
    public void initialize(UniqueUser constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean exists;
        if ("email".equals(field)) {
            exists = userRepository.existsByEmail(value);
        } else if ("username".equals(field)) {
            exists = userRepository.existsByUsername(value);
        } else {
            return true;
        }

        if (exists) {
            throw new ApplicationException(
                context.getDefaultConstraintMessageTemplate().replace("{field}", field),
                HttpStatus.BAD_REQUEST,
                "1001"
            );
        }
        return true;
    }
} 
