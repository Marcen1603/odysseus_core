package mg.dynaquest.monitor.responseprediction.preprocessor;

/**
 * Interface for online learnalgorithms.
 *  
 * Everytime a request was processed by DynaQuest the method
 * {@link #processedRequest(String, PreprocessedMetadata)} gets called.
 * You have to register the listener to an {@link #IMetadataPreprocessor} using
 * {@link IMetadataPreprocessor#registerOnlineListener(IOnlineListener)}.
 * @author Jonas Jacobi
 */
public interface IOnlineListener {
	/**
	 * Gets called everytime DynaQuest processed a request.
	 * If a request used multiple sources/access operators, this method gets
	 * called multiple times.
	 *  
	 * @param source The source the request used
	 * @param request the preprocessed metadata for the accessoperator used
	 * in the request
	 */
	void processedRequest(String source, PreprocessedMetadata request);
}
