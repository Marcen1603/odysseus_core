/*******************************************************************************


 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.ac.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionListener;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecutionGenerator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.core.server.sla.SLADictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * Standardimplementierung der Admission Control auf Basis von
 * {@link IAdmissionControl}. Verwaltet alle bekannten Kostenmodelle und setzt
 * sie bei Bedarf ein.
 * 
 * @author Timo Michelsen
 * 
 */
public class StandardAC implements IAdmissionControl, IPlanModificationListener {

	private static final Logger LOG = LoggerFactory.getLogger(StandardAC.class);
	private static final long ESTIMATION_TOO_OLD_MILLIS = 3000;
	private static final double UNDERLOAD_FACTOR = 0.80;
	private static final double UNDERLOAD_USER_FACTOR = 0.80;
	private static final double EVENT_BUFFERING_COUNT = 3;

	private Map<String, ICostModel> costModels = Maps.newHashMap();
	private ICostModel selectedCostModel;
	private IServerExecutor executor;

	private ICost maxCost;
	private ICost underloadCost;
	private ICost actCost;

	private Map<IPhysicalQuery, ICost> queryCosts = Maps.newHashMap();
	private Map<IPhysicalQuery, ICost> runningQueryCosts = Maps.newHashMap();
	private Map<IPhysicalQuery, Long> timestamps = Maps.newHashMap();

	private Map<IUser, ICost> userCosts = Maps.newHashMap();

	private IPossibleExecutionGenerator generator = new StandardPossibleExecutionGenerator();

	private final List<IAdmissionListener> listeners = Lists.newArrayList();
	private final List<IAdmissionStatusListener> statusListeners = Lists.newArrayList();
	private final long startTime = System.currentTimeMillis();

	private long overloadEventBuffer = 0;
	private final Map<IUser, Long> userOverloadEventBuffer = Maps.newHashMap();

	@Override
	public ICost getCost(IPhysicalQuery query) {
		return queryCosts.get(query);
	}

	@Override
	public synchronized boolean canStartQuery(IPhysicalQuery query) {
		if (runningQueryCosts.containsKey(query))
			return true;

		ICost queryCost = determineCost(query);

		// OVERALL COST
		ICost totalCost = mergeCosts(actCost, queryCost);
		LOG.debug("Total cost if executed: {}", totalCost);

		if (isGreater(totalCost, maxCost)) {
			LOG.debug("Executing queries would exceed maximum total cost");
			LOG.debug("Maximum total cost: {}", maxCost);
			return false;
		}

		// USER COST
		Optional<Double> optFactor = determineMaximumCostFactor(query);
		if (optFactor.isPresent()) {
			double sla = optFactor.get();
			ICost maxUserCost = maxCost.fraction(sla);
			ICost userTotalCost = determineUserCost(query.getSession().getUser(), queryCost);
			if (isGreater(userTotalCost, maxUserCost)) {
				LOG.debug("Executing queries would exceed maximum cost of user {}", query.getSession().getUser().getName());
				LOG.debug("Maxmimum cost for user: {}", maxUserCost);
				return false;
			}
		}

		return true;
	}

	@Override
	public synchronized void updateEstimations() {

		long startTimestamp = System.currentTimeMillis();
		actCost = selectedCostModel.getOverallCost();

		Map<IPhysicalQuery, ICost> newCostEstimations = Maps.newHashMap();
		userCosts = Maps.newHashMap();
		runningQueryCosts = Maps.newHashMap();
		Map<IUser, ICost> userMaximumCostFactors = Maps.newHashMap();

		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries().toArray(new IPhysicalQuery[0])) {
			if (query.isOpened()) {
				ICost cost = estimateCost(getAllOperators(query), true);
				newCostEstimations.put(query, cost);

				IUser user = query.getSession().getUser();
				userCosts.put(user, mergeCosts(userCosts.get(user), cost));

				Optional<Double> optSLA = determineMaximumCostFactor(query);
				if (optSLA.isPresent()) {
					userMaximumCostFactors.put(user, maxCost.fraction(optSLA.get()));
				}
			}
		}
		runningQueryCosts.putAll(newCostEstimations);
		queryCosts.putAll(newCostEstimations);

