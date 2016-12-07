/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParallelIntraOperatorSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValueElement;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * @author Dennis Nowak
 *
 */
public class IntraOperatorIndividualInitializer {
	
	public static void createIndividualIntraOperatorConfiguration(Collection<IQueryBuildSetting<?>> settings, String operatorId, int degree, int bufferSize){
		ParallelIntraOperatorSetting parallelIntraOperatorSetting = null;
		for(IQueryBuildSetting<?> setting:settings){
			if(setting.getClass().equals(ParallelIntraOperatorSetting.class)) {
				parallelIntraOperatorSetting = (ParallelIntraOperatorSetting) setting;
			}
		}
		ParallelIntraOperatorSettingValueElement individualElement = new ParallelIntraOperatorSettingValueElement(degree, bufferSize);
		if(parallelIntraOperatorSetting.getValue().hasIndividualSettingsForOperator(operatorId)) {
			parallelIntraOperatorSetting.getValue().removeIndividualSettings(operatorId);
		}
		parallelIntraOperatorSetting.getValue().addIndividualSettings(operatorId, individualElement);
	}

}
