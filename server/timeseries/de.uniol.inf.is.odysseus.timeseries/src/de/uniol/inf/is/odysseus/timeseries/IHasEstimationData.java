package de.uniol.inf.is.odysseus.timeseries;

/**
 * Interface with methods to add and remove data from estimation data.
 * 
 * @author Christoph Schröer <T>
 * 
 * 
 * @param <T>
 *            Type of Estimation Data
 */
public interface IHasEstimationData<T> {

	/**
	 * 
	 * @param data
	 *            to add
	 */
	public void addEstimationData(T data);

	/**
	 * 
	 * @param data
	 *            to remove
	 */
	public void removeEstimationData(T data);

	/**
	 * clear estimation data
	 */
	public void clear();

}
