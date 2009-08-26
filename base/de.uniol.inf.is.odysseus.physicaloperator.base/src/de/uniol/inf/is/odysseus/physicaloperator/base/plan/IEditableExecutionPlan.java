package de.uniol.inf.is.odysseus.physicaloperator.base.plan;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;

public interface IEditableExecutionPlan extends IExecutionPlan {
	public void setSources(ArrayList<IIterableSource<?>> iterableSources);
	
	public void setPartialPlans(ArrayList<IPartialPlan> patialPlans);
	
	public void close();

	public void open() throws OpenFailedException;
}
