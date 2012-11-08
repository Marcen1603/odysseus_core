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
package de.uniol.inf.is.odysseus.scheduler.slapriorityscheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPhysicalQueryScheduling;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformancePlacement;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventListener;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAConformanceFactory;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAConformancePlacementFactory;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAViolationEvent;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAViolationLogger;

abstract public class AbstractDynamicPriorityPlanScheduling implements
		IPhysicalQueryScheduling, ISLAViolationEventDistributor, ISchedulingEventListener {
	
	Logger logger = LoggerFactory.getLogger(AbstractDynamicPriorityPlanScheduling.class);

	final CurrentPlanPriorityComperator comperator = new CurrentPlanPriorityComperator();

	private final List<IScheduling> queue;
	final private List<IScheduling> lastRun = new LinkedList<IScheduling>();

	private LinkedList<SLAViolationEvent> eventQueue = new LinkedList<SLAViolationEvent>();
	private List<ISLAViolationEventListener> listeners;
	private Set<IPhysicalQuery> extendedQueries = new HashSet<IPhysicalQuery>();
	private List<ISLAConformance> conformances = new ArrayList<ISLAConformance>();
	
	final private Set<IScheduling> pausedPlans;

	public AbstractDynamicPriorityPlanScheduling() {
		queue = new LinkedList<IScheduling>();
		this.listeners = new ArrayList<ISLAViolationEventListener>();
		this.addSLAViolationEventListener(new SLAViolationLogger());
		this.pausedPlans = new HashSet<IScheduling>();
	}

	@SuppressWarnings("unchecked")
	public AbstractDynamicPriorityPlanScheduling(
			AbstractDynamicPriorityPlanScheduling dynamicPriorityPlanScheduling) {
		queue = new LinkedList<IScheduling>(dynamicPriorityPlanScheduling.queue);
		this.eventQueue = (LinkedList<SLAViolationEvent>) dynamicPriorityPlanScheduling.eventQueue
				.clone();
		this.listeners = new ArrayList<ISLAViolationEventListener>(
				dynamicPriorityPlanScheduling.listeners);
		this.pausedPlans = new HashSet<IScheduling>(dynamicPriorityPlanScheduling.pausedPlans);
	}

	abstract protected void updatePriority(IScheduling current);

	@Override
	public void addPlan(IScheduling scheduling) {
		synchronized (queue) {
			queue.add(scheduling);
			// Init with Base Priority
			scheduling.getPlan().setCurrentPriority(
					scheduling.getPlan().getBasePriority());
			IPhysicalQuery query = scheduling.getPlan();
			if (!this.extendedQueries.contains(query)) {
				// add SLA conformance operator to plan for monitoring
				this.extendedQueries.add(query);
				SLA sla = (SLA)query.getParameter(SLA.class.getName());
				ISLAConformance conformance = new SLAConformanceFactory()
						.createSLAConformance(sla, this, query);

				ISLAConformancePlacement placement = new SLAConformancePlacementFactory()
						.buildSLAConformancePlacement(sla);
				placement.placeSLAConformance(query, conformance);
				synchronized (conformances) {
					this.conformances.add(conformance);
				}
			}
			scheduling.addSchedulingEventListener(this);
		}
	}

	@Override
	public void clear() {
		synchronized (queue) {
			logger.debug("clearing queue");
			for (IScheduling plan : queue) {
				plan.removeSchedulingEventListener(this);
			}
			pausedPlans.clear();
			queue.clear();
		}
	}

	@Override
	public IScheduling nextPlan() {
		synchronized (pausedPlans) {
			while (pausedPlans.size() == queue.size()) {
				try {
					pausedPlans.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		synchronized (conformances) {
			for (ISLAConformance conformance : this.conformances) {
				conformance.checkViolation();
			}
		}
		synchronized (eventQueue) {
			while (!this.eventQueue.isEmpty()) {
				this.fireSLAViolationEvent(this.eventQueue.pop());
			}
		}

		synchronized (lastRun) {
			if (lastRun.size() > 0) {
				return updateMetaAndReturnPlan(lastRun.remove(0));
			}
		}
		synchronized (queue) {

			if (queue.size() == 0) {
				logger.warn("Queue is empty!!");
				return null;
			}

			Collections.sort(queue, comperator);
			Iterator<IScheduling> iter = queue.iterator();
			synchronized (lastRun) {
				lastRun.add(iter.next());
				long prio = queue.get(0).getPlan().getCurrentPriority();
				while (iter.hasNext()) {
					IScheduling s = iter.next();
					if (s.getPlan().getCurrentPriority() == prio) {
						lastRun.add(s);
					} else {
						break;
					}
				}
				return updateMetaAndReturnPlan(lastRun.remove(0));
			}
		}
	}

	private void fireSLAViolationEvent(SLAViolationEvent event) {
		for (ISLAViolationEventListener listener : this.listeners) {
			listener.slaViolated(event);
		}
	}

	protected IScheduling updateMetaAndReturnPlan(IScheduling toSchedule) {
		updatePriority(toSchedule);
		return toSchedule;
	}

	@Override
	public int planCount() {
		synchronized (queue) {
			return queue.size();
		}
	}

	@Override
	public void removePlan(IScheduling plan) {
		synchronized (queue) {
			queue.remove(plan);
		}
		synchronized (pausedPlans) {
			pausedPlans.remove(plan);
			plan.removeSchedulingEventListener(this);	
		}
	}

	@Override
	abstract public IPhysicalQueryScheduling clone();

	@Override
	public void queueSLAViolationEvent(SLAViolationEvent event) {
		synchronized (eventQueue) {
			this.eventQueue.add(event);
		}
	}

	public void addSLAViolationEventListener(ISLAViolationEventListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * removes an event listener for {@link SLAViolationEvent}
	 * 
	 * @param listener
	 *            the event listener to remove
	 * @return
	 */
	public boolean removeSLAViolationEventListener(
			ISLAViolationEventListener listener) {
		return this.listeners.remove(listener);
	}
	
	@Override
	public void nothingToSchedule(IScheduling sched) {
		pausedPlans.add(sched);
	}

	@Override
	public void scheddulingPossible(IScheduling sched) {
		synchronized (pausedPlans) {
			pausedPlans.remove(sched);
			pausedPlans.notifyAll();
		}
	}

}