		refreshTimestamps(timestamps, runningQueryCosts.keySet());

		if (LOG.isDebugEnabled()) {
			LOG.debug("Cost of execution plan : " + actCost);
			for (IUser user : userCosts.keySet()) {
				LOG.debug("Cost for {} : {}", user.getName(), userCosts.get(user));
			}
		}

		if (!statusListeners.isEmpty()) {
			fireAdmissionStatus(userMaximumCostFactors);
		}

//		System.out.println(queryCosts.size() + "; " + runningQueryCosts.size() + "; " + actCost);

		// check, if user-load is too heavy
		for (IUser user : userCosts.keySet()) {
			if (!userMaximumCostFactors.containsKey(user)) {
				continue;
			}

			ICost userCost = userCosts.get(user);
			ICost maxUserCost = userMaximumCostFactors.get(user);
			if (isGreater(userCost, maxUserCost)) {
				LOG.warn("Costs for user {} are too high: {}", user.getName(), userCost);
				LOG.warn("Maximum allowed for user: {}", maxUserCost);

				incrementUserEventBuffer(userOverloadEventBuffer, user);
				if (getUserEventBuffer(userOverloadEventBuffer, user) >= EVENT_BUFFERING_COUNT) {
					fireOverloadUserEvent(user);
				}

			} else {
				resetUserEventBuffer(userOverloadEventBuffer, user);

				ICost userUnderloadCost = maxUserCost.fraction(UNDERLOAD_USER_FACTOR);
				if (isGreater(userUnderloadCost, userCost)) {
					LOG.debug("Cost for user {} is below underload-level: {}", user.getName(), underloadCost);

					fireUnderloadUserEvent(user);
				}
			}
		}

		// check, if system-load is too heavy
		if (isGreater(actCost, maxCost)) {
			// too high load now!
			LOG.warn("Cost is too high. MaxCost = {}", maxCost);

			overloadEventBuffer++;
			if( overloadEventBuffer >= EVENT_BUFFERING_COUNT ) {
				fireOverloadEvent();
			}
		} else {
			overloadEventBuffer = 0;
			
			if (isGreater(underloadCost, actCost)) {
				LOG.debug("Cost is below underload-level: {}", underloadCost);
				fireUnderloadEvent();
			}
		}

