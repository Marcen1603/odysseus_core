package de.uniol.inf.is.odysseus.wsenrich.logicaloperator;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableList;
import de.uniol.inf.is.odysseus.cache.removalstrategy.RemovalStrategyRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry.KeyFinderRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry.RequestBuilderRegistry;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "WSENRICH")
public class WSEnrichAO extends UnaryLogicalOp {

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
	 * If false, tuples with a null-response will be filtered (they will not
	 * appear in the output True, tuples with a null-response will appear in the
	 * output
	 */
	private boolean outerJoin = false;

	/**
	 * If true, received Data from a Webservice are returned as keyValuePairs
	 */
	private boolean keyValueOutput = false;

	/**
	 * Enables multi tuple output
	 */
	private boolean multiTupleOutput = false;

	/**
	 * the location of the wsdl-file
	 */
	private String wsdlLocation;

	/*
	 * Caching-Parameters
	 */

	/**
	 * Parameter for caching functionality
	 */
	private boolean caching = false;

	/**
	 * Parameter for the removal strategy
	 */
	private String removalStrategy = "FIFO";

	/**
	 * The expiration time of cache entrys
	 */
	private long expirationTime = 1000 * 60 * 5; // 5 minutes

	/**
	 * The number of tuples a cache can maximal hold
	 */
	private int cacheSize = 20;
	
	/**
	 * The position of the unique key in the input stream
	 */
	private List<Integer> uniqueKeys = null;

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
		this.url = wsEnrichAO.url;
		this.urlsuffix = wsEnrichAO.urlsuffix;
		this.arguments = wsEnrichAO.arguments;
		this.operation = wsEnrichAO.operation;
		this.receivedData = wsEnrichAO.receivedData;
		this.charset = wsEnrichAO.charset;
		this.parsingMethod = wsEnrichAO.parsingMethod;
		this.getOrPost = wsEnrichAO.setGetOrPost();
		this.outerJoin = wsEnrichAO.outerJoin;
		this.keyValueOutput = wsEnrichAO.keyValueOutput;
		this.multiTupleOutput = wsEnrichAO.multiTupleOutput;
		this.wsdlLocation = wsEnrichAO.wsdlLocation;
		this.caching = wsEnrichAO.caching;
		this.removalStrategy = wsEnrichAO.removalStrategy;
		this.expirationTime = wsEnrichAO.expirationTime;
		this.cacheSize = wsEnrichAO.cacheSize;
		this.uniqueKeys = wsEnrichAO.uniqueKeys;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new WSEnrichAO(this);
	}

