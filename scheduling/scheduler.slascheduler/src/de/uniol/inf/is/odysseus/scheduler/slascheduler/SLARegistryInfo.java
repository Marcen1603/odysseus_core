package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.List;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IBuffer;

/**
 * container for all data items managed by the maps of the SLARegistry
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLARegistryInfo {
	/**
	 * the sla costfunction of a partial plan
	 */
	private ISLAConformance conformance;
	/**
	 * the cost function of a partial plan
	 */
	private ICostFunction costFunction;
	/**
	 * the timestamp of the last execution of a partial plan
	 */
	private long lastExecTimeStamp;
	/**
	 * the starvation freedom object of a partial plan
	 */
	private IStarvationFreedom starvationFreedom;
	/**
	 * the operator of a partial plan, the conformance object is connected to
	 */
	private ISubscribable<?, ?> connectionPoint;
	/**
	 * list of buffers owned by the query
	 */
	private List<IBuffer<?>> buffers;
	
	/**
	 * 
	 * @return the starvation freedom object of a partial plan
	 */
	public IStarvationFreedom getStarvationFreedom() {
		return starvationFreedom;
	}
	
	/**
	 * sets the starvation freedom object of a partial plan
	 * @param starvationFreedom the new starvation freedom object
	 */
	public void setStarvationFreedom(IStarvationFreedom starvationFreedom) {
		this.starvationFreedom = starvationFreedom;
	}

	/**
	 * builds a new {@link SLARegistryInfo} object
	 */
	public SLARegistryInfo() {
		super();
		this.lastExecTimeStamp = 0;
	}

	/**
	 * @return the sla conformance object of a partial plan
	 */
	public ISLAConformance getConformance() {
		return conformance;
	}

	/**
	 * sets the sla conformance object of a partial plan
	 * @param conformance the new sla conformance object
	 */
	public void setConformance(ISLAConformance conformance) {
		this.conformance = conformance;
	}

	/**
	 * @return the cost function of a partial plan
	 */
	public ICostFunction getCostFunction() {
		return costFunction;
	}

	/**
	 * sets the cost function of a partial plan
	 * @param costFunction the new cost function
	 */
	public void setCostFunction(ICostFunction costFunction) {
		this.costFunction = costFunction;
	}

	/**
	 * @return the timestamp of the last execution
	 */
	public long getLastExecTimeStamp() {
		return lastExecTimeStamp;
	}

	/**
	 * sets the timestamp of last execution
	 * @param lastExecTimeStamp the new timestamp of last execution
	 */
	public void setLastExecTimeStamp(long lastExecTimeStamp) {
		this.lastExecTimeStamp = lastExecTimeStamp;
	}

	/**
	 * sets the new connection point of the sla conformance object
	 * @param connectionPoint the new connection point
	 */
	public void setConnectionPoint(ISubscribable<?, ?> connectionPoint) {
		this.connectionPoint = connectionPoint;
	}

	/**
	 * @return returns the operator the sla conformance object is connected to
	 */
	public ISubscribable<?, ?> getConnectionPoint() {
		return connectionPoint;
	}
	/**
	 * sets the new list of buffers
	 * @param buffers the new list of buffers
	 */
	public void setBuffers(List<IBuffer<?>> buffers) {
		this.buffers = buffers;
	}

	/**
	 * @return the list of buffers owned by the query
	 */
	public List<IBuffer<?>> getBuffers() {
		return buffers;
	}

}
