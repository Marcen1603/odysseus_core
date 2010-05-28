package de.uniol.inf.is.odysseus.scheduler.strategy;

import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

/**
 * Superclass for scheduling strategies. When scheduled by the scheduler, it
 * will execute sources returned by subsequent calls to the abstract method
 * {@link #nextSource()}, until either the time limit is reached or
 * {@link #nextSource()} returns null.
 * 
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractScheduling implements IScheduling {
	
	private IPartialPlan plan = null;
	protected boolean isPlanChanged = true;

	public AbstractScheduling(IPartialPlan plan) {
		this.plan = plan;
	}
	
	public void planChanged()
	{
		this.isPlanChanged = true;
	}
	
	abstract public void applyChangedPlan();

	@Override
	/** Returns true if nothing more to schedule
	 */
	public boolean schedule(long maxTime) {
		// Testen ob die Zeit richtig gesetzt ist (ansonsten wuerde die Schleife
		// in transferNext() aufrufen. Evtl. den Test weiter nach oben schieben?
//		if (maxTime <= 0){
//			throw new IllegalArgumentException("maxTime must be greater 0");
//		}
		
		// if the underlying plan has changed, we need to call
		// the update-method before starting the scheduling:
		if (this.isPlanChanged) {
			this.applyChangedPlan();
			this.isPlanChanged = false;
		}		
		
		long endTime = System.currentTimeMillis() + maxTime;
		IIterableSource<?> nextSource = nextSource();
		while(nextSource != null && System.currentTimeMillis() < endTime){
			//System.out.println("Process ISource "+nextSource);
			if (nextSource.hasNext() && nextSource.isActive()) {
				nextSource.transferNext();
			}
			if (nextSource.isDone()){
				sourceDone(nextSource);
			}
			nextSource = nextSource();
		}
		return isDone();
	}

	public abstract IIterableSource<?> nextSource();
	public abstract void sourceDone(IIterableSource<?> source);
	public abstract boolean isDone();

	public IPartialPlan getPlan() {
		return plan;
	};
}
