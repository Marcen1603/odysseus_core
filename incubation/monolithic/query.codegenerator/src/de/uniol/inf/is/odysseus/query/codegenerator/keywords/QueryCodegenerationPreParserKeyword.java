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




public class QueryCodegenerationPreParserKeyword extends AbstractQueryPreParserKeyword {

	public static final String KEYWORD = "QUERYCODEGENERATION";
	

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
		Gson gson = new Gson();
		TransformationParameter transformationParameter = gson.fromJson(parameter, TransformationParameter.class);  

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
