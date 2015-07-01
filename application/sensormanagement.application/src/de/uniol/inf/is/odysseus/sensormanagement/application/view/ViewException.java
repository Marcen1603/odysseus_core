package de.uniol.inf.is.odysseus.sensormanagement.application.view;

public class ViewException extends RuntimeException 
{
	private static final long serialVersionUID = 4236667630877897795L;

	public ViewException(String message)
	{
		super(message);
	}

	public ViewException(Exception e)
	{
		super(e);
	}

	public ViewException(String msg, Exception e) 
	{
		super(msg, e);
	}
}
