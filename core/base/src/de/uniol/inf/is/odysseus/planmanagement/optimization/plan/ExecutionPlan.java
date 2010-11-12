package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

/**
 * EditableExecutionPlan is an object which is used to store an execution plan.
 * This structure is used for communication between plan management,
 * optimization and scheduling.
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public class ExecutionPlan implements IExecutionPlan {

	static Logger _logger = null;

	static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ExecutionPlan.class);
		}
		return _logger;
	}

	/**
	 * Describes if the physical operators are opened.
	 */
	private boolean open = false;

	/**
	 * List of all parts of this execution plan. Used for scheduling.
	 */
	final List<IPartialPlan> partialPlans;

	/**
	 * List of all parts of this execution plan. Used for scheduling.
	 */
	final List<IPartialPlan> partialPlansNotToSchedule;

	/**
	 * List of all leaf sources that need to be scheduled periodically.
	 */
	final List<IIterableSource<?>> leafSources;

	private Set<IPhysicalOperator> roots = null;

	public ExecutionPlan(){ 
		partialPlans = new ArrayList<IPartialPlan>();
		partialPlansNotToSchedule = new ArrayList<IPartialPlan>();
		leafSources = new ArrayList<IIterableSource<?>>();
	}
	
	public ExecutionPlan(ExecutionPlan otherPlan){
		this.open = otherPlan.open;
		this.leafSources = new ArrayList<IIterableSource<?>>(otherPlan.leafSources);
		this.partialPlans = new ArrayList<IPartialPlan>(otherPlan.partialPlans);
		this.partialPlansNotToSchedule = new ArrayList<IPartialPlan>(otherPlan.partialPlans);
		if (otherPlan.roots != null){
			this.roots = new HashSet<IPhysicalOperator>(otherPlan.roots);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan#
	 * getPartialPlans()
	 */
	@Override
	public List<IPartialPlan> getPartialPlans() {
		return Collections.unmodifiableList(this.partialPlans);
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
		return Collections.unmodifiableList(this.leafSources);
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
		this.partialPlans.clear();
		this.partialPlansNotToSchedule.clear();
		for (IPartialPlan plan : patialPlans) {
			if (plan.hasIteratableSources()) {
				this.partialPlans.add(plan);
			} else {
				this.partialPlansNotToSchedule.add(plan);
			}
		}
		updateRoots();

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
		this.leafSources.clear();
		this.leafSources.addAll(leafSources);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan
	 * #open()
	 */
	@Override
	public void open() throws OpenFailedException {
		if (!open) {
			Set<IPhysicalOperator> roots = getRoots();
			getLogger().debug("Calling Open for " + roots);
			for (IPhysicalOperator root : roots) {
				if (root.isSink()) {
					((ISink<?>) root).open();
				} else {
					throw new IllegalArgumentException(
							"Open() cannot be called on a source -->" + root);
				}

			}
			open = true;
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
		if (open) {
			List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
			for (IPartialPlan partialPlan : this.partialPlans) {
				roots.addAll(partialPlan.getQueryRoots());
			}
			for (IPhysicalOperator root : roots) {
				if (root.isSink()) {
					((ISink<?>) root).close();
				} else {
					throw new IllegalArgumentException(
							"Close() cannot be called on a source -->" + root);
				}

			}
			open = false;
		}
	}

	@Override
	public Set<IPhysicalOperator> getRoots() {
		if (roots == null) {
			updateRoots();
		}
		return Collections.unmodifiableSet(roots);
	}

	private void updateRoots() {
		roots = new HashSet<IPhysicalOperator>();
		for (IPartialPlan partialPlan : this.partialPlans) {
			roots.addAll(partialPlan.getQueryRoots());
		}
		for (IPartialPlan partialPlan : this.partialPlansNotToSchedule) {
			roots.addAll(partialPlan.getQueryRoots());
		}		
	}
	
	@Override
	public IExecutionPlan clone(){
		return new ExecutionPlan(this);
	}

}
