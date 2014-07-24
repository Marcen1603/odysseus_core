package de.uniol.inf.is.odysseus.script.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class DropStoredProcedure extends AbstractPreParserExecutorKeyword {
	
	public static final String DROPPROCEDURE = "DROPPROCEDURE";	

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {

	}

	@Override
	public List<IExecutorCommand>  execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException {				
		String name = parameter.trim();
		if(getServerExecutor().containsStoredProcedures(name, caller)){
			getServerExecutor().removeStoredProcedure(name, caller);
		}		
		return null;
	}

}
