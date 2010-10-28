package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	/**
	 * Sources which should be scheduled.
	 */
	private ArrayList<IIterableSource<?>> iterableSource;

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

	/**
	 * Cache Ids for Sources to speed up getSourceID
	 */
	private Map<IIterableSource<?>, Integer> sourceIds;

	/**
	 * Creates a new PartialPlan.
	 * 
	 * @param iterableSource
	 *            Sources which should be scheduled.
	 * @param roots
	 *            Roots which should be scheduled.
	 * @param basePriority
	 *            Initial priority with which the objects should be scheduled.
	 *            Real priorty can change at runtime
	 */
	public PartialPlan(List<IIterableSource<?>> iterableSource,
			List<IPhysicalOperator> roots, int basePriority, IQuery partof,
			IQuery... otherParts) {
		this.iterableSource = new ArrayList<IIterableSource<?>>(iterableSource);
		this.sourceIds = new HashMap<IIterableSource<?>, Integer>();
		for (int i = 0; i < iterableSource.size(); i++) {
			sourceIds.put(iterableSource.get(i), i); // Iterator does not
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.physicaloperator.plan.IPartialPlan#
	 * getIterableSource()
	 */
	@Override
	public List<IIterableSource<?>> getIterableSource() {
		return Collections.unmodifiableList(iterableSource);
	}

	@Override
	public IIterableSource<?> getIterableSource(int id) {
		return iterableSource.get(id);
	}

	@Override
	public int getSourceId(IIterableSource<?> source) {
		return sourceIds.get(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.plan.IPartialPlan#getRoots
	 * ()
	 */
	@Override
	public List<IPhysicalOperator> getRoots() {
		return roots;
	}

	@Override
	public List<IPhysicalOperator> getQueryRoots() {
		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		for (IQuery q : partOf) {
			roots.addAll(q.getRoots());
		}
		return roots;
	}
	
	@Override
	public List<IQuery> getQueries(){
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return iterableSource.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "Roots:";

		for (IPhysicalOperator root : this.roots) {
			if (result != "") {
				result += AppEnv.LINE_SEPARATOR;
			}
			result += root.toString() + ", Owner: " + root.getOwnerIDs();
		}

		result += AppEnv.LINE_SEPARATOR + "Sources:";

		for (IIterableSource<?> source : iterableSource) {
			if (result != "") {
				result += AppEnv.LINE_SEPARATOR;
			}
			result += source.toString() + ", Owner: " + source.getOwnerIDs();
		}
		return result;
	}
}
