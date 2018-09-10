package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import java.io.IOException;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.support.SoapUIException;

import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IMessageCreator;

public class SoapUIImpl implements IMessageCreator {
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(SoapUIImpl.class);
	
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
	 * The SoapMessage
	 */
	private String soapMessage;
	
	/**
	 * Default Constructor
	 */
	public SoapUIImpl() {
		//Needed for the SoapMessageRegistry
	}
	
	/**
	 * Constructor for the SoapMessageCreator
	 * The wsdl file will be automatically read by the constructor
	 * @param wsdlUrl the url to the wsdl-file
	 * @param operationName the name of the operation to call
	 */
	private SoapUIImpl(String wsdlLocation, String operation) {
		this.wsdlUrl = wsdlLocation;
		this.operationName = operation;
	}
	
	@Override
	public void buildMessage() {
		try {	
			this.project = new WsdlProject();
			WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, wsdlUrl);
			this.wsdl = wsdls[0];
			this.soapMessage = createMessage();
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
	 * Builds the Soap-Message for the defined operation
	 * @param operationName the name of the operation to create the soap message
	 */
	private String createMessage() {
			if(operationExists()) {	
				return this.wsdl.getOperationByName(this.operationName).createRequest(true);	
			}
			else {
				return "";
			}
	}
	
	/**
	 * searches the wsdl-file for the given operation name
	 * @return true, only if the operation exists
	 * @throws OperationNotFoundException is thrown if the operation does not exists
	 */
	private  boolean operationExists() {
		boolean found = false;
		Operation[] operations =  this.wsdl.getAllOperations();
		for(int i = 0; i < operations.length; i++) {	
			if(operations[i].getName().equals(this.operationName)) {
				found = true;
				break;		
			}
		}
		if(!found) {	
			logger.error("The specified operation was not found in the wsdl file");
		}
		return found;
	}
	

	@Override
	public String getOperationName() {
		return operationName;
	}

	@Override
	public String getMessage() {
		return soapMessage;
	}

	@Override
	public IMessageCreator createInstance(String wsdlLocation, String operation) {
		return new SoapUIImpl(wsdlLocation, operation);
	}

	@Override
	public String getName() {
		return "SOAP";
	}
	
}

