package de.uniol.inf.is.odysseus.wsenrich.physicaloperator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractEnrichPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ILeftMergeFunction;
import de.uniol.inf.is.odysseus.wsenrich.util.HttpEntityToStringConverter;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IConnectionForWebservices;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IKeyFinder;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IMessageManipulator;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IRequestBuilder;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IMessageCreator;

public class WSEnrichPO<M extends IMetaAttribute> extends AbstractEnrichPO<Tuple<M>, M> {

	private final String serviceMethod;
	private final String method;
	private final String contentType;
	private final String url;
	private final Boolean urlIsTemplate;
	private final String urlsuffix;
	private final List<Option> arguments;
	private final List<Option> header;
	private final String operation;
	private final List<SDFAttribute> receivedData;
	private final String charset;
	private final String parsingMethod;
	private final boolean outerJoin;
	private final boolean allowNullValues;
	private final boolean keyValueOutput;
	private final boolean multiTupleOutput;
	private final int[] parameterPositions;
	private final IConnectionForWebservices connection;
	private final IRequestBuilder requestBuilder;
	private final HttpEntityToStringConverter converter;
	private final IKeyFinder keyFinder;
	private final IMessageCreator messageCreator;
	private final IMessageManipulator messageManipulator;

	static Logger logger = LoggerFactory.getLogger(WSEnrichPO.class);

	public WSEnrichPO(String serviceMethod, String method, String contentType, String url, Boolean urlIsTemplate,
			String urlsuffix, List<Option> arguments, List<Option> header, String operation,
			List<SDFAttribute> receivedData, String charset, String parsingMethod, boolean outerJoin, boolean allowNullValues,
			boolean keyValueOutput, boolean multiTupleOutput, int[] uniqueKey,
			IDataMergeFunction<Tuple<M>, M> dataMergeFunction, ILeftMergeFunction<Tuple<M>, M> dataLeftMergeFunction,
			IMetadataMergeFunction<M> metaMergeFunction, IConnectionForWebservices connection,
			IRequestBuilder requestBuilder, HttpEntityToStringConverter converter, IKeyFinder keyFinder,
			IMessageCreator soapMessageCreator, IMessageManipulator soapMessageManipulator, ICache cacheManager) {

		super(cacheManager, dataMergeFunction, dataLeftMergeFunction, metaMergeFunction, uniqueKey);
		this.serviceMethod = serviceMethod;
		this.method = method;
		this.contentType = contentType;
		this.url = url;
		this.urlIsTemplate = urlIsTemplate;
		this.urlsuffix = urlsuffix;
		this.arguments = arguments;
		this.header = header;
		this.operation = operation;
		this.receivedData = receivedData;
		this.charset = charset;
		this.parsingMethod = parsingMethod;
		this.outerJoin = outerJoin;
		this.allowNullValues = allowNullValues;
		this.keyValueOutput = keyValueOutput;
		this.multiTupleOutput = multiTupleOutput;
		this.parameterPositions = new int[arguments.size()];
		this.connection = connection;
		this.requestBuilder = requestBuilder;
		this.converter = converter;
		this.keyFinder = keyFinder;
		this.messageCreator = soapMessageCreator;
		this.messageManipulator = soapMessageManipulator;
	}

	@Override
	protected void internal_process_open() throws OpenFailedException {
		if (messageCreator != null) {
			messageCreator.buildMessage();
		}
		initParameterPositions();
	}

