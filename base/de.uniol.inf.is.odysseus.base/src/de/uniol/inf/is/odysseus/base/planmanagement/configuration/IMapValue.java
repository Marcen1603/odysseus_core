package de.uniol.inf.is.odysseus.base.planmanagement.configuration;

/**
 * Describes an entry of {@link AbstractTypeSafeMap}. Each entry has a special
 * value.
 * 
 * @author Wolf Bauer
 * 
 * @param <E>
 *            Type of the stored value.
 */
public interface IMapValue<E> {
	/**
	 * Returns the stored value of this entry.
	 * 
	 * @return The stored value of this entry.
	 */
	public E getValue();
}