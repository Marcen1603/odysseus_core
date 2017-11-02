package de.uniol.inf.is.odysseus.wsenrich.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractEnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry.KeyFinderRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry.RequestBuilderRegistry;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "WSENRICH", doc = "Enrich tuples with data from external web services.", category = {
		LogicalOperatorCategory.ENRICH })
public class WSEnrichAO extends AbstractEnrichAO {

	/**
	 *
	 */
	private static final long serialVersionUID = -9004473427566266528L;

	/**
	 * Static Variable for the Name of the Post-Argument-Method
	 */
	public static final String POST_WITH_ARGUMENTS = "POST_ARGUMENTS";

	/**
	 * Static Variable for the Name of the Post-Document-Method
	 */
	public static final String POST_WITH_DOCUMENT = "POST_DOCUMENT";

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
	public static final String SERVICE_METHOD_REST = "REST";

	/**
	 * Static Variable for the Service Method Soap
	 */
	public static final String SERVICE_METHOD_SOAP = "SOAP";

	/**
	 * Static Variable for the parsing Method xpath
	 */
	private static final String PARSING_XML_XPATH = "XPATH";

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
	 * Allow to set the content type
	 */
	private String contentType;

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
	 * Header information
	 */
	private List<Option> header;

	/**
	 * The name of the operation to call a Soap-Webservice
	 */
	private String operation;

	/**
	 * The data fields of the webservice
	 */
	private List<SDFAttribute> receivedData;

	/**
	 * The charset that is used for the webservice conversation
	 */
	private String charset = "UTF-8";

	/**
	 * The return Type of a webservice-response, can be XML or JSON
	 */
	private String parsingMethod;

	/**
	 * internal variable for the final resolution, which Method to call the
	 * webservice is used
	 */
	private String getOrPost;

	/**
	 * If true, received Data from a Webservice are returned as keyValuePairs
	 */
	private boolean keyValueOutput = false;

	/**
	 * the location of the wsdl-file
	 */
	private String wsdlLocation;

	private String template;

	private boolean urlIsTemplate = false;

	private boolean allowNullValues = true;

	/**
	 * Default-Constructor for the WSEnrichAO
	 */
	public WSEnrichAO() {
		super();
	}

	/**
	 * Constructor for the WSEnrichAO
	 *
	 * @param wsEnrichAO
	 */
	public WSEnrichAO(WSEnrichAO wsEnrichAO) {
		super(wsEnrichAO);
		this.serviceMethod = wsEnrichAO.serviceMethod;
		this.method = wsEnrichAO.method;
		this.contentType = wsEnrichAO.contentType;
		this.url = wsEnrichAO.url;
		this.urlIsTemplate = wsEnrichAO.urlIsTemplate;
		this.urlsuffix = wsEnrichAO.urlsuffix;
		this.arguments = wsEnrichAO.arguments;
		this.template = wsEnrichAO.template;
		this.header = wsEnrichAO.header;
		this.operation = wsEnrichAO.operation;
		this.receivedData = wsEnrichAO.receivedData;
		this.charset = wsEnrichAO.charset;
		this.parsingMethod = wsEnrichAO.parsingMethod;
		this.getOrPost = wsEnrichAO.setGetOrPost();
		this.keyValueOutput = wsEnrichAO.keyValueOutput;
		this.wsdlLocation = wsEnrichAO.wsdlLocation;
		this.allowNullValues = wsEnrichAO.allowNullValues;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new WSEnrichAO(this);
	}

	@Override
	public void initialize() {
		SDFSchema webserviceData = SDFSchemaFactory.createNewWithAttributes(fixOutputNames(receivedData), getInputSchema());
		SDFSchema outputSchema = SDFSchema.union(getInputSchema(), webserviceData);
		setOutputSchema(outputSchema);
	}

	private Collection<SDFAttribute> fixOutputNames(List<SDFAttribute> receivedData2) {
		List<SDFAttribute> ret = new ArrayList<SDFAttribute>();
		for (SDFAttribute a: receivedData2){
			String oldAttributeName = a.getAttributeName();
			oldAttributeName = SDFAttribute.replaceSpecialChars(oldAttributeName);
			ret.add(new SDFAttribute(a.getSourceName(), oldAttributeName, a));
		}
		return ret;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int port) {
		if (recalcOutputSchemata || getOutputSchema() == null) {
			initialize();
		}
		return getOutputSchema();
	}

	/**
	 * @return The Service-Method. REST or SOAP
	 */
	public String getServiceMethod() {
		return this.serviceMethod;
	}

