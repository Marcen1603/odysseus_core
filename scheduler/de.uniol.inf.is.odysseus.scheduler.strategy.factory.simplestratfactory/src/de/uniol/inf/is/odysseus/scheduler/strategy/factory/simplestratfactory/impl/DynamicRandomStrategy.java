package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import java.util.LinkedList;
import java.util.Random;

import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractDynamicSchedulingStrategy;;

/**
 * Selects in every run a random ISource from all sources
 * @author Marco Grawunder
 *
 */

public class DynamicRandomStrategy extends AbstractDynamicSchedulingStrategy {

	static private Random rnd = new Random(System.currentTimeMillis());
	private IPartialPlan plan;
		
	public DynamicRandomStrategy(IPartialPlan plan) {
		super(plan);	
		this.plan = plan;
	}

	@Override
	public IIterableSource<?> nextSource() {
		IIterableSource<?> op = null;
		synchronized (operators) {
			if (operators.size() > 0){
				op = operators.get(rnd.nextInt(operators.size()));
			}
		}
		return op;	 
	}

	@Override
	public void applyChangedPlan()
	{
		operators = new LinkedList<IIterableSource<?>>(this.plan.getIterableSource());
	}
	
}
