package de.uniol.inf.is.odysseus.query.codegenerator.keywords;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.query.codegenerator.commands.QueryCodegenerationCommand;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
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

}
