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
package de.uniol.inf.is.odysseus.logicaloperator.benchmark;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(name = "BENCHMARK", minInputPorts = 1, maxInputPorts = Integer.MAX_VALUE)
public class BenchmarkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 9094076133083176377L;
	private int processingTimeInns;
	private double selectivity;

	public BenchmarkAO(BenchmarkAO benchmarkAO) {
		super(benchmarkAO);
		this.processingTimeInns = benchmarkAO.processingTimeInns;
		this.selectivity = benchmarkAO.selectivity;
	}

	public BenchmarkAO() {
		this.processingTimeInns = -1;
		this.selectivity = -1;
	}

	public BenchmarkAO(int processingTimeInns, double selectivity) {
		this.processingTimeInns = processingTimeInns;
		this.selectivity = selectivity;
	}

	public int getProcessingTimeInns() {
		return processingTimeInns;
	}

	@Parameter(type = IntegerParameter.class, name = "TIME")
	public void setProcessingTimeInns(int processingTimeInns) {
		this.processingTimeInns = processingTimeInns;
	}

	public double getSelectivity() {
		return selectivity;
	}

	@Parameter(type = DoubleParameter.class)
	public void setSelectivity(double selectivity) {
		this.selectivity = selectivity;
	}

	@Override
	public SDFSchema getOutputSchemaIntern() {
		return getInputSchema(0);
	}

	@Override
	public boolean isAllPhysicalInputSet() {
		boolean set = true;
		for (int i = 0; i < getNumberOfInputs() && set; i++) {
			set = set && (getPhysSubscriptionTo(i) != null);
		}
		return set;
	}

	@Override
	public BenchmarkAO clone() {
		return new BenchmarkAO(this);
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (selectivity < 0) {
			addError(new IllegalParameterException(
					"selectivity has to be greater than 0"));
			isValid = false;
		}
		if (processingTimeInns < 0) {
			addError(new IllegalParameterException(
					"time has to be greater than 0"));
			isValid = false;
		}
		return isValid;
	}
}
