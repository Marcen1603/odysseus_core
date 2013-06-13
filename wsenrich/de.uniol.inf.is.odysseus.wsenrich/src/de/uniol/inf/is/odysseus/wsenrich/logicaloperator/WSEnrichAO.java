package de.uniol.inf.is.odysseus.wsenrich.logicaloperator;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

//TODO  libs checken!!!
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="WSENRICH")
public class WSEnrichAO extends UnaryLogicalOp {

	/**
	 * Static Variable for the Name of the Post-Method
	 */
	private static final String POST_METHOD = "POST";

	/**
	 * Static Variable for the Name of the Get-Method
	 */
	private static final String GET_METHOD = "GET";
	
	/**
	 * Static Variable for the Service Method Rest
	 */
	private static final String SERVICE_METHOD_REST = "REST";
	
	/**
	 * Static Variable for the Service Mehtod Soap
	 */
	private static final String SERVICE_METHOD_SOAP = "SOAP";
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(WSEnrichAO.class);
	
	/**
	 * The service Method to call a Webservice: REST or SOAP
	 */
	private String serviceMethod;
	
	/**
	 * The method for the message-echangeing (GET or POST)
	 */
	private String method;

	/**
	 * The static HTTP-URL-Part before the arguments
	 */
	private String url;

	/**
	 * The static HTTP-URL-Part after the arguments
	 */
	private String urlsuffix;

	/**
	 * The arguments to send
	 */
	private List<Option> arguments;
	
	/**
	 * The name of the operation to call a Soap-Webservice
	 */
	private String operation;

	/**
	 * Default-Konstruktor for the WSEnrichAO
	 */
	public WSEnrichAO() {
		super();
	}

	/**
	 * Konstruktor for the WSEnrichAO
	 * 
	 * @param wsEnrichAO
	 */
	public WSEnrichAO(WSEnrichAO wsEnrichAO) {

		super(wsEnrichAO);
		this.serviceMethod = wsEnrichAO.serviceMethod;
		this.method = wsEnrichAO.method;
		this.url = wsEnrichAO.url;
		this.urlsuffix = wsEnrichAO.urlsuffix;
		this.arguments = wsEnrichAO.arguments;
		this.operation = wsEnrichAO.operation;

	}

	@Override
	public AbstractLogicalOperator clone() {

		return new WSEnrichAO(this);
	}

	@Override
	public boolean isValid() {

		boolean valid = true;

		if (!method.equals(GET_METHOD) || !method.equals(POST_METHOD)) {
			addError(new IllegalParameterException(
					"Method must be \"GET\" or \"POST\""));
			valid = false;
		}
		if (!serviceMethod.equals(SERVICE_METHOD_REST) || !serviceMethod.equals(SERVICE_METHOD_SOAP)) {
			addError(new IllegalParameterException(
					"The serviceMethod must be \"REST\" or \"SOAP\""));
			valid = false;
		}
		if (serviceMethod.equals(SERVICE_METHOD_REST) && (!operation.equals("") || operation == null)) {
			addError(new IllegalParameterException(
					"If you want to receive Data from a REST-Service you don�t need to define a operation!"));
			valid = false;
		}
		if (serviceMethod.equals(SERVICE_METHOD_SOAP) && (operation.equals("") || operation == null)) {
			addError(new IllegalParameterException(
					"If you want to receive Data from a SOAP-Servie you have to define a operation!"));
			valid = false;
		}

		return valid;

	}
	
	//TODO sp�ter noch �ndern, wenn XML-Verarbeitung funktioniert
	@Override
	public void initialize() {
		
		SDFAttribute attribute = new SDFAttribute("Webservice-Daten", "KeyValueParameter", SDFDatatype.STRING);
		SDFSchema webserviceData = new SDFSchema("", attribute);
		SDFSchema outputSchema = SDFSchema.union(getInputSchema(), webserviceData);
		setOutputSchema(outputSchema);
		
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int port) {
		return super.getOutputSchema();
	}
	
/*	private static String sdfSchemaToString(SDFSchema schema, String identifier) {
		StringBuilder sb = new StringBuilder(140);
		sb.append(identifier + ", Schema=[");
		boolean addComma = false;
		for (SDFAttribute attribute : schema) {
			if(addComma) 
				sb.append(";");
				sb.append("['" + attribute.getURI() + "','"
						+ attribute.getDatatype() + "']");
				addComma = true;
			}
			sb.append("]");
			return sb.toString();
		}

*/		

	/**
	 * @return The Service-Method. REST or SOAP
	 */
	public String getServiceMethod() {
		return this.serviceMethod;
	}
	
	/**
	 * Setter for the Service-Method. Value has to be REST or SOAP
	 * @param serviceMethod 
	 */
	@Parameter(type = StringParameter.class, name = "serviceMethod")
	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
	}
	
	/**
	 * @return The method to call the Webservice. GET or POST
	 */
	public String getMethod() {
		return this.method;
	}
	
	/**
	 * Setter for the Method to call the Webservice. It has to be REST or SOAP
	 * @param method
	 */
	@Parameter(type = StringParameter.class, name = "method")
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * @return The static part of the Url before Arguments
	 */
	public String getUrl() {
		return this.url;
	}
	
	/**
	 * Setter for the static part of the Url before Arguments
	 * @param url
	 */
	@Parameter(type = StringParameter.class, name = "url")
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return The static part of the Url after Arguments (optional)
	 */
	public String getUrlSuffix() {
		return this.urlsuffix;
	}
	
	/**
	 * Setter for the static part oo the Url after Arguments (optional)
	 * @param urlSuffix
	 */
	@Parameter(type = StringParameter.class, name ="urlSuffix", optional = true)
	public void setUrlSuffix(String urlSuffix) {
		this.urlsuffix = urlSuffix;
	}
	
	/**
	 * @return The arguments received from the Datastream
	 */
	public List<Option> getArguments() {
		return this.arguments;
	}
	
	/**
	 * Setter for the arguments received from the Datastream
	 * @param arguments
	 */
	@Parameter(type = OptionParameter.class, name = "arguments", isList = true)
	public void setArguments(List<Option> arguments) {
		this.arguments = arguments;
	}
	
	/**
	 * @return The name of the Operation used to call a SOAP-Webservice 
	 * (only needed for Soap-Webservices)
	 */
	public String getOpeation() {
		return this.operation;
	}
	
	/**
	 * Setter for the Operation used to call a SOAP-Webservice.
	 * (only needet for Soap-Webservices)
	 * @param operation
	 */
	@Parameter(type = StringParameter.class, name = "operation", optional = true)
	public void setOperation(String operation) {
		this.operation = operation;
	}
		

}
