package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.parallelization.rcp.windows.StrategySelectionRow;

public class BenchmarkerConfiguration {
	
	private List<StrategySelectionRow> selectedStratgies;
	private List<Integer> degrees;
	private Integer buffersize;
	private Integer numberOfElements;

	public List<StrategySelectionRow> getSelectedStratgies() {
		return selectedStratgies;
	}

	public void setSelectedStratgies(List<StrategySelectionRow> selectedStratgies) {
		this.selectedStratgies = selectedStratgies;
	}

	public List<Integer> getDegrees() {
		return degrees;
	}

	public void setDegrees(List<Integer> degrees) {
		this.degrees = degrees;
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
	
	public List<BenchmarkerExecution> getBenchmarkerExecutions(){
		List<BenchmarkerExecution> executions = new ArrayList<BenchmarkerExecution>();
		
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		executions.add(new BenchmarkerExecution());
		
		return executions;
	}
}
