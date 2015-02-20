package de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractSimpleFactory;

/**
 * An implementation of <tt>AbstractSimpleFactory</tt> that returns
 * an <tt>IRasterizer</tt>.
 * 
 * @author marcus
 *
 */
public class RasterizerFactory extends AbstractSimpleFactory<IRasterizer> {

	/** the singleton instance */
	private final static RasterizerFactory INSTANCE = new RasterizerFactory();
	
	/**
	 * Returns the <tt>RasterizerFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>RasterizerFactory</tt> as an eager singleton
	 */
	public static RasterizerFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private RasterizerFactory() { }
	
	@Override
	protected IRasterizer create() {
		return new AdvancedBresenhamRasterizer();
	}

}
