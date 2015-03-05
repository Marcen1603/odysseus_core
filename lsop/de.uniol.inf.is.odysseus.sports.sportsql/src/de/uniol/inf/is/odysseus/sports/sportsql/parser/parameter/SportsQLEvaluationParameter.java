package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class SportsQLEvaluationParameter  implements ISportsQLParameter{
	
	private boolean evaluation = false;
	private String filename = null;
	private String filepath = null;
	private String sinkname = null;
	private boolean calcLatenzOp = false;
	
	public SportsQLEvaluationParameter(boolean evaluation, String filename, String filepath, String sinkname, boolean calcLatenzOp){
		this.evaluation = evaluation;
		this.filename = filename;
		this.filepath = filepath;
		this.sinkname = sinkname;
		this.calcLatenzOp = calcLatenzOp;
	}
	
	public SportsQLEvaluationParameter(){
		
	}

	public boolean isEvaluation() {
		return this.evaluation;
	}
	public void setEvaluation(boolean evaluation) {
		this.evaluation = evaluation;
	}
	public String getFileName() {
		return filename;
	}
	
	@JsonIgnoreProperties
	public void setFileName(String fileName) {
		this.filename = fileName;
	}
	public String getFilePath() {
		return filepath;
	}
	
	@JsonIgnoreProperties
	public void setFilePath(String filePath) {
		this.filepath = filePath;
	}
	public String getSinkName() {
		return sinkname;
	}
	
	public void setSinkName(String sinkName) {
		this.sinkname = sinkName;
	}

	public boolean isCalcLatenzOp() {
		return calcLatenzOp;
	}

	public void setCalcLatenzOp(boolean calcLatenzOp) {
		this.calcLatenzOp = calcLatenzOp;
	}
	
	

}
