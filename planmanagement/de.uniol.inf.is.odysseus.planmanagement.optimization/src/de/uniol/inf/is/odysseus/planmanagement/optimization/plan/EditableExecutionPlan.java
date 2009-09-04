package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class EditableExecutionPlan implements IEditableExecutionPlan {

	protected boolean open;

	protected List<IPartialPlan> partialPlans = new ArrayList<IPartialPlan>();

	protected List<IIterableSource<?>> iterableSources = new ArrayList<IIterableSource<?>>();

	private List<IPhysicalOperator> roots;

	protected boolean isOpen() {
		return open;
	}

	@Override
	public List<IPartialPlan> getPartialPlans() {
		return this.partialPlans;
	}

	@Override
	public List<IIterableSource<?>> getSources() {
		return this.iterableSources;
	}

	@Override
	public void setPartialPlans(List<IPartialPlan> patialPlans) {
		this.open = false;
		this.partialPlans = patialPlans;
	}

	@Override
	public void setSources(List<IIterableSource<?>> iterableSources) {
		this.open = false;
		this.iterableSources = iterableSources;
	}

	@Override
	public void open() throws OpenFailedException {
		if (!isOpen()) {
			if (roots != null){
				for (IPhysicalOperator r:roots){
					r.open();
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

	public void setRoots(List<IPhysicalOperator> roots) {
		this.roots = roots;
	}
	
	@Override
	public List<IPhysicalOperator> getRoots() {
		return roots;
	}

	@Override
	public void initWith(IExecutionPlan newExecutionPlan) {
		this.setPartialPlans(new ArrayList<IPartialPlan>(
				newExecutionPlan.getPartialPlans()));
		this.setSources(new ArrayList<IIterableSource<?>>(
						newExecutionPlan.getSources()));
		this.setRoots(new ArrayList<IPhysicalOperator>(newExecutionPlan.getRoots()));
	}
	
}
