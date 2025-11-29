package exceptions;

public class InvalidInputException extends ReservationException {
    public InvalidInputException(String message) {
        super(message);
    }
}