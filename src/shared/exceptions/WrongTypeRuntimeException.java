package shared.exceptions;

public class WrongTypeRuntimeException extends RuntimeException {

    private final Class expected;
    private final Class actual;

    public WrongTypeRuntimeException(Class expected, Class actual){
        this.actual = actual;
        this.expected = expected;
    }

    @Override
    public String getMessage() {
        return "expected class is: " + expected.toGenericString() + ", but actual class is: " + actual.toGenericString();
    }
}
