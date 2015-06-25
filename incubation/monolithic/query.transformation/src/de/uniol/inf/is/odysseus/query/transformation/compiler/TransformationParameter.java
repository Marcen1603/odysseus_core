package de.uniol.inf.is.odysseus.query.transformation.compiler;

public class TransformationParameter {
	
	private String programLanguage;
	private String tempDirectory;
	private String destinationDirectory;
	private int queryId;
	
	public TransformationParameter(String programLanguage, String tempDirectory, String destinationDirectory, int queryId){
		this.programLanguage = programLanguage;
		this.tempDirectory = tempDirectory;
		this.destinationDirectory = destinationDirectory;
		this.queryId = queryId;
	}
	
	public String getProgramLanguage() {
		return programLanguage;
	}
	public void setProgramLanguage(String programLanguage) {
		this.programLanguage = programLanguage;
	}
	public String getDestinationDirectory() {
		return destinationDirectory;
	}
	public void setDestinationDirectory(String destinationDirectory) {
		this.destinationDirectory = destinationDirectory;
	}
	public String getTempDirectory() {
		return tempDirectory;
	}
	public void setTempDirectory(String tempDirectory) {
		this.tempDirectory = tempDirectory;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	
	
	public String getParameterForDebug(){
		StringBuilder debugParameter = new StringBuilder();
		debugParameter.append("Programlanguage:" +programLanguage);
		debugParameter.append("Temp path: "+tempDirectory);
		debugParameter.append("Target path: "+ destinationDirectory);
		debugParameter.append("QueryId: "+ queryId);
		
		return debugParameter.toString();
		
	}

}
