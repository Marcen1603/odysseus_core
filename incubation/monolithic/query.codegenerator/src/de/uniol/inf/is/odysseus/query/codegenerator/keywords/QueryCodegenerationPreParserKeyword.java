package de.uniol.inf.is.odysseus.query.codegenerator.keywords;


import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.query.codegenerator.commands.QueryCodegenerationCommand;
import de.uniol.inf.is.odysseus.query.codegenerator.executor.registry.CExecutorRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.registry.TargetPlatformRegistry;
import de.uniol.inf.is.odysseus.script.keyword.AbstractQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

public class QueryCodegenerationPreParserKeyword extends AbstractQueryPreParserKeyword {

	public static final String KEYWORD = "QUERYCODEGENERATION";
	private PreParserKeywordParameterHelper<QueryCodegenerationKeywordParameter> parameterHelper;

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
	
		// validate parameter string
		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(QueryCodegenerationKeywordParameter.class, QueryCodegenerationConstants.PATTERN_CUSTOM_PARAMETER_VALUE);
		parameterHelper.validateParameterString(parameter);
		
		Map<IKeywordParameter, String> result = parameterHelper.parse(parameter);
		
		String targetPlatform =result.get(QueryCodegenerationKeywordParameter.TARGETPLATFROM);
		String tempDirectory = result.get(QueryCodegenerationKeywordParameter.TEMPDIRECTORY);
		String destinationDirectory =result.get(QueryCodegenerationKeywordParameter.DESTINATIONDIRECTORY);
		String odysseusPath= result.get(QueryCodegenerationKeywordParameter.ODYSSEUSPATH);
		String executorString =result.get(QueryCodegenerationKeywordParameter.EXECUTOR);
		
		if (!checkDirecotry(tempDirectory)) {
			throw new IllegalArgumentException(
					"Temp directory "+tempDirectory+" not exist or not readable!");
		}
		
		if (!checkDirecotry(destinationDirectory)) {
			throw new IllegalArgumentException(
					"Desitnation directory "+destinationDirectory+" not exist or not readable!");
		}
		
		if (!checkDirecotry(odysseusPath)) {
			throw new IllegalArgumentException(
					"Odysseus code directory "+odysseusPath+" not exist or not readable!");
		}
		
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
	
		
		if(!CExecutorRegistry.existExecutor(targetPlatform, executorString)){
			StringBuilder availableExecutor = new StringBuilder();
			
			for(String executorTemp : CExecutorRegistry.getAllExecutor(targetPlatform)	){
				availableExecutor.append(executorTemp);
				availableExecutor.append(System.getProperty("line.separator"));
			}
			
			throw new IllegalArgumentException(
					"No executor "+executorString+" found!"+System.getProperty("line.separator")
					+"available executor:"+System.getProperty("line.separator")
					+availableExecutor.toString());
		}
	
	}
	

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
		Map<IKeywordParameter, String> result = parameterHelper.parse(parameter);
		String targetPlatform =result.get(QueryCodegenerationKeywordParameter.TARGETPLATFROM);
		String tempDirectory = result.get(QueryCodegenerationKeywordParameter.TEMPDIRECTORY);
		String destinationDirectory =result.get(QueryCodegenerationKeywordParameter.DESTINATIONDIRECTORY);
		String odysseusPath= result.get(QueryCodegenerationKeywordParameter.ODYSSEUSPATH);
		String executorString =result.get(QueryCodegenerationKeywordParameter.EXECUTOR);
		 
		
		TransformationParameter transformationParameter = new TransformationParameter(targetPlatform,  tempDirectory,  destinationDirectory,  0,  odysseusPath, true ,  executorString);  

		List<IExecutorCommand> commands = new LinkedList<>();
		
		QueryCodegenerationCommand cmd = new QueryCodegenerationCommand(caller,transformationParameter);
		commands.add(cmd);
	
		return commands;
	}

	@Override
	protected boolean startQuery() {
		// TODO Auto-generated method stub
		return false;
	}

	
	private boolean checkDirecotry(String folder) {
		File folderFile = new File(folder);

		if (folderFile.exists() && folderFile.canRead()) {
			return true;
		} else {
			return false;
		}
	}
}
