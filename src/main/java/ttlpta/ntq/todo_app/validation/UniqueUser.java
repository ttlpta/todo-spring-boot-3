package ttlpta.ntq.todo_app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUserValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUser {
    String message() default "User with this {field} already exists";
    String field();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 
