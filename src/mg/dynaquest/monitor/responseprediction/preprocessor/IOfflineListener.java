package mg.dynaquest.monitor.responseprediction.preprocessor;

/**
 * Interface for offline learnalgorithms.
 * 
 * You have to register the listener to an IPreprocessor with
 * the method {@link IMetadataPreprocessor#registerOfflineListener(IOfflineListener listener, int updateInterval)}.
 * Everytime the updateInterval is reached (which means updateInterval requests have been processed by Dynaquest)
 * {@link #requestCountReached()} gets called.
 * @author Jonas Jacobi
 */
public interface IOfflineListener {
	/**
	 * Gets called everytime DynaQuest processed updateInterval requests,
	 * with updateInterval being specified on listener registration. 
	 */
	public void requestCountReached();
}
