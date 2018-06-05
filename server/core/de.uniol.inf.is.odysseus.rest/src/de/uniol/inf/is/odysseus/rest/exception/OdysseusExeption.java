package de.uniol.inf.is.odysseus.rest.exception;

import org.restlet.resource.Status;

@Status(value=400, serialize=true)
public class OdysseusExeption extends RuntimeException {

	private static final long serialVersionUID = 7090525846819610497L;

	public OdysseusExeption(String message, Exception e) {
		super(message,e);
	}
}
