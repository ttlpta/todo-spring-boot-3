package ttlpta.ntq.user.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public ApplicationException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }
} 
