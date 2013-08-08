package de.uniol.inf.is.odysseus.wsenrich.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.cache.ICache;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.wsenrich.util.HttpEntityToStringConverter;
import de.uniol.inf.is.odysseus.wsenrich.util.IConnectionForWebservices;
import de.uniol.inf.is.odysseus.wsenrich.util.IKeyFinder;
import de.uniol.inf.is.odysseus.wsenrich.util.IMessageManipulator;
import de.uniol.inf.is.odysseus.wsenrich.util.IRequestBuilder;
import de.uniol.inf.is.odysseus.wsenrich.util.ISoapMessageCreator;

public class WSEnrichPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	private final String serviceMethod;
	private final String method;
	private final String url;
	private final String urlsuffix;
	private final List<Option> arguments;
	private final String operation;
	private final List<SDFAttribute> receivedData;
	private final String charset;
	private final String parsingMethod;
	private final boolean outerJoin;
	private final boolean keyValueOutput;
	private final boolean multiTupleOutput;
	private final int[] parameterPositions;
	private final IDataMergeFunction<Tuple<T>, T> dataMergeFunction;
	private final IMetadataMergeFunction<T> metaMergeFunction;
	private final IConnectionForWebservices connection;
	private final IRequestBuilder requestBuilder;
	private final HttpEntityToStringConverter converter;
	private final IKeyFinder keyFinder;
	private final ISoapMessageCreator soapMessageCreator;
	private final IMessageManipulator soapMessageManipulator;
	private final ICache cacheManager;
	static Logger logger = LoggerFactory.getLogger(WSEnrichPO.class);
	
	public WSEnrichPO(String serviceMethod, String method, String url, String urlsuffix,
					List<Option> arguments, String operation, List<SDFAttribute> receivedData,
					String charset, String parsingMethod, boolean outerJoin, boolean keyValueOutput,
					boolean multiTupleOutput, IDataMergeFunction<Tuple<T>, T> dataMergeFunction,
					IMetadataMergeFunction<T> metaMergeFunction,
					IConnectionForWebservices connection, IRequestBuilder requestBuilder, 
					HttpEntityToStringConverter converter, IKeyFinder keyFinder, 
					ISoapMessageCreator soapMessageCreator, IMessageManipulator soapMessageManipulator,
					ICache cacheManager) {
						
		super();
		this.serviceMethod = serviceMethod;
		this.method = method;
		this.url = url;
		this.urlsuffix = urlsuffix;
		this.arguments = arguments;
		this.operation = operation;
		this.receivedData = receivedData;
		this.charset = charset;
		this.parsingMethod = parsingMethod;
		this.outerJoin = outerJoin;
		this.keyValueOutput = keyValueOutput;
		this.multiTupleOutput = multiTupleOutput;
		this.parameterPositions = new int[arguments.size()];
		this.dataMergeFunction = dataMergeFunction;
		this.metaMergeFunction = metaMergeFunction;
		this.connection = connection;
		this.requestBuilder = requestBuilder;
		this.converter = converter;
		this.keyFinder = keyFinder;
		this.soapMessageCreator = soapMessageCreator;
		this.soapMessageManipulator = soapMessageManipulator;
		this.cacheManager = cacheManager;				
	}
	
	public WSEnrichPO(WSEnrichPO<T> wsEnrichPO) {
		
		super(wsEnrichPO);
		this.serviceMethod = wsEnrichPO.serviceMethod;
		this.method = wsEnrichPO.method;
		this.url = wsEnrichPO.url;
		this.urlsuffix = wsEnrichPO.urlsuffix;
		this.arguments = wsEnrichPO.arguments;
		this.operation = wsEnrichPO.operation;
		this.receivedData = wsEnrichPO.receivedData;
		this.charset = wsEnrichPO.charset;
		this.parsingMethod = wsEnrichPO.parsingMethod;
		this.outerJoin = wsEnrichPO.outerJoin;
		this.keyValueOutput = wsEnrichPO.keyValueOutput;
		this.multiTupleOutput = wsEnrichPO.multiTupleOutput;
		this.parameterPositions = Arrays.copyOf(
				wsEnrichPO.parameterPositions,
				wsEnrichPO.parameterPositions.length);
		this.dataMergeFunction = wsEnrichPO.dataMergeFunction.clone();
		this.metaMergeFunction = wsEnrichPO.metaMergeFunction.clone();
		this.connection = wsEnrichPO.connection;
		this.requestBuilder = wsEnrichPO.requestBuilder;
		this.converter = wsEnrichPO.converter;
		this.keyFinder = wsEnrichPO.keyFinder;
		this.soapMessageCreator = wsEnrichPO.soapMessageCreator;
		this.soapMessageManipulator = wsEnrichPO.soapMessageManipulator;
		this.cacheManager = wsEnrichPO.cacheManager;	
	}
	
		
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<T> inputTuple, int port) {
		if(cacheManager == null) {
			process_next_without_cache(inputTuple, port);
		} else {
			process_next_with_cache(inputTuple, port);
		}
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		if(soapMessageCreator != null) {
			soapMessageCreator.buildSoapMessage();
		}
		initParameterPositions();
		dataMergeFunction.init();
	}
	
	@Override
	protected void process_close() {
		connection.closeConnection();
		//Print the Caching Statistics
		if(this.cacheManager != null) {
			System.out.println("Caching Statistics: ");
			System.out.println("Cache hits: " + cacheManager.getCacheHits());
			System.out.println("Cache miss: " + cacheManager.getCacheMiss());
			System.out.println("Cache inserts: " + cacheManager.getCacheInsert());
			System.out.println("Cache removes: " + cacheManager.getCacheRemoves());
			cacheManager.close();
		}	
	}

	@Override
	public AbstractPipe<Tuple<T>, Tuple<T>> clone() {
		return new WSEnrichPO<T>(this);
	}
	
	/**
	 * Goes through the variables (a set of inputTuples attribute
	 * names) and saves each position in parameterPositions[]. This has to be
	 * done only once and prevents therefore a search of the positions for
	 * each input tuple/ process_next-call.
	 */
	private void initParameterPositions() {
		for(int i = 0; i < arguments.size(); i++) {
			String variableName = arguments.get(i).getValue();
			//Attributes of InputSchema are the Values of the KeyValueList
			SDFAttribute attribute = getOutputSchema().findAttribute(variableName);
			if(attribute == null) {
				throw new RuntimeException("Could not find attribute " + variableName + " in InputTuple.");
			}
			parameterPositions[i] = getOutputSchema().indexOf(attribute);	
		}
	}
	
	/**
	 * Returns the Attributes that are defined in the Query for the
	 * Webservice. 
	 * @param inputTuple the current tuple from the input stream
	 * @return the attributes for the webservicequery
	 */
	private List<Option> getQueryParameters(Tuple<T> inputTuple, List<Option> arguments) {
		List<Option> queryParameters = arguments;
		for(int i = 0; i < parameterPositions.length; i++) {
			queryParameters.set(i, new Option(queryParameters.get(i).getName(), inputTuple.getAttribute(parameterPositions[i]).toString()));
		}
		return queryParameters;	
	}
	
	/**
	 * Compares all class attributes, that were present in the AO
	 */
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if(this == ipo) 
			return true;
		if(!super.equals(ipo))
			return false;
		if(getClass() != ipo.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		WSEnrichPO other = (WSEnrichPO) ipo;
		if(!serviceMethod.equals(other.serviceMethod))
			return false;
		if(!method.equals(other.method))
			return false;
		if(!url.equals(other.url))
			return false;
		if(urlsuffix == null || urlsuffix.equals("")) {
			if(other.urlsuffix != null || !other.urlsuffix.equals(""))
				return false;
		} else if (!urlsuffix.equals(other.urlsuffix))
			return false;
		if(arguments == null) {
			if(other.arguments !=null)
				return false;
		} else if (arguments != null && other.arguments != null) {
			
			@SuppressWarnings("unchecked")
			List<Option> temp = other.arguments;
			for(int i = 0; i < arguments.size(); i++){
			
				if(!arguments.get(i).getName().equals(temp.get(i).getName()) ||
					!arguments.get(i).getValue().equals(temp.get(i).getValue())) 
					return false;
			}
		}	
		if(operation == null || operation.equals("")){
			if(other.operation != null || !other.operation.equals(""))
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if(charset == null || charset.equals("")) {
			if(other.charset != null || !other.charset.equals("")) {
				return false;
			}
		} else if (!charset.equals(other.charset)) {
			return false;
		}
		if(parsingMethod == null || parsingMethod.equals("")) {
			if(other.parsingMethod != null || !other.parsingMethod.equals("")) {
				return false;
			}
		} else if(!parsingMethod.equals(other.parsingMethod)) {
			return false;
		}
		if(outerJoin == false) {
			if(other.outerJoin == true) {
				return false;
			}
		} else if(outerJoin == true) {
			if(other.outerJoin == false) {
				return false;
			}
		}
		if(keyValueOutput == false) {
			if(other.keyValueOutput == true) {
				return false;
			}
		} else if(keyValueOutput == true) {
			if(other.keyValueOutput == false) {
				return false;
			}
		}
		if(multiTupleOutput == false) {
			if(other.multiTupleOutput == true) {
				return false;
			}
		} else if(multiTupleOutput == true) {
			if(other.multiTupleOutput == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Retrieves data from the specified webservice and puts the tuples into the cache
	 * @param inputTuple the input tuple
	 * @param port the port
	 */
	@SuppressWarnings("unchecked")
	private void process_next_with_cache(Tuple<T> inputTuple, int port) {
		//get Tuples from the Cache with the HashCode of the inputTuple as the referencing key
		ArrayList<IStreamObject<?>> cachedTuples = cacheManager.get(inputTuple.hashCode());
		if(cachedTuples == null) {
			List<Option> queryParameters = getQueryParameters(inputTuple, arguments);
			String postData = "";
			if(soapMessageCreator != null && soapMessageManipulator != null) {
				soapMessageManipulator.setMessage(soapMessageCreator.getSoapMessage());
				soapMessageManipulator.setArguments(queryParameters);
				postData = soapMessageManipulator.buildMessage();
			}
			//Build the Url and arguments
			requestBuilder.setUrlPrefix(url);
			requestBuilder.setUrlSuffix(urlsuffix);
			requestBuilder.setArguments(queryParameters);
			requestBuilder.setPostData(postData);
			requestBuilder.buildUri();
			//String postData = requestBuilder.getPostData();
			String uri = requestBuilder.getUri();
			//Connect to the Url
			connection.setUri(uri);
			connection.setArguments(requestBuilder.getPostData());
			connection.connect(charset, method);
			HttpEntity entity = connection.retrieveBody();
			//Convert the Http Entity into a String, finally close the Http Connection
			converter.setInput(entity);
			converter.convert();
			//Set the Message for the Key (Element) Finder an find the defined Elements and paste
			//them to the tuple(s)
			keyFinder.setMessage(converter.getOutput(), charset, multiTupleOutput);
			//Create a ArrayList for the Tuples of the webservice data for the cache
			ArrayList<IStreamObject<?>> tuplesToCache = new ArrayList<IStreamObject<?>>(keyFinder.getTupleCount());
			
			for(int i = 0; i < keyFinder.getTupleCount(); i++) {
				Tuple<T> wsTuple = new Tuple<>(receivedData.size(), false);
				for(int j = 0; j < receivedData.size(); j++) {
					keyFinder.setSearch(receivedData.get(j).getAttributeName());
					Object value = keyFinder.getValueOf(keyFinder.getSearch(), keyValueOutput, i);
					if((value == null || value.equals("")) && !outerJoin) {
						return;
					} else if((value == null || value.equals("")) && outerJoin) {
						wsTuple.setAttribute(j, "null");
					} else {
						wsTuple.setAttribute(j, value);
					}
				}
				tuplesToCache.add(wsTuple);
				Tuple<T> outputTuple = dataMergeFunction.merge(inputTuple, wsTuple, metaMergeFunction, Order.LeftRight);
				outputTuple.setMetadata((T)inputTuple.getMetadata().clone());
				transfer(outputTuple);
			}
		cacheManager.put(inputTuple.hashCode(), tuplesToCache);
		} else { //This means, there are tuples in the cache with the specified key
			for(int i = 0; i < cachedTuples.size(); i++) {
				Tuple<T> outputTuple = dataMergeFunction.merge(inputTuple, (Tuple<T>) cachedTuples.get(i), metaMergeFunction, Order.LeftRight);
				outputTuple.setMetadata((T)inputTuple.getMetadata().clone());
				transfer(outputTuple);
			}
		}
	}
	
	/**
	 * retrieves data from the specified weberservice an put them to the output stream, no cache!!
	 * @param inputTuple the input tuple
	 * @param port the port
	 */
	@SuppressWarnings("unchecked")
	private void process_next_without_cache(Tuple<T> inputTuple, int port) {

		List<Option> queryParameters = getQueryParameters(inputTuple, arguments);
		String postData = "";
		if(soapMessageCreator != null && soapMessageManipulator != null) {
			soapMessageManipulator.setMessage(soapMessageCreator.getSoapMessage());
			soapMessageManipulator.setArguments(queryParameters);
			postData = soapMessageManipulator.buildMessage();
		}
		
		//Build the Url and arguments
		requestBuilder.setUrlPrefix(url);
		requestBuilder.setUrlSuffix(urlsuffix);
		requestBuilder.setArguments(queryParameters);
		requestBuilder.setPostData(postData);
		requestBuilder.buildUri();
		//String postData = requestBuilder.getPostData();
		String uri = requestBuilder.getUri();
		
		//Connect to the Url
		connection.setUri(uri);
		connection.setArguments(requestBuilder.getPostData());
		connection.connect(charset, method);
		HttpEntity entity = connection.retrieveBody();
		
		//Convert the Http Entity into a String, finally close the Http Connection
		converter.setInput(entity);
		converter.convert();
		//Set the Message for the Key (Element) Finder an find the defined Elements and paste
		//them to the tuple(s)
		keyFinder.setMessage(converter.getOutput(), charset, multiTupleOutput);
		
		for(int i = 0; i < keyFinder.getTupleCount(); i++) {
			Tuple<T> wsTuple = new Tuple<>(receivedData.size(), false);
			for(int j = 0; j < receivedData.size(); j++) {
				keyFinder.setSearch(receivedData.get(j).getAttributeName());
				Object value = keyFinder.getValueOf(keyFinder.getSearch(), keyValueOutput, i);
				if((value == null || value.equals("")) && !outerJoin) {
					return;
				} else if((value == null || value.equals("")) && outerJoin) {
					wsTuple.setAttribute(j, "null");
				} else {
					wsTuple.setAttribute(j, value);
				}
			}
			Tuple<T> outputTuple = dataMergeFunction.merge(inputTuple, wsTuple, metaMergeFunction, Order.LeftRight);
			outputTuple.setMetadata((T)inputTuple.getMetadata().clone());
			transfer(outputTuple);
		}
	}
}
