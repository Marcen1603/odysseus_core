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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.parallelization.interoperator.keyword.InterOperatorGlobalKeywordBuilder;
import de.uniol.inf.is.odysseus.parallelization.interoperator.keyword.InterOperatorParallelizationPreParserKeywordBuilder;

public class BenchmarkerExecution {
	private Integer degree;
	private Integer buffersize;
	private boolean useThreadedBuffer;
	private Integer numberOfElements;
	private boolean allowPostOptmization;
	private Map<String, BenchmarkExecutionElement> elements = new HashMap<String, BenchmarkExecutionElement>();
	private List<Long> executionTimes = new ArrayList<Long>();
	private int numberOfExecutions;

	public BenchmarkerExecution(Integer degree, Integer buffersize,
			boolean allowPostOptmization, Integer numberOfElements) {
		this.degree = degree;
		this.buffersize = buffersize;
		this.allowPostOptmization = allowPostOptmization;
		this.numberOfElements = numberOfElements;
	}

	public BenchmarkerExecution() {
		
	}

	@Override
	public BenchmarkerExecution clone() {
		BenchmarkerExecution clone = new BenchmarkerExecution();
		clone.degree = this.degree;
		clone.buffersize = this.buffersize;
		clone.numberOfElements = this.numberOfElements;
		clone.allowPostOptmization = this.allowPostOptmization;
		clone.useThreadedBuffer = this.useThreadedBuffer;
		clone.numberOfExecutions = this.numberOfExecutions;
		clone.executionTimes = new ArrayList<Long>(executionTimes);
		clone.elements = new HashMap<String, BenchmarkExecutionElement>();
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
		// set the global degree of the benchmark execution to all elements if no custom value is defined
		for (BenchmarkExecutionElement element : elements.values()) {
			element.setExecutionDegree(degree, iteration);
		}
	}

	public Integer getBuffersize() {
		return buffersize;
	}

	public void setBuffersize(Integer buffersize) {
		this.buffersize = buffersize;
	}

	public Integer getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(Integer numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public Map<String, BenchmarkExecutionElement> getElements() {
		return elements;
	}

	public boolean isAllowPostOptmization() {
		return allowPostOptmization;
	}

	public void setAllowPostOptmization(boolean allowPostOptmization) {
		this.allowPostOptmization = allowPostOptmization;
	}

	public void addElement(String operatorId, BenchmarkExecutionElement element) {
		if (!elements.containsKey(operatorId)) {
			elements.put(operatorId, element);
		} else {
			throw new IllegalArgumentException(
					"Duplicate execution element for operator id " + operatorId);
		}
	}
	
	public long getAverageExecutionTime(long maximumExecutionTime) {
		long sumExecutionTime = 0l;
		
		for (Long executionTime : executionTimes) {
			if (executionTime == -1l){
				return -1l;
			} else if (executionTime == 0l){
				return 0l;
			}  else if (executionTime >= maximumExecutionTime){
				return maximumExecutionTime;
			} else {
				sumExecutionTime += executionTime;				
			}
		}
		return sumExecutionTime / executionTimes.size();
	}

	public void addExecutionTime(long executionTime) {
		this.executionTimes.add(executionTime);
	}

	public boolean isUseThreadedBuffer() {
		return useThreadedBuffer;
	}

	public void setUseThreadedBuffer(boolean useThreadedBuffer) {
		this.useThreadedBuffer = useThreadedBuffer;
	}

	public String getOdysseusScript() {
		StringBuilder builder = new StringBuilder();
		builder.append(InterOperatorGlobalKeywordBuilder
				.buildParameterString(degree, buffersize,
						allowPostOptmization, useThreadedBuffer));
		for (BenchmarkExecutionElement element : elements.values()) {
			builder.append(InterOperatorParallelizationPreParserKeywordBuilder
					.buildKeywordWithParameters(element.getStartOperatorid(), element.getEndOperatorId(),
							element.getExecutionDegree(), buffersize, element.getStrategy(),
							element.getFragmentType()));
		} 
		return builder.toString();
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Global degree: "+degree);
		builder.append(", Post optimization allowed: "+allowPostOptmization);
		builder.append(", Use threaded buffer: "+useThreadedBuffer);
		for (BenchmarkExecutionElement element : elements.values()) {
			builder.append(", "+element.toString());
		}
		return builder.toString();
	}

	public int getNumberOfExecutions() {
		return numberOfExecutions;
	}

	public void setNumberOfExecutions(int numberOfExecutions) {
		this.numberOfExecutions = numberOfExecutions;
	}
	
	
	
	

}
