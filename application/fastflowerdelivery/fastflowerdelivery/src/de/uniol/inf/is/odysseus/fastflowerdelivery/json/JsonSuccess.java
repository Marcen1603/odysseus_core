package de.uniol.inf.is.odysseus.fastflowerdelivery.json;

/**
 * Transports a success message to the client
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class JsonSuccess extends JsonData{

	private String success = "Success";

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
	
	/**
	 * Default constructor necessary for gson serialization
	 */
	public JsonSuccess() {}
	
	/**
	 * @param success
	 * 			the success message to deliver to the client
	 */
	public JsonSuccess(String success) {
		this.success = success;
	}
	
}
