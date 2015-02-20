package de.uniol.inf.is.odysseus.trajectory.compare.util;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractSimpleFactory;

/**
 * An implementation of <tt>AbstractSimpleFactory</tt> that returns a
 * <tt>DefaultTupleToRawTrajectoryConverter</tt>.
 * 
 * @author marcus
 *
 */
public class TupleToRawTrajectoryConverterFactory extends AbstractSimpleFactory<ITupleToRawTrajectoryConverter> {

	/** the singleton instance */
	private final static TupleToRawTrajectoryConverterFactory INSTANCE = new TupleToRawTrajectoryConverterFactory();
	
	/**
	 * Returns the <tt>TupleToRawTrajectoryConverterFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>TupleToRawTrajectoryConverterFactory</tt> as an eager singleton
	 */
	public static TupleToRawTrajectoryConverterFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private TupleToRawTrajectoryConverterFactory() { }

	@Override
	protected ITupleToRawTrajectoryConverter create() {
		return new DefaultTupleToRawTrajectoryConverter();
	}

}
