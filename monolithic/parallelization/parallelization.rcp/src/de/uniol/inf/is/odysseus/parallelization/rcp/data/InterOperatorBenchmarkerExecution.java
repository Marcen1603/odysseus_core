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

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.parallelization.interoperator.keyword.InterOperatorGlobalKeywordBuilder;
import de.uniol.inf.is.odysseus.parallelization.interoperator.keyword.InterOperatorParallelizationPreParserKeywordBuilder;

public class InterOperatorBenchmarkerExecution extends
		AbstractBenchmarkerExecution {
	private Integer degree;
	private Integer buffersize;
	private boolean useThreadedBuffer;
	private boolean allowPostOptmization;
	private Map<String, InterOperatorBenchmarkerExecutionElement> elements = new HashMap<String, InterOperatorBenchmarkerExecutionElement>();

	public InterOperatorBenchmarkerExecution() {
		super();
	}

	@Override
	public InterOperatorBenchmarkerExecution clone() {
		InterOperatorBenchmarkerExecution clone = new InterOperatorBenchmarkerExecution();
		clone.degree = this.degree;
		clone.buffersize = this.buffersize;
		clone.allowPostOptmization = this.allowPostOptmization;
		clone.useThreadedBuffer = this.useThreadedBuffer;
		clone.elements = new HashMap<String, InterOperatorBenchmarkerExecutionElement>();
		for (String elementKey : elements.keySet()) {
			clone.elements.put(elementKey, elements.get(elementKey).clone());
		}
		return clone;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(int degree, int iteration) {
		this.degree = degree;
		// set the global degree of the benchmark execution to all elements if
		// no custom value is defined
		for (InterOperatorBenchmarkerExecutionElement element : elements.values()) {
			element.setExecutionDegree(degree, iteration);
		}
	}

	public Integer getBuffersize() {
		return buffersize;
	}

	public void setBuffersize(Integer buffersize) {
		this.buffersize = buffersize;
	}

	public Map<String, InterOperatorBenchmarkerExecutionElement> getElements() {
		return elements;
	}

	public boolean isAllowPostOptmization() {
		return allowPostOptmization;
	}

	public void setAllowPostOptmization(boolean allowPostOptmization) {
		this.allowPostOptmization = allowPostOptmization;
	}

	public void addElement(String operatorId, InterOperatorBenchmarkerExecutionElement element) {
		if (!elements.containsKey(operatorId)) {
			elements.put(operatorId, element);
		} else {
			throw new IllegalArgumentException(
					"Duplicate execution element for operator id " + operatorId);
		}
	}

	public boolean isUseThreadedBuffer() {
		return useThreadedBuffer;
	}

	public void setUseThreadedBuffer(boolean useThreadedBuffer) {
		this.useThreadedBuffer = useThreadedBuffer;
	}

	@Override
	public String getOdysseusScript() {
		StringBuilder builder = new StringBuilder();
		builder.append(InterOperatorGlobalKeywordBuilder.buildParameterString(
				degree, buffersize, allowPostOptmization, useThreadedBuffer));
		for (InterOperatorBenchmarkerExecutionElement element : elements.values()) {
			builder.append(InterOperatorParallelizationPreParserKeywordBuilder
					.buildKeywordWithParameters(element.getStartOperatorid(),
							element.getEndOperatorId(),
							element.getExecutionDegree(), buffersize,
							element.getStrategy(), element.getFragmentType()));
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Inter-Operator Parallelization: Global degree: " + degree);
		builder.append(", Post optimization allowed: " + allowPostOptmization);
		builder.append(", Use threaded buffer: " + useThreadedBuffer);
		for (InterOperatorBenchmarkerExecutionElement element : elements.values()) {
			builder.append(", " + element.toString());
		}
		return builder.toString();
	}

}
