package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

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

	public void validate(List<StrategySelectionRow> otherStrategies,
			List<Integer> globalDegrees) {
		if (!endOperatorId.isEmpty()) {
			if (uniqueOperatorid.equals(endOperatorId)) {
				// we don't need it, if start and end id are equal
				endOperatorId = null;
			}

			ILogicalOperator downstreamOperator = LogicalGraphHelper
					.findDownstreamOperatorWithId(endOperatorId,
							logicalOperator);
			if (downstreamOperator == null) {
				throw new IllegalArgumentException(
						"Id for end operator is invalid. Maybe the id is "
								+ "incorrect or the operator is not downstream located");
			} else {
				for (StrategySelectionRow otherStrategy : otherStrategies) {
					if (otherStrategy.getUniqueOperatorid().equals(
							endOperatorId)) {
						throw new IllegalArgumentException(
								"It is not allowed to use an end operator id, while strategies "
										+ "exists with the same start operator id");
					}
				}
				try {
					LogicalGraphHelper.checkWayToEndPoint(logicalOperator,
							false, endOperatorId, true);
				} catch (Exception e) {
					throw new IllegalArgumentException("Way from start id "
							+ uniqueOperatorid + " to end operator id "
							+ endOperatorId + "is not valid.");
				}
			}
		}

		if (!customDegrees.isEmpty()) {
			String[] splittedDegrees = customDegrees.trim().split(",");
			if (splittedDegrees.length > globalDegrees.size()) {
				throw new IllegalArgumentException(
						"Number of custom degrees need to be less or equal than global degrees");
			} else {
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
}
