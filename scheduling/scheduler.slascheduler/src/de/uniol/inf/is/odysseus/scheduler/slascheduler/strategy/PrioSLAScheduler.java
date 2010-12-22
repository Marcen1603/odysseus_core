package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ScheduleMeta;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

// This class is only for testing purposes to compare
// sla based scheduling with priority based scheduling
// do not use this class in real szenarios ;-)

public class PrioSLAScheduler extends AbstractTimebasedSLAScheduler{

	
	final private int minPrio;
	final Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

	
	public PrioSLAScheduler(int minPrio) {
		super(PrioCalcMethod.MAX);
		this.minPrio = minPrio;
	}
	
	public PrioSLAScheduler(PrioSLAScheduler other){
		super(other);
		this.minPrio = other.minPrio;
	}

	public PrioSLAScheduler(PrioCalcMethod method) {
		super(method);
		this.minPrio = 0;
	}

	@Override
	public IScheduling nextPlan() {
		IScheduling plan = super.nextPlan();
		if (plan != null) return plan;
		long maxPrio = getMaxPrio();
		return initLastRunAndReturn(maxPrio);
		
	}
	
	protected IScheduling updatePriority(IScheduling current) {
		long currentPriority = current.getPlan().getCurrentPriority();
		long newPrio = currentPriority - 1;
		if (newPrio <= minPrio) {
			newPrio = current.getPlan().getBasePriority();
		}
		if (newPrio != currentPriority) {
			current.getPlan().setCurrentPriority(newPrio);
		}
		return current;
	}
	
	@Override
	protected IScheduling updateMetaAndReturnPlan(IScheduling toSchedule) {
		drainHistory(toSchedule);
		return updatePriority(super.updateMetaAndReturnPlan(toSchedule));
	}

	@Override
	public PrioSLAScheduler clone() {
		return new PrioSLAScheduler(this);
	}

}
