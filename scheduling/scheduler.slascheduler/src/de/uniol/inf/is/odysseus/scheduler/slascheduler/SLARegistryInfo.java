package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;

/**
 * container for all data items managed by the maps of the SLARegistry
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLARegistryInfo {

	private SLA sla;

	private ISLAConformance conformance;

	private ICostFunction costFunction;

	private long lastExecTimeStamp;

	private IStarvationFreedom starvationFreedom;

	public IStarvationFreedom getStarvationFreedom() {
		return starvationFreedom;
	}

	public void setStarvationFreedom(IStarvationFreedom starvationFreedom) {
		this.starvationFreedom = starvationFreedom;
	}

	public SLARegistryInfo() {
		super();
		this.lastExecTimeStamp = 0;
	}

	public SLA getSla() {
		return sla;
	}

	public void setSla(SLA sla) {
		this.sla = sla;
	}

	public ISLAConformance getConformance() {
		return conformance;
	}

	public void setConformance(ISLAConformance conformance) {
		this.conformance = conformance;
	}

	public ICostFunction getCostFunction() {
		return costFunction;
	}

	public void setCostFunction(ICostFunction costFunction) {
		this.costFunction = costFunction;
	}

	public long getLastExecTimeStamp() {
		return lastExecTimeStamp;
	}

	public void setLastExecTimeStamp(long lastExecTimeStamp) {
		this.lastExecTimeStamp = lastExecTimeStamp;
	}

}
