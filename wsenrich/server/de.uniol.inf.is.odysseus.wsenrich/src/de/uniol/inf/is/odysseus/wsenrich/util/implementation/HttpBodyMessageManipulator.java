package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IMessageManipulator;

public class HttpBodyMessageManipulator implements IMessageManipulator {

	String message;
	List<Option> arguments;

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void setArguments(List<Option> arguments) {
		this.arguments = arguments;
	}

	@Override
	public String buildMessage() {
		String newMessage = message;
		for (Option o:arguments){
			newMessage = newMessage.replaceAll(o.getName(), o.getValue());
		}

		return newMessage;
	}

	@Override
	public String getName() {
		return "HttpBodyMessageManipulator";
	}

	@Override
	public IMessageManipulator createInstance() {
		return new HttpBodyMessageManipulator();
	}

}
