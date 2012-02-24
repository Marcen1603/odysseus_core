package de.uniol.inf.is.odysseus.script.keyword;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.script.executor.ExecutorHandler;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public abstract class AbstractPreParserExecutorKeyword extends AbstractPreParserKeyword {

	IExecutor getExecutor() throws OdysseusScriptException{
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new OdysseusScriptException("No executor found");
		return executor;
	}
	
}
