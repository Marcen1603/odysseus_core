package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

/**
 * EditableExecutionPlan is an object which is used to store an execution plan.
 * This structure is used for communication between plan management,
 * optimization and scheduling.
 * 
 * @author Wolf Bauer
 * 
 */
public class ExecutionPlan implements IExecutionPlan {

	/**
	 * Describes if the physical operators are opened.
	 */
	protected boolean open;

	/**
	 * List of all parts of this execution plan. Used for scheduling.
	 */
	protected List<IPartialPlan> partialPlans = new ArrayList<IPartialPlan>();

	/**
	 * List of all iterable sources. Used for scheduling.
	 */
	protected List<IIterableSource<?>> iterableSources = new ArrayList<IIterableSource<?>>();

	/**
	 * List of all query roots.
	 */
	private List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();

	/**
	 * Describes if the physical operators are opened.
	 * 
	 * @return physical operators are opened or not.
	 */
	protected boolean isOpen() {
		return open;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan#
	 * getPartialPlans()
	 */
	@Override
	public List<IPartialPlan> getPartialPlans() {
		return this.partialPlans;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan#getSources
	 * ()
	 */
	@Override
	public List<IIterableSource<?>> getSources() {
		return this.iterableSources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan
	 * #setPartialPlans(java.util.List)
	 */
	@Override
	public void setPartialPlans(List<IPartialPlan> patialPlans) {
		this.open = false;
		this.partialPlans = patialPlans;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan
	 * #setSources(java.util.List)
	 */
	@Override
	public void setSources(List<IIterableSource<?>> iterableSources) {
		this.open = false;
		this.iterableSources = iterableSources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan
	 * #open()
	 */
	@Override
	public void open() throws OpenFailedException {
		if (!isOpen()) {
			for (IPhysicalOperator r : roots) {
				r.open();
			}
			this.open = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan
	 * #close()
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan
	 * #setRoots(java.util.List)
	 */
	public void setRoots(List<IPhysicalOperator> roots) {
		this.roots = roots;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan#getRoots
	 * ()
	 */
	@Override
	public List<IPhysicalOperator> getRoots() {
		return roots;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan
	 * #
	 * initWith(de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan
	 * )
	 */
	@Override
	public void initWith(IExecutionPlan newExecutionPlan) {
		this.setPartialPlans(new ArrayList<IPartialPlan>(newExecutionPlan
				.getPartialPlans()));
		this.setSources(new ArrayList<IIterableSource<?>>(newExecutionPlan
				.getSources()));
		this.setRoots(new ArrayList<IPhysicalOperator>(newExecutionPlan
				.getRoots()));
	}

}
