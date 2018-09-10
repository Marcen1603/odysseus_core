/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParallelIntraOperatorSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValueElement;
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
		ParallelIntraOperatorSetting parallelIntraOperatorSetting = null;
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(ParallelIntraOperatorSetting.class)) {
				parallelIntraOperatorSetting = (ParallelIntraOperatorSetting) setting;
			}
		}
		ParallelIntraOperatorSettingValueElement individualElement = new ParallelIntraOperatorSettingValueElement(
				parallelizationDegree, bufferSize);
		if (parallelIntraOperatorSetting.getValue().hasIndividualSettingsForOperator(getOperator().getUniqueIdentifier())) {
			parallelIntraOperatorSetting.getValue().removeIndividualSettings(getOperator().getUniqueIdentifier());
		}
		parallelIntraOperatorSetting.getValue().addIndividualSettings(getOperator().getUniqueIdentifier(), individualElement);

	}

}
