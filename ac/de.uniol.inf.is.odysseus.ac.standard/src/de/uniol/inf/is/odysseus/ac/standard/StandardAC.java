package de.uniol.inf.is.odysseus.ac.standard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionListener;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Standardimplementierung der Admission Control auf Basis von
 * {@link IAdmissionControl}. Verwaltet alle bekannten Kostenmodelle und setzt
 * sie bei Bedarf ein.
 * 
 * @author Timo Michelsen
 * 
 */
public class StandardAC implements IAdmissionControl, IPlanModificationListener {

	private static final long ESTIMATION_TOO_OLD_MILLIS = 3000;

	private Map<String, ICostModel> costModels;
	private ICostModel selectedCostModel;
	private IServerExecutor executor;

	private ICost maxCost;
	private ICost actCost;

	private Map<IPhysicalQuery, ICost> queryCosts = new HashMap<IPhysicalQuery, ICost>();
	private Map<IPhysicalQuery, ICost> runningQueryCosts = new HashMap<IPhysicalQuery, ICost>();
	private Map<IPhysicalQuery, Long> timestamps = new HashMap<IPhysicalQuery, Long>();

	private IPossibleExecutionGenerator generator = new PossibleExecutionGenerator();

	private final List<IAdmissionListener> listeners = new ArrayList<IAdmissionListener>();

    private static final Logger LOGGER = LoggerFactory.getLogger(StandardAC.class);

	@Override
	public ICost getCost(IPhysicalQuery query) {
		return queryCosts.get(query);
	}

	@Override
	public synchronized boolean canStartQuery(IPhysicalQuery query) {

		if (runningQueryCosts.containsKey(query))
			return true;

		ICost queryCost = null;
		if (queryCosts.containsKey(query)) {
			Long lastTime = timestamps.get(query);

			// last estimation too long before?
			if (System.currentTimeMillis() - lastTime > ESTIMATION_TOO_OLD_MILLIS) {
				queryCost = estimateCost(getAllOperators(query), false);
				timestamps.put(query, System.currentTimeMillis());
			} else {
				queryCost = queryCosts.get(query);
			}

		} else {
			queryCost = estimateCost(getAllOperators(query), false);
			queryCosts.put(query, queryCost);
			timestamps.put(query, System.currentTimeMillis());
		}

		// add costs of new query with actual system load
		ICost totalCost = null;
		if (actCost != null)
			totalCost = actCost.merge(queryCost);
		else
			totalCost = queryCost;
		LOGGER.debug("Total cost if executed: " + totalCost);

		// check, if total is lower than maximum allowed
		int cmp = totalCost.compareTo(maxCost);
		if (cmp == -1 || cmp == 0) {
			// low enough
			LOGGER.debug("Total cost would be lower than maximum cost");
		} else {
		    LOGGER.debug("Executing queries would exceed maximum cost");
		    LOGGER.debug("Maximum Cost: " + maxCost);
		}
		return cmp == -1 || cmp == 0;
	}

	@Override
	public synchronized void updateEstimations() {

		// check execution plan as one query
		// collect them
		List<IPhysicalOperator> operators = getAllOperators();
		actCost = estimateCost(operators, true);

		// update query estimations
		Map<IPhysicalQuery, ICost> map = new HashMap<IPhysicalQuery, ICost>();
		for (IPhysicalOperator op : operators) {
			List<IOperatorOwner> owners = op.getOwner();

			// find first active owner
			IPhysicalQuery query = null;
			for (IOperatorOwner owner : owners) {
				IPhysicalQuery q = (IPhysicalQuery) owner;
				if (q.isOpened()) {
					query = q;
					break;
				}
			}

			// operator not started?
			if (query == null)
				continue;

			if (map.containsKey(query)) {
				ICost cost = map.get(query);
				ICost newCost = cost.merge(actCost.getCostOfOperator(op));
				map.put(query, newCost);
			} else {
				map.put(query, actCost.getCostOfOperator(op));
			}
		}
		runningQueryCosts.putAll(map);
		queryCosts.putAll(map);

		for (IPhysicalQuery query : runningQueryCosts.keySet())
			timestamps.put(query, System.currentTimeMillis());

		// check, if system-load is too heavy
		LOGGER.debug("Cost of execution plan : " + actCost);
		int cmp = actCost.compareTo(maxCost);
		if (cmp > 0) {
			// too high load now!

			LOGGER.debug("Cost is too high");
			LOGGER.debug("MaxCost = " + maxCost);

			fireOverloadEvent();
		}
	}

