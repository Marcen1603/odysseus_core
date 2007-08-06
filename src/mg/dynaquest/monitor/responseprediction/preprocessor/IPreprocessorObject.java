package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a preprocessor object. 
 * 
 * Each preprocessor object calculates a characteristic of the events
 * of planoperators (e.g. the latency)
 * @author Jonas Jacobi
 *
 * @param <T> the type of the characteristic calculated by the preprocessor object
 */
public interface IPreprocessorObject<T> extends Serializable {

	/**
	 * Calculate characteristic of events and store the result
	 * in a {@link PreprocessedMetadata} object
	 * @param events the events
	 * @param data store the calculated results in this object
	 */
	public abstract void processData(List<POEventData> events,
			PreprocessedMetadata data);

}
