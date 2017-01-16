/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.AbstractParallelizationConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class IntraOperatorParallelizationConfiguration extends AbstractParallelizationConfiguration {

	public IntraOperatorParallelizationConfiguration(ILogicalOperator operator) {
		super(operator);
	}

	@Override
	public void execute(Collection<IQueryBuildSetting<?>> settings) {
		IntraOperatorIndividualInitializer.createIndividualIntraOperatorConfiguration(settings,
				getOperator().getUniqueIdentifier(), parallelizationDegree, bufferSize);

	}

}
