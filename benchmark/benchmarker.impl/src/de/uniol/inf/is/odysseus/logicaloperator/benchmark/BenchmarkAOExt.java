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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;

/**
 * @author Jonas Jacobi
 */
public class BenchmarkAOExt extends AbstractLogicalOperator {

	private static final long serialVersionUID = 9094076133083176377L;
	private int processingTimeInns;
	private double selectivity;
	
	public BenchmarkAOExt(BenchmarkAOExt benchmarkAO) {
		super();
		this.processingTimeInns = benchmarkAO.processingTimeInns;
		this.selectivity = benchmarkAO.selectivity;
	}
	
	public BenchmarkAOExt(int processingTimeInns, double selectivity) {
		this.processingTimeInns = processingTimeInns;
		this.selectivity = selectivity;
	}
	
	public int getProcessingTimeInns() {
		return processingTimeInns;
	}
	
	public void setProcessingTimeInns(int processingTimeInns) {
		this.processingTimeInns = processingTimeInns;
	}
	
	public double getSelectivity() {
		return selectivity;
	}
	
	public void setSelectivity(double selectivity) {
		this.selectivity = selectivity;
	}

	
	@Override
	public boolean isAllPhysicalInputSet() {
		boolean set = true;
		for (int i=0;i<getNumberOfInputs() && set;i++){
			set = set && (getPhysSubscriptionTo(i) != null);
		}
		return set;
	}
	
	@Override
	public BenchmarkAOExt clone() {
		return new BenchmarkAOExt(this);
	}
}
