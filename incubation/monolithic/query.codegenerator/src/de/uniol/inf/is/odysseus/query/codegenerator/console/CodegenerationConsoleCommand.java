package de.uniol.inf.is.odysseus.query.codegenerator.console;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.query.codegenerator.CAnalyseServiceBinding;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.scheduler.ICScheduler;
import de.uniol.inf.is.odysseus.query.codegenerator.scheduler.registry.CSchedulerRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.registry.TargetPlatformRegistry;


public class CodegenerationConsoleCommand implements CommandProvider {

	static Map<String, TransformationParameter> configList = new HashMap<String, TransformationParameter>();
	
	
	 public void _queryCodegen(CommandInterpreter ci) {
		 
		 final String ERROR_USAGE  = "Usage: queryCodegen <configName> <queryId>";
		 
		 String configName = ci.nextArgument();
		 Integer queryId = getIntegerParameter(ci, ERROR_USAGE);
		 
		if(configList.containsKey(configName.toLowerCase())){
			TransformationParameter transformationParameter = configList.get(configName.toLowerCase());
			if(queryId != null){
				transformationParameter.setQueryId(queryId);	
			}
			
			CAnalyseServiceBinding.getAnalyseComponent().startQueryTransformation(transformationParameter, null);
			
		}
		 
	 }
	 


	 public void _addCodegenConfig(CommandInterpreter ci){
		 String configName  = ci.nextArgument();
		 String targetPlatform = ci.nextArgument();
		 String targetDirectory= ci.nextArgument();
		 int queryId= Integer.parseInt(ci.nextArgument());
		 String odysseusDirectory= ci.nextArgument();
		 String executor= ci.nextArgument();
		 String optionsString = ci.nextArgument();
	
	
		 configList.put(configName.toLowerCase(), new TransformationParameter( targetPlatform,  targetDirectory,  queryId,  odysseusDirectory, true,  executor,  parseOptions(optionsString)));
	 }

	 
	 
	 public void _showAllConfigs(CommandInterpreter ci) {
		 StringBuilder sb = new StringBuilder();
		 sb.append("--Codegeneration configs--\n");
			for (Entry<String, TransformationParameter> config : configList.entrySet())
			{
				sb.append(config.getKey()).append("\n");
				
				sb.append("- targetPlatform: "+config.getValue().getProgramLanguage()).append("\n");
				sb.append("- targetDirectory: "+config.getValue().getTargetDirectory()).append("\n");
				sb.append("- queryId: "+config.getValue().getQueryId()).append("\n");
				sb.append("- odysseusDirectory: "+config.getValue().getOdysseusDirectory()).append("\n");
				sb.append("- executor: "+config.getValue().getExecutor()).append("\n");
				
				sb.append("\n");	
			}
		 
			
		 ci.println(sb.toString());
	 }
	 
	 
	 
	 public void _showRegistrys(CommandInterpreter ci) {
		 StringBuilder sb = new StringBuilder();
		 sb.append("--Targetplatforms--\n");
			for(String name : TargetPlatformRegistry.getAllTargetPlatform()) {
				sb.append(name).append("\n");
			}
		 sb.append("\n");
		 
		 sb.append("--Executors--\n");
			for (Entry<String, Map<String, ICScheduler>> platform : CSchedulerRegistry.getAllExecutor().entrySet())
			{
				sb.append(platform.getKey()).append("\n");
				
				for (Entry<String, ICScheduler> executor : platform.getValue().entrySet())
				{
					sb.append("-"+executor.getKey()).append("\n");
				}
				
					sb.append("\n");
				
			}
			
		
		sb.append("\n");
		
		ci.println(sb.toString());
		
	 }
	
	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Codegeneration commands---\n");
		sb.append("    queryCodegen <configName> <queryID>		   		- Transform a query to a standalone application\n");
		sb.append("    showRegistrys 				   		- Show all registry values\n");
		sb.append("    addCodegenConfig <configName> <targetPlatform> <targetDirectory> <queryId> <odysseusPath> <scheduler> <options>	- Add a codegeneration config\n");
		sb.append("    showAllConfigs 						- Show all configs\n");
		
		
		return sb.toString();
	}

	
	private Integer getIntegerParameter(CommandInterpreter ci,String errorMessage) {
		String parameterAsString = ci.nextArgument();
		if (Strings.isNullOrEmpty(parameterAsString)) {
			ci.println(errorMessage);
			return null;
		}

		Integer parameterAsInt;

		try {
			parameterAsInt = Integer.parseInt(parameterAsString);
		} catch (NumberFormatException e) {
			ci.println(errorMessage);
			return null;
		}
		
		return parameterAsInt;
	}
	
	
	private Map<String,String> parseOptions(String options){
		 Map<String,String> optionMap = new  HashMap<String,String>();
		 
		 if(options !=null){
			 String[] splitedOptions = options.split(",");
			 
			 if(splitedOptions.length>0){
				 for(int i=0; i<splitedOptions.length; i++){
					 
					 String optionKey = splitedOptions[i].split("=")[0];
					 String optionValue = splitedOptions[i].split("=")[1];
					 optionMap.put(optionKey, optionValue);
					 
				 }
			 }
		 }
		
		 return optionMap;	
	}
}