	/**
	 * Setter for the Service-Method. Value has to be REST or SOAP
	 *
	 * @param serviceMethod
	 */
	@Parameter(type = StringParameter.class, name = "serviceMethod", doc="Method by which the web service should be accessed: Possible Values 'REST' and 'SOAP'")
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
	 *
	 * @param method
	 */
	@Parameter(type = StringParameter.class, name = "method", doc = "How to access the web service. For REST this is typically GET. Other methods are POST_ARGUMENTS, POST_DOCUMENT")
	public void setMethod(String method) {
		this.method = method;
	}

	@Parameter(type = StringParameter.class, name = "contentType", optional = true, doc  = "The http request header content type.")
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	/**
	 * @return The static part of the Url before Arguments
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Setter for the static part of the Url before Arguments
	 *
	 * @param url
	 */
	@Parameter(type = StringParameter.class, name = "url", doc="static part of the url, will be used in request. Remark: Must contain ? to seperate arguments")
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return The static part of the Url after Arguments (optional)
	 */
	public String getUrlSuffix() {
		return this.urlsuffix;
	}


	public boolean isUrlIsTemplate() {
		return urlIsTemplate;
	}

	@Parameter(type = BooleanParameter.class, name="templateURL", optional = true, doc = "If the url is a template, set to true.")
	public void setUrlIsTemplate(boolean urlIsTemplate) {
		this.urlIsTemplate = urlIsTemplate;
	}

	@Parameter(type = BooleanParameter.class, optional = true, doc="In some case, e.g. when receiving complex documents, single attributes could be empty but the result should not be empty. Set to false if this is not desired")
	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	public boolean isAllowNullValues() {
		return allowNullValues;
	}

	@Parameter(type = StringParameter.class, optional = true, doc="A template that should be used in a post request as content. Elements are replaced from arguments.")
	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	/**
	 * Setter for the static part of the Url after Arguments (optional)
	 *
	 * @param urlSuffix
	 */
	@Parameter(type = StringParameter.class, name = "urlSuffix", optional = true, doc="url part after argument, will be appended at the end of the url (e.g for further static parts)")
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
	 *
	 * @param arguments
	 */
	@Parameter(type = OptionParameter.class, name = "arguments", isList = true, optional = true, doc ="This is a key value map, first element is the name of the attribute in web service, the second the name of the attribute from the input stream that delivers the input element")
	public void setArguments(List<Option> arguments) {
		this.arguments = arguments;
	}

	/**
	 * Setter for the headers
	 *
	 * @param arguments
	 */
	@Parameter(type = OptionParameter.class, name = "header", isList = true, optional = true, doc ="Additional headers for the http request.")
	public void setHeader(List<Option> header) {
		this.header = header;
	}

	/**
	 * @return The headers
	 */
	public List<Option> getHeader() {
		return this.header;
	}

	/**
	 * @return The name of the Operation used to call a SOAP-Webservice (only
	 *         needed for Soap-Webservices)
	 */
	public String getOperation() {
		return this.operation;
	}

	/**
	 * Setter for the Operation used to call a SOAP-Webservice. (only needed for
	 * Soap-Webservices)
	 *
	 * @param operation
	 */
	@Parameter(type = StringParameter.class, name = "operation", optional = true, doc ="operation used to call a SOAP-Webservice")
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return The Datafields received through the webservice
	 */
	public List<SDFAttribute> getReceivedData() {
		return receivedData;
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "datafields", isList = true, doc ="the datafields received through the webservice, given as path expressions")
	public void setReceivedData(List<SDFAttribute> receivedData) {
		this.receivedData = receivedData;
	}

	/**
	 * @return The used charset for the webservice conversation
	 */
	public String getCharset() {
		return this.charset;
	}

	/**
	 * Setter for the charset
	 *
	 * @param charset
	 *            The charset. Standard ist UTF-8
	 */
	@Parameter(type = StringParameter.class, name = "charset", optional = true, doc = "the charset, e.g. UTF-8")
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return The return-type of the webservice-response, can be JSON or XML
	 */
	public String getParsingMethod() {
		return this.parsingMethod;
	}

	/**
	 * Setter for the return type of the webservice-response. Can be XML or JSON
	 *
	 * @param parsingMethod
	 *            the return type
	 */
	@Parameter(type = StringParameter.class, name = "parsingMethod", doc ="How to extract the values from the returned document: Currently available: 'JSONEXPERIMENTAL','JSONPATH','XMLEXPERIMENTAL','XPATH'")
	public void setParsingMethod(String parsingMethod) {
		this.parsingMethod = parsingMethod;
	}

