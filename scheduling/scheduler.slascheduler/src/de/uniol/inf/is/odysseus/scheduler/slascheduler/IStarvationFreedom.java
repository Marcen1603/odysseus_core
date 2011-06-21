package de.uniol.inf.is.odysseus.scheduler.slascheduler;

public interface IStarvationFreedom {

	/**
	 * returns a cost value avoid starvation freedom
	 * 
	 * @param decay
	 *            the decay factor of the starvation-freedom-function. This
	 *            param is needed for fine-tuning of the function and should be
	 *            given by the system
	 * @return a cost value representing the costs that would be caused by
	 *         starvation of a query. this value does not represent effective
	 *         costs, but should be relative to the values of the cost functions
	 *         oc() and mg().
	 */
	public double sf(double decay);

}
