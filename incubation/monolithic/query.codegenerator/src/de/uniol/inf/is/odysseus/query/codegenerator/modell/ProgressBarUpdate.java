package de.uniol.inf.is.odysseus.query.codegenerator.modell;

import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageStatusType;

public class ProgressBarUpdate {
	
	private String text;
	private int progressValue;
	private UpdateMessageStatusType statusType;
	
	public ProgressBarUpdate(int progressValue,String text, UpdateMessageStatusType statusType){
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

	public UpdateMessageStatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(UpdateMessageStatusType statusType) {
		this.statusType = statusType;
	}
	
}
