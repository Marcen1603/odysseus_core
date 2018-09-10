package de.uniol.inf.is.odysseus.parallelization.initialization.strategies;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * @author Dennis Nowak
 *
 */
public interface IParallelizationStrategyInitializer {

	public void initialize(List<IQueryBuildSetting<?>> settings) throws OdysseusScriptException;

}
