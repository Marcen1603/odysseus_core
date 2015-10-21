package de.uniol.inf.is.odysseus.query.codegenerator.modell;

import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageEventType;

public class CodegeneratorMessageEvent {
	
	private String text;
	private int progressValue;
	private UpdateMessageEventType statusType;
	
	public CodegeneratorMessageEvent(int progressValue,String text, UpdateMessageEventType statusType){
		this.progressValue = progressValue;
		this.text =text;
		this.statusType = statusType;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getProgressValue() {
		return progressValue;
	}
	public void setProgressValue(int progressValue) {
		this.progressValue = progressValue;
	}

	public UpdateMessageEventType getStatusType() {
		return statusType;
	}

	public void setStatusType(UpdateMessageEventType statusType) {
		this.statusType = statusType;
	}
	
}
