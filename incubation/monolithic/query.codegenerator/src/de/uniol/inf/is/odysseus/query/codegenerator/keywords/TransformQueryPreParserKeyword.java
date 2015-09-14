package de.uniol.inf.is.odysseus.query.codegenerator.keywords;


import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.queryBuildSetting.TestSetting;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;




public class TransformQueryPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "TRANSFORMQUERY";
	
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
	
	
	}


	@Override
	public List<IExecutorCommand>  execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
	
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		
		Gson gson = new Gson();
		TransformationParameter transformationParameter = gson.fromJson(parameter, TransformationParameter.class);  
		
		/*
		String[] transPara = parameter.split(",");
		
	
		TransformationParameter transformationParameter = new TransformationParameter(
				transPara[0], transPara[1], transPara[2],
				0, transPara[3],
				true, transPara[4]);
		*/
		settings.add(new TestSetting(transformationParameter));
		
	
	
		return null;

	}

}
