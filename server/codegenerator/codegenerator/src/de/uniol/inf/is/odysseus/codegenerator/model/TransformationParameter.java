package de.uniol.inf.is.odysseus.codegenerator.model;

import java.util.HashMap;
import java.util.Map;


/**
 * this class holds the transformationParameter that are needed for
 * the codegeneration
 * 
 * @author MarcPreuschaft
 *
 */
public class TransformationParameter {
	
	//name of the targetPlatform
	private String targetPlatform;
	//scheduler name
	private String scheduler;
	//target directory
	private String targetDirectory;
	//odysseus code path
	private String odysseusPath;
	
	private boolean generateOdysseusJar;
	
	private int queryId;
	
	//special options 
	private Map<String,String> options = new HashMap<String,String>();
	
	public TransformationParameter(String targetPlatform, String targetDirectory, int queryId, String odysseusPath, boolean generateOdysseusJar, String scheduler,Map<String,String> options){
		this.targetPlatform = targetPlatform;
		this.targetDirectory = targetDirectory;
		this.queryId = queryId;
		this.setOdysseusPath(odysseusPath);
		this.setGenerateOdysseusJar(generateOdysseusJar);
		this.scheduler = scheduler;
		this.setOptions(options);
	}
	
	/**
	 * return the targetPlatform name
	 * @return
	 */
	public String getTargetPlatformName() {
		return targetPlatform;
	}
	
	/**
	 * set the targetPlatform name
	 * @param programLanguage
	 */
	public void setTargetPlatformName(String programLanguage) {
		this.targetPlatform = programLanguage;
	}
	
	/**
	 * return the target directory
	 * @return
	 */
	public String getTargetDirectory() {
		return targetDirectory;
	}
	
	/**
	 * set the target directory
	 * @param targetDirectory
	 */
	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	/**
	 * get the query id , which transformed
	 * @return
	 */
	public int getQueryId() {
		return queryId;
	}

	/**
	 * set the query id
	 * @param queryId
	 */
	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	
	/**
	 * get the odysseus code path
	 * @return
	 */
	public String getOdysseusDirectory() {
		return odysseusPath;
	}

	/**
	 * set the odysseus code path
	 * @param odysseusPath
	 */
	public void setOdysseusPath(String odysseusPath) {
		this.odysseusPath = odysseusPath;
	}

	/**
	 * get the scheduler name
	 * @return
	 */
	public String getScheduler() {
		return scheduler;
	}

	/**
	 * set the scheduler name
	 * @param scheduler
	 */
	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}


	/**
	 * get the special options 
	 * @return
	 */
	public Map<String,String> getOptions() {
		return options;
	}

	
	/**
	 * set the special options
	 * @param options
	 */
	public void setOptions(Map<String,String> options) {
		this.options = options;
	}
	
	
	/**
	 * get important parameter for better debug infos
	 * @return
	 */
	public String getParameterForDebug(){
		StringBuilder debugParameter = new StringBuilder();
		debugParameter.append("Targetplatform:" +targetPlatform);
		debugParameter.append("Target directory "+targetDirectory);
		debugParameter.append("QueryId: "+ queryId);
		
		return debugParameter.toString();
		
	}
	
	/**
	 * check if odysseus jar's are recreated
	 * @return
	 */
	public boolean isGenerateOdysseusJar() {
		return generateOdysseusJar;
	}

	/**
	 * if true , the odysseus jar files are regenerated
	 * @param generateOdysseusJar
	 */
	public void setGenerateOdysseusJar(boolean generateOdysseusJar) {
		this.generateOdysseusJar = generateOdysseusJar;
	}

	
}
