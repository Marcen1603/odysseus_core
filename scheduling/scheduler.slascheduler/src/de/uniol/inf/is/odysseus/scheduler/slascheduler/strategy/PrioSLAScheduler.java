package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

// This class is only for testing purposes to compare
// sla based scheduling with priority based scheduling
// do not use this class in real szenarios ;-)

public class PrioSLAScheduler extends AbstractSLAScheduler{

	
	private int minPrio;

	public PrioSLAScheduler(int minPrio) {
		super(PrioCalcMethod.MAX);
		this.minPrio = minPrio;
	}
	
	public PrioSLAScheduler(PrioSLAScheduler other){
		super(other);
		this.minPrio = other.minPrio;
	}

	@Override
	protected IScheduling updateMetaAndReturnPlan(IScheduling plan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractSLAScheduler clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
