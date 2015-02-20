package de.uniol.inf.is.odysseus.trajectory.compare.util;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractFactory;

/**
 * An implementation of <tt>AbstractFactory</tt> that returns the same <tt>UtmPointCreator</tt>
 * for the same <i>UTM zone</i>.
 * 
 * @author marcus
 *
 */
public class UtmPointCreatorFactory extends AbstractFactory<UtmPointCreator, Integer>{

	/** the singleton instance */
	private static final UtmPointCreatorFactory INSTANCE = new UtmPointCreatorFactory();
	
	/**
	 * Returns the <tt>UtmPointCreatorFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>UtmPointCreatorFactory</tt> as an eager singleton
	 */
	public static UtmPointCreatorFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private UtmPointCreatorFactory() { }
	
	
	@Override
	protected Integer convertKey(Integer key) {
		return key;
	}


	@Override
	protected UtmPointCreator createProduct(Integer convertedKey) {
		return new UtmPointCreator(convertedKey);
	}

}