	@Override
	public boolean isValid() {
		boolean valid = true;

		/*
		 * Check for correct Handler names
		 */

		// Walk through the registered Handlers of IRequestBuilder and check if
		// theres a matching handler
		ImmutableList<String> requestHandlers = RequestBuilderRegistry
				.getHandlerNames();
		if (!requestHandlers.contains(method.toLowerCase())) {
			addError(new IllegalParameterException(
					"Method must be \"GET\" or \"POST_ARGUMENTS\" or \"POST_DOCUMENT\""));
			valid = false;
		}
		// Walk through the registered Handlers of IRequestBuilder and check if
		// theres a matching handler
		ImmutableList<String> parserHandlers = KeyFinderRegistry
				.getHandlerNames();
		if (!parserHandlers.contains(parsingMethod.toLowerCase())) {
			addError(new IllegalParameterException(
					"You have to declare the Parsing Method to parse the webservice-response. This can be XMLEXPERIMENTAL  "
							+ "or XPATH for XML-Documents or JSONEXPERIMENTAL or JSONPATH for JSON Documents."));
			valid = false;
		}
		if (caching) {
			ImmutableList<String> removalStrategies = RemovalStrategyRegistry
					.getHandlerNames();
			if (!removalStrategies.contains(removalStrategy.toLowerCase())) {
				addError(new IllegalParameterException(
						"Removal Strategy must be FIFO, LRU, LFU or Random!"));
				valid = false;
			}
		}
		// Can´t be checked with the List of Handlers of
		// SoapMessageCeatorRegistry because "REST" is not registered there
		if (!(serviceMethod.equals(SERVICE_METHOD_REST) || serviceMethod
				.equals(SERVICE_METHOD_SOAP))) {
			addError(new IllegalParameterException(
					"The serviceMethod must be \"REST\" or \"SOAP\""));
			valid = false;
		}

		/*
		 * Check dependencies beetween the variables
		 */

		if ((operation != null && serviceMethod.equals(SERVICE_METHOD_REST))) {
			addError(new IllegalParameterException(
					"If you want to receive Data from a REST-Service you don´t have to define a operation!"));
			valid = false;
		}
		if ((operation == null && serviceMethod.equals(SERVICE_METHOD_SOAP))) {
			addError(new IllegalParameterException(
					"If you want to receive Data from a SOAP-Servie you have to define a operation!"));
			valid = false;
		}
		if ((wsdlLocation == null || wsdlLocation.equals(""))
				&& serviceMethod.equals(SERVICE_METHOD_SOAP)) {
			addError(new IllegalParameterException(
					"If you want to receive Data from a SOAP-Service you have to define the location to the wsdl file of this webservice"));
			valid = false;
		}
		if (multiTupleOutput && !parsingMethod.equals(PARSING_XML_XPATH)) {
			addError(new IllegalParameterException(
					"MultiTupleOutput currently only works with the XPATH-Parser. You have to declare the parameter 'paringMethod' with 'XPATH'"));
			valid = false;
		}
		if (arguments == null) {
			addError(new IllegalParameterException(
					"Missing Parameter 'arguments'. You have to declare min 1 Datafield you wand to submit to the webservice."));
			valid = false;
		}
		if (receivedData == null) {
			addError(new IllegalParameterException(
					"Missing Parameter 'datafields'. You have to declare min 1 Datafield of the webservice for the Outputschema."));
			valid = false;
		}
		if (expirationTime < 0) {
			addError(new IllegalParameterException("ExpirationTime must be > 0"));
			valid = false;
		}
		if (cacheSize <= 0) {
			addError(new IllegalParameterException("cacheSize must be > 0"));
			valid = false;
		}
		return valid;
	}

