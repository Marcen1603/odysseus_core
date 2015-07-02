package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.HashMap;
import java.util.Map;

public class BenchmarkerExecution {
	private Integer degree;
	private Integer buffersize;
	private Integer numberOfElements;
	private Map<String, BenchmarkExecutionElement> elements;

	public BenchmarkerExecution(Integer degree, Integer buffersize,
			Integer numberOfElements) {
		this.degree = degree;
		this.buffersize = buffersize;
		this.numberOfElements = numberOfElements;
		this.elements = new HashMap<String, BenchmarkExecutionElement>();
	}

	public BenchmarkerExecution() {
		this.elements = new HashMap<String, BenchmarkExecutionElement>();
	}
	
	@Override
	public BenchmarkerExecution clone(){
		BenchmarkerExecution clone = new BenchmarkerExecution();
		clone.degree = this.degree;
		clone.buffersize = this.buffersize;
		clone.numberOfElements = this.numberOfElements;
		clone.elements = new HashMap<String, BenchmarkExecutionElement>(elements);
		return clone;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
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

	public void addElement(String operatorId, BenchmarkExecutionElement element) {
		if (!elements.containsKey(operatorId)) {
			elements.put(operatorId, element);
		} else {
			throw new IllegalArgumentException(
					"Duplicate execution element for operator id " + operatorId);
		}
	}

}
