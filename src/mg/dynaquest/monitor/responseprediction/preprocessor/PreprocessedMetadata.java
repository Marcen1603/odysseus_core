package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to store the characteristics in the recorded events of a planoperator
 * like latency or datarate. You need the preprocessor object
 * that calculated a characteristic to access its results with
 * {@link #getResult(IPreprocessorObject)}.
 * @author Jonas Jacobi
 */
public class PreprocessedMetadata implements Serializable {
	private static final long serialVersionUID = 1784010376447024826L;

	private String planOperator;

	private Map<IPreprocessorObject<?>, Object> result;

	private long startTime;

	private long endTime;

	/**
	 * Constructor
	 * @param poGuid unique identifier of the planoperator the characteristics are calculated for
	 */
	public PreprocessedMetadata(String poGuid) {
		this.result = new HashMap<IPreprocessorObject<?>, Object>();
		this.planOperator = poGuid;
	}

	/**
	 * Get a specific characteristic
	 * @param <T> the type of the characteristic
	 * @param o the preprocessor object which calculated the characteristic
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getResult(IPreprocessorObject<T> o) {
		return (T) (this.result.get(o));
	}

	/**
	 * Store the a characteristic. This method is used by the preprocessor objects
	 * to store their results
	 * @param <T> the type of stored characteristic
	 * @param processor the preprocessor object that calculated the characteristic
	 * @param data the value of the calculated characteristic
	 */
	public <T> void putResult(IPreprocessorObject<T> processor, T data) {
		this.result.put(processor, data);
	}

	/**
	 * Get the unique identifier of the planoperator
	 * @return
	 */
	public String getPlanOperatorGuid() {
		return this.planOperator;
	}

	/**
	 * Get the time the open_init event occured
	 * @return the time the open_init event occured in milliseconds
	 * since 1970
	 */
	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * Get the time the last event occured (usually close_done)
	 * @return the time the last event occured in milliseconds
	 * since 1970
	 */
	public long getEndTime() {
		return this.endTime;
	}

	/**
	 * Set the time the first event occured (open_init)
	 * @param time the time the first event occured in milliseconds
	 * since 1970
	 */
	public void setStartTime(long time) {
		this.startTime = time;
	}

	/**
	 * Set the time the last event occured (usually close_done)
	 * @param time the time the last event occured in milliseconds since 1970
	 */
	public void setEndTime(long time) {
		this.endTime = time;
	}
}
