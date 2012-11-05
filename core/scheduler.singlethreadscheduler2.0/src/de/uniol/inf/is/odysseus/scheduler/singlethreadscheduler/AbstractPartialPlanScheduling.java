package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.server.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;

abstract public class AbstractPartialPlanScheduling implements IPartialPlanScheduling, ISchedulingEventListener, IClone {

	final private Set<IScheduling> pausedPlans;
	
	public AbstractPartialPlanScheduling() {
		pausedPlans = new HashSet<IScheduling>();
	}

	AbstractPartialPlanScheduling(AbstractPartialPlanScheduling other){
		this.pausedPlans = new HashSet<IScheduling>(other.pausedPlans);
	}
	
	@Override
	public void clear() {
		pausedPlans.clear();
	}
	
	protected void checkPausedPlans() {
		synchronized (pausedPlans) {
			while (pausedPlans.size() == planCount()) {
				try {
					pausedPlans.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	
	@Override
	public void nothingToSchedule(IScheduling sched) {
		pausedPlans.add(sched);
	}

	@Override
	public void scheddulingPossible(IScheduling sched) {
		synchronized (pausedPlans) {
			pausedPlans.remove(sched);
			pausedPlans.notifyAll();
		}
	}
	
	@Override
	abstract public IPartialPlanScheduling clone();


}
