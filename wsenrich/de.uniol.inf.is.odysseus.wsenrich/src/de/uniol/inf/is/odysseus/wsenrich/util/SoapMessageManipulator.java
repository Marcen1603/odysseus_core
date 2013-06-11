package de.uniol.inf.is.odysseus.wsenrich.util;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;

public class SoapMessageManipulator {
	
	private static final Character STARTTAG = '<';
	
	private static final Character ENDTAG = '>';
	
	private static final Character SLASH = '/';
	
	private static final String BODY_STARTTAG = "<soapenv:Body>";
	
	private StringBuffer message;
	
	private List<Option> arguments;
	
	private StringBuffer startOfMessage;
	
	private StringBuffer endOfMessage;
	
	private StringBuffer argumentsOfMesssage;
	
	/**
	 * Constructor for the SoapMessagemanipulator. Arguments are automatically added
	 * to the final message
	 * @param message the message without arguments
	 * @param arguments the arguments that should be added to the message
	 */
	public SoapMessageManipulator(String message, List<Option> arguments) {
		
		this.message = new StringBuffer(message);
		buildStartOfMessage();
		buildArgumentsOfMessage(arguments);
		buildEndOfMessage();
		
	}
	
	/**
	 * Builds the first fixed start part (before arguments) of the Soap Message. Inlcudes Soap-Envelope, Soap-Header and
	 * the Start-Tag of the Soap-Body
	 */
	private StringBuffer buildStartOfMessage() {
		
		int endOfStartMessage = this.message.indexOf(STARTTAG + "soapenv:Body" + ENDTAG);
		this.startOfMessage = new StringBuffer(this.message.substring(0, endOfStartMessage));
		this.startOfMessage.append(BODY_STARTTAG);
		
		int endOfOperationDefinition = this.message.indexOf(ENDTAG.toString(), this.startOfMessage.length());
		this.startOfMessage.append(this.message.substring(endOfStartMessage + BODY_STARTTAG.length(), endOfOperationDefinition + 1));
		return this.startOfMessage;
		
	}
	
	/**
	 * Builds the fixed ent part (after arguments) of the Soap Message. Includes Soap-Envelope Endtag and
	 * the Soap-Body Endtag
	 */
	private StringBuffer buildEndOfMessage() {
		
		int operationBegin = this.startOfMessage.lastIndexOf(STARTTAG.toString());
		int nextEscapeSequenz = this.startOfMessage.indexOf(" ", operationBegin);
		int nextTagSequenz = this.startOfMessage.indexOf(ENDTAG.toString(), operationBegin);
		
		if(nextEscapeSequenz <= 1) {
			
			this.endOfMessage = new StringBuffer(STARTTAG + SLASH.toString() + this.message.substring(operationBegin + 1, nextTagSequenz) + ENDTAG);
		
		} else {
			
			this.endOfMessage = new StringBuffer(STARTTAG + SLASH.toString() + this.message.substring(operationBegin + 1, nextEscapeSequenz) + ENDTAG);
		
		}
		
		int beginOfEndMesssge = this.message.indexOf(STARTTAG + "/soapenv:Body" + ENDTAG);
		this.endOfMessage.append(this.message.substring(beginOfEndMesssge, this.message.length()));
		return this.endOfMessage;
		
	}
	
	/**
	 * Adds the arguments to the message
	 */
	public StringBuffer buildArgumentsOfMessage(List<Option> arguments) {
		
		this.arguments = arguments;
		this.argumentsOfMesssage = new StringBuffer();
		
		for(Option argument : arguments) {
			
			this.argumentsOfMesssage.append(STARTTAG + argument.getName() + ENDTAG);
			this.argumentsOfMesssage.append(argument.getValue());
			this.argumentsOfMesssage.append(STARTTAG + SLASH.toString() + argument.getName() +  ENDTAG);
			
		}
		
		return this.argumentsOfMesssage;
		
	}
	
	/**
	 * @return the list of arguments which should be added to the message
	 */
	public List<Option> getArguments() {
		
		return this.arguments;
	}
	
	/**
	 * @return the argument-part of the xml document
	 */
	public StringBuffer getArgumentsToString() {
		
		return this.argumentsOfMesssage;
	}
	
	/**
	 * @return the fixed start part (before arguments) of the message
	 */
	public String getStartOfMessage() {
		
		return this.startOfMessage.toString();
	}
	
	/**
	 * @return the fixed end part (after arguments) of the message
	 */
	public String getEndOfMessage() {
		
		return this.endOfMessage.toString();
	}
	
	/**
	 * @return the builded message
	 */
	public String buildMessage() {
		
		StringBuffer output = new StringBuffer();
		output.append(this.startOfMessage);
		output.append(this.argumentsOfMesssage);
		output.append(this.endOfMessage);
		return output.toString();
	
	}
	

}

