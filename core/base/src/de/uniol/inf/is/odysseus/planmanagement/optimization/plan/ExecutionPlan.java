package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

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
	 * List of all leaf sources that need to be scheduled periodically.
	 */
	protected List<IIterableSource<?>> leafSources = new ArrayList<IIterableSource<?>>();

	private List<IPhysicalOperator> roots;

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
	 * @seede.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan#
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
	 * de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan#getSources
	 * ()
	 */
	@Override
	public List<IIterableSource<?>> getLeafSources() {
		return this.leafSources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan
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
	 * @see de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan
	 * #setSources(java.util.List)
	 */
	@Override
	public void setLeafSources(List<IIterableSource<?>> leafSources) {
		this.open = false;
		this.leafSources = leafSources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan
	 * #open()
	 */
	@Override
	public void open() throws OpenFailedException {
		if (!isOpen()) {
			// Call only if theres a plan to execute
			if (roots != null) {
				for (IPhysicalOperator root : roots) {
					if (root.isSink()){
						((ISink<?>)root).open();
					}else{
						throw new IllegalArgumentException("Open cannot be called on a source");
					}
				}

				// for (IPartialPlan partialPlan : this.partialPlans) {
				// for (ISink<?> root : partialPlan.getRoots()) {
				// root.open();
				// }
				// }
				this.open = true;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan
	 * #close()
	 */
	@Override
	public void close() {
		if (isOpen()) {
			for (IPartialPlan partialPlan : this.partialPlans) {
				for (IPhysicalOperator root : partialPlan.getRoots()) {
					if (root.isSink()){
						((ISink<?>)root).close();	
					}else{
						throw new IllegalArgumentException("Close cannot be called on a source");
					}
					
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan #
	 * initWith
	 * (de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan )
	 */
	@Override
	public void initWith(IExecutionPlan newExecutionPlan) {
		this.setPartialPlans(new ArrayList<IPartialPlan>(newExecutionPlan
				.getPartialPlans()));
		this.setLeafSources(new ArrayList<IIterableSource<?>>(newExecutionPlan
				.getLeafSources()));
		this.setRoots(newExecutionPlan.getRoots());
	}

	@Override
	public void setRoots(List<IPhysicalOperator> roots) {
		this.roots = roots;
	}

	@Override
	public List<IPhysicalOperator> getRoots() {
		return roots;
	}


}
