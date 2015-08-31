package de.uniol.inf.is.odysseus.query.codegenerator.modell;

public class ProgressBarUpdate {
	
	private String text;
	private int progressValue;
	private StatusType statusType;
	
	
	public ProgressBarUpdate(int progressValue,String text, StatusType statusType){
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

	public StatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusType statusType) {
		this.statusType = statusType;
	}
	
}
