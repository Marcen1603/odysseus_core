package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
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
		this.customDegrees = "GLOBAL";
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
