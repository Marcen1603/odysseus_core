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

import de.uniol.inf.is.odysseus.benchmarker.impl.BenchmarkAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

/**
 * @author Jonas Jacobi
 */
public class BenchmarkBuilder extends AbstractOperatorBuilder {

	private static final String PROCESSING_TIME = "TIME";
	private static final String SELECTIVITY = "SELECTIVITY";

	private final DirectParameter<Double> selectivity = new DirectParameter<Double>(
			SELECTIVITY, REQUIREMENT.MANDATORY);

	private final IntegerParameter processingTimeInns = new IntegerParameter(
			PROCESSING_TIME, REQUIREMENT.MANDATORY);

	public BenchmarkBuilder() {
		super(1, 1);
		setParameters(selectivity, processingTimeInns);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		Integer processingTimeInns = this.processingTimeInns.getValue();
		Double selectivity = this.selectivity.getValue();

		BenchmarkAO benchmarkAO = new BenchmarkAO(processingTimeInns,
				selectivity);

		return benchmarkAO;
	}

	@Override
	protected boolean internalValidation() {
		boolean isValid = true;
		if (selectivity.getValue() < 0) {
			addError(new IllegalParameterException("selectivity has to be greater than 0"));
			isValid = false;
		}
		if (processingTimeInns.getValue() < 0) {
			addError(new IllegalParameterException("time has to be greater than 0"));
			isValid = false;
		}
		return isValid;
	}
}
