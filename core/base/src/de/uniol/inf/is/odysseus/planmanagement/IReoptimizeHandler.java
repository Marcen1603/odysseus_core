package de.uniol.inf.is.odysseus.planmanagement;

/**
 * Describes an object which handles reoptimize requests.
 * 
 * @author Wolf Bauer
 * 
 * @param <ReoptimizableListenerType>
 *            Type of the objects which are called if an reoptimization is
 *            requested.
 */
public interface IReoptimizeHandler<ReoptimizableListenerType> {
	/**
	 * Adds an object which is called if an reoptimization is requested.
	 * 
	 * @param reoptimizationListener Object which will be added.
	 */
	public void addReoptimizeListener(
			ReoptimizableListenerType reoptimizationListener);

	/**
	 * Removes an object which is called if an reoptimization is requested.
	 * 
	 * @param reoptimizationListener Object which will be removed.
	 */
	public void removeReoptimizeListener(
			ReoptimizableListenerType reoptimizationListener);
}
