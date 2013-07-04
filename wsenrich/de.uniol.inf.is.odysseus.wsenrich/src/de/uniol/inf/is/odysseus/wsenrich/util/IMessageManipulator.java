package de.uniol.inf.is.odysseus.wsenrich.util;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;

public interface IMessageManipulator {
	
	/**
	 * @param message the message to manipulate
	 */
	public void setMessage(String message);
	
	/**
	 * @param arguments the arguments to add to the message
	 */
	public void setArguments(List<Option> arguments);
	
	/**
	 * @return the builded message
	 */
	public String buildMessage();
	
	/**
	 * @return the name of the instance
	 */
	public String getName();
	
	/**
	 * @return a concerete implementation of this interface
	 */
	public IMessageManipulator createInstance();

}
