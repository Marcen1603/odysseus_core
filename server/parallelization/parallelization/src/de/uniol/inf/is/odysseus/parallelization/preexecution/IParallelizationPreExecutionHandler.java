package de.uniol.inf.is.odysseus.parallelization.preexecution;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public interface IParallelizationPreExecutionHandler {
	
	void validateParameters(String parameterString);
	
	void preExecute(String parameterString, List<IQueryBuildSetting<?>> settings) throws OdysseusScriptException;
	
	String getType();

}
