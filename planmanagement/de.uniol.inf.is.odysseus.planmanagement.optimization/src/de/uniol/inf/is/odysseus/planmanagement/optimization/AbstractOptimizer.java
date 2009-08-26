package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.AbstractEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize.AbstractStartEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;

public abstract class AbstractOptimizer implements IOptimizer {

	protected Logger logger;

	protected Map<String, IBufferPlacementStrategy> bufferPlacementStrategies = new HashMap<String,IBufferPlacementStrategy>();

	protected IPlanOptimizer planOptimizer;

	protected IQueryOptimizer queryOptimizer;

	protected IPlanMigrationStrategie planMigrationStrategie;

	//protected IBufferPlacementStrategy bufferPlacementStrategy;

	private ArrayList<IErrorEventListener> errorEventListener = new ArrayList<IErrorEventListener>();

	public void activate() {
		this.logger = LoggerFactory.getLogger(AbstractOptimizer.class);
		this.logger.trace("Create Executor.");
	}

	public void bindBufferPlacementStrategy(
			IBufferPlacementStrategy bufferPlacementStrategy) {
		String bpN = bufferPlacementStrategy.getName();
		synchronized (this.bufferPlacementStrategies) {
			this.bufferPlacementStrategies.put(bpN, bufferPlacementStrategy);
		}
	}

	public void unbindBufferPlacementStrategy(
			IBufferPlacementStrategy bufferPlacementStrategy) {
		synchronized (this.bufferPlacementStrategies) {
			this.bufferPlacementStrategies.remove(bufferPlacementStrategy.getName());
		}
	}

	public void bindPlanOptimizer(IPlanOptimizer planOptimizer) {
		this.planOptimizer = planOptimizer;
	}

	public void unbindPlanOptimizer(IPlanOptimizer planOptimizer) {
		if (this.planOptimizer == planOptimizer) {
			this.planOptimizer = null;
		}
	}

	public void bindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		this.queryOptimizer = queryOptimizer;
	}

	public void unbindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		if (this.queryOptimizer == queryOptimizer) {
			this.queryOptimizer = null;
		}
	}

	public void bindPlanMigrationStrategie(
			IPlanMigrationStrategie planMigrationStrategie) {
		this.planMigrationStrategie = planMigrationStrategie;
	}

	public void unbindPlanMigrationStrategie(
			IPlanMigrationStrategie planMigrationStrategie) {
		if (this.planMigrationStrategie == planMigrationStrategie) {
			this.planMigrationStrategie = null;
		}
	}

	public String getInfoString(Object object, String label) {
		String infos = AppEnv.LINE_SEPERATOR + label + ": ";
		if (object != null) {
			infos += object.getClass();
		} else {
			infos += "not set. ";
		}
		return infos;
	}

	protected void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.sendErrorEvent(eventArgs);
		}
	}

	protected abstract IExecutionPlan processOptimizeEvent(
			AbstractStartEvent<?> eventArgs) throws QueryOptimizationException;

	@Override
	public void optimizeStartEvent(AbstractStartEvent<?> eventArgs)
			throws QueryOptimizationException {

		this.logger.info("Start optimize: " + eventArgs.getID());

		AbstractEndEvent<?> endEventargs = eventArgs.createEndOptimizeEvent(
				this, processOptimizeEvent(eventArgs));

		if (endEventargs != null) {
			eventArgs.getSender().optimizeEndEvent(endEventargs);
			this.logger.info("End optimize, send event: "
					+ endEventargs.getID());
			return;
		}
		this.logger.info("End optimization. No new execution plan generated.");
	}

	@Override
	public String getInfos() {
		String infos = "<Optimizer class=\"" + this + "\"> ";

		infos += "<BufferPlacementStrategy>";
		for (String bufferPlacementStrategy : this.bufferPlacementStrategies.keySet()) {
			infos += getInfoString(bufferPlacementStrategy,
					"BufferPlacementStrategy comp.: ");
		}
		infos += "</BufferPlacementStrategy>";

		infos += getInfoString(this.planMigrationStrategie,
				"PlanMigrationStrategie");
		infos += getInfoString(this.planOptimizer, "PlanOptimizer");
		infos += getInfoString(this.queryOptimizer, "QueryOptimizer");

		infos += AppEnv.LINE_SEPERATOR + "</Optimizer> ";

		return infos;
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategies() {
		return this.bufferPlacementStrategies.keySet();
	}
	
	@Override
	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy) {
		return this.bufferPlacementStrategies.get(strategy);
	}
	
	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.add(errorEventListener);
	}

	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}
}
