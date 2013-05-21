/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;

/**
 * @author Merlin Wasmann
 * 
 */
public class PlanGenerationMethodFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlanGenerationMethodFactory.class);

	public static IPlanGenerationMethod createPlanGenerationMethod(
			PlanGenerationConfiguration config, ICostModel<ILogicalOperator> costModel) {
		Map<String, String> configs = config.getValue();
		if (configs.get("PLANGENERATIONMETHOD") == null) {
			// TODO: error handling
			configs.put("PLANGENERATIONMETHOD", "copy");
//			configs.put("method", "exhaustiveSearch");
//			configs.put("method", "dynamicProgramming");
			LOG.debug("Method not found or empty. Default is copy");
		}
		switch (configs.get("PLANGENERATIONMETHOD")) {
		case "dynamicProgramming":
			return new DynamicProgramming(costModel);

		case "iterativeDynamicProgramming":
			// TODO: handle params for idp
			return new IterativeDynamicProgramming(costModel);

		case "exhaustiveSearch":
			return new ExhaustiveSearch(costModel);

		case "copy":
			return new CopyPlanGenerationMethod();
			
		default:
			return new ExhaustiveSearch();
		}
	}
}
