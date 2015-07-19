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

import de.uniol.inf.is.odysseus.parallelization.rcp.windows.table.StrategySelectionRow;

public class BenchmarkerConfiguration {

	public static final int DEFAULT_NUMBER_OF_EXECUTIONS = 5;
	public static final long DEFAULT_MAX_EXECUTION_TIME = 80000;
	public static final int DEFAULT_NUMBER_OF_ELEMENTS = 20000;
	
	private List<StrategySelectionRow> selectedStratgies;
	private List<Integer> degrees;
	private Integer buffersize;
	private boolean useThreadedBuffer = false;
	private boolean useNonThreadedBuffer = false;
	private Integer numberOfElements;
	private Long maximumExecutionTime;
	private boolean allowPostOptimization;
	private int numberOfExecutions;

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
					selectedRow.getStrategy(), selectedRow.getFragmentType(), selectedRow.getEndOperatorId());
			if (!selectedRow.getCustomDegrees().trim().isEmpty()){
				currentElement.setPossibleDegrees(selectedRow.getCustomDegrees().trim());
			}

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
			int index = 0;
			for (Integer degree : degrees) {
				if (useThreadedBuffer){
					cloneAndConfigureExecution(executions, benchmarkerExecution,
							index, degree, true);
				}
				if (useNonThreadedBuffer){
					cloneAndConfigureExecution(executions, benchmarkerExecution,
							index, degree, false);
				}
				index++;
			}
		}

		return executions;
	}

	private void cloneAndConfigureExecution(
			List<BenchmarkerExecution> executions,
			BenchmarkerExecution benchmarkerExecution, int index, Integer degree, boolean useThreadedBuffer) {
		BenchmarkerExecution clonedExecution = benchmarkerExecution.clone();
		clonedExecution.setDegree(degree, index);
		clonedExecution.setBuffersize(buffersize);
		clonedExecution.setNumberOfElements(numberOfElements);
		clonedExecution.setAllowPostOptmization(allowPostOptimization);
		clonedExecution.setNumberOfExecutions(numberOfExecutions);
		clonedExecution.setUseThreadedBuffer(useThreadedBuffer);
		executions.add(clonedExecution);
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
							currentElement.getStartOperatorid(),
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

	public Long getMaximumExecutionTime() {
		return maximumExecutionTime;
	}

	public void setMaximumExecutionTime(Long maximumExecutionTime) {
		this.maximumExecutionTime = maximumExecutionTime;
	}

	public boolean isUseThreadedBuffer() {
		return useThreadedBuffer;
	}

	public void setUseThreadedBuffer(boolean useThreadedBuffer) {
		this.useThreadedBuffer = useThreadedBuffer;
	}

	public boolean isUseNonThreadedBuffer() {
		return useNonThreadedBuffer;
	}

	public void setUseNonThreadedBuffer(boolean useNonThreadedBuffer) {
		this.useNonThreadedBuffer = useNonThreadedBuffer;
	}

	public int getNumberOfExecutions() {
		return numberOfExecutions;
	}

	public void setNumberOfExecutions(int numberOfExecutions) {
		this.numberOfExecutions = numberOfExecutions;
	}
}
