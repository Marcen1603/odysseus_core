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
package de.uniol.inf.is.odysseus.parallelization.rcp.windows.table;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

/**
 * this class represents one row inside of the strategy selection table
 * 
 * @author ChrisToenjesDeye
 *
 */
public class StrategySelectionRow {
	private int rowId;
	private ILogicalOperator logicalOperator;
	private String uniqueOperatorid;
	private IParallelTransformationStrategy<? extends ILogicalOperator> strategy;
	private Class<? extends AbstractFragmentAO> fragmentType;
	private String endOperatorId;
	private String customDegrees;

	public StrategySelectionRow(
			int rowId,
			ILogicalOperator logicalOperator,
			String uniqueOperatorid,
			IParallelTransformationStrategy<? extends ILogicalOperator> strategy,
			Class<? extends AbstractFragmentAO> fragmentType) {
		super();
		this.rowId = rowId;
		this.logicalOperator = logicalOperator;
		this.uniqueOperatorid = uniqueOperatorid;
		this.strategy = strategy;
		this.fragmentType = fragmentType;
		this.endOperatorId = "";
		this.customDegrees = "";
	}

	/**
	 * validate contents of this row
	 * 
	 * @param otherStrategies
	 * @param globalDegrees
	 */
	public void validate(List<StrategySelectionRow> otherStrategies,
			List<Integer> globalDegrees) {
		if (!endOperatorId.isEmpty()) {
			// validate the endoperator id
			if (uniqueOperatorid.equalsIgnoreCase(endOperatorId)) {
				// we don't need it, if start and end id are equal
				endOperatorId = null;
			}

			// check if the id is valid
			// if the id is valid, check if the operator is downstream located
			ILogicalOperator downstreamOperator = LogicalGraphHelper
					.findDownstreamOperatorWithId(endOperatorId,
							logicalOperator);
			if (downstreamOperator == null) {
				throw new IllegalArgumentException(
						"Id for end operator is invalid. Maybe the id is "
								+ "incorrect or the operator is not downstream located");
			} else {
				// check if the endoperator id is the start id of another
				// strategy. this is not allowed
				for (StrategySelectionRow otherStrategy : otherStrategies) {
					if (otherStrategy.getUniqueOperatorid().equalsIgnoreCase(
							endOperatorId)) {
						throw new IllegalArgumentException(
								"It is not allowed to use an end operator id, while strategies "
										+ "exists with the same start operator id");
					}
				}
				try {
					// check if the way from start to end operator is valid (no
					// splits or stateful operators etc)
					LogicalGraphHelper.checkWayToEndPoint(logicalOperator,
							endOperatorId, true);
				} catch (Exception e) {
					throw new IllegalArgumentException("Way from start id "
							+ uniqueOperatorid + " to end operator id "
							+ endOperatorId + "is not valid.");
				}
			}
		}

		// check if the degrees are valid
		if (!customDegrees.isEmpty()) {
			String[] splittedDegrees = customDegrees.trim().split(",");
			// the number of custom degrees need to be less or equal with the
			// global degrees
			if (splittedDegrees.length > globalDegrees.size()) {
				throw new IllegalArgumentException(
						"Number of custom degrees need to be less or equal than global degrees");
			} else {
				// check if all degrees are integers
				for (int i = 0; i < splittedDegrees.length; i++) {
					try {
						Integer.parseInt(splittedDegrees[i]);
					} catch (Exception e) {
						throw new IllegalArgumentException(
								"Custom degrees contains illegal values. Please use integers");
					}
				}
			}
		}
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public ILogicalOperator getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(ILogicalOperator logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	public String getUniqueOperatorid() {
		return uniqueOperatorid;
	}

	public void setUniqueOperatorid(String uniqueOperatorid) {
		this.uniqueOperatorid = uniqueOperatorid;
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

	public String getCustomDegrees() {
		return customDegrees;
	}

	public void setCustomDegrees(String customDegrees) {
		this.customDegrees = customDegrees;
	}
}