	@Override
	protected List<IStreamObject<?>> internal_process(Tuple<M> inputTuple) {

		List<Option> queryParameters = getQueryParameters(inputTuple, arguments);
		String postData = "";
		if (messageCreator != null && messageManipulator != null) {
			messageManipulator.setMessage(messageCreator.getMessage());
			messageManipulator.setArguments(queryParameters);
			postData = messageManipulator.buildMessage();
		}
		// Build the Url and arguments
		requestBuilder.setUrlPrefix(url);
		requestBuilder.setUrlSuffix(urlsuffix);
		requestBuilder.setArguments(queryParameters);
		requestBuilder.setPostData(postData);
		requestBuilder.buildUri();
		// String postData = requestBuilder.getPostData();
		String uri = requestBuilder.getUri();
		if (urlIsTemplate) {
			uri = replace(queryParameters, uri);
		}
		logger.trace("URI: " + uri);
		logger.trace("PostData :" + postData);
		// Connect to the Url
		connection.setUri(uri);
		connection.setArguments(requestBuilder.getPostData());
		logger.trace("Connection arguments " + requestBuilder.getPostData());
		if (header != null) {
			logger.trace("Header:" + header);
			connection.setHeaders(header);
		}
		try {
			connection.connect(charset, method, contentType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		HttpEntity entity = connection.retrieveBody();
		// Convert the Http Entity into a String, finally close the Http
		// Connection
		converter.setInput(entity);
		try {
			converter.convert();
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException(e);
		}
		if (logger.isTraceEnabled()) {
			logger.trace(converter.getOutput());
		}
		// Set the Message for the Key (Element) Finder an find the defined
		// Elements and paste
		// them to the tuple(s)
		keyFinder.setMessage(converter.getOutput(), charset, multiTupleOutput);

		ArrayList<IStreamObject<?>> queryResult = new ArrayList<IStreamObject<?>>(keyFinder.getTupleCount());

		for (int i = 0; i < keyFinder.getTupleCount(); i++) {
			Tuple<M> wsTuple = new Tuple<>(receivedData.size(), false);
			for (int j = 0; j < receivedData.size(); j++) {
				Object value = null;
				try {
					keyFinder.setSearch(receivedData.get(j).getAttributeName());
					value = keyFinder.getValueOf(keyFinder.getSearch(), keyValueOutput, i);
				} catch (Exception e) {
					logger.warn("Error setting attribute "+j,e);
				}

				if ((value == null || value.equals("")) && !outerJoin && !allowNullValues) {
					return null;
				} else if ((value == null || value.equals("")) && (outerJoin || allowNullValues)) {
					wsTuple.setAttribute(j, null);
				} else {
					wsTuple.setAttribute(j, value);
				}
			}
			queryResult.add(wsTuple);
		}
		return queryResult;
	}

	private String replace(List<Option> queryParameters, String uri) {
		String newUri = uri;
		for (Option o : queryParameters) {
			newUri = newUri.replaceAll(o.getName(), o.getValue());
		}

		return newUri;
	}

	@Override
	protected void internal_process_close() {
		connection.closeConnection();
	}

	/**
	 * Goes through the variables (a set of inputTuples attribute names) and
	 * saves each position in parameterPositions[]. This has to be done only
	 * once and prevents therefore a search of the positions for each input
	 * tuple/ process_next-call.
	 */
	private void initParameterPositions() {
		for (int i = 0; i < arguments.size(); i++) {
			String variableName = arguments.get(i).getValue();
			// Attributes of InputSchema are the Values of the KeyValueList
			SDFAttribute attribute = getOutputSchema().findAttribute(variableName);
			if (attribute == null) {
				throw new RuntimeException("Could not find attribute " + variableName + " in InputTuple.");
			}
			parameterPositions[i] = getOutputSchema().indexOf(attribute);
		}
	}

	/**
	 * Returns the Attributes that are defined in the Query for the Webservice.
	 *
	 * @param inputTuple
	 *            the current tuple from the input stream
	 * @return the attributes for the webservicequery
	 */
	private List<Option> getQueryParameters(Tuple<M> inputTuple, List<Option> arguments) {
		List<Option> queryParameters = new ArrayList<Option>(arguments);
		for (int i = 0; i < parameterPositions.length; i++) {
			queryParameters.set(i, new Option(queryParameters.get(i).getName(),
					inputTuple.getAttribute(parameterPositions[i]).toString()));
		}
		return queryParameters;
	}

	/**
	 * Compares all class attributes, that were present in the AO
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (this == ipo)
			return true;
		if (!super.equals(ipo))
			return false;
		if (getClass() != ipo.getClass())
			return false;
		WSEnrichPO<M> other = (WSEnrichPO<M>) ipo;

		if (!super.isSemanticallyEqual(other)) {
			return false;
		}
		if (!serviceMethod.equals(other.serviceMethod))
			return false;
		if (!method.equals(other.method))
			return false;
		if (!url.equals(other.url))
			return false;
		if (urlsuffix == null || urlsuffix.equals("")) {
			if (other.urlsuffix != null || !other.urlsuffix.equals(""))
				return false;
		} else if (!urlsuffix.equals(other.urlsuffix))
			return false;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (arguments != null && other.arguments != null) {
			List<Option> temp = other.arguments;
			for (int i = 0; i < arguments.size(); i++) {

				if (!arguments.get(i).getName().equals(temp.get(i).getName())
						|| !arguments.get(i).getValue().equals(temp.get(i).getValue()))
					return false;
			}
		}
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (header != null && other.header != null) {
			List<Option> temp = other.header;
			for (int i = 0; i < header.size(); i++) {

				if (!header.get(i).getName().equals(temp.get(i).getName())
						|| !header.get(i).getValue().equals(temp.get(i).getValue()))
					return false;
			}
		}
		if (operation == null || operation.equals("")) {
			if (other.operation != null || !other.operation.equals(""))
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if (charset == null || charset.equals("")) {
			if (other.charset != null || !other.charset.equals("")) {
				return false;
			}
		} else if (!charset.equals(other.charset)) {
			return false;
		}
		if (parsingMethod == null || parsingMethod.equals("")) {
			if (other.parsingMethod != null || !other.parsingMethod.equals("")) {
				return false;
			}
		} else if (!parsingMethod.equals(other.parsingMethod)) {
			return false;
		}
		if (outerJoin == false) {
			if (other.outerJoin == true) {
				return false;
			}
		} else if (outerJoin == true) {
			if (other.outerJoin == false) {
				return false;
			}
		}
		if (keyValueOutput == false) {
			if (other.keyValueOutput == true) {
				return false;
			}
		} else if (keyValueOutput == true) {
			if (other.keyValueOutput == false) {
				return false;
			}
		}
		if (multiTupleOutput == false) {
			if (other.multiTupleOutput == true) {
				return false;
			}
		} else if (multiTupleOutput == true) {
			if (other.multiTupleOutput == false) {
				return false;
			}
		}

		return true;
	}

}
