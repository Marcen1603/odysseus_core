package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * A PartialPlan is a part of the global execution plan. It consist of root
 * sinks, iterable sources and a priority. These objects are used for scheduling
 * the execution plan.
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public class PartialPlan implements IPartialPlan {

	static Logger _logger;
	static long planIdCounter = 0;

	static synchronized Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(PartialPlan.class);
		}
		return _logger;
	}

	/**
	 * Sources which should be scheduled.
	 */
	final private ArrayList<IIterableSource<?>> iterableSources;

	/**
	 * Roots which should be scheduled.
	 */
	private List<IPhysicalOperator> roots;

	/**
	 * Priority with which the objects should be scheduled.
	 */
	private long currentPriority;

	/**
	 * Priority at creation time
	 */
	private long basePriority;

	/**
	 * 
	 */
	private List<IQuery> partOf;

	private long planId;
	
	/**
	 * Cache Ids for Sources to speed up getSourceID
	 */
	final private Map<IIterableSource<?>, Integer> sourceIds;

	/**
	 * Creates a new PartialPlan.
	 * 
	 * @param iterableSources
	 *            Sources which should be scheduled.
	 * @param roots
	 *            Roots which should be scheduled.
	 * @param basePriority
	 *            Initial priority with which the objects should be scheduled.
	 *            Real priorty can change at runtime
	 */
	public PartialPlan(List<IIterableSource<?>> iterableSources,
			List<IPhysicalOperator> roots, int basePriority, IQuery partof,
			IQuery... otherParts) {
		this.iterableSources = new ArrayList<IIterableSource<?>>(
				iterableSources);
		this.sourceIds = new  HashMap<IIterableSource<?>, Integer>();
		for (int i = 0; i < iterableSources.size(); i++) {
			sourceIds.put(iterableSources.get(i), i); // Iterator does not
														// garantee order ...
														// (?)
		}
		this.roots = roots;
		this.currentPriority = basePriority;
		this.basePriority = basePriority;
		this.partOf = new ArrayList<IQuery>();
		this.partOf.add(partof);
		for (IQuery q : otherParts) {
			this.partOf.add(q);
		}
		planId = planIdCounter++;
	}

	@Override
	public boolean hasIteratableSources() {
		return iterableSources != null && iterableSources.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.physicaloperator.plan.IPartialPlan#
	 * getIterableSource()
	 */
	@Override
	public List<IIterableSource<?>> getIterableSources() {
		return Collections.unmodifiableList(iterableSources);
	}

	@Override
	public IIterableSource<?> getIterableSource(int id) {
		return iterableSources.get(id);
	}

	@Override
	public synchronized int getSourceId(IIterableSource<?> source) {
		Integer id = sourceIds.get(source);
		return id != null?id:-1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.plan.IPartialPlan#getRoots
	 * ()
	 */
	@Override
	public List<IPhysicalOperator> getRoots() {
		return Collections.unmodifiableList(roots);
	}

	@Override
	public List<IPhysicalOperator> getQueryRoots() {

		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		for (IQuery q : partOf) {
			roots.addAll(q.getRoots());
		}
		getLogger().debug("get Query Roots " + roots);
		return roots;
	}

	@Override
	public List<IQuery> getQueries() {
		return Collections.unmodifiableList(partOf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.plan.IPartialPlan#getPriority
	 * ()
	 */
	@Override
	public long getCurrentPriority() {
		return this.currentPriority;
	}

	@Override
	public void setCurrentPriority(long newPriority) {
		this.currentPriority = newPriority;
	}

	@Override
	public long getBasePriority() {
		return this.basePriority;
	}

	@Override
	public long getId() {
		return planId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = getId()+"Roots:";

		for (IPhysicalOperator root : this.roots) {
			if (result != "") {
				result += AppEnv.LINE_SEPARATOR;
			}
			result += root.toString() + ", Owner: " + root.getOwnerIDs();
		}

		result += AppEnv.LINE_SEPARATOR + "Sources:";

		for (IIterableSource<?> source : iterableSources) {
			if (result != "") {
				result += AppEnv.LINE_SEPARATOR;
			}
			result += source.toString() + ", Owner: " + source.getOwnerIDs();
		}
		return result;
	}

}
