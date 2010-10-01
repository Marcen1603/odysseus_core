package de.uniol.inf.is.odysseus.planmanagement.configuration;

/**
 * Describes an object that informs other object that entries of
 * {@link AbstractTypeSafeMap} are modified.
 * 
 * @author Wolf Bauer
 * 
 * @param <T>
 */
public interface IValueChangeHandler<T extends IMapValue<?>> {
	/**
	 * Adds an object that should be informed if an entry is modified.
	 * 
	 * @param listener
	 *            Object that should be informed if an entry is modified.
	 */
	public void addValueChangeListener(IValueChangeListener<T> listener);

	/**
	 * Removes an object that should no longer be informed if an entry is
	 * modified.
	 * 
	 * @param listener
	 *            Abject that should no longer be informed if an entry is
	 *            modified.
	 */
	public void removeValueChangeListener(IValueChangeListener<T> listener);
}
