package de.uniol.inf.is.odysseus.fastflowerdelivery.json;

/**
 * Transports error messages to the client
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class JsonError extends JsonData {

	private String error = "";
	
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	
	/**
	 * Default constructor necessary for gson serialization
	 */
	public JsonError() {}
	
	/**
	 * @param error
	 * 			the error message to deliver to the client
	 */
	public JsonError(String error) {
		this.setError(error);
	}
	
}
