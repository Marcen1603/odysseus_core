package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import java.util.LinkedList;
import java.util.Random;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractDynamicScheduling;;

/**
 * Selects in every run a random ISource from all sources
 * @author Marco Grawunder
 *
 */

public class DynamicRandomScheduling extends AbstractDynamicScheduling {

	static private Random rnd = new Random(System.currentTimeMillis());
	private IPartialPlan plan;
		
	public DynamicRandomScheduling(IPartialPlan plan) {
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
		operators = new LinkedList<IIterableSource<?>>(this.plan.getIterableSources());
	}
	
}
