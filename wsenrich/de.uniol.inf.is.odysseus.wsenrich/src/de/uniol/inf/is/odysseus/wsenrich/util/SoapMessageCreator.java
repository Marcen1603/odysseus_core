package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.MessagePart;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.support.SoapUIException;

import de.uniol.inf.is.odysseus.wsenrich.exceptions.OperationNotFoundException;

public class SoapMessageCreator {
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(SoapMessageCreator.class);
	
	/**
	 * The url to the wsdl file
	 */
	private String wsdlUrl;
	
	/**
	 * The operation to call
	 */
	private String operationName;
	
	/**
	 * internal representation of a wsdl-definition
	 */
	private WsdlProject project;
	
	/**
	 * wsdl implementation
	 */
	private WsdlInterface wsdl;
	
	
	/**
	 * Constructor for the SoapMessageCreator
	 * The wsdl file will be automatically read by the constructor
	 * @param wsdlUrl the url to the wsdl-file
	 * @param operationName the name of the operation to call
	 */
	public SoapMessageCreator(String wsdlUrl) {
		
		this.wsdlUrl = wsdlUrl;
		readWsdl();
		
	}
	
	/**
	 * Try to parse the wsdl-File
	 */
	private void readWsdl() {
		
		try {
			
			this.project = new WsdlProject();
			WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, wsdlUrl);
			this.wsdl = wsdls[0];
			
		} catch (XmlException e) {
			
			logger.error("Exception by parsing Wsdl-File. Cause: {}", e.getMessage());
			
		} catch (IOException e) {
			
			logger.error("Exception by parsing Wsdl-File. Maybe you defined a wrong Url to the Wsdl-File. Cause: {}", e.getMessage());
			
		} catch (SoapUIException e) {
			
			logger.error("Internal Exception while parsing Wsdl-File. Cause: {}", e.getMessage());
			
		} catch (Exception e) {
			
			logger.error("Generally Exception while parsing Wsdl-File. Cause: {}", e.getMessage());
		}
		
		
	}
	
	/**
	 * Setter for the operation 
	 * @param operation the name of the operation
	 */
	private void setOperation(String operation) {
		
		this.operationName = operation;
	}
	
	/**
	 * @return the name of the operation
	 */
	private String getOperation() {
		
		return this.operationName;
	}
	
	/**
	 * Builds the Soap-Message for the defined operation
	 * @param operationName the name of the operation where the soap-message
	 * has to be created
	 */
	public String getSOAPMessage(String operationName) {
		
		if(this.operationName == null || this.operationName.equals("")) {
			
			setOperation(operationName);	
		}
		
		try {
			
			if(this.operationExists()) {
				
				return wsdl.getOperationByName(operationName).createRequest(true);	
			}
			
		} catch (OperationNotFoundException e) {
			
			logger.error("Exception while building the Soap-Message. Maybe you defined the wrong operation name. Cause: {}", e.getMessage());
			
		}
		return "";
			
		
	}
	
	/**
	 * Returns the required datafields for the operation
	 * @param operationName the name of the operation
	 * @return the required datafields of the operation
	 */
	public List<String> getMessageParts(String operationName)  {
		
		List<String> datafields = new ArrayList<String>();
		
		if(this.operationName == null || this.operationName.equals("")) {
			
			setOperation(operationName);
		}
		
		WsdlOperation operation = (WsdlOperation) this.wsdl.getOperationByName(this.getOperation());
		
		try {
			
			if(this.operationExists()) {
				
				MessagePart[] mp = operation.getDefaultRequestParts();
				for(int i = 0; i < mp.length; i++) {
					
					datafields.add(mp[i].getName());
				}
				
				return datafields;
			}
		} catch (OperationNotFoundException e) {
			
			logger.error("Exception while building the Soap-Message. Maybe you defined the wrong operation name. Cause: {}", e.getMessage());
		}
		
		return null;
			
	}
	
	/**
	 * searches the wsdl-file for the given operation name
	 * @return true, only if the operation exists
	 * @throws OperationNotFoundException is thrown if the operation does not exists
	 */
	private boolean operationExists() throws OperationNotFoundException {
		
		boolean found = false;
		
		Operation[] operations =  wsdl.getAllOperations();
		
		for(int i = 0; i < operations.length; i++) {
			
			if(operations[i].getName().equals(getOperation())) {
				found = true;
				break;		
			}
		}
		
		if(!found) {
			
			throw new OperationNotFoundException();
		}
		return found;
	}
	
	/**
	 * @return the amount of all operations found in the wsdl-file
	 */
	public Operation[] getOperationsOfWsdl() {
		
		return wsdl.getAllOperations();
		
	}
	
	/**
	 * @return the amount of all messages found in the wsdl-file
	 */
	public String[] getEndpointAdress() {
		
		return wsdl.getEndpoints();
	}

}

