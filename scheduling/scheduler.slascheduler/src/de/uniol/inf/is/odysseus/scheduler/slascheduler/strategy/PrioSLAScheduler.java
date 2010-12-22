package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ScheduleMeta;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

// This class is only for testing purposes to compare
// sla based scheduling with priority based scheduling
// do not use this class in real szenarios ;-)

public class PrioSLAScheduler extends AbstractTimebasedSLAScheduler{

	
	private int minPrio;
	Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

	
	public PrioSLAScheduler(int minPrio) {
		super(PrioCalcMethod.MAX);
		this.minPrio = minPrio;
	}
	
	public PrioSLAScheduler(PrioSLAScheduler other){
		super(other);
		this.minPrio = other.minPrio;
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
		ScheduleMeta meta = toSchedule.getPlan().getScheduleMeta();
		if (minTime.get(toSchedule) == null) {
			long minTimeP = calcMinTimePeriod(toSchedule.getPlan());
			minTime.put(toSchedule, minTimeP);
			//logger.debug("MinTime for "+is+" set to "+minTimeP);
		}

		meta.scheduleDone(minTime.get(toSchedule));
		return updatePriority(toSchedule);
	}

	@Override
	public AbstractSLAScheduler clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
