package de.uniol.inf.is.odysseus.planmanagement.optimization;

import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public interface IPostOptimitzationAction {

	public void run(IQuery query, OptimizationConfiguration parameter);

}
