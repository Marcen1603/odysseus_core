package windscadaanwendung.hd.ae;

/**
 * This class represents the data of a historical event which was caused because
 * of the measurements.
 * 
 * @author MarkMilster
 * 
 */
public class AEEntry {

	private boolean warning;
	private boolean error;
	private String timestamp = "";
	private int farmId;
	private int wkaId;
	private String valueType = "";
	private boolean confirm = false;
	private String comment = "";
	private int id;

	/**
	 * @return the timestamp when the event was caused
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the farmId in which this event was caused
	 */
	public int getFarmId() {
		return farmId;
	}

	/**
	 * @param farmId
	 *            the farmId to set
	 */
	public void setFarmId(int farmId) {
		this.farmId = farmId;
	}

	/**
	 * @return the wkaId in which this event was caused
	 */
	public int getWkaId() {
		return wkaId;
	}

	/**
	 * @param wkaId
	 *            the wkaId to set
	 */
	public void setWkaId(int wkaId) {
		this.wkaId = wkaId;
	}

	/**
	 * @return the valueType the valueTyp which caused this event (e.g.
	 *         windSpeed)
	 */
	public String getValueType() {
		return valueType;
	}

	/**
	 * @param valueType
	 *            the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * true if this was already confirmed
	 * 
	 * @return the confirm
	 */
	public boolean isConfirm() {
		return confirm;
	}

	/**
	 * @param confirm
	 *            the confirm to set
	 */
	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	/**
	 * A String which you can youse to comment this event. It can be stored in
	 * the database of historical values
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * True if this event is warning (low priority)
	 * 
	 * @return the warning
	 */
	public boolean isWarning() {
		return warning;
	}

	/**
	 * @param warning
	 *            the warning to set
	 */
	public void setWarning(boolean warning) {
		this.warning = warning;
	}

	/**
	 * True if this event is an error (high priority)
	 * 
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * An unique id of this event to identify it.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AEEntry " + String.valueOf(getId()) + " " + "Warning: "
				+ String.valueOf(isWarning()) + " Error: "
				+ String.valueOf(isError());
	}

}
