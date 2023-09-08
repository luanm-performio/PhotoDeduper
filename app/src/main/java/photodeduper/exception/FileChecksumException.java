package photodeduper.exception;

public class FileChecksumException extends RuntimeException {
    public FileChecksumException(final String message) {
        super(message);
    }
}
