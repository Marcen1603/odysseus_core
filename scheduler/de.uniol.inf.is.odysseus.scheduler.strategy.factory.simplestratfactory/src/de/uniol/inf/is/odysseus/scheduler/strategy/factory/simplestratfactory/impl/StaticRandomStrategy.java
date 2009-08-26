package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListSchedulingStrategy;
/**
 * Calculates an intial random strategy
 * @author Marco Grawunder
 *
 */

public class StaticRandomStrategy extends AbstractExecListSchedulingStrategy {

	public StaticRandomStrategy(IPartialPlan plan) {
		super(plan, true);
	}

	@Override
	protected List<IIterableSource<?>> calculateExecutionList(
			IPartialPlan operators) {
		List<IIterableSource<?>> ops = new LinkedList<IIterableSource<?>>(operators.getIterableSource());
		List<IIterableSource<?>> execList = new LinkedList<IIterableSource<?>>();
		Random rnd = new Random(System.currentTimeMillis());
		int size = ops.size(); 
		while (size > 0){
			execList.add(ops.remove(rnd.nextInt(size)));
			size = ops.size();
		}
		return execList;
	}
	
	@Override
	public void applyChangedPlan()
	{
		this.executionList = this.calculateExecutionList(this.getPlan());
		this.iterator = this.executionList.iterator();
	}

}
