package eu.planlos.anwesenheitsliste.service;

public class UnknownUserSaveException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnknownUserSaveException(String message) {
		super(message);
	}

}
