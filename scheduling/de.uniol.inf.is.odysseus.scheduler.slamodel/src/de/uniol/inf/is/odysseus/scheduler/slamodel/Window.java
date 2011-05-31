package de.uniol.inf.is.odysseus.scheduler.slamodel;

import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.TimeUnit;

/**
 * Instances of this class represent evaluation windows for sla
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Window {
	/**
	 * the length of a window
	 */
	private int length;
	/**
	 * the time unit of {@link length}
	 */
	private TimeUnit unit;

	/**
	 * creates a new evaluation window
	 * 
	 * @param length
	 *            the length of the window
	 * @param unit
	 *            the unit of length
	 */
	public Window(int length, TimeUnit unit) {
		super();
		this.setLength(length);
		this.setUnit(unit);
	}

	/**
	 * 
	 * @return the length of teh window
	 */
	public int getLength() {
		return length;
	}

	/**
	 * sets the length of the window
	 * 
	 * @param length
	 *            the new length. Must be greater 0.
	 * @throws IllegalArgumentException
	 *             iff length <= 0
	 */
	public void setLength(int length) {
		if (length <= 0)
			throw new IllegalArgumentException("illegal window size (" + length
					+ "). window size must be greater 0.");
		this.length = length;
	}

	/**
	 * 
	 * @return the time unit of the window's length
	 */
	public TimeUnit getUnit() {
		return unit;
	}

	/**
	 * sets the time unit of the windows length.
	 * 
	 * @param unit
	 *            the time unit of the windows
	 */
	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	
	
}
