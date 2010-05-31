package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

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
	private List<ISink<?>> roots;

	/**
	 * Priority with which the objects should be scheduled.
	 */
	private int priority;

	/**
	 *  Cache Ids for Sources to speed up getSourceID
	 */
	private Map<IIterableSource<?>, Integer> sourceIds;

	/**
	 * Creates a new PartialPlan.
	 * 
	 * @param iterableSource
	 *            Sources which should be scheduled.
	 * @param roots
	 *            Roots which should be scheduled.
	 * @param priority
	 *            Priority with which the objects should be scheduled.
	 */
	public PartialPlan(List<IIterableSource<?>> iterableSource,
			List<ISink<?>> roots, int priority) {
		this.iterableSource = new ArrayList<IIterableSource<?>>(iterableSource);
		this.sourceIds = new HashMap<IIterableSource<?>, Integer>();
		for (int i=0;i<iterableSource.size();i++){
			sourceIds.put(iterableSource.get(i), i); // Iterator does not garantee order ... (?)
		}
		this.roots = roots;
		this.priority = priority;
	}

	/**
	 * Returns a ","-separated string of the owner IDs.
	 * 
	 * @param owner
	 *            Owner which have IDs.
	 * @return ","-separated string of the owner IDs.
	 */
	private String getOwnerIDs(List<IOperatorOwner> owner) {
		String result = "";
		for (IOperatorOwner iOperatorOwner : owner) {
			if (result != "") {
				result += ", ";
			}
			result += iOperatorOwner.getID();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan#
	 * getIterableSource()
	 */
	@Override
	public List<IIterableSource<?>> getIterableSource() {
		return iterableSource;
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
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan#getRoots
	 * ()
	 */
	@Override
	public List<ISink<?>> getRoots() {
		return roots;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan#getPriority
	 * ()
	 */
	@Override
	public int getPriority() {
		return this.priority;
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

		for (ISink<?> root : this.roots) {
			if (result != "") {
				result += AppEnv.LINE_SEPARATOR;
			}
			result += root.toString() + ", Owner: "
					+ getOwnerIDs(root.getOwner());
		}

		result += AppEnv.LINE_SEPARATOR + "Sources:";

		for (IIterableSource<?> source : iterableSource) {
			if (result != "") {
				result += AppEnv.LINE_SEPARATOR;
			}
			result += source.toString() + ", Owner: "
					+ getOwnerIDs(source.getOwner());
		}
		return result;
	}
}
