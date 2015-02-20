package de.uniol.inf.is.odysseus.trajectory.compare.util;

import java.util.Locale;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.util.AbstractObjectLoaderFactory;

/**
 * Implementation of <tt>AbstractObjectLoaderFactory</tt> that holds different 
 * <tt>IQueryTrajectoryLoaders</tt> for different <i>file extensions</i>.
 * 
 * @author marcus
 *
 */
public class QueryTrajectoryLoaderFactory extends AbstractObjectLoaderFactory<IQueryTrajectoryLoader, RawQueryTrajectory, String, Integer>{

	/** the singleton instance */
	private final static QueryTrajectoryLoaderFactory INSTANCE = new QueryTrajectoryLoaderFactory();
	
	/**
	 * Returns the <tt>QueryTrajectoryLoaderFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>QueryTrajectoryLoaderFactory</tt> as an eager singleton
	 */
	public static QueryTrajectoryLoaderFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private QueryTrajectoryLoaderFactory() { }
	
	
	@Override
	protected String convertKey(String key) {
		return key.substring(key.lastIndexOf('.') + 1).toUpperCase(Locale.US);
	}

	@Override
	protected IQueryTrajectoryLoader createLoader(String convertedKey) {
		switch(convertedKey) {
			case "CSV" : return new CsvQueryTrajectoryLoader();
		}
		throw new IllegalArgumentException("No IQueryTrajectoryLoader found for file extension: " + convertedKey);
	}

}
