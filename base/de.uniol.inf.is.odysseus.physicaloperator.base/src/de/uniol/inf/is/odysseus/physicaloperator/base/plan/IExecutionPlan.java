package de.uniol.inf.is.odysseus.physicaloperator.base.plan;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;

public interface IExecutionPlan {
	public ArrayList<IIterableSource<?>> getSources();
	
	public ArrayList<IPartialPlan> getPartialPlans();
}
