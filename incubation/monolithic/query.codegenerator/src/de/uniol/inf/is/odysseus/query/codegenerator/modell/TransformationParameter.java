package de.uniol.inf.is.odysseus.query.codegenerator.modell;

public class TransformationParameter {
	
	private String targetPlatform;
	private String targetDirectory;
	private String odysseusPath;
	private boolean generateOdysseusJar;
	private int queryId;
	private String executor;
	
	public TransformationParameter(String targetPlatform, String targetDirectory, int queryId, String odysseusPath, boolean generateOdysseusJar, String executor){
		this.targetPlatform = targetPlatform;
		this.targetDirectory = targetDirectory;
		this.queryId = queryId;
		this.setOdysseusPath(odysseusPath);
		this.setGenerateOdysseusJar(generateOdysseusJar);
		this.executor = executor;
	}
	
	
	public String getProgramLanguage() {
		return targetPlatform;
	}
	public void setProgramLanguage(String programLanguage) {
		this.targetPlatform = programLanguage;
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}
	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	
	
	public String getParameterForDebug(){
		StringBuilder debugParameter = new StringBuilder();
		debugParameter.append("Targetplatform:" +targetPlatform);
		debugParameter.append("Target directory "+targetDirectory);
		debugParameter.append("QueryId: "+ queryId);
		
		return debugParameter.toString();
		
	}


	public String getOdysseusPath() {
		return odysseusPath;
	}


	public void setOdysseusPath(String odysseusPath) {
		this.odysseusPath = odysseusPath;
	}


	public boolean isGenerateOdysseusJar() {
		return generateOdysseusJar;
	}


	public void setGenerateOdysseusJar(boolean generateOdysseusJar) {
		this.generateOdysseusJar = generateOdysseusJar;
	}


	public String getExecutor() {
		return executor;
	}


	public void setExecutor(String executor) {
		this.executor = executor;
	}

	
}
