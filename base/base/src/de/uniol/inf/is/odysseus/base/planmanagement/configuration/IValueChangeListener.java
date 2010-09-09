package de.uniol.inf.is.odysseus.base.planmanagement.configuration;

/**
 * Describes an object which will be informed if an entry of
 * {@link AbstractTypeSafeMap} is modified.
 * 
 * @author Wolf Bauer
 * 
 * @param <T>
 *            Type of the supported entries.
 */
public interface IValueChangeListener<T extends IMapValue<?>> {
	/**
	 * Informs this object that an entry is modified.
	 * 
	 * @param newValueContainer
	 *            The entry that is modified.
	 */
	public void settingChanged(T newValueContainer);
}
