package de.uniol.inf.is.odysseus.ac.standard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.ac.IAdmissionListener;
import de.uniol.inf.is.odysseus.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class StandardAC implements IAdmissionControl, IPlanModificationListener {

	private static final long ESTIMATION_TOO_OLD = 3000; // ms
	
	private Map<String, ICostModel> costModels;
	private ICostModel selectedCostModel;
	private IExecutor executor;

	private ICost maxCost;
	private ICost actCost;

	private Map<IQuery, ICost> queryCosts = new HashMap<IQuery, ICost>();
	private Map<IQuery, ICost> runningQueryCosts = new HashMap<IQuery, ICost>();
	private Map<IQuery, Long> timestamps = new HashMap<IQuery, Long>();

	private IPossibleExecutionGenerator generator = new PossibleExecutionGenerator();

	private final List<IAdmissionListener> listeners = new ArrayList<IAdmissionListener>();

	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(StandardAC.class);
		}
		return _logger;
	}
	
	@Override
	public ICost getCost(IQuery query) {
		return queryCosts.get(query);
	}

	public synchronized boolean canStartQuery(IQuery query) {

		if (runningQueryCosts.containsKey(query))
			return true;
		
		ICost queryCost = null;
		if( queryCosts.containsKey(query)) {
			Long lastTime = timestamps.get(query);
			
			// last estimation too long before?
			if( System.currentTimeMillis() - lastTime > ESTIMATION_TOO_OLD) {
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
		getLogger().debug("Total cost if executed: " + totalCost);

		// check, if total is lower than maximum allowed
		int cmp = totalCost.compareTo(maxCost);
		if (cmp == -1 || cmp == 0) {
			// low enough
			getLogger().debug("Total cost would be lower than maximum cost");
			return true;
		} else {
			// too high costs!
			getLogger().debug("Executing queries would exceed maximum cost");
			getLogger().debug("Maximum Cost: " + maxCost);
			return false;
		}
	}

	@Override
	public synchronized void updateEstimations() {

		// check execution plan as one query
		// collect them
		List<IPhysicalOperator> operators = getAllOperators();
		actCost = estimateCost(operators, true);

		// update query estimations
		Map<IQuery, ICost> map = new HashMap<IQuery, ICost>();
		for (IPhysicalOperator op : operators) {
			List<IOperatorOwner> owners = op.getOwner();

			// find first active owner
			IQuery query = null;
			for( IOperatorOwner owner : owners ) {
				IQuery q = (IQuery)owner;
				if( q.isOpened()) {
					query = q;
					break;
				}
			}
			
			// operator not started?
			if( query == null )
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
		
		for( IQuery query : runningQueryCosts.keySet() ) 
			timestamps.put(query, System.currentTimeMillis());
		
		// check, if system-load is too heavy
		getLogger().debug("Cost of execution plan : " + actCost);
		int cmp = actCost.compareTo(maxCost);
		if (cmp > 0) {
			// too high load now!

			getLogger().debug("Cost is too high");
			getLogger().debug("MaxCost = " + maxCost);

			fireOverloadEvent();
		}
	}

	private List<IPhysicalOperator> getAllOperators() {
		List<IPhysicalOperator> operators = new ArrayList<IPhysicalOperator>();

		for (IQuery query : executor.getQueries())
			for (IPhysicalOperator op : query.getPhysicalChilds())
				if (!operators.contains(op) && 
					!op.getClass().getSimpleName().contains("DataSourceObserverSink") &&
					op.getOwner().contains(query))
					operators.add(op);

		return operators;
	}

	private List<IPhysicalOperator> getAllOperators(IQuery query) {
		List<IPhysicalOperator> operators = new ArrayList<IPhysicalOperator>();
		// filter
		for (IPhysicalOperator operator : query.getPhysicalChilds()) {
			if (!operators.contains(operator) && 
				!operator.getClass().getSimpleName().contains("DataSourceObserverSink") && 
				operator.getOwner().contains(query))
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

		getLogger().debug("CostModel " + name + " selected");
		getLogger().debug("Maximum cost allowed:" + maxCost);
	}

	@Override
	public String getSelectedCostModel() {
		if (selectedCostModel == null)
			return null;

		return selectedCostModel.getClass().getSimpleName();
	}

	ICostModel getSelectedCostModelInstance() {
		return selectedCostModel;
	}

	@Override
	public synchronized void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {

		IQuery query = (IQuery) eventArgs.getValue();
		List<IPhysicalOperator> operators = getAllOperators(query);

		getLogger().debug("EVENT : " + eventArgs.getEventType());
		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
			// query removed!
			getLogger().debug("Query " + query + " removed");

			queryCosts.remove(query);
			timestamps.remove(query);

		} else if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs.getEventType())) {
			// query added!
			getLogger().debug("Query " + query + " added");

			// do cost-estimation now
			ICost queryCost = estimateCost(operators, false);

			queryCosts.put(query, queryCost);
			timestamps.put(query, System.currentTimeMillis());

		} else if (PlanModificationEventType.QUERY_START.equals(eventArgs.getEventType())) {
			getLogger().debug("Query " + query + " started");

			ICost queryCost = queryCosts.get(query);

			if (!runningQueryCosts.containsKey(query)) {
				runningQueryCosts.put(query, queryCost);
			}
//			updateEstimations();

		} else if (PlanModificationEventType.QUERY_STOP.equals(eventArgs.getEventType())) {
			getLogger().debug("Query " + query + " stopped");

			if (runningQueryCosts.containsKey(query)) {
				runningQueryCosts.remove(query);
			}

//			updateEstimations();
		}
	}

	private ICost estimateCost(List<IPhysicalOperator> operators, boolean onUpdate) {
		if (getSelectedCostModel() != null) {

			ICostModel costModel = getCostModels().get(getSelectedCostModel());
			ICost queryCost = costModel.estimateCost(operators, onUpdate);
			return queryCost;
		} else {

			throw new RuntimeException(" No CostModel selected");
		}

	}

	public void bindCostModel(ICostModel costModel) {
		getCostModels().put(costModel.getClass().getSimpleName(), costModel);
		getLogger().debug("Costmodel bound: " + costModel.getClass().getSimpleName());

		if (getSelectedCostModel() == null) {
			selectCostModel(costModel.getClass().getSimpleName());
		}
	}

	public void unbindCostModel(ICostModel costModel) {
		getCostModels().remove(costModel.getClass().getSimpleName());

		getLogger().debug("Costmodel unbound: " + costModel.getClass().getSimpleName());
	}

	private Map<String, ICostModel> getCostModels() {
		if (costModels == null)
			costModels = new HashMap<String, ICostModel>();
		return costModels;
	}

	public void bindExecutor(IExecutor executor) {
		this.executor = executor;

		this.executor.addPlanModificationListener(this);

		getLogger().debug("Executor bound");
	}

	public void unbindExecutor(IExecutor executor) {
		if (executor == this.executor) {
			this.executor.removePlanModificationListener(this);
			this.executor = null;

			getLogger().debug("Executor unbound");
		}
	}

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

	private void fireOverloadEvent() {
		synchronized (listeners) {
			for (IAdmissionListener listener : listeners) {
				try {
					if (listener != null) {
						listener.overloadOccured(this);
					}
				} catch (Exception ex) {
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
