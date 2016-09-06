package de.uniol.inf.is.odysseus.codegenerator.keywords;


import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.codegenerator.commands.QueryCodegenerationCommand;
import de.uniol.inf.is.odysseus.codegenerator.model.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.scheduler.registry.CSchedulerRegistry;
import de.uniol.inf.is.odysseus.codegenerator.target.platform.registry.TargetPlatformRegistry;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.keyword.AbstractQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;


/**
 * CODEGENERATION keyword. This keyword can used to start 
 * codegeneration with odysseus script 
 * 
 * @author MarcPreuschaft
 *
 */
public class QueryCodegenerationPreParserKeyword extends AbstractQueryPreParserKeyword {

	public static final String KEYWORD = "CODEGENERATION";	
	private PreParserKeywordParameterHelper<QueryCodegenerationKeywordParameter> parameterHelper;

	/**
	 * validates the parameters defined in this keyword
	 */
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
	
		// validate parameter string
		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(QueryCodegenerationKeywordParameter.class, QueryCodegenerationConstants.PATTERN_CUSTOM_PARAMETER_VALUE);
		parameterHelper.validateParameterString(parameter);
		
		Map<IKeywordParameter, String> result = parameterHelper.parse(parameter);
		
		
		//get all needed parameters
		String targetPlatform =result.get(QueryCodegenerationKeywordParameter.TARGETPLATFROM);
		String targetDirectory = result.get(QueryCodegenerationKeywordParameter.TARGETDIRECTORY);
		String odysseusPath= result.get(QueryCodegenerationKeywordParameter.ODYSSEUSPATH);
		String schedulerString =result.get(QueryCodegenerationKeywordParameter.SCHEDULER);
		
		
		// check directorys 
		if (!checkDirecotry(targetDirectory)) {
			throw new IllegalArgumentException(
					"Target directory "+targetDirectory+" not exist or not readable!");
		}
		
		if (!checkDirecotry(odysseusPath)) {
			throw new IllegalArgumentException(
					"Odysseus code directory "+odysseusPath+" not exist or not readable!");
		}
		
		//check the targetPlatform name
		if(!TargetPlatformRegistry.existTargetPlatform(targetPlatform)){
				StringBuilder availableTargetPlatform = new StringBuilder();
			
			for(String targetPlatformTemp : TargetPlatformRegistry.getAllTargetPlatform()){
				availableTargetPlatform.append("- "+targetPlatformTemp+System.getProperty("line.separator"));
			}
			
			
			throw new IllegalArgumentException(
					"No targetPlatrom "+targetPlatform+" found!"+System.getProperty("line.separator")
					+"available targetplatform: "+System.getProperty("line.separator")
					+availableTargetPlatform.toString());
		}
	
		//check the scheduler name
		if(!CSchedulerRegistry.existScheduler(targetPlatform, schedulerString)){
			StringBuilder availableScheduler = new StringBuilder();
			
			for(String schedulerTemp : CSchedulerRegistry.getAllScheduler(targetPlatform)	){
				availableScheduler.append(schedulerTemp);
				availableScheduler.append(System.getProperty("line.separator"));
			}
			
			throw new IllegalArgumentException(
					"No scheduler "+schedulerString+" found!"+System.getProperty("line.separator")
					+"available scheduler:"+System.getProperty("line.separator")
					+availableScheduler.toString());
		}
	
	}
	
	/**
	 * create a new execute command (QueryCodegenerationCommand). 
	 * 
	 */
	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
		Map<IKeywordParameter, String> result = parameterHelper.parse(parameter);
		String targetPlatform =result.get(QueryCodegenerationKeywordParameter.TARGETPLATFROM);
		String targetDirectory = result.get(QueryCodegenerationKeywordParameter.TARGETDIRECTORY);
		String odysseusPath= result.get(QueryCodegenerationKeywordParameter.ODYSSEUSPATH);
		String schedulerString =result.get(QueryCodegenerationKeywordParameter.SCHEDULER);
		String options =result.get(QueryCodegenerationKeywordParameter.OPTIONS);
		String queryname =result.get(QueryCodegenerationKeywordParameter.QUERYNAME);
		
		TransformationParameter transformationParameter = new TransformationParameter(targetPlatform,  targetDirectory,  0,  odysseusPath, true ,  schedulerString, parseOptions(options));  

		List<IExecutorCommand> commands = new LinkedList<>();
		
		QueryCodegenerationCommand cmd = new QueryCodegenerationCommand(caller,transformationParameter,Resource.specialCreateResource(queryname, caller.getUser()));
		commands.add(cmd);
	
		return commands;
	}


	/**
	 * Checks if the folder exist and can read 
	 * 
	 * @param folder
	 * @return
	 */
	private boolean checkDirecotry(String folder) {
		File folderFile = new File(folder);

		if (folderFile.exists() && folderFile.canRead()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * parse the special options for the targetPlatform
	 * 
	 * @param options
	 * @return
	 */
	private Map<String,String> parseOptions(String options){
		 Map<String,String> optionMap = new  HashMap<String,String>();
		 
		 if(options !=null){
			 String[] splitedOptions = options.split(",");
			 
			 if(splitedOptions.length>0){
				 for(int i=0; i<splitedOptions.length; i++){
					 
					 String optionKey = splitedOptions[i].split("\\(")[0];
					 String optionValue = splitedOptions[i].replaceAll(".*\\(|\\).*", "");
					 optionMap.put(optionKey, optionValue);
					 
				 }
			 }
		 }
		
		 return optionMap;	
	}
	
	@Override
	protected boolean startQuery() {
		return false;
	}

}
