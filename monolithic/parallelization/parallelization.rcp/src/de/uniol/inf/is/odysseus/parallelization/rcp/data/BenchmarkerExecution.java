package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.parallelization.interoperator.keyword.InterOperatorParallelizationKeywordParameterBuilder;
import de.uniol.inf.is.odysseus.parallelization.parameter.ParallizationKeywordParameterBuilder;

public class BenchmarkerExecution {
	private Integer degree;
	private Integer buffersize;
	private boolean useThreadedBuffer;
	private Integer numberOfElements;
	private boolean allowPostOptmization;
	private Map<String, BenchmarkExecutionElement> elements;
	private long executionTime;

	public BenchmarkerExecution(Integer degree, Integer buffersize,
			boolean allowPostOptmization, Integer numberOfElements) {
		this.degree = degree;
		this.buffersize = buffersize;
		this.allowPostOptmization = allowPostOptmization;
		this.numberOfElements = numberOfElements;
		this.elements = new HashMap<String, BenchmarkExecutionElement>();
	}

	public BenchmarkerExecution() {
		this.elements = new HashMap<String, BenchmarkExecutionElement>();
	}

	@Override
	public BenchmarkerExecution clone() {
		BenchmarkerExecution clone = new BenchmarkerExecution();
		clone.degree = this.degree;
		clone.buffersize = this.buffersize;
		clone.numberOfElements = this.numberOfElements;
		clone.allowPostOptmization = this.allowPostOptmization;
		clone.useThreadedBuffer = this.useThreadedBuffer;
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
	
	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public boolean isUseThreadedBuffer() {
		return useThreadedBuffer;
	}

	public void setUseThreadedBuffer(boolean useThreadedBuffer) {
		this.useThreadedBuffer = useThreadedBuffer;
	}

	public String getOdysseusScript() {
		StringBuilder builder = new StringBuilder();
		builder.append(ParallizationKeywordParameterBuilder
				.buildInterOperatorKeywordWithParameters(degree, buffersize,
						allowPostOptmization, useThreadedBuffer));
		for (BenchmarkExecutionElement element : elements.values()) {
			builder.append(InterOperatorParallelizationKeywordParameterBuilder
					.buildKeywordWithParameters(element.getStartOperatorid(),
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
	
	
	
	

}
