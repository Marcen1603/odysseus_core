/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.kdds.frequent.builder;

import de.uniol.inf.is.odysseus.kdds.frequent.logical.FrequentItemAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class FrequentItemAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = -9177473830261170455L;
	private DirectParameter<String> strategyParam = new DirectParameter<String>("TYPE", REQUIREMENT.OPTIONAL);
	private DirectParameter<Double> sizeParam = new DirectParameter<Double>("SIZE", REQUIREMENT.MANDATORY);
	private ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>("ATTRIBUTES", REQUIREMENT.MANDATORY, new ResolvedSDFAttributeParameter("project attribute",
			REQUIREMENT.MANDATORY));

	public FrequentItemAOBuilder() {
		super(1, 1);
		setParameters(strategyParam, sizeParam, attributes);
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		FrequentItemAO.Strategy strategy = FrequentItemAO.Strategy.Simple;
		String type = strategyParam.getValue().toUpperCase();
		double size = sizeParam.getValue().doubleValue();
		if (type.equals("SIMPLE")) {
			strategy = FrequentItemAO.Strategy.Simple;
		}
		if (type.equals("LOSSY") || type.equals("LOSSYCOUNTING")) {
			strategy = FrequentItemAO.Strategy.LossyCounting;
		}
		if (type.equals("SPACE") || type.equals("SPACESAVING")) {
			strategy = FrequentItemAO.Strategy.SpaceSaving;
		}

		FrequentItemAO frequent = new FrequentItemAO(size, strategy, attributes.getValue());
		return frequent;
	}

}
