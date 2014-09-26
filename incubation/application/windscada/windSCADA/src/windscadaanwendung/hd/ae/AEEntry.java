package windscadaanwendung.hd.ae;

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
	
	public AEEntry() {
	}
	
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the farmId
	 */
	public int getFarmId() {
		return farmId;
	}
	/**
	 * @param farmId the farmId to set
	 */
	public void setFarmId(int farmId) {
		this.farmId = farmId;
	}
	/**
	 * @return the wkaId
	 */
	public int getWkaId() {
		return wkaId;
	}
	/**
	 * @param wkaId the wkaId to set
	 */
	public void setWkaId(int wkaId) {
		this.wkaId = wkaId;
	}
	/**
	 * @return the valueType
	 */
	public String getValueType() {
		return valueType;
	}
	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	/**
	 * @return the confirm
	 */
	public boolean isConfirm() {
		return confirm;
	}
	/**
	 * @param confirm the confirm to set
	 */
	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the warning
	 */
	public boolean isWarning() {
		return warning;
	}
	/**
	 * @param warning the warning to set
	 */
	public void setWarning(boolean warning) {
		this.warning = warning;
	}
	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "AEEntry " + String.valueOf(getId()) + " " + "Warning: " + String.valueOf(isWarning()) + " Error: " + String.valueOf(isError());
	}

}