	private List<IPhysicalOperator> getAllOperators() {
		List<IPhysicalOperator> operators = new ArrayList<IPhysicalOperator>();

		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries())
			for (IPhysicalOperator op : query.getPhysicalChilds())
				if (!operators.contains(op) && !op.getClass().getSimpleName().contains("DataSourceObserverSink") && op.getOwner().contains(query))
					operators.add(op);

		return operators;
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

	@Override
	public Set<String> getRegisteredCostModels() {
		return getCostModels().keySet();
	}

	@Override
	public synchronized void selectCostModel(String name) {
		if (!getCostModels().containsKey(name))
			throw new RuntimeException("CostModel " + name + " not found");

		selectedCostModel = getCostModels().get(name);
		maxCost = selectedCostModel.getMaximumCost();
		actCost = selectedCostModel.getZeroCost();

		LOGGER.debug("CostModel " + name + " selected");
		LOGGER.debug("Maximum cost allowed:" + maxCost);
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
	 * {@link PossibleExecutionGenerator} verwendet.
	 * 
	 * @return Aktuell verwendetes Kostenmodell
	 */
	ICostModel getSelectedCostModelInstance() {
		return selectedCostModel;
	}

	@Override
	public synchronized void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {

		IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		List<IPhysicalOperator> operators = getAllOperators(query);

		LOGGER.debug("EVENT : " + eventArgs.getEventType());
		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
			// query removed!
			LOGGER.debug("Query " + query + " removed");

			queryCosts.remove(query);
			timestamps.remove(query);

		} else if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs.getEventType())) {
			// query added!
			LOGGER.debug("Query " + query + " added");

			// do cost-estimation now
			ICost queryCost = estimateCost(operators, false);

			queryCosts.put(query, queryCost);
			timestamps.put(query, System.currentTimeMillis());

		} else if (PlanModificationEventType.QUERY_START.equals(eventArgs.getEventType())) {
			LOGGER.debug("Query " + query + " started");

			ICost queryCost = queryCosts.get(query);

			if (!runningQueryCosts.containsKey(query)) {
				runningQueryCosts.put(query, queryCost);
			}

		} else if (PlanModificationEventType.QUERY_STOP.equals(eventArgs.getEventType())) {
			LOGGER.debug("Query " + query + " stopped");

			if (runningQueryCosts.containsKey(query)) {
				runningQueryCosts.remove(query);
			}
		}
	}

	private ICost estimateCost(List<IPhysicalOperator> operators, boolean onUpdate) {
		if (getSelectedCostModel() == null) {
			throw new IllegalStateException("No CostModel selected.");
		}

		ICostModel costModel = getCostModels().get(getSelectedCostModel());
		ICost queryCost = costModel.estimateCost(operators, onUpdate);
		return queryCost;
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
		getCostModels().put(costModel.getClass().getSimpleName(), costModel);
		LOGGER.debug("Costmodel bound: " + costModel.getClass().getSimpleName());

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
		getCostModels().remove(costModel.getClass().getSimpleName());

		LOGGER.debug("Costmodel unbound: " + costModel.getClass().getSimpleName());
	}

	private Map<String, ICostModel> getCostModels() {
		if (costModels == null)
			costModels = new HashMap<String, ICostModel>();
		return costModels;
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

		LOGGER.debug("Executor bound");
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

			LOGGER.debug("Executor unbound");
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

	// Feuert das Overload-Event an alle registrierten Listener
	// der Admission Control
	private void fireOverloadEvent() {
		synchronized (listeners) {
			for (IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.overloadOccured(this);
					}
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<IPossibleExecution> getPossibleExecutions() {
		// generate possible executions
		List<IPossibleExecution> executions = generator.getPossibleExecutions(this, runningQueryCosts, maxCost);

		return executions;
	}

}
