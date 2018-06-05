package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IMessageCreator;

public class HttpBodyMessage implements IMessageCreator {

	private String message;

	public HttpBodyMessage(String message){
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getOperationName() {
		return null;
	}

	@Override
	public String getName() {
		return "HttpBodyMessage";
	}

	@Override
	public void buildMessage() {
		// nothing to do
	}

	@Override
	public IMessageCreator createInstance(String wsdlLocation, String operation) {
		return null;
	}

}
