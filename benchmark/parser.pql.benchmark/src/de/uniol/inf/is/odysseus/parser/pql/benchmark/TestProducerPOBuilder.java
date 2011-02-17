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
package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import de.uniol.inf.is.odysseus.benchmarker.impl.TestProducerAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class TestProducerPOBuilder extends AbstractOperatorBuilder {
	IntegerParameter invertedPriorityRatio = new IntegerParameter(
			"INVERTEDPRIORITYRATIO", REQUIREMENT.MANDATORY);
	ListParameter<BatchItem> parts = new ListParameter<BatchItem>("PARTS",
			REQUIREMENT.MANDATORY, new BatchParameter("BATCH",
					REQUIREMENT.MANDATORY));

	public TestProducerPOBuilder() {
		super(0, 0);
		setParameters(invertedPriorityRatio, parts);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		TestProducerAO ao = new TestProducerAO();
		ao.setInvertedPriorityRatio(invertedPriorityRatio.getValue());
		for (BatchItem batch : parts.getValue()) {
			ao.addTestPart(batch.size, batch.wait);
		}

		return ao;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
