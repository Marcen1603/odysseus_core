/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

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
	 * SLA-based
	 */

	private ScheduleMeta scheduleMeta;
	
	/**
	 * 
	 */
	private List<IPhysicalQuery> partOf;

	private long planId;

	/**
	 * Cache Ids for Sources to speed up getSourceID
	 */
	final private Map<IIterableSource<?>, Integer> sourceIds;
	private Set<IPhysicalQuery> particpatingQueries;

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
			List<IPhysicalOperator> roots, int basePriority, IPhysicalQuery partof,
			IPhysicalQuery... otherParts) {
		this.iterableSources = new ArrayList<IIterableSource<?>>(
				iterableSources);
		this.sourceIds = new HashMap<IIterableSource<?>, Integer>();
		for (int i = 0; i < iterableSources.size(); i++) {
			sourceIds.put(iterableSources.get(i), i); // Iterator does not
														// garantee order ...
														// (?)
		}
		this.roots = roots;
		this.currentPriority = basePriority;
		this.basePriority = basePriority;
		this.partOf = new ArrayList<IPhysicalQuery>();
		this.partOf.add(partof);
		for (IPhysicalQuery q : otherParts) {
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
	 * @seede.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IPartialPlan#
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
		return id != null ? id : -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IPartialPlan#getRoots
	 * ()
	 */
	@Override
	public List<IPhysicalOperator> getRoots() {
		return Collections.unmodifiableList(roots);
	}

	@Override
	public List<IPhysicalOperator> getQueryRoots() {

		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		for (IPhysicalQuery q : partOf) {
			roots.addAll(q.getRoots());
		}
	//	getLogger().debug("get Query Roots " + roots);
		return roots;
	}

	@Override
	public List<IPhysicalQuery> getQueries() {
		return Collections.unmodifiableList(partOf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IPartialPlan#getPriority
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

	@Override
	public ScheduleMeta getScheduleMeta() {
		return scheduleMeta;
	}
	
	@Override
	public void setScheduleMeta(ScheduleMeta scheduleMeta) {
		this.scheduleMeta = scheduleMeta;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(getId() + "Roots:");

		for (IPhysicalOperator root : this.roots) {
			result.append(AppEnv.LINE_SEPARATOR);
			result.append(root.toString()).append(", Owner: ").append(root.getOwnerIDs());
		}

		result.append(AppEnv.LINE_SEPARATOR + "Sources:");

		for (IIterableSource<?> source : iterableSources) {
				result.append(AppEnv.LINE_SEPARATOR);
				result.append(source.toString()).append(", Owner: ").append(source.getOwnerIDs());
		}
		return result.toString();
	}

	public void setParticipatingQueries(Set<IPhysicalQuery> q) {
		this.particpatingQueries = q;
	}
	
	public Set<IPhysicalQuery> getParticpatingQueries() {
		return Collections.unmodifiableSet(particpatingQueries);
	}

}
