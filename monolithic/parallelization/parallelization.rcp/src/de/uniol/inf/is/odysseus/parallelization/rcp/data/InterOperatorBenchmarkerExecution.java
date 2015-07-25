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

/**
 * benchmarker execution for inter operator parallelization. Contains all needed
 * data to create the specific odysseus script keywords
 * 
 * @author ChrisToenjesDeye
 *
 */
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

	public InterOperatorBenchmarkerExecution(
			InterOperatorBenchmarkerExecution other) {
		this.degree = other.degree;
		this.buffersize = other.buffersize;
		this.allowPostOptmization = other.allowPostOptmization;
		this.useThreadedBuffer = other.useThreadedBuffer;
		this.elements = new HashMap<String, InterOperatorBenchmarkerExecutionElement>();
		for (String elementKey : other.elements.keySet()) {
			this.elements.put(elementKey, other.elements.get(elementKey)
					.clone());
		}
	}

	@Override
	public InterOperatorBenchmarkerExecution clone() {
		return new InterOperatorBenchmarkerExecution(this);
	}

	public Integer getDegree() {
		return degree;
	}

	/**
	 * sets the degree to the global execution and also sets it to all elements
	 * (but only if the elements contains no custom degrees)
	 * 
	 * @param degree
	 * @param iteration
	 */
	public void setDegree(int degree, int iteration) {
		this.degree = degree;
		// set the global degree of the benchmark execution to all elements if
		// no custom value is defined
		for (InterOperatorBenchmarkerExecutionElement element : elements
				.values()) {
			element.setExecutionDegree(degree, iteration);
		}
	}

	/**
	 * adds an execution element (contains strategies for each operator ... )
	 * execution elements represents the combinations from the different
	 * selected strategies and fragmentations
	 * 
	 * @param operatorId
	 * @param element
	 */
	public void addElement(String operatorId,
			InterOperatorBenchmarkerExecutionElement element) {
		if (!elements.containsKey(operatorId)) {
			elements.put(operatorId, element);
		} else {
			throw new IllegalArgumentException(
					"Duplicate execution element for operator id " + operatorId);
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

	public boolean isUseThreadedBuffer() {
		return useThreadedBuffer;
	}

	public void setUseThreadedBuffer(boolean useThreadedBuffer) {
		this.useThreadedBuffer = useThreadedBuffer;
	}

	/**
	 * returns the odysseus script keywords for this execution
	 * 
	 * @return odysseus script keywords
	 */
	@Override
	public String getOdysseusScript() {
		StringBuilder builder = new StringBuilder();
		// create #PARALLELIZATION keyword
		builder.append(InterOperatorGlobalKeywordBuilder.buildParameterString(
				degree, buffersize, allowPostOptmization, useThreadedBuffer));
		// create #INTEROPERATOR keyword for each element
		for (InterOperatorBenchmarkerExecutionElement element : elements
				.values()) {
			builder.append(InterOperatorParallelizationPreParserKeywordBuilder
					.buildKeywordWithParameters(element.getStartOperatorid(),
							element.getEndOperatorId(),
							element.getExecutionDegree(), buffersize,
							element.getStrategy(), element.getFragmentType(),
							element.isUseThreadedOperators()));
		}
		return builder.toString();
	}

	/**
	 * custom to string method. this string is used to show the currently
	 * executed execution in benchmarker UI
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Inter-Operator Parallelization: Global degree: "
				+ degree);
		builder.append(", Post optimization allowed: " + allowPostOptmization);
		builder.append(", Use threaded buffer: " + useThreadedBuffer);
		for (InterOperatorBenchmarkerExecutionElement element : elements
				.values()) {
			builder.append(", " + element.toString());
		}
		return builder.toString();
	}

}