		if (LOG.isDebugEnabled()) {
			long elapsedTime = System.currentTimeMillis() - startTimestamp;
			LOG.debug("Updatetime: {} ms", elapsedTime);
		}
	}

	private static void incrementUserEventBuffer(Map<IUser, Long> buffer, IUser user) {
		Long eventBuffer = buffer.get(user);
		if (eventBuffer == null) {
			buffer.put(user, 1L);
		} else {
			buffer.put(user, eventBuffer + 1);
		}
	}

	private static long getUserEventBuffer(Map<IUser, Long> buffer, IUser user) {
		Long eventBuffer = buffer.get(user);
		return eventBuffer != null ? eventBuffer : 0L;
	}

	private static void resetUserEventBuffer(Map<IUser, Long> buffer, IUser user) {
		buffer.put(user, 0L);
	}

	@Override
	public Set<String> getRegisteredCostModels() {
		return costModels.keySet();
	}

	@Override
	public synchronized void selectCostModel(String name) {
		if (!costModels.containsKey(name))
			throw new RuntimeException("CostModel " + name + " not found");

		selectedCostModel = costModels.get(name);
		maxCost = selectedCostModel.getMaximumCost();
		actCost = selectedCostModel.getZeroCost();
		underloadCost = maxCost.fraction(UNDERLOAD_FACTOR);

		LOG.debug("CostModel {} selected", name);
		LOG.debug("Maximum cost allowed: {}", maxCost);
		LOG.debug("Cost for underload-event after overload: {}", underloadCost);
	}

	@Override
	public String getSelectedCostModel() {
		if (selectedCostModel == null)
			return null;

		return selectedCostModel.getClass().getSimpleName();
	}

	/**
	 * Paket-bekannte Methode, um das ausgewählte Kostenmodell als
	 * {@link ICostModel}-Instanz zurückzugeben. Wird aktuell nur von
	 * {@link StandardPossibleExecutionGenerator} verwendet.
	 * 
	 * @return Aktuell verwendetes Kostenmodell
	 */
	ICostModel getSelectedCostModelInstance() {
		return selectedCostModel;
	}

	@Override
	public synchronized void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {

		IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		int queryID = query.getID();

		LOG.debug("EVENT for Query {}: {}", queryID, eventArgs.getEventType());
		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
			queryCosts.remove(query);
			timestamps.remove(query);

		} else if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs.getEventType())) {
			// do cost-estimation now
			ICost queryCost = estimateCost(getAllOperators(query), false);

			queryCosts.put(query, queryCost);
			timestamps.put(query, System.currentTimeMillis());

		} else if (PlanModificationEventType.QUERY_START.equals(eventArgs.getEventType())) {
			ICost queryCost = queryCosts.get(query);

			if (!runningQueryCosts.containsKey(query)) {
				runningQueryCosts.put(query, queryCost);
				IUser user = query.getSession().getUser();
				userCosts.put(user, mergeCosts(userCosts.get(user), queryCost));
			}

		} else if (PlanModificationEventType.QUERY_STOP.equals(eventArgs.getEventType())) {
			ICost queryCost = runningQueryCosts.remove(query);
			IUser user = query.getSession().getUser();
			userCosts.put(user, substractCosts(userCosts.get(user), queryCost));
		}
	}

	/**
	 * Wird aufgerufen, wenn im OSGi-Framework ein Kostenmodell registriert
	 * wird. Ist dies das erste bekannte Kostenmodell, wird es automatisch
	 * ausgewählt und vortan für Kostenschätzungen verwendet.
	 * 
	 * @param costModel
	 *            Neues Kostenmodell
	 */
	public void bindCostModel(ICostModel costModel) {
		costModels.put(costModel.getClass().getSimpleName(), costModel);
		LOG.debug("Costmodel bound: " + costModel.getClass().getSimpleName());

		if (getSelectedCostModel() == null) {
			selectCostModel(costModel.getClass().getSimpleName());
		}
	}

	/**
	 * Wird aufgerufen, wenn im OSGi-Framework ein Kostenmodell deregistriert
	 * wird.
	 * 
	 * @param costModel
	 *            Das entfernte Kostenmodell
	 */
	public void unbindCostModel(ICostModel costModel) {
		costModels.remove(costModel.getClass().getSimpleName());

		LOG.debug("Costmodel unbound: " + costModel.getClass().getSimpleName());
	}

	/**
	 * Wird aufgerufen, wenn im OSGi-Framework einen {@link IExecutor}
	 * registriert wird. Für die Admission Control wird genau ein
	 * {@link IExecutor} benötigt.
	 * 
	 * @param executor
	 *            Neuer {@link IExecutor}
	 */
	public void bindExecutor(IExecutor executor) {
		this.executor = (IServerExecutor) executor;

		this.executor.addPlanModificationListener(this);

		LOG.debug("Executor bound");
	}

	/**
	 * Wird aufgerufen, wenn im OSGi-Framework der {@link IExecutor}
	 * deregistriert wird.
	 * 
	 * @param executor
	 *            Zu entfernender {@link IExecutor}
	 */
	public void unbindExecutor(IExecutor executor) {
		if (executor == this.executor) {
			this.executor.removePlanModificationListener(this);
			this.executor = null;

			LOG.debug("Executor unbound");
		}
	}

	public void bindAdmissionStatusListener(IAdmissionStatusListener listener) {
		synchronized (statusListeners) {
			statusListeners.add(listener);
		}
		LOG.debug("Status listener {} bound.", listener);
	}

	public void unbindAdmissionStatusListener(IAdmissionStatusListener listener) {
		synchronized (statusListeners) {
			if (statusListeners.contains(listener)) {
				statusListeners.remove(listener);
				LOG.debug("Status listener {} unbound.", listener);
			}
		}
	}

	public void bindPossibleExecutionGenerator(IPossibleExecutionGenerator generator) {
		LOG.debug("Bound PossibleExecutionGenerator {}", generator);
		this.generator = generator;
	}

	public void unbindPossibleExecutionGenerator(IPossibleExecutionGenerator generator) {
		if (this.generator == generator) {
			this.generator = new StandardPossibleExecutionGenerator();
			LOG.debug("Unbound PossibleExecutionGenerator {}. Using default now.", generator);
		}
	}

	/**
	 * Liefert den aktuell registrierten Executor oder <code>null</code>.
	 * 
	 * @return Registrierter Executor oder <code>null</code>, falls kein
	 *         {@link IExecutor} registriert wurde.
	 */
	public IExecutor getExecutor() {
		return this.executor;
	}

	@Override
	public ICost getActualCost() {
		return actCost;
	}

	@Override
	public ICost getMaximumCost() {
		return maxCost;
	}

	@Override
	public void addListener(IAdmissionListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IAdmissionListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public List<IPossibleExecution> getPossibleExecutions(IUser user) {
		if (user == null) {
			return generator.getPossibleExecutions(this, runningQueryCosts, actCost, maxCost);
		} else {
			Map<IPhysicalQuery, ICost> userQueries = Maps.newHashMap();
			Optional<Double> optFactor = determineMaximumCostFactor(user);
			for (IPhysicalQuery query : runningQueryCosts.keySet()) {
				if (query.getSession().getUser().equals(user)) {
					userQueries.put(query, runningQueryCosts.get(query));
				}
			}
			return generator.getPossibleExecutions(this, userQueries, actCost, maxCost.fraction(optFactor.isPresent() ? optFactor.get() : 1.0));
		}
	}

	@Override
	public boolean isOverloaded() {
		return isGreater(getActualCost(), getMaximumCost());
	}

	private void fireAdmissionStatus(Map<IUser, ICost> maxUserCosts) {
		Map<IUser, ICost> underloadCosts = Maps.newHashMap();
		for (IUser user : maxUserCosts.keySet()) {
			underloadCosts.put(user, maxUserCosts.get(user).fraction(UNDERLOAD_USER_FACTOR));
		}

		long ts = System.currentTimeMillis();
		AdmissionStatus status = new AdmissionStatus(runningQueryCosts.size(), queryCosts.size() - runningQueryCosts.size(), queryCosts.size(), actCost, maxCost, underloadCost,
				ImmutableMap.copyOf(userCosts), ImmutableMap.copyOf(maxUserCosts), ImmutableMap.copyOf(underloadCosts), ImmutableMap.copyOf(queryCosts), ts, ts - startTime, selectedCostModel
						.getClass().getSimpleName());

		synchronized (statusListeners) {
			for (IAdmissionStatusListener listener : statusListeners) {
				try {
					listener.updateAdmissionStatus(this, status);
				} catch (Throwable t) {
					LOG.error("Exception during invoking admission status listener", t);
				}
			}
		}
	}

	private ICost estimateCost(List<IPhysicalOperator> operators, boolean onUpdate) {
		if (getSelectedCostModel() == null) {
			throw new IllegalStateException("No CostModel selected.");
		}

		long startTimestamp = System.currentTimeMillis();

		ICostModel costModel = costModels.get(getSelectedCostModel());
		ICost queryCost = costModel.estimateCost(operators, onUpdate);

		if (LOG.isDebugEnabled()) {
			long elapsedTime = System.currentTimeMillis() - startTimestamp;
			LOG.debug("Estimationtime : {} ms", elapsedTime);
		}

		return queryCost;
	}

	private void fireOverloadEvent() {
		synchronized (listeners) {
			for (IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.overloadOccured(this);
					}
				} catch (Throwable ex) {
					LOG.error("Exception during firing overload-event", ex);
				}
			}
		}
	}

	private void fireUnderloadEvent() {
		synchronized (listeners) {
			for (IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.underloadOccured(this);
					}
				} catch (Throwable ex) {
					LOG.error("Exception during firing underload-event", ex);
				}
			}
		}
	}

	private void fireOverloadUserEvent(IUser user) {
		synchronized (listeners) {
			for (IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.overloadUserOccured(this, user);
					}
				} catch (Throwable ex) {
					LOG.error("Exception during firing overload-user-event", ex);
				}
			}
		}
	}

	private void fireUnderloadUserEvent(IUser user) {
		synchronized (listeners) {
			for (IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.underloadUserOccured(this, user);
					}
				} catch (Throwable ex) {
					LOG.error("Exception during firing underload-user-event", ex);
				}
			}
		}
	}

	private ICost determineCost(IPhysicalQuery query) {
		if (queryCosts.containsKey(query)) {
			Long lastTime = timestamps.get(query);

			if (System.currentTimeMillis() - lastTime > ESTIMATION_TOO_OLD_MILLIS) {
				ICost queryCost = estimateCost(getAllOperators(query), false);
				timestamps.put(query, System.currentTimeMillis());
				return queryCost;
			} else {
				return queryCosts.get(query);
			}

		} else {
			ICost queryCost = estimateCost(getAllOperators(query), false);
			queryCosts.put(query, queryCost);
			timestamps.put(query, System.currentTimeMillis());
			return queryCost;
		}
	}

	private ICost determineUserCost(IUser user, ICost queryCost) {
		ICost userCost = userCosts.get(user);
		if (userCost == null) {
			return queryCost;
		} else {
			return queryCost.merge(userCost);
		}
	}

	private static Optional<Double> determineMaximumCostFactor(IPhysicalQuery query) {
		return determineMaximumCostFactor(query.getSession().getUser());
	}

	private static Optional<Double> determineMaximumCostFactor(IUser user) {
		String slaName = SLADictionary.getInstance().getUserSLA(user);
		if (slaName == null) {
			return Optional.absent();
		}
		SLA sla = SLADictionary.getInstance().getSLA(slaName);
		if (sla == null) {
			return Optional.absent();
		}
		Double factor = sla.getMaxAdmissionCostFactor();
		return Optional.of(factor < 0.0000001 ? 1.0 : factor);
	}

	private static boolean isGreater(ICost first, ICost last) {
		return first.compareTo(last) > 0;
	}

	private static ICost mergeCosts(ICost a, ICost b) {
		if (a == null && b != null) {
			return b;
		} else if (a != null && b == null) {
			return a;
		} else if (a == null && b == null) {
			return null;
		} else {
			return a.merge(b);
		}
	}

	private static ICost substractCosts(ICost a, ICost b) {
		if (a == null && b != null) {
			return b;
		} else if (a != null && b == null) {
			return a;
		} else if (a == null && b == null) {
			return null;
		} else {
			return a.substract(b);
		}
	}

	private static List<IPhysicalOperator> getAllOperators(IPhysicalQuery query) {
		List<IPhysicalOperator> operators = new ArrayList<IPhysicalOperator>();
		// filter
		for (IPhysicalOperator operator : query.getPhysicalChilds()) {
			if (!operators.contains(operator) && !operator.getClass().getSimpleName().contains("DataSourceObserverSink") && operator.getOwner().contains(query))
				operators.add(operator);
		}
		return operators;
	}

	private static void refreshTimestamps(Map<IPhysicalQuery, Long> timestamps, Collection<IPhysicalQuery> queriesToUpdate) {
		long timestamp = System.currentTimeMillis();
		for (IPhysicalQuery query : queriesToUpdate) {
			timestamps.put(query, timestamp);
		}
	}
}
