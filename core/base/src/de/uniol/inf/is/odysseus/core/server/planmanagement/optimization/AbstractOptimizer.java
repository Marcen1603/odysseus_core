/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.querysharing.IQuerySharingOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * AbstractOptimizer implements base optimization functions. It manages services
 * for query optimization, plan optimization, buffer placement and plan
 * migration.
 * 
 * @author Wolf Bauer, Jonas Jacobi, Marco Grawunder
 */
/**
 * @author Wolf Bauer
 *
 */
public abstract class AbstractOptimizer implements IOptimizer {

	
	volatile protected static Logger _logger = null;

	synchronized protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(AbstractOptimizer.class);
		}
		return _logger;
	}

	/**
	 * Map of registered buffer placement strategies.
	 */
	protected Map<String, IBufferPlacementStrategy> bufferPlacementStrategies = new HashMap<String, IBufferPlacementStrategy>();

	/**
	 * List of PostOptimizationActions
	 */
	private List<IPostOptimizationAction> postOptimizationActions = new ArrayList<IPostOptimizationAction>();
	
	/**
	 * Registered plan optimization service.
	 */
	protected IPlanOptimizer planOptimizer;

	/**
	 * Registered query optimization service.
	 */
	protected IQueryOptimizer queryOptimizer;
	
	/**
	 * optional Query-Sharing-Component
	 */
	protected IQuerySharingOptimizer querySharingOptimizer = null;

	/**
	 * List of error event listener. If an error occurs these objects should be informed.
	 */
	private ArrayList<IErrorEventListener> errorEventListener = new ArrayList<IErrorEventListener>();
	
	protected OptimizationConfiguration configuration = new OptimizationConfiguration();

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 */
	public void activate() {
		// load logger
		getLogger().trace("Create Executor.");
	}

	/**
	 * Method to bind a {@link IBufferPlacementStrategy}. Used by OSGi.
	 * 
	 * @param bufferPlacementStrategy new {@link IBufferPlacementStrategy} service
	 */
	public void bindBufferPlacementStrategy(
			IBufferPlacementStrategy bufferPlacementStrategy) {
		String bpN = bufferPlacementStrategy.getName();
		getLogger().debug("bindBufferPlacementStrategy "+bpN);
		synchronized (this.bufferPlacementStrategies) {
			this.bufferPlacementStrategies.put(bpN, bufferPlacementStrategy);
		}
	}

	/**
	 * Method to unbind a {@link IBufferPlacementStrategy}. Used by OSGi.
	 * 
	 * @param bufferPlacementStrategy {@link IBufferPlacementStrategy} service to unbind
	 */
	public void unbindBufferPlacementStrategy(
			IBufferPlacementStrategy bufferPlacementStrategy) {
		synchronized (this.bufferPlacementStrategies) {
			this.bufferPlacementStrategies.remove(bufferPlacementStrategy
					.getName());
		}
	}

	public void bindPostOptimizationAction(
			IPostOptimizationAction postOptimizationAction) {
		getLogger().debug("bindPostOptimazationAction "+postOptimizationAction);
		synchronized (this.postOptimizationActions) {
			this.postOptimizationActions.add(postOptimizationAction);
		}
	}
	
	public void unbindPostOptimizationAction(
			IPostOptimizationAction postOptimizationAction) {
		getLogger().debug("unbindPostOptimizationAction "+postOptimizationAction);
		synchronized (this.postOptimizationActions) {
			this.postOptimizationActions.remove(postOptimizationAction);
		}
	}

	
	/**
	 * Method to bind a {@link IPlanOptimizer}. Used by OSGi.
	 * 
	 * @param planOptimizer new {@link IPlanOptimizer} service
	 */
	public void bindPlanOptimizer(IPlanOptimizer planOptimizer) {
		this.planOptimizer = planOptimizer;
	}

	/**
	 * Method to unbind a {@link IPlanOptimizer}. Used by OSGi.
	 * 
	 * @param planOptimizer {@link IPlanOptimizer} service to unbind
	 */
	public void unbindPlanOptimizer(IPlanOptimizer planOptimizer) {
		if (this.planOptimizer == planOptimizer) {
			this.planOptimizer = null;
		}
	}

	/**
	 * Method to bind a {@link IQueryOptimizer}. Used by OSGi.
	 * 
	 * @param queryOptimizer new {@link IQueryOptimizer} service
	 */
	public void bindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		this.queryOptimizer = queryOptimizer;
	}

	/**
	 * Method to unbind a {@link IQueryOptimizer}. Used by OSGi.
	 * 
	 * @param queryOptimizer {@link IQueryOptimizer} service to unbind
	 */
	public void unbindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		if (this.queryOptimizer == queryOptimizer) {
			this.queryOptimizer = null;
		}
	}
	
	/**
	 * bindQuerySharingOptimizer bindet eine Querysharing-Komponente ein
	 * 
	 * @param querySharingOptimizer
	 *            neue Querysharing-Komponente
	 */
	public void bindQuerySharingOptimizer(IQuerySharingOptimizer querySharingOptimizer) {
		this.querySharingOptimizer = querySharingOptimizer;
		getLogger().debug("QuerysharingOptimizer bound " + querySharingOptimizer);
	}
	
	/**
	 * unbindQuerysharingOptimizer entfernt eine Querysharing-Komponente
	 * 
	 * @param querySharingOptimizer
	 *            zu entfernende Querysharing-Komponente
	 */
	public void unbindQuerySharingOptimizer(IQuerySharingOptimizer querySharingOptimizer) {
		if (this.querySharingOptimizer == querySharingOptimizer) {
			this.querySharingOptimizer = null;
			getLogger().debug("QuerysharingOptimizer unbound " + querySharingOptimizer);
		}
	}
	
	/**
	 * QuerySharingOptimizer liefert den aktuellen QuerySharingOpzimizer.
	 * 
	 * @return aktueller QuerySharingOptimizer
	 * 
	 */
	@Override
	public IQuerySharingOptimizer getQuerySharingOptimizer()  {
		if (this.querySharingOptimizer != null) {
			return this.querySharingOptimizer;
		}

		return null;
	}
	
	
	/**
	 * Get a formated info string for object. if object not null
	 * 
	 * @param object
	 *            object to describe
	 * @param label
	 *            label for description
	 * @return String: "LINE_SEPERATOR + label + ":" + class name of object" or
	 *         "LINE_SEPERATOR + label + ":" + not set"
	 */
	public String getInfoString(Object object, String label) {
		return getInfoString(object.toString(), label);
	}

	/**
	 * Get a formated info string for object. if object not null
	 * 
	 * @param object
	 *            object to describe
	 * @param label
	 *            label for description
	 * @return String: "LINE_SEPERATOR + label + ":" + object" or
	 *         "LINE_SEPERATOR + label + ":" + not set"
	 */
	public String getInfoString(String object, String label) {
		String infos = AppEnv.LINE_SEPARATOR + label + ": ";
		if (object != null) {
			infos += object;
		} else {
			infos += "not set. ";
		}
		return infos;
	}

	/**
	 * Sends an {@link ErrorEvent} to all registered EventListenern
	 * 
	 * @param eventArgs {@link ErrorEvent} to send
	 */
	@Override
	public void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.errorEventOccured(eventArgs);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer#preStartOptimization(de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public void beforeQueryStart(IPhysicalQuery queryToStart,
			IExecutionPlan executionPlan)
			throws QueryOptimizationException {
	}

	
	protected void doPostOptimizationActions(IPhysicalQuery query, OptimizationConfiguration parameter) {
		for (IPostOptimizationAction action: postOptimizationActions){
			getLogger().debug("Do PostOptimizationAction "+action);
			action.run(query, parameter);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer#reoptimize(de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public void reoptimize(IPhysicalQuery query,
			IExecutionPlan executionPlan)
			throws QueryOptimizationException {
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer#reoptimize(de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlan, de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public void reoptimize(IExecutionPlan executionPlan)
			throws QueryOptimizationException {
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer#preStopOptimization(de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public void beforeQueryStop(IPhysicalQuery queryToStop,
			IExecutionPlan execPlan) throws QueryOptimizationException {
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.IInfoProvider#getInfos()
	 */
	@Override
	public String getInfos() {
		String infos = "<Optimizer class=\"" + this + "\"> ";

		infos += "<BufferPlacementStrategy>";
		for (String bufferPlacementStrategy : this.bufferPlacementStrategies
				.keySet()) {
			infos += getInfoString(bufferPlacementStrategy,
					"BufferPlacementStrategy comp.: ");
		}
		infos += "</BufferPlacementStrategy>";

//		infos += getInfoString(this.planMigrationStrategy,
//				"PlanMigrationStrategie");
		infos += getInfoString(this.planOptimizer, "PlanOptimizer");
		infos += getInfoString(this.queryOptimizer, "QueryOptimizer");

		infos += AppEnv.LINE_SEPARATOR + "</Optimizer> ";

		return infos;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer#getRegisteredBufferPlacementStrategies()
	 */
	@Override
	public Set<String> getRegisteredBufferPlacementStrategies() {
		return this.bufferPlacementStrategies.keySet();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer#getBufferPlacementStrategy(java.lang.String)
	 */
	@Override
	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy) {
		return this.bufferPlacementStrategies.get(strategy);
	}
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler#addErrorEventListener(de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener)
	 */
	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.add(errorEventListener);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler#removeErrorEventListener(de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener)
	 */
	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}
	
	@Override
	public OptimizationConfiguration getConfiguration() {
		return this.configuration;
	}
}
