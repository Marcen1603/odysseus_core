package de.uniol.inf.is.odysseus.wsenrich.physicaloperator;

import java.util.List;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;

public class WSEnrichPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	
	private final String serviceMethod;
	private final String method;
	private final String url;
	private final String urlsuffix;
	private final List<Option> arguments;
	private final String operation;
	private final int[] parameterPositions;
	private final IDataMergeFunction<Tuple<T>, T> dataMergeFunction;
	
	public WSEnrichPO(String serviceMethod, String method, String url, String urlsuffix,
					List<Option> arguments, String operation, IDataMergeFunction<Tuple<T>, T> dataMergeFunction) {
						
		super();
		this.serviceMethod = serviceMethod;
		this.method = method;
		this.url = url;
		this.urlsuffix = urlsuffix;
		this.arguments = arguments;
		this.operation = operation;
		this.parameterPositions = new int[arguments.size()];
		this.dataMergeFunction = dataMergeFunction;
						
	}
	
	public WSEnrichPO(WSEnrichPO<T> wsEnrichPO) {
		
		super(wsEnrichPO);
		this.serviceMethod = wsEnrichPO.serviceMethod;
		this.method = wsEnrichPO.method;
		this.url = wsEnrichPO.url;
		this.urlsuffix = wsEnrichPO.urlsuffix;
		this.arguments = wsEnrichPO.arguments;
		this.operation = wsEnrichPO.operation;
		this.parameterPositions = Arrays.copyOf(
				wsEnrichPO.parameterPositions,
				wsEnrichPO.parameterPositions.length);
		this.dataMergeFunction = wsEnrichPO.dataMergeFunction.clone();
		
	}
	
		
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<T> object, int port) {
		// TODO Auto-generated method stub
		
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
	}
*/
	
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
		return true;
		
	}

}