	@Override
	public void initialize() {
		SDFSchema webserviceData = new SDFSchema("", receivedData);
		SDFSchema outputSchema = SDFSchema.union(getInputSchema(),
				webserviceData);
		setOutputSchema(outputSchema);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int port) {
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
	 * 
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
	 * 
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
	 * Setter for the static part of the Url after Arguments (optional)
	 * 
	 * @param urlSuffix
	 */
	@Parameter(type = StringParameter.class, name = "urlSuffix", optional = true)
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
	@Parameter(type = OptionParameter.class, name = "arguments", isList = true)
	public void setArguments(List<Option> arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return The name of the Operation used to call a SOAP-Webservice (only
	 *         needed for Soap-Webservices)
	 */
	public String getOpeation() {
		return this.operation;
	}

	/**
	 * Setter for the Operation used to call a SOAP-Webservice. (only needet for
	 * Soap-Webservices)
	 * 
	 * @param operation
	 */
	@Parameter(type = StringParameter.class, name = "operation", optional = true)
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return The Datafields received through the webservice
	 */
	public List<SDFAttribute> getReceivedData() {
		return receivedData;
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "datafields", isList = true)
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
	@Parameter(type = StringParameter.class, name = "charset", optional = true)
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
	@Parameter(type = StringParameter.class, name = "parsingMethod")
	public void setParsingMethod(String parsingMethod) {
		this.parsingMethod = parsingMethod;
	}

	/**
	 * Setter for the Get or Post-variable
	 */
	private String setGetOrPost() {
		if (this.method.equals(POST_WITH_ARGUMENTS)
				|| this.method.equals(POST_WITH_DOCUMENT)) {
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
	 * Setter for filtering Null Tuples. If true, Tuples with a null-response
	 * will not appear in the output stream, If false, Tuples with a
	 * null-response will appear in the output stream with "null"
	 * 
	 * @param filterNullTuples
	 */
	@Parameter(type = BooleanParameter.class, optional = true, name = "outerJoin")
	public void setOuterJoin(boolean outerJoin) {
		this.outerJoin = outerJoin;
	}

	/**
	 * @return Null Tuples will be filtered or not
	 */
	public boolean getOuterJoin() {
		return this.outerJoin;
	}

	/**
	 * Setter to enable or disable key value output in the output stream
	 * 
	 * @param keyValueOutput
	 */
	@Parameter(type = BooleanParameter.class, optional = true, name = "keyValueOutput")
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
	@Parameter(type = StringParameter.class, optional = true, name = "wsdlLocation")
	public void setWsdlLocaton(String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}

	/**
	 * @return mulit tuple output
	 */
	public boolean getMultiTupleOutput() {
		return this.multiTupleOutput;
	}

	/**
	 * Setter to enable or disable multi tuple output
	 * 
	 * @param multiTupleOutput
	 */
	@Parameter(type = BooleanParameter.class, optional = true, name = "multiTupleOutput")
	public void setMultiTupleOutput(boolean multiTupleOutput) {
		this.multiTupleOutput = multiTupleOutput;
	}

	/**
	 * @return true if caching is enable, false then
	 */
	public boolean getCache() {
		return this.caching;
	}

	/**
	 * Setter to enable or disable caching
	 * 
	 * @param cache
	 */
	@Parameter(type = BooleanParameter.class, optional = true, name = "caching")
	public void setCache(boolean cache) {
		this.caching = cache;
	}

	/**
	 * @return the removal strategy of the cache
	 */
	public String getRemovalStrategy() {
		return this.removalStrategy;
	}

	/**
	 * Setter for the removal strategy
	 * 
	 * @param removalStrategy
	 */
	@Parameter(type = StringParameter.class, optional = true, name = "removalStrategy")
	public void setRemovalStrategy(String removalStrategy) {
		this.removalStrategy = removalStrategy;
	}

	/**
	 * @return the expiration time of cache entrys
	 */
	public long getExpirationTime() {
		return this.expirationTime;
	}

	/**
	 * Setter for the expiration time. One minute is 1000 * 60
	 * 
	 * @param expirationTime
	 */
	@Parameter(type = LongParameter.class, optional = true, name = "expirationTime")
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	/**
	 * @return the max number of tuples the cache can hold
	 */
	public int getCacheSize() {
		return this.cacheSize;
	}

	/**
	 * Setter for the max number of tuples a cache can hold
	 * @param cacheSize
	 */
	@Parameter(type = IntegerParameter.class, optional = true, name = "cacheSize")
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}
	
	/**
	 * @return the unique key attributes of the input stream
	 */
	public List<Integer> getUniqueKeysAsList() {
		return this.uniqueKeys;
	}
	
	/**
	 * @return the unique key attributes as a int-Array
	 */
	public int[] getUniqueKeysAsArray() {
		if(this.uniqueKeys == null) {
			return null;
		} else {
			int[] keys = new int[this.uniqueKeys.size()];
			for(int i = 0; i < this.uniqueKeys.size(); i++) {
				keys[i] = this.uniqueKeys.get(i);
			}
			return keys;
		}
	}
	
	/**
	 * Setter for the unique keys of the input stream
	 * @param keys the keys
	 */
	@Parameter(type = IntegerParameter.class, name = "uniqueKeys", isList = true, optional = true)
	public void setUniqueKey(List<Integer> keys) {
		this.uniqueKeys = keys;
	}
}
