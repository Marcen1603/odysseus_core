package de.uniol.inf.is.odysseus.sensormanagement.server.script;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;
import de.uniol.inf.is.odysseus.sensormanagement.server.SensorFactory;

public class InitSensorManagementKeyword extends AbstractPreParserKeyword {

	public static final String NAME = "INITSENSORMGMT";

	private String serverInstanceName;
	private String loggingDir;
	
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		
		PreParserKeywordParameterHelper<InitSensorManagementKeywordParameter> parameterHelper = 
				PreParserKeywordParameterHelper.newInstance(InitSensorManagementKeywordParameter.class);		
		
		parameterHelper.validateParameterString(parameter);
		Map<IKeywordParameter, String> result = parameterHelper.parse(parameter);
		
		serverInstanceName = result.get(InitSensorManagementKeywordParameter.INSTANCE_NAME);
		loggingDir = result.get(InitSensorManagementKeywordParameter.LOGGING_DIRECTORY);		
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		List<IExecutorCommand> ret = new LinkedList<>();
		ret.add(new AbstractExecutorCommand(caller)
		{
			private static final long serialVersionUID = 4547495160063129512L;
			@Override public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
				SensorFactory.getInstance().init(serverInstanceName, loggingDir);
			}
		});
		return ret;
	}

}
