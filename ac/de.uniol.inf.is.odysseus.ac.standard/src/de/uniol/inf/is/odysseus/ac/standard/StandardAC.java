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

	private final Map<String, ICostModel<?>> costModels = Maps.newHashMap();
	private ICostModel<IPhysicalOperator> selectedCostModel;
	private IServerExecutor executor;

	private ICost<IPhysicalOperator> maxCost;
	private ICost<IPhysicalOperator> minCost;
	private ICost<IPhysicalOperator> actCost;

	private final Map<IPhysicalQuery, ICost<IPhysicalOperator>> queryCosts = Maps.newHashMap();
	private Map<IPhysicalQuery, ICost<IPhysicalOperator>> runningQueryCosts = Maps.newHashMap();
	private final Map<IPhysicalQuery, Long> timestamps = Maps.newHashMap();

	private Map<IUser, ICost<IPhysicalOperator>> userCosts = Maps.newHashMap();

	private final List<IAdmissionListener> listeners = Lists.newArrayList();
	private final List<IAdmissionStatusListener> statusListeners = Lists.newArrayList();
	private final long startTime = System.currentTimeMillis();

	private long overloadEventBuffer = 0;
	private final Map<IUser, Long> userOverloadEventBuffer = Maps.newHashMap();

	@Override
	public void addListener(IAdmissionListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void bindAdmissionStatusListener(IAdmissionStatusListener listener) {
		synchronized (statusListeners) {
			statusListeners.add(listener);
		}
		LOG.debug("Status listener {} bound.", listener);
	}

	/**
	 * Wird aufgerufen, wenn im OSGi-Framework ein Kostenmodell registriert
	 * wird. Ist dies das erste bekannte Kostenmodell, wird es automatisch
	 * ausgewählt und vortan für Kostenschätzungen verwendet.
	 * 
	 * @param costModel
	 *            Neues Kostenmodell
	 */
	public void bindCostModel(ICostModel<?> costModel) {
		costModels.put(costModel.getClass().getSimpleName(), costModel);
		LOG.debug("Costmodel bound: " + costModel.getClass().getSimpleName());

		if (getSelectedCostModel() == null) {
			selectCostModel(costModel.getClass().getSimpleName());
		}
	}

	/**
	 * Wird aufgerufen, wenn im OSGi-Framework einen {@link IExecutor}
	 * registriert wird. Für die Admission Control wird genau ein
	 * {@link IExecutor} benötigt.
	 * 
	 * @param executor
	 *            Neuer {@link IExecutor}
	 */
	public void setExecutor(IServerExecutor executor) {
		this.executor = executor;

		this.executor.addPlanModificationListener(this);

		LOG.debug("Executor bound");
	}

	@Override
	public synchronized boolean canStartQuery(IPhysicalQuery query) {
		if (runningQueryCosts.containsKey(query)) {
			return true;
		}

		final ICost<IPhysicalOperator> queryCost = determineCost(query);

		// OVERALL COST
		final ICost<IPhysicalOperator> totalCost = mergeCosts(actCost, queryCost);
		LOG.debug("Total cost if executed: {}", totalCost);

		if (isGreater(totalCost, maxCost)) {
			LOG.debug("Executing queries would exceed maximum total cost");
			LOG.debug("Maximum total cost: {}", maxCost);
			return false;
		}

		// USER COST
		final Optional<Double> optFactor = determineMaximumCostFactor(query);
		if (optFactor.isPresent()) {
			final double sla = optFactor.get();
			final ICost<IPhysicalOperator> maxUserCost = maxCost.fraction(sla);
			final ICost<IPhysicalOperator> userTotalCost = determineUserCost(query.getSession().getUser(), queryCost);
			if (isGreater(userTotalCost, maxUserCost)) {
				LOG.debug("Executing queries would exceed maximum cost of user {}", query.getSession().getUser().getName());
				LOG.debug("Maxmimum cost for user: {}", maxUserCost);
				return false;
			}
		}

		return true;
	}

	@Override
	public ICost<IPhysicalOperator> getActualCost() {
		return actCost;
	}

	@Override
	public ICost<IPhysicalOperator> getCost(IPhysicalQuery query) {
		return queryCosts.get(query);
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
	public ICost<IPhysicalOperator> getMaximumCost() {
		return maxCost;
	}

	@Override
	public ICost<IPhysicalOperator> getMinimumCost() {
		return minCost;
	}

	@Override
	public Set<String> getRegisteredCostModels() {
		return costModels.keySet();
	}

	@Override
	public String getSelectedCostModel() {
		if (selectedCostModel == null) {
			return null;
		}

		return selectedCostModel.getClass().getSimpleName();
	}

	@Override
	public boolean isOverloaded() {
		return isGreater(getActualCost(), getMaximumCost());
	}

	@Override
	public synchronized void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {

		final IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		final int queryID = query.getID();

		LOG.debug("EVENT for Query {}: {}", queryID, eventArgs.getEventType());
		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
			queryCosts.remove(query);
			timestamps.remove(query);

		} else if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs.getEventType())) {
			// do cost-estimation now
			final ICost<IPhysicalOperator> queryCost = estimateCost(getAllOperators(query), false);

			queryCosts.put(query, queryCost);
			timestamps.put(query, System.currentTimeMillis());

		} else if (PlanModificationEventType.QUERY_START.equals(eventArgs.getEventType())) {
			final ICost<IPhysicalOperator> queryCost = queryCosts.get(query);

			if (!runningQueryCosts.containsKey(query)) {
				runningQueryCosts.put(query, queryCost);
				final IUser user = query.getSession().getUser();
				userCosts.put(user, mergeCosts(userCosts.get(user), queryCost));
			}

		} else if (PlanModificationEventType.QUERY_STOP.equals(eventArgs.getEventType())) {
			final ICost<IPhysicalOperator> queryCost = runningQueryCosts.remove(query);
			final IUser user = query.getSession().getUser();
			userCosts.put(user, substractCosts(userCosts.get(user), queryCost));
		}
	}

	@Override
	public void removeListener(IAdmissionListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void selectCostModel(String name) {
		if (!costModels.containsKey(name)) {
			throw new RuntimeException("CostModel " + name + " not found");
		}

		selectedCostModel = (ICostModel<IPhysicalOperator>) costModels.get(name);
		maxCost = selectedCostModel.getMaximumCost();
		actCost = selectedCostModel.getZeroCost();
		minCost = maxCost.fraction(UNDERLOAD_FACTOR);

		LOG.debug("CostModel {} selected", name);
		LOG.debug("Maximum cost allowed: {}", maxCost);
		LOG.debug("Cost for underload-event after overload: {}", minCost);
	}

	public void unbindAdmissionStatusListener(IAdmissionStatusListener listener) {
		synchronized (statusListeners) {
			if (statusListeners.contains(listener)) {
				statusListeners.remove(listener);
				LOG.debug("Status listener {} unbound.", listener);
			}
		}
	}

	/**
	 * Wird aufgerufen, wenn im OSGi-Framework ein Kostenmodell deregistriert
	 * wird.
	 * 
	 * @param costModel
	 *            Das entfernte Kostenmodell
	 */
	public void unbindCostModel(ICostModel<?> costModel) {
		costModels.remove(costModel.getClass().getSimpleName());

		LOG.debug("Costmodel unbound: " + costModel.getClass().getSimpleName());
	}

	public void unsetExecutor(IServerExecutor executor) {
		if (executor == this.executor) {
			this.executor.removePlanModificationListener(this);
			this.executor = null;

			LOG.debug("Executor unbound");
		}
	}

	@Override
	public synchronized void updateEstimations() {

		final long startTimestamp = System.currentTimeMillis();
		actCost = selectedCostModel.getOverallCost();

		final Map<IPhysicalQuery, ICost<IPhysicalOperator>> newCostEstimations = Maps.newHashMap();
		userCosts = Maps.newHashMap();
		runningQueryCosts = Maps.newHashMap();
		final Map<IUser, ICost<IPhysicalOperator>> userMaximumCostFactors = Maps.newHashMap();

		for (final IPhysicalQuery query : executor.getExecutionPlan().getQueries().toArray(new IPhysicalQuery[0])) {
			if (query.isOpened()) {
				final ICost<IPhysicalOperator> cost = estimateCost(getAllOperators(query), true);
				newCostEstimations.put(query, cost);

				final IUser user = query.getSession().getUser();
				userCosts.put(user, mergeCosts(userCosts.get(user), cost));

				final Optional<Double> optSLA = determineMaximumCostFactor(query);
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
			for (final IUser user : userCosts.keySet()) {
				LOG.debug("Cost for {} : {}", user.getName(), userCosts.get(user));
			}
		}

		if (!statusListeners.isEmpty()) {
			fireAdmissionStatus(userMaximumCostFactors);
		}

		// check, if user-load is too heavy
		for (final IUser user : userCosts.keySet()) {
			if (!userMaximumCostFactors.containsKey(user)) {
				continue;
			}

			final ICost<IPhysicalOperator> userCost = userCosts.get(user);
			final ICost<IPhysicalOperator> maxUserCost = userMaximumCostFactors.get(user);
			if (isGreater(userCost, maxUserCost)) {
				LOG.warn("Costs for user {} are too high: {}", user.getName(), userCost);
				LOG.warn("Maximum allowed for user: {}", maxUserCost);

				incrementUserEventBuffer(userOverloadEventBuffer, user);
				if (getUserEventBuffer(userOverloadEventBuffer, user) >= EVENT_BUFFERING_COUNT) {
					fireOverloadUserEvent(user);
				}

			} else {
				resetUserEventBuffer(userOverloadEventBuffer, user);

				final ICost<IPhysicalOperator> userUnderloadCost = maxUserCost.fraction(UNDERLOAD_USER_FACTOR);
				if (isGreater(userUnderloadCost, userCost)) {
					LOG.debug("Cost for user {} is below underload-level: {}", user.getName(), minCost);

					fireUnderloadUserEvent(user);
				}
			}
		}

		// check, if system-load is too heavy
		if (isGreater(actCost, maxCost)) {
			// too high load now!
			LOG.warn("Cost is too high. MaxCost = {}", maxCost);

			overloadEventBuffer++;
			if (overloadEventBuffer >= EVENT_BUFFERING_COUNT) {
				fireOverloadEvent();
			}
		} else {
			overloadEventBuffer = 0;

			if (isGreater(minCost, actCost)) {
				LOG.debug("Cost is below underload-level: {}", minCost);
				fireUnderloadEvent();
			}
		}

		if (LOG.isDebugEnabled()) {
			final long elapsedTime = System.currentTimeMillis() - startTimestamp;
			LOG.debug("Updatetime: {} ms", elapsedTime);
		}
	}

	/**
	 * Paket-bekannte Methode, um das ausgewählte Kostenmodell als
	 * {@link ICostModel}-Instanz zurückzugeben. Wird aktuell nur von
	 * {@link StandardPossibleExecutionGenerator} verwendet.
	 * 
	 * @return Aktuell verwendetes Kostenmodell
	 */
	ICostModel<IPhysicalOperator> getSelectedCostModelInstance() {
		return selectedCostModel;
	}

	private ICost<IPhysicalOperator> determineCost(IPhysicalQuery query) {
		if (queryCosts.containsKey(query)) {
			final Long lastTime = timestamps.get(query);

			if (System.currentTimeMillis() - lastTime > ESTIMATION_TOO_OLD_MILLIS) {
				final ICost<IPhysicalOperator> queryCost = estimateCost(getAllOperators(query), false);
				timestamps.put(query, System.currentTimeMillis());
				return queryCost;
			}
			return queryCosts.get(query);

		}
		final ICost<IPhysicalOperator> queryCost = estimateCost(getAllOperators(query), false);
		queryCosts.put(query, queryCost);
		timestamps.put(query, System.currentTimeMillis());
		return queryCost;

	}

	private ICost<IPhysicalOperator> determineUserCost(IUser user, ICost<IPhysicalOperator> queryCost) {
		final ICost<IPhysicalOperator> userCost = userCosts.get(user);
		if (userCost == null) {
			return queryCost;
		} 
		
		return queryCost.merge(userCost);
	}

	@SuppressWarnings("unchecked")
	private ICost<IPhysicalOperator> estimateCost(List<IPhysicalOperator> operators, boolean onUpdate) {
		if (getSelectedCostModel() == null) {
			throw new IllegalStateException("No CostModel selected.");
		}

		final long startTimestamp = System.currentTimeMillis();

		final ICostModel<IPhysicalOperator> costModel = (ICostModel<IPhysicalOperator>) costModels.get(getSelectedCostModel());
		final ICost<IPhysicalOperator> queryCost = costModel.estimateCost(operators, onUpdate);

		if (LOG.isDebugEnabled()) {
			final long elapsedTime = System.currentTimeMillis() - startTimestamp;
			LOG.debug("Estimationtime : {} ms", elapsedTime);
		}

		return queryCost;
	}

	private void fireAdmissionStatus(Map<IUser, ICost<IPhysicalOperator>> maxUserCosts) {
		final Map<IUser, ICost<IPhysicalOperator>> underloadCosts = Maps.newHashMap();
		for (final IUser user : maxUserCosts.keySet()) {
			underloadCosts.put(user, maxUserCosts.get(user).fraction(UNDERLOAD_USER_FACTOR));
		}

		final long ts = System.currentTimeMillis();
		final AdmissionStatus status = new AdmissionStatus(runningQueryCosts.size(), queryCosts.size() - runningQueryCosts.size(), queryCosts.size(), actCost, maxCost, minCost,
				ImmutableMap.copyOf(userCosts), ImmutableMap.copyOf(maxUserCosts), ImmutableMap.copyOf(underloadCosts), ImmutableMap.copyOf(queryCosts), ts, ts - startTime, selectedCostModel
						.getClass().getSimpleName());

		synchronized (statusListeners) {
			for (final IAdmissionStatusListener listener : statusListeners) {
				try {
					listener.updateAdmissionStatus(this, status);
				} catch (final Throwable t) {
					LOG.error("Exception during invoking admission status listener", t);
				}
			}
		}
	}

	private void fireOverloadEvent() {
		synchronized (listeners) {
			for (final IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.overloadOccured(this);
					}
				} catch (final Throwable ex) {
					LOG.error("Exception during firing overload-event", ex);
				}
			}
		}
	}

	private void fireOverloadUserEvent(IUser user) {
		synchronized (listeners) {
			for (final IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.overloadUserOccured(this, user);
					}
				} catch (final Throwable ex) {
					LOG.error("Exception during firing overload-user-event", ex);
				}
			}
		}
	}

	private void fireUnderloadEvent() {
		synchronized (listeners) {
			for (final IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.underloadOccured(this);
					}
				} catch (final Throwable ex) {
					LOG.error("Exception during firing underload-event", ex);
				}
			}
		}
	}

	private void fireUnderloadUserEvent(IUser user) {
		synchronized (listeners) {
			for (final IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.underloadUserOccured(this, user);
					}
				} catch (final Throwable ex) {
					LOG.error("Exception during firing underload-user-event", ex);
				}
			}
		}
	}

	private static Optional<Double> determineMaximumCostFactor(IPhysicalQuery query) {
		return determineMaximumCostFactor(query.getSession().getUser());
	}

	private static Optional<Double> determineMaximumCostFactor(IUser user) {
		final String slaName = SLADictionary.getInstance().getUserSLA(user);
		if (slaName == null) {
			return Optional.absent();
		}
		final SLA sla = SLADictionary.getInstance().getSLA(slaName);
		if (sla == null) {
			return Optional.absent();
		}
		final Double factor = sla.getMaxAdmissionCostFactor();
		return Optional.of(factor < 0.0000001 ? 1.0 : factor);
	}

	private static List<IPhysicalOperator> getAllOperators(IPhysicalQuery query) {
		final List<IPhysicalOperator> operators = new ArrayList<IPhysicalOperator>();
		// filter
		for (final IPhysicalOperator operator : query.getPhysicalChilds()) {
			if (!operators.contains(operator) && !operator.getClass().getSimpleName().contains("DataSourceObserverSink") && operator.getOwner().contains(query)) {
				operators.add(operator);
			}
		}
		return operators;
	}

	private static long getUserEventBuffer(Map<IUser, Long> buffer, IUser user) {
		final Long eventBuffer = buffer.get(user);
		return eventBuffer != null ? eventBuffer : 0L;
	}

	private static void incrementUserEventBuffer(Map<IUser, Long> buffer, IUser user) {
		final Long eventBuffer = buffer.get(user);
		if (eventBuffer == null) {
			buffer.put(user, 1L);
		} else {
			buffer.put(user, eventBuffer + 1);
		}
	}

	private static boolean isGreater(ICost<IPhysicalOperator> first, ICost<IPhysicalOperator> last) {
		return first.compareTo(last) > 0;
	}

	private static ICost<IPhysicalOperator> mergeCosts(ICost<IPhysicalOperator> a, ICost<IPhysicalOperator> b) {
		if( a == null ) {
			if( b == null ) {
				return null;
			} 
			return b;
			
		} 
		if( b == null ) {
			return a;
		} 
		
		return a.merge(b);
	}

	private static void refreshTimestamps(Map<IPhysicalQuery, Long> timestamps, Collection<IPhysicalQuery> queriesToUpdate) {
		final long timestamp = System.currentTimeMillis();
		for (final IPhysicalQuery query : queriesToUpdate) {
			timestamps.put(query, timestamp);
		}
	}

	private static void resetUserEventBuffer(Map<IUser, Long> buffer, IUser user) {
		buffer.put(user, 0L);
	}

	private static ICost<IPhysicalOperator> substractCosts(ICost<IPhysicalOperator> a, ICost<IPhysicalOperator> b) {
		if( a == null ) {
			if( b == null ) {
				return null;
			} 
			return b;
			
		} 
		if( b == null ) {
			return a;
		} 
		
		return a.substract(b);
	}
}
