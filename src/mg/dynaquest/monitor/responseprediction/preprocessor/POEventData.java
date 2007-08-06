package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.io.Serializable;

import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.queryexecution.event.POEventType;

/**
 * A stripped down representation of a {@link POMonitorEvent}.
 * @author Jonas Jacobi
 */
public class POEventData implements Serializable {
	private static final long serialVersionUID = -657680130836923621L;

	private long time;

	private POEventType type;

	private String poGuid;

	public POEventData() {
	}
	
	/**
	 * Constructor
	 * @param time time in milliseconds since 1970 when the event occured
	 * @param type type of the event
	 * @param poGuid unique identifier of the planoperator for which
	 * the event was fired.
	 */
	public POEventData(long time, POEventType type, String poGuid) {
		super();
		this.time = time;
		this.type = type;
		this.poGuid = poGuid;
	}

	/**
	 * Get the unique identifier of the planoperator for which the event was fired
	 * @return unique identifier of the planoperator
	 */
	public String getPoGuid() {
		return poGuid;
	}

	/**
	 * Set the unique identifier of the planoperator for which the event was fired
	 * @param poGuid uniqe identifier of the planoperator
	 */
	public void setPoGuid(String poGuid) {
		this.poGuid = poGuid;
	}

	/**
	 * Get the time when the event occured
	 * @return the time in milliseconds since 1970 
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Set the time when the event occured
	 * @param time the time in milliseconds since 1970
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Get the event type
	 * @return the event type
	 */
	public POEventType getType() {
		return type;
	}

	/**
	 * set the event type
	 * @param type the event type
	 */
	public void setType(POEventType type) {
		this.type = type;
	}
}
