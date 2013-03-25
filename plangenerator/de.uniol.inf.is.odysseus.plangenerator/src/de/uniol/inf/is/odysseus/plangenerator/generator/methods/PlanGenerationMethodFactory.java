/**
 * 
 */
package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;

/**
 * @author Merlin Wasmann
 * 
 */
public class PlanGenerationMethodFactory {

	public static IPlanGenerationMethod createPlanGenerationMethod(
			PlanGenerationConfiguration config, ICostModel<ILogicalOperator> costModel) {
		Map<String, String> configs = config.getValue();
		if (configs.get("method") == null) {
			// TODO: error handling
			configs.put("method", "copy");
//			configs.put("method", "exhaustiveSearch");
//			configs.put("method", "dynamicProgramming");
		}
		switch (configs.get("method")) {
		case "dynamicProgramming":
			return new DynamicProgramming(costModel);

		case "iterativeDynamicProgramming":
			// TODO: handle params for idp
			return new IterativeDynamicProgramming(costModel);

		case "exhaustiveSearch":
			return new ExhaustiveSearch();

		case "copy":
			return new CopyPlanGenerationMethod();
			
		default:
			return new ExhaustiveSearch();
		}
	}
}
