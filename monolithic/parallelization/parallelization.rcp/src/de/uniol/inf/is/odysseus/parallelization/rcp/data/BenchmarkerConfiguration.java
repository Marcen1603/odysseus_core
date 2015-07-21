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

	// global configuration
	private boolean useInterOperatorParallelization = false;
	private Integer numberOfElements;
	private Long maximumExecutionTime;
	private int numberOfExecutions;

	// inter operator configuration
	private boolean useIntraOperatorParallelization = false;
	private List<Integer> interOperatorDegrees;
	private Integer buffersize;
	private boolean useThreadedBuffer = false;
	private boolean useNonThreadedBuffer = false;
	private boolean allowPostOptimization;
	private List<StrategySelectionRow> selectedStratgies;

	// intra operator configuration
	private List<Integer> intraOperatorDegrees;

	public List<StrategySelectionRow> getSelectedStratgies() {
		return selectedStratgies;
	}

	public void setSelectedStratgies(
			List<StrategySelectionRow> selectedStratgies) {
		this.selectedStratgies = selectedStratgies;
	}

	public List<Integer> getInterOperatorDegrees() {
		return interOperatorDegrees;
	}

	public void setInterOperatorDegrees(List<Integer> degrees) {
		this.interOperatorDegrees = degrees;
	}

	public List<Integer> getIntraOperatorDegrees() {
		return intraOperatorDegrees;
	}

	public void setIntraOperatorDegrees(List<Integer> degrees) {
		this.intraOperatorDegrees = degrees;
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

	public List<IBenchmarkerExecution> getBenchmarkerExecutions() {
		List<IBenchmarkerExecution> executions = new ArrayList<IBenchmarkerExecution>();

		// Calculating executions for inter operator parallelization
		// map rows to elements
		if (useInterOperatorParallelization) {
			Map<String, List<InterOperatorBenchmarkerExecutionElement>> executionElements = new HashMap<String, List<InterOperatorBenchmarkerExecutionElement>>();
			for (StrategySelectionRow selectedRow : selectedStratgies) {
				InterOperatorBenchmarkerExecutionElement currentElement = new InterOperatorBenchmarkerExecutionElement(
						selectedRow.getUniqueOperatorid(),
						selectedRow.getStrategy(),
						selectedRow.getFragmentType(),
						selectedRow.getEndOperatorId());
				if (!selectedRow.getCustomDegrees().trim().isEmpty()) {
					currentElement.setPossibleDegrees(selectedRow
							.getCustomDegrees().trim());
				}

				if (!executionElements.containsKey(selectedRow
						.getUniqueOperatorid())) {
					executionElements.put(selectedRow.getUniqueOperatorid(),
							new ArrayList<InterOperatorBenchmarkerExecutionElement>());
				}

				executionElements.get(selectedRow.getUniqueOperatorid()).add(
						currentElement);
			}

			List<InterOperatorBenchmarkerExecution> combinedElements = combineElements(
					executionElements, 0, null,
					new ArrayList<InterOperatorBenchmarkerExecution>());
			for (InterOperatorBenchmarkerExecution benchmarkerExecution : combinedElements) {
				int index = 0;
				for (Integer degree : interOperatorDegrees) {
					if (useThreadedBuffer) {
						cloneAndConfigureExecution(executions,
								benchmarkerExecution, index, degree, true);
					}
					if (useNonThreadedBuffer) {
						cloneAndConfigureExecution(executions,
								benchmarkerExecution, index, degree, false);
					}
					index++;
				}
			}
		}

		// Calculating executions for intra operator parallelization
		if (useIntraOperatorParallelization) {
			for (Integer degree : intraOperatorDegrees) {
				executions.add(new IntraOperatorBenchmarkerExecution(degree));
			}
		}

		return executions;
	}

	private void cloneAndConfigureExecution(
			List<IBenchmarkerExecution> executions,
			InterOperatorBenchmarkerExecution benchmarkerExecution, int index,
			Integer degree, boolean useThreadedBuffer) {
		InterOperatorBenchmarkerExecution clonedExecution = benchmarkerExecution
				.clone();
		clonedExecution.setDegree(degree, index);
		clonedExecution.setUseThreadedBuffer(useThreadedBuffer);
		executions.add(clonedExecution);
	}

	private List<InterOperatorBenchmarkerExecution> combineElements(
			Map<String, List<InterOperatorBenchmarkerExecutionElement>> executionElements,
			int currentLevel, List<InterOperatorBenchmarkerExecutionElement> currentElements,
			List<InterOperatorBenchmarkerExecution> result) {
		if (currentElements == null) {
			currentElements = new ArrayList<InterOperatorBenchmarkerExecutionElement>();
		}

		for (InterOperatorBenchmarkerExecutionElement executionElement : executionElements
				.get(getNameAtIndex(executionElements, currentLevel))) {

			currentElements.add(executionElement);
			if (currentLevel >= executionElements.keySet().size() - 1) {
				InterOperatorBenchmarkerExecution benchmarkerExecution = new InterOperatorBenchmarkerExecution();
				benchmarkerExecution.setBuffersize(buffersize);
				benchmarkerExecution
						.setAllowPostOptmization(allowPostOptimization);
				for (InterOperatorBenchmarkerExecutionElement currentElement : currentElements) {
					benchmarkerExecution
							.addElement(currentElement.getStartOperatorid(),
									currentElement);
				}
				result.add(benchmarkerExecution);
			} else {
				int nextLevel = currentLevel + 1;
				combineElements(executionElements, nextLevel, currentElements,
						result);
			}
			currentElements.remove(currentElements.size() - 1);
		}

		return result;
	}

	private String getNameAtIndex(
			Map<String, List<InterOperatorBenchmarkerExecutionElement>> executionElements,
			int currentPosition) {
		ArrayList<String> arrayList = new ArrayList<String>(
				executionElements.keySet());
		return arrayList.get(currentPosition);
	}

	public boolean isUseInterOperatorParallelization() {
		return useInterOperatorParallelization;
	}

	public void setUseInterOperatorParallelization(
			boolean useInterOperatorParallelization) {
		this.useInterOperatorParallelization = useInterOperatorParallelization;
	}

	public boolean isUseIntraOperatorParallelization() {
		return useIntraOperatorParallelization;
	}

	public void setUseIntraOperatorParallelization(
			boolean useIntraOperatorParallelization) {
		this.useIntraOperatorParallelization = useIntraOperatorParallelization;
	}

}
