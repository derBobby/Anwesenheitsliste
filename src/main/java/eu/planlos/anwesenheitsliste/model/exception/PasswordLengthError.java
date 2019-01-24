package eu.planlos.anwesenheitsliste.model.exception;

public class PasswordLengthError extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PasswordLengthError(String message) {
		super(message);
	}
}
