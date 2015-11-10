package de.uniol.inf.is.odysseus.codegenerator.modell;

import de.uniol.inf.is.odysseus.codegenerator.modell.enums.UpdateMessageEventType;


/**
 * Modell class for the messageEvents. Used from the CodegneratorMessageBus
 * 
 * @author MarcPreuschaft
 *
 */
public class CodegeneratorMessageEvent {
	
	//text of the message
	private String text;
	
	private int progressValue;
	
	//message type
	private UpdateMessageEventType statusType;
	
	public CodegeneratorMessageEvent(int progressValue,String text, UpdateMessageEventType statusType){
		this.progressValue = progressValue;
		this.text =text;
		this.statusType = statusType;
	}
	
	/**
	 * return the message text
	 * @return
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * set the message text
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * set the progress value
	 * @param progressValue
	 */
	public void setProgressValue(int progressValue) {
		this.progressValue = progressValue;
	}
	
	/**
	 * get the progress value
	 * @return
	 */
	public int getProgressValue() {
		return progressValue;
	}
	
	/**
	 * set the message type
	 * @param statusType
	 */
	public void setStatusType(UpdateMessageEventType statusType) {
		this.statusType = statusType;
	}

	/**
	 * return the message type
	 * @return
	 */
	public UpdateMessageEventType getStatusType() {
		return statusType;
	}

	
	
}
