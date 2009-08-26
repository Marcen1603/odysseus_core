package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListSchedulingStrategy;

/**
 * Implements round robin scheduling of operators.
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RoundRobinStrategy extends AbstractExecListSchedulingStrategy {

	public RoundRobinStrategy(IPartialPlan operators, boolean useIter) {
		super(operators, useIter);
	}

	@Override
	protected List<IIterableSource<?>> calculateExecutionList(IPartialPlan operators) {
		return new LinkedList<IIterableSource<?>>(operators.getIterableSource());
	}
	
	@Override
	public void applyChangedPlan()
	{
//		System.out.println("RoundRobinStrategy: Updating changed Plan");
		this.executionList = this.calculateExecutionList(this.getPlan());
		this.iterator = this.executionList.iterator();
	}

}
