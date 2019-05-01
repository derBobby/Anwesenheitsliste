package eu.planlos.anwesenheitsliste.model.exception;

public class PasswordLengthException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PasswordLengthException(String message) {
		super(message);
	}
}
