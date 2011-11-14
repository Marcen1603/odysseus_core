package de.uniol.inf.is.odysseus.script.keyword;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.script.executor.ExecutorHandler;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;

public abstract class AbstractPreParserExecutorKeyword extends AbstractPreParserKeyword {

	IExecutor getExecutor(){
		return ExecutorHandler.getExecutor();
	}
	
}
