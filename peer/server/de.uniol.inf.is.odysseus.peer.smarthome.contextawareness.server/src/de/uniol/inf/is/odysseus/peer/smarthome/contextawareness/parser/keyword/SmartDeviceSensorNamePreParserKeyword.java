package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.parser.keyword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class SmartDeviceSensorNamePreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "SMARTDEVICE_SENSOR_NAME";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		if (parameter.length() == 0)
			throw new OdysseusScriptException("Parameter needed for #"+KEYWORD);
		
		String[] splitted = parameter.trim().split(" ");
		@SuppressWarnings("unused")
		List<String> parameters = generateParameterList(splitted);
		
		
		
		variables.put(KEYWORD, parameter);
	}

	private static List<String> generateParameterList(String[] splitted) {
		ArrayList<String> list = new ArrayList<>();
		
		for(int i=0; i<splitted.length;i++){
			list.add(splitted[i]);
		}
		
		return list;
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		variables.put(KEYWORD, parameter);
		return null;
	}

}
