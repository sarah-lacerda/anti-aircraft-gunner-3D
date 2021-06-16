package exception;

import static java.text.MessageFormat.format;

public class FailedToLoadResourceException extends RuntimeException {

    private static final String MESSAGE = "Error loading resource {0} from file \"{1}\". {2}";

    public FailedToLoadResourceException(String resourceName, String filename, String message) {
        super(format(MESSAGE, resourceName, filename, message));
    }

    public FailedToLoadResourceException(String resourceName, String filename, String message, Throwable cause) {
        super(format(MESSAGE, resourceName, filename, message), cause);
    }

    public FailedToLoadResourceException(String resourceName, String filename, Throwable cause) {
        super(format(MESSAGE, resourceName, filename, cause));
    }
}
