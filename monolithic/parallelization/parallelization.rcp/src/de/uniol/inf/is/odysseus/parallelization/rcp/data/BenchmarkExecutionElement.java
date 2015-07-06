package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class BenchmarkExecutionElement {

	private String uniqueOperatorid;
	private IParallelTransformationStrategy<? extends ILogicalOperator> strategy;
	private Class<? extends AbstractFragmentAO> fragmentType;

	public BenchmarkExecutionElement(
			String uniqueOperatorid,
			IParallelTransformationStrategy<? extends ILogicalOperator> strategy,
			Class<? extends AbstractFragmentAO> fragmentType) {
		this.uniqueOperatorid = uniqueOperatorid;
		this.strategy = strategy;
		this.fragmentType = fragmentType;
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

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("( OperatorId: "+uniqueOperatorid);
		builder.append(", Strategy: "+strategy.getName());
		builder.append(", Fragmentation: "+fragmentType.getSimpleName()+") ");
		return builder.toString();
	}
	
}

