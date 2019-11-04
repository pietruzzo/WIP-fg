package shared.exceptions;

public class InvalidOperationChain extends RuntimeException {

    public InvalidOperationChain(String message) {
        super(message);
    }
}
