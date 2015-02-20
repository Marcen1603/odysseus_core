package de.uniol.inf.is.odysseus.trajectory.util;

/**
 * An interface for loading objects of type <tt>E</tt> from parameter <tt>P</tt>
 * and additional information <tt>A</tt>.
 * 
 * @author marcus
 *
 * @param <E> the type of the object that will be loaded
 * @param <P> the type of parameter that is required to load an object of type <tt>E</tt>
 * @param <A> the type of the additional information to load an object of type <tt>E</tt>
 */
public interface IObjectLoader<E, P, A> {

	/**
	 * Loads and returns an object of type <tt>E</tt> from the the passed <i>parameter</i> and the
	 * <i>additional info</i>.
	 * 
	 * @param param the <i>parameter</i> that is required to load an object of type <tt>E</tt>
	 * @param additional the </i>additional</i> information to load an object of type <tt>E</tt>
	 * @return an object of type <tt>E</tt> loaded from the passed parameter and additional
	 *         info
	 */
	public E load(P param, A additional);
}
