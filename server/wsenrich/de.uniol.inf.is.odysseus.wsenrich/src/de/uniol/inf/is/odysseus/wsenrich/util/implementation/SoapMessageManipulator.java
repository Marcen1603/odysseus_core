package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IMessageManipulator;

public class SoapMessageManipulator implements IMessageManipulator {
	
	/**
	 * static variable for the starttag "<"
	 */
	private static final Character STARTTAG = '<';
	
	/**
	 * static variable for the endtag ">"
	 */
	private static final Character ENDTAG = '>';
	
	/**
	 * static variable for a slash "/"
	 */
	private static final Character SLASH = '/';
	
	/**
	 * static variable for the start of the soap-body
	 */
	private static final String BODY_STARTTAG = "<soapenv:Body>";
	
	/**
	 * the xml message as a StringBuffer
	 */
	private StringBuffer message;
	
	/**
	 * the arguments to add to the message
	 */
	private List<Option> arguments;
	
	/**
	 * the start of the message (everything before <soapenv:Body>)
	 */
	private StringBuffer startOfMessage;
	
	/**
	 * the end of the message (everything after </soapenv:Body>
	 */
	private StringBuffer endOfMessage;
	
	/**
	 * the arguments of the message as a StringBuffer
	 */
	private StringBuffer argumentsOfMesssage;
	
	/**
	 * Default Conctructor
	 */
	public SoapMessageManipulator() {
		//Needed for the MessageManipulatorRegistry
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
	 * Builds the fixed end part (after arguments) of the Soap Message. Includes Soap-Envelope Endtag and
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
	private StringBuffer buildArgumentsOfMessage() {
		this.argumentsOfMesssage = new StringBuffer();
		for(Option argument : arguments) {
			this.argumentsOfMesssage.append(STARTTAG + argument.getName() + ENDTAG);
			this.argumentsOfMesssage.append(""+argument.getValue());
			this.argumentsOfMesssage.append(STARTTAG + SLASH.toString() + argument.getName() +  ENDTAG);	
		}
		return this.argumentsOfMesssage;
	}
	
	@Override
	public void setMessage(String message) {
		this.message = new StringBuffer(message);
	}
	
	@Override
	public void setArguments(List<Option> arguments) {
		this.arguments = arguments;
	}
	
	
	/**
	 * @return the list of arguments which should be added to the message
	 */
//	public List<Option> getArguments() {
//		return this.arguments;
//	}
	
	/**
	 * @return the argument-part of the xml document
	 */
//	public StringBuffer getArgumentsToString() {
//		return this.argumentsOfMesssage;
//	}
	
	/**
	 * @return the fixed start part (before arguments) of the message
	 */
//	public String getStartOfMessage() {
//		return this.startOfMessage.toString();
//	}
	
	/**
	 * @return the fixed end part (after arguments) of the message
	 */
//	public String getEndOfMessage() {
//		return this.endOfMessage.toString();
//	}
	
	@Override
	public String buildMessage() {
		StringBuffer output = new StringBuffer();
		output.append(buildStartOfMessage());
		output.append(buildArgumentsOfMessage());
		output.append(buildEndOfMessage());
		return output.toString();
	}

	@Override
	public String getName() {
		return "SOAP";
	}

	@Override
	public IMessageManipulator createInstance() {
		return new SoapMessageManipulator();
	}
}

