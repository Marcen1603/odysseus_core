package de.uniol.inf.is.odysseus.fastflowerdelivery.service;

import javax.servlet.http.HttpServletRequest;

/**
 * This object provides several methods to easily 
 * retrieve parameter data from an http request.
 * While retrieving the parameters, the data will
 * be validated.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class ServiceRequest {

	/**
	 * To retrieve the parameter data, the actual http request is necessary
	 */
	private HttpServletRequest httpRequest;
	
	/**
	 * Creates the object using an existing http request
	 * @param req
	 * 			the existing http request
	 */
	public ServiceRequest(HttpServletRequest req) {
		this.httpRequest = req;
	}

	/**
	 * Retrieve a parameter as a string.
	 * Checks the parameter to exist at all
	 * and not to be empty
	 * @param param
	 * 			the identifier of the parameter
	 * @return the String contained in the parameter
	 */
	public String getStringParameter(String param) {
		String result = httpRequest.getParameter(param);
		if(result == null || result.isEmpty())
			throw new WebServiceException(param + " is empty.");
		return result;
	}
	
	/**
	 * Retrieve a parameter as an integer.
	 * Checks the parameter to exist at all
	 * and not to be empty.
	 * Also checks if this parameter can be parsed
	 * as a valid integer.
	 * @param param
	 * 			the identifier of the parameter
	 * @return the String contained in the parameter
	 */
	public int getIntegerParameter(String param) {
		int result = 0;
		String strParam = getStringParameter(param);
		try {
			result = Integer.parseInt(strParam);
		} catch(Exception ex) {
			throw new WebServiceException(param + " must be a number.");
		}
		return result;
	}
	
	/**
	 * Retrieve a parameter as a string.
	 * Checks the parameter to exist at all
	 * and not to be empty.
	 * Also checks if this parameter can be parsed
	 * as a valid long.
	 * @param param
	 * 			the identifier of the parameter
	 * @return the String contained in the parameter
	 */
	public long getLongParameter(String param) {
		long result = 0;
		String strParam = getStringParameter(param);
		try {
			result = Long.parseLong(strParam);
		} catch(Exception ex) {
			throw new WebServiceException(param + " must be a number.");
		}
		return result;
	}
	
}
