package de.uniol.inf.is.odysseus.trajectory.util;

/**
 * An abstract simple factory for returning a <i>product</i>.
 * 
 * @author marcus
 *
 * @param <P> the type of the <i>product</i>
 */
public abstract class AbstractSimpleFactory<P> {
	
	/** the <i>product</i> to be returned */
	private final P product;
	
	/**
	 * Beware class from being instantiated.
	 */
	protected AbstractSimpleFactory() {
		this.product = this.create();
	}
	
	/**
	 * Returns the <i>product</i> of this <tt>AbstractSimpleFactory</tt>.
	 * 
	 * @return the <i>product</i> of this <tt>AbstractSimpleFactory</tt>
	 */
	public P getProduct() {
		return this.product;
	}

	/**
	 * Creates and returns the <i>product</i> of this <tt>AbstractSimpleFactory</tt>.
	 * 
	 * @return the newly created <i>product</i> of this <tt>AbstractSimpleFactory</tt>
	 */
	protected abstract P create();
}
