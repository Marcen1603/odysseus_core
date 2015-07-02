package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.parallelization.rcp.windows.StrategySelectionRow;

public class BenchmarkerConfiguration {

	private List<StrategySelectionRow> selectedStratgies;
	private List<Integer> degrees;
	private Integer buffersize;
	private Integer numberOfElements;
	private boolean allowPostOptimization;

	public List<StrategySelectionRow> getSelectedStratgies() {
		return selectedStratgies;
	}

	public void setSelectedStratgies(
			List<StrategySelectionRow> selectedStratgies) {
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

	public boolean isAllowPostOptimization() {
		return allowPostOptimization;
	}

	public void setAllowPostOptimization(boolean allowPostOptimization) {
		this.allowPostOptimization = allowPostOptimization;
	}
	
	public List<BenchmarkerExecution> getBenchmarkerExecutions() {
		// map rows to elements
		Map<String, List<BenchmarkExecutionElement>> executionElements = new HashMap<String, List<BenchmarkExecutionElement>>();
		for (StrategySelectionRow selectedRow : selectedStratgies) {
			BenchmarkExecutionElement currentElement = new BenchmarkExecutionElement(
					selectedRow.getUniqueOperatorid(),
					selectedRow.getStrategy(), selectedRow.getFragmentType());

			if (!executionElements.containsKey(selectedRow
					.getUniqueOperatorid())) {
				executionElements.put(selectedRow.getUniqueOperatorid(),
						new ArrayList<BenchmarkExecutionElement>());
			}

			executionElements.get(selectedRow.getUniqueOperatorid()).add(
					currentElement);
		}

		List<BenchmarkerExecution> executions = new ArrayList<BenchmarkerExecution>();

		List<BenchmarkerExecution> combinedElements = combineElements(executionElements, 0, null,
				new ArrayList<BenchmarkerExecution>());
		for (BenchmarkerExecution benchmarkerExecution : combinedElements) {
			for (Integer degree : degrees) {
				BenchmarkerExecution clonedExecution = benchmarkerExecution.clone();
				clonedExecution.setDegree(degree);
				clonedExecution.setBuffersize(buffersize);
				clonedExecution.setNumberOfElements(numberOfElements);
				clonedExecution.setAllowPostOptmization(allowPostOptimization);
				executions.add(clonedExecution);
			}
		}

		return executions;
	}

	private List<BenchmarkerExecution> combineElements(
			Map<String, List<BenchmarkExecutionElement>> executionElements,
			int currentLevel,
			List<BenchmarkExecutionElement> currentElements,
			List<BenchmarkerExecution> result) {
		if (currentElements == null) {
			currentElements = new ArrayList<BenchmarkExecutionElement>();
		}

		for (BenchmarkExecutionElement executionElement : executionElements
				.get(getNameAtIndex(executionElements, currentLevel))) {

			currentElements.add(executionElement);
			if (currentLevel >= executionElements.keySet().size() - 1) {
				BenchmarkerExecution benchmarkerExecution = new BenchmarkerExecution();
				for (BenchmarkExecutionElement currentElement : currentElements) {
					benchmarkerExecution.addElement(
							currentElement.getUniqueOperatorid(),
							currentElement);
				}
				result.add(benchmarkerExecution);
			} else {
				int nextLevel = currentLevel+1;
				combineElements(executionElements, nextLevel,
						currentElements, result);
			}
			currentElements.remove(currentElements.size() - 1);
		}

		return result;
	}

	private String getNameAtIndex(
			Map<String, List<BenchmarkExecutionElement>> executionElements,
			int currentPosition) {
		ArrayList<String> arrayList = new ArrayList<String>(executionElements.keySet());
		return arrayList.get(currentPosition);
	}
}
