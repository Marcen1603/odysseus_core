package de.uniol.inf.is.odysseus.base.planmanagement.plan;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRule;

public abstract class AbstractPlanReoptimizeRule 
	implements IReoptimizeRule<IEditablePlan> {

	protected ArrayList<IPlan> reoptimizable;

	@Override
	public void addReoptimieRequester(IEditablePlan reoptimizable) {
		this.reoptimizable.add(reoptimizable);
	}
	
	@Override
	public void removeReoptimieRequester(IEditablePlan reoptimizable) {
		this.reoptimizable.remove(reoptimizable);
	}
	
	protected void fireReoptimizeEvent() {
		for (IPlan reoptimizableType : this.reoptimizable) {
			reoptimizableType.reoptimize();
		}
	}
}
