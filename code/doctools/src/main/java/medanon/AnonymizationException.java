package medanon;

public class AnonymizationException extends Exception {

	public AnonymizationException(Throwable e) {
		super(e);
	}

	public AnonymizationException(String message, Throwable cause) {
		super(message, cause);
	}

	private static final long serialVersionUID = 1L;

}
