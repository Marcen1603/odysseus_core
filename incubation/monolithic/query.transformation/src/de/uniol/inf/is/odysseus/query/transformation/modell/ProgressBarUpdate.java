package de.uniol.inf.is.odysseus.query.transformation.modell;

public class ProgressBarUpdate {
	
	private String text;
	private int progressValue;
	
	public ProgressBarUpdate(int progressValue,String text){
		this.progressValue = progressValue;
		this.text =text;
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
	
}
