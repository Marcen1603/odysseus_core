/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

/**
 * this execution element represents the custom configurations for each operator
 * in intra operator parallelization
 * 
 * @author ChrisToenjesDeye
 *
 */
public class InterOperatorBenchmarkerExecutionElement {

	private String startOperatorid;
	private String endOperatorId;
	private IParallelTransformationStrategy<? extends ILogicalOperator> strategy;
	private Class<? extends AbstractFragmentAO> fragmentType;
	private List<Integer> possibleDegrees;
	private int executionDegree;
	private boolean useThreadedOperators;

	public InterOperatorBenchmarkerExecutionElement(
			String uniqueOperatorid,
			IParallelTransformationStrategy<? extends ILogicalOperator> strategy,
			Class<? extends AbstractFragmentAO> fragmentType,
			String endOperatorId, boolean useThreadedOperators) {
		this.startOperatorid = uniqueOperatorid;
		this.strategy = strategy;
		this.fragmentType = fragmentType;
		this.endOperatorId = endOperatorId;
		this.useThreadedOperators = useThreadedOperators;
		this.possibleDegrees = new ArrayList<Integer>();
	}

	public InterOperatorBenchmarkerExecutionElement(
			InterOperatorBenchmarkerExecutionElement other) {
		this.startOperatorid = other.startOperatorid;
		this.strategy = other.strategy;
		this.fragmentType = other.fragmentType;
		this.endOperatorId = other.endOperatorId;
		this.executionDegree = other.executionDegree;
		this.useThreadedOperators = other.useThreadedOperators;
		this.possibleDegrees = new ArrayList<Integer>(other.possibleDegrees);
	}

	/**
	 * custom to string method. this string is used to show the currently
	 * executed execution in benchmarker UI
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("( OperatorId: " + startOperatorid);
		if (!this.endOperatorId.isEmpty()) {
			builder.append(", End operator id: " + this.endOperatorId);
		}
		builder.append(", degree: " + executionDegree);
		builder.append(", Strategy: " + strategy.getName());
		builder.append(", Fragmentation: " + fragmentType.getSimpleName()
				+ ") ");
		builder.append(", Use threaded operators: " + useThreadedOperators);
		return builder.toString();
	}

	/**
	 * the execution degree is custom only for this element. if the second
	 * degree value is used in global configuration, also the second degree
	 * value from this custom degrees is used
	 * 
	 * @param executionDegree
	 * @param iteration
	 */
	public void setExecutionDegree(int executionDegree, int iteration) {
		if (possibleDegrees.isEmpty()) {
			this.executionDegree = executionDegree;
		} else {
			if (iteration < possibleDegrees.size()) {
				this.executionDegree = possibleDegrees.get(iteration)
						.intValue();
			} else {
				this.executionDegree = executionDegree;
			}
		}
	}

	/**
	 * sets the multiple possible degrees for this execution element from a
	 * comma seperated string
	 * 
	 * @param possibleDegrees
	 */
	public void setPossibleDegrees(String possibleDegrees) {
		String[] splittedPossibleDegrees = possibleDegrees.trim().split(",");
		for (int i = 0; i < splittedPossibleDegrees.length; i++) {
			this.possibleDegrees.add(Integer
					.parseInt(splittedPossibleDegrees[i]));
		}
	}

	@Override
	public InterOperatorBenchmarkerExecutionElement clone() {
		return new InterOperatorBenchmarkerExecutionElement(this);
	}

	public String getStartOperatorid() {
		return startOperatorid;
	}

	public void setStartOperatorid(String uniqueOperatorid) {
		this.startOperatorid = uniqueOperatorid;
	}

	public IParallelTransformationStrategy<? extends ILogicalOperator> getStrategy() {
		return strategy;
	}

	public void setStrategy(
			IParallelTransformationStrategy<? extends ILogicalOperator> strategy) {
		this.strategy = strategy;
	}

	public Class<? extends AbstractFragmentAO> getFragmentType() {
		return fragmentType;
	}

	public void setFragmentType(Class<? extends AbstractFragmentAO> fragmentType) {
		this.fragmentType = fragmentType;
	}

	public String getEndOperatorId() {
		return endOperatorId;
	}

	public void setEndOperatorId(String endOperatorId) {
		this.endOperatorId = endOperatorId;
	}

	public List<Integer> getPossibleDegrees() {
		return possibleDegrees;
	}

	public Integer getPossibleDegreeAtIndex(int index) {
		return possibleDegrees.get(index);
	}

	public void setPossibleDegrees(List<Integer> possibleDegrees) {
		this.possibleDegrees = possibleDegrees;
	}

	public int getExecutionDegree() {
		return executionDegree;
	}

	public boolean isUseThreadedOperators() {
		return useThreadedOperators;
	}

	public void setUseThreadedOperators(boolean useThreadedOperators) {
		this.useThreadedOperators = useThreadedOperators;
	}

}
