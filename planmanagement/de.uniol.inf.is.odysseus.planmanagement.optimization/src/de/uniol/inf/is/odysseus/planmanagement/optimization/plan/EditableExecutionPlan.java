package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class EditableExecutionPlan implements IEditableExecutionPlan {

	protected boolean open;

	protected ArrayList<IPartialPlan> partialPlans = new ArrayList<IPartialPlan>();

	protected ArrayList<IIterableSource<?>> iterableSources = new ArrayList<IIterableSource<?>>();

	protected boolean isOpen() {
		return open;
	}

	@Override
	public ArrayList<IPartialPlan> getPartialPlans() {
		return this.partialPlans;
	}

	@Override
	public ArrayList<IIterableSource<?>> getSources() {
		return this.iterableSources;
	}

	@Override
	public void setPartialPlans(ArrayList<IPartialPlan> patialPlans) {
		this.open = false;
		this.partialPlans = patialPlans;
	}

	@Override
	public void setSources(ArrayList<IIterableSource<?>> iterableSources) {
		this.open = false;
		this.iterableSources = iterableSources;
	}

	@Override
	public void open() throws OpenFailedException {
		if (!isOpen()) {
			//if there are no operators above a source ("SELECT * FROM Source"),
			//open sources directly, otherwise they get indirectly opened by
			//the open calls at the roots
			if (partialPlans.isEmpty()) {
				for (IIterableSource<?> curSource : iterableSources) {
					curSource.open();
				}
			} else {
				for (IPartialPlan partialPlan : this.partialPlans) {
					for (ISink<?> root : partialPlan.getRoots()) {
						root.open();
					}
				}
			}
		}
	}

	@Override
	public void close() {
		if (isOpen()) {
			for (IPartialPlan partialPlan : this.partialPlans) {
				for (ISink<?> root : partialPlan.getRoots()) {
					root.close();
				}
			}
		}
	}
}
