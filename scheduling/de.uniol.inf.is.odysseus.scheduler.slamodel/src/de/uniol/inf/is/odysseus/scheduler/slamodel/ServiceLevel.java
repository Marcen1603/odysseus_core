package de.uniol.inf.is.odysseus.scheduler.slamodel;

public class ServiceLevel<T> {

	private T threshold;
	
	private Penalty penalty;
	
	private SLA sla;
	
	public ServiceLevel() {
		
	}
	
	public void setThreshold(T threshold) {
		this.threshold = threshold;
	}

	public T getThreshold() {
		return threshold;
	}

	public void setSla(SLA sla) {
		this.sla = sla;
	}

	public SLA getSla() {
		return sla;
	}

	public void setPenalty(Penalty penalty) {
		this.penalty = penalty;
	}

	public Penalty getPenalty() {
		return penalty;
	}
	
}
