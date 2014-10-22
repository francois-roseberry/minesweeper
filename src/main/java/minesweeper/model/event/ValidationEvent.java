package minesweeper.model.event;

public class ValidationEvent {

	private final Class<?> validatedClass;
	private final Object data;

	public ValidationEvent(final Class<?> validatedClass) {
		this(validatedClass, null);
	}

	public ValidationEvent(final Class<?> validatedClass, final Object data) {
		this.validatedClass = validatedClass;
		this.data = data;
	}

	public Class<?> getValidatedClass() {
		return validatedClass;
	}

	public Object getData() {
		return data;
	}
}