	/**
	 * Setter for the Get or Post-variable
	 */
	private String setGetOrPost() {
		if (this.method.equals(POST_WITH_ARGUMENTS) || this.method.equals(POST_WITH_DOCUMENT)) {
			return POST_METHOD;
		} else
			return GET_METHOD;
	}

	/**
	 * @return The Method for the Http Connection
	 */
	public String getGetOrPost() {
		return this.getOrPost;
	}

	/**
	 * Setter to enable or disable key value output in the output stream
	 *
	 * @param keyValueOutput
	 */
	@Parameter(type = BooleanParameter.class, optional = true, name = "keyValueOutput", doc = "If true, received Data from a Webservice are returned as keyValuePair.")
	public void setKeyValueOutput(boolean keyValueOutput) {
		this.keyValueOutput = keyValueOutput;
	}

	/**
	 * @return KeyValueOutput is enabled or not
	 */
	public boolean getKeyValueOutput() {
		return this.keyValueOutput;
	}

	/**
	 * @return the path to the wsdl-file
	 */
	public String getWsdlLocation() {
		return this.wsdlLocation;
	}

	/**
	 * Setter for the url to the location of the wsdl file
	 *
	 * @param wsdlLocation
	 */
	@Parameter(type = StringParameter.class, optional = true, name = "wsdlLocation", doc ="url to the location of the wsdl file, i.e. what is the address of the web service for soap")
	public void setWsdlLocaton(String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}

	@Override
	public boolean isValid() {
		boolean valid = super.isValid();

		/*
		 * Check for correct Handler names
		 */

		// Walk through the registered Handlers of IRequestBuilder and check if
		// theres a matching handler
		ImmutableList<String> requestHandlers = RequestBuilderRegistry
				.getHandlerNames();
		if (!requestHandlers.contains(method.toLowerCase())) {
			addError(
					"Method must be \"GET\" or \"POST_ARGUMENTS\" or \"POST_DOCUMENT\"");
			valid = false;
		}
		// Walk through the registered Handlers of IRequestBuilder and check if
		// theres a matching handler
		ImmutableList<String> parserHandlers = KeyFinderRegistry
				.getHandlerNames();
		if (!parserHandlers.contains(parsingMethod.toLowerCase())) {
			addError(
					"You have to declare the Parsing Method to parse the webservice-response. This can be XMLEXPERIMENTAL  "
							+ "or XPATH for XML-Documents or JSONEXPERIMENTAL or JSONPATH for JSON Documents.");
			valid = false;
		}

		// Can´t be checked with the List of Handlers of
		// SoapMessageCeatorRegistry because "REST" is not registered there
		if (!(serviceMethod.equals(SERVICE_METHOD_REST) || serviceMethod
				.equals(SERVICE_METHOD_SOAP))) {
			addError(
					"The serviceMethod must be \"REST\" or \"SOAP\"");
			valid = false;
		}

		/*
		 * Check dependencies beetween the variables
		 */

		if ((operation != null && serviceMethod.equals(SERVICE_METHOD_REST))) {
			addError(
					"If you want to receive Data from a REST-Service you don´t have to define a operation!");
			valid = false;
		}
		if ((operation == null && serviceMethod.equals(SERVICE_METHOD_SOAP))) {
			addError(
					"If you want to receive Data from a SOAP-Servie you have to define a operation!");
			valid = false;
		}
		if ((wsdlLocation == null || wsdlLocation.equals(""))
				&& serviceMethod.equals(SERVICE_METHOD_SOAP)) {
			addError(
					"If you want to receive Data from a SOAP-Service you have to define the location to the wsdl file of this webservice");
			valid = false;
		}
		if (getMultiTupleOutput() && !parsingMethod.equals(PARSING_XML_XPATH)) {
			addError(
					"MultiTupleOutput currently only works with the XPATH-Parser. You have to declare the parameter 'paringMethod' with 'XPATH'");
			valid = false;
		}
		if (arguments == null) {
			addError(
					"Missing Parameter 'arguments'. You have to declare min 1 Datafield you wand to submit to the webservice.");
			valid = false;
		}
		if (receivedData == null) {
			addError(
					"Missing Parameter 'datafields'. You have to declare min 1 Datafield of the webservice for the Outputschema.");
			valid = false;
		}

		// Template checks
		if (template != null){

			if(arguments == null){
				addError("For 'template' the field 'arguments' is required!");
				valid = false;
			}
			if (!(method.equalsIgnoreCase(POST_METHOD) || method.equalsIgnoreCase(POST_WITH_ARGUMENTS) || method.equalsIgnoreCase(POST_WITH_DOCUMENT))){
				addError("Templates can only be used with POST requests!");
				valid = false;
			}
		}

	return valid;
}

}
