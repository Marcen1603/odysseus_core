package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListScheduling;

/**
 * Implements round robin scheduling of operators.
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RoundRobinScheduling extends AbstractExecListScheduling {

	public RoundRobinScheduling(IPartialPlan operators) {
		super(operators);
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
