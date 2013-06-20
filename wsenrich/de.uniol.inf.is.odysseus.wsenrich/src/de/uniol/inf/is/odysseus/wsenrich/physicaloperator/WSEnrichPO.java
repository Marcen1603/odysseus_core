package de.uniol.inf.is.odysseus.wsenrich.physicaloperator;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.wsenrich.exceptions.DatafieldNotFoundException;
import de.uniol.inf.is.odysseus.wsenrich.util.HttpEntityToStringConverter;
import de.uniol.inf.is.odysseus.wsenrich.util.IConnectionForWebservices;
import de.uniol.inf.is.odysseus.wsenrich.util.IKeyFinder;
import de.uniol.inf.is.odysseus.wsenrich.util.IRequestBuilder;

public class WSEnrichPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	
	private final String serviceMethod;
	private final String method;
	private final String url;
	private final String urlsuffix;
	private final List<Option> arguments;
	private final String operation;
	private final List<SDFAttribute> receivedData;
	private final String charset;
	private final String returnType;
	private final int[] parameterPositions;
	private final IDataMergeFunction<Tuple<T>, T> dataMergeFunction;
	private final IConnectionForWebservices connection;
	private final IRequestBuilder requestBuilder;
	private final HttpEntityToStringConverter converter;
	private final IKeyFinder keyFinder;
	static Logger logger = LoggerFactory.getLogger(WSEnrichPO.class);
	
	public WSEnrichPO(String serviceMethod, String method, String url, String urlsuffix,
					List<Option> arguments, String operation, List<SDFAttribute> receivedData,
					String charset, String returnType, IDataMergeFunction<Tuple<T>, T> dataMergeFunction,
					IConnectionForWebservices connection, IRequestBuilder requestBuilder, 
					HttpEntityToStringConverter converter, IKeyFinder keyFinder) {
						
		super();
		this.serviceMethod = serviceMethod;
		this.method = method;
		this.url = url;
		this.urlsuffix = urlsuffix;
		this.arguments = arguments;
		this.operation = operation;
		this.receivedData = receivedData;
		this.charset = charset;
		this.returnType = returnType;
		this.parameterPositions = new int[arguments.size()];
		this.dataMergeFunction = dataMergeFunction;
		this.connection = connection;
		this.requestBuilder = requestBuilder;
		this.converter = converter;
		this.keyFinder = keyFinder;
						
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
		this.returnType = wsEnrichPO.returnType;
		this.parameterPositions = Arrays.copyOf(
				wsEnrichPO.parameterPositions,
				wsEnrichPO.parameterPositions.length);
		this.dataMergeFunction = wsEnrichPO.dataMergeFunction.clone();
		this.connection = wsEnrichPO.connection;
		this.requestBuilder = wsEnrichPO.requestBuilder;
		this.converter = wsEnrichPO.converter;
		this.keyFinder = wsEnrichPO.keyFinder;
		
	}
	
		
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<T> inputTuple, int port) {

	//	Object queryParameters = getQueryParameters(inputTuple);
		
		List<Option> queryParameters = getQueryParameters(inputTuple, arguments);
		
		requestBuilder.setUrlPrefix(url);
		requestBuilder.setUrlSuffix(urlsuffix);
		requestBuilder.setArguments(queryParameters);
		requestBuilder.buildUri();
		String postData = requestBuilder.getPostData();
		String uri = requestBuilder.getUri();
		
		connection.setUri(uri);
		connection.setArgument(postData);
		
		connection.connect(charset, method);
		HttpEntity entity = connection.retrieveBody();
		converter.setInput(entity);
		converter.convert();
		connection.closeConnection();
		
		keyFinder.setMessage(converter.getOutput());
		
		//TODO: falsch!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! (Schleife prodziert n tuple!!
		for(int i = 0; i < receivedData.size(); i++) {
			
			keyFinder.setSearch(receivedData.get(i).getAttributeName());
			
			try {
				
				inputTuple.append(keyFinder.getValueOf(keyFinder.getSearch()), false);
				transfer(inputTuple);
				
			} catch (DatafieldNotFoundException e) {
				logger.error(e.getMessage());	
			}	
		}
	}
	
	@Override
	protected synchronized void process_open() throws OpenFailedException {
		
		initParameterPositions();
		dataMergeFunction.init();
		
	}
	
	@Override
	protected synchronized void process_close() {
		
		//TODO
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
/*	private Object[] getQueryParameters(Tuple<T> inputTuple) {
		
		Object[] queryParameters = new Object[parameterPositions.length];
		
		for(int i = 0; i < queryParameters.length; i++) {
			
			queryParameters[i] = inputTuple.getAttribute(parameterPositions[i]);
		}
		
		return queryParameters;
	} */
	
	public List<Option> getQueryParameters(Tuple<T> inputTuple, List<Option> arguments) {
		
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
		if(returnType == null || returnType.equals("")) {
			if(other.returnType != null || !other.returnType.equals("")) {
				return false;
			}
		} else if(!returnType.equals(other.returnType)) {
			return false;
		}
		return true;
		
	}

}
