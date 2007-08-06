package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface for metadata preprocessors.
 *
 * A metadata preprocessor provides access to executed plans and the collected metadata for each
 * execution.
 * Learners can be registered and will be notified when DynaQuest processed a request.
 * 
 * @author Jonas Jacobi
 */
public interface IMetadataPreprocessor extends mg.dynaquest.monitor.POMonitorEventListener, Serializable {
	
	/**
	 * Registers an offline learner.
	 * Everytime DynaQuest processed updateInterval requests the preprocessor
	 * has to call {@link IOfflineListener#requestCountReached()} on listener.
	 * @param listener the offline learner to register
	 * @param updateInterval the number of requests DynaQuest has to process, before
	 * {@link IOfflineListener#requestCountReached() requestCountReached()} gets called.
	 */
	public void registerOfflineListener(IOfflineListener listener, int updateInterval);
	
	/**
	 * Registers an online learner for notification when DynaQuest processed a request.
	 * Everytime DynaQuest processed a request the
	 * metadata preprocessor has to call 
	 * {@link IOnlineListener#processedRequest(String, PreprocessedMetadata)}
	 * for each registered {@link IOnlineListener}
	 * 
	 * @param listener the online learner to register
	 */
	public void registerOnlineListener(IOnlineListener listener);
	
	/**
	 * Get all plans executed by DynaQuest
	 * @return all executed plans
	 */
	public List<PlanOperator> getPlans();
	
	/**
	 * Get all plans executed by DynaQuest after a specific time
	 * @param afterTimestamp a timestamp with miliseconds since 1.1.1970.
	 * All returned plans have to be newer than this. 
	 * @return all plans executed after afterTimestamp
	 */
	public List<PlanOperator> getPlans(long afterTimestamp);
	
	/**
	 * Get the preprocessed metadata for a planoperator.
	 * If you want metadata for multiple planoperators use {@link #getMetadata(Collection)}
	 * instead.
	 * @param poGuid the unique id of the planoperator
	 * @return the preprocessed metadata
	 * @throws Exception gets thrown if metadata could not be retrieved or preprocessed
	 */
	public PreprocessedMetadata getMetadata(String poGuid) throws Exception;
	
	/**
	 * Get the preprocessed metadata for multiple planoperators.
	 * Use this instead of {@link #getMetadata(String)} if you want
	 * the metadata for multiple planoperators, as the implementation
	 * of this method might allow optimizations.
	 * @param pos the planoperators
	 * @return a map containing the preprocessed metadata for each planoperator
	 * in pos
	 * @throws Exception gets thrown if metadata could not be retrieved or preprocessed
	 */
	public Map<PlanOperator, PreprocessedMetadata> getMetadata(Collection<PlanOperator> pos) throws Exception;
}
