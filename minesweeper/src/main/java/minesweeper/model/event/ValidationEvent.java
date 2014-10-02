package minesweeper.model.event;

public class ValidationEvent
{
	private Class validatedClass;
	private Object data;
	
	public ValidationEvent(Class validatedClass)
	{
		this.validatedClass = validatedClass;
	}
	
	public ValidationEvent(Class validatedClass, Object data)
	{
		this.validatedClass = validatedClass;
		this.data = data;
	}
	
	public Class getValidatedClass()
	{
		return this.validatedClass;
	}
	
	public Object getData()
	{
		return this.data;
	}
}
