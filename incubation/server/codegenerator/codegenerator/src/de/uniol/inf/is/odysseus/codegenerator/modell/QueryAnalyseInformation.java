package de.uniol.inf.is.odysseus.codegenerator.modell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;

/**
 * This class holds the query analyse information and is
 * used by the codegeneration.analyse component 
 * 
 * @author MarcPreuschaft
 *
 */
public class QueryAnalyseInformation {

	//list of the detected dataHandler
	private Map<String, String> dataHandler = new HashMap<String, String>();
	//list of the detected mepFunktions
	private Map<String, String> mepFunctions = new HashMap<String, String>();
	//list of the detected protocolHandler
	private Map<String, String> protocolHandler = new HashMap<String, String>();
	//list of the detected transportHandler
	private Map<String, String> transportHandler = new HashMap<String, String>();
	//list of the detected metaDataTypes
	private Map<String, String> metaDataTypes = new HashMap<String, String>();
	//list of the detected sweepAreas
	private Map<String, String> sweepAreas = new HashMap<String, String>();
	//list of the detected aggreagteFunctions
	private Map<String, String> aggregateFunctionBuilder = new HashMap<String, String>();
	//list of iterableSources in the query
	private List<ILogicalOperator> iIterableSource = new ArrayList<ILogicalOperator>();
	//list of all detected operators
	private Map<ILogicalOperator, String> operatorList  = new HashMap<ILogicalOperator, String>();
	//for this operators we can create operatorConfugruation files
	private Map<ILogicalOperator,Map<String,String>> operatorConfigurationList =  new HashMap<ILogicalOperator,Map<String,String>>();
	
	//all sources Ops
	private List<ILogicalOperator> sourceOPs = new ArrayList<ILogicalOperator>();
	
	//all sink ops
	private List<ILogicalOperator> sinkOPs = new ArrayList<ILogicalOperator>();;
	
	//a unique id for the operators
	private int uniqueId = 0;
	
	
	public void addAggregateFunctionBuilder(IAggregateFunctionBuilder builder) {
		  if(builder != null ){
		
			String fullClassName = builder.getClass().getName();
			String simpleClassName = builder.getClass().getSimpleName();
	
			if (!aggregateFunctionBuilder.containsKey(fullClassName)) {
				aggregateFunctionBuilder.put(fullClassName, simpleClassName);
			}
		}
		
		
	}
	
	public Map<String, String> getNeededAggregateFunctionBuilder() {
		return aggregateFunctionBuilder;
	}
	
	public void addSweepArea(String sweepArea){
		String fullClassName = "";
		String simpleClassName = "";
		
		try {
			fullClassName = SweepAreaRegistry.getSweepArea(sweepArea).getClass().getName();
			simpleClassName = SweepAreaRegistry.getSweepArea(sweepArea).getClass().getSimpleName();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
		if (!sweepAreas.containsKey(fullClassName)) {
			sweepAreas.put(fullClassName, simpleClassName);
		}
		
	}
	
	public Map<String, String> getNeededSweepAreas() {
		return sweepAreas;
	}

	public void addMetaData(String metaData){
		
		if(metaData != null && !metaData.equals("")){
			String fullClassName = MetadataRegistry.getMetadataType(metaData).getClass().getName();
			String simpleClassName = MetadataRegistry.getMetadataType(metaData).getClass().getSimpleName();
			
			if (!metaDataTypes.containsKey(fullClassName)) {
				metaDataTypes.put(fullClassName, simpleClassName);
			}
		}
		
		
	}
	
	public Map<String, String> getNeededMetaDataTypes() {
		return metaDataTypes;
	}
	
	public void addDataHandler(String datatype){
		
		if(datatype != null && !datatype.equals("")){
			String fullClassName = DataHandlerRegistry.getIDataHandlerClass(datatype).getClass().getName();
			String simpleClassName = DataHandlerRegistry.getIDataHandlerClass(datatype).getClass().getSimpleName();
			
			if (!dataHandler.containsKey(fullClassName)) {
				dataHandler.put(fullClassName, simpleClassName);
			}
		}

	}

	public Map<String, String> getNeededDataHandler() {
		return dataHandler;
	}
	
	public void addMEPFunction(Map<String,IExpression<?>> expressions){
		for (Entry<String, IExpression<?>> entry : expressions.entrySet())
		{
			addMEPFunction(entry.getValue());
		}
	}
	
	public void addMEPFunction(IExpression<?> mepExpression) {
		
		if(mepExpression != null){
			String fullClassName =	mepExpression.toFunction().getClass().getName();
			String simpleClassName = mepExpression.toFunction().getClass().getSimpleName();

				if (!mepFunctions.containsKey(fullClassName)) {
					mepFunctions.put(fullClassName, simpleClassName);
				}
		}
		
	}
	
	public Map<String, String> getNeededMEPFunctions() {
		return mepFunctions;
	}

	public void addProtocolHandler(String protocolHandlerString) {
		
		if(protocolHandlerString != null &&  !protocolHandlerString.equals("")){
			String fullClassName = ProtocolHandlerRegistry.getIProtocolHandlerClass(protocolHandlerString).getClass().getName();
			String simpleClassName = ProtocolHandlerRegistry.getIProtocolHandlerClass(protocolHandlerString).getClass().getSimpleName();
	
			if (!protocolHandler.containsKey(fullClassName)) {
				protocolHandler.put(fullClassName, simpleClassName);
			}
		}
	}

	public Map<String, String> getNeededProtocolHandler() {
		return protocolHandler;
	}

	public void addTransportHandler(String transportHandlerString) {
		if(transportHandlerString != null && !transportHandlerString.equals("")){
			
			String fullClassName = TransportHandlerRegistry.getITransportHandlerClass(transportHandlerString).getClass().getName();
			String simpleClassName = TransportHandlerRegistry.getITransportHandlerClass(transportHandlerString).getClass().getSimpleName();
			
			if (!transportHandler.containsKey(fullClassName)) {
				transportHandler.put(fullClassName, simpleClassName);
			}
			
		}

	}
	
	

	public Map<String, String> getNeededTransportHandler() {
		return transportHandler;
	}
	
	
	public void addIterableSource(ILogicalOperator operator){
		iIterableSource.add(operator);
	}
	
	public List<ILogicalOperator> getIterableSources(){
		return iIterableSource;
	}
	
	public void addOperatorConfiguration(ILogicalOperator operator, Map<String,String> optionMap){
		operatorConfigurationList.put(operator,optionMap);
	}
	
	public Map<ILogicalOperator,Map<String,String>> getOperatorConfigurationList(){
		return operatorConfigurationList;
	}
	
	public void addSourceOp(ILogicalOperator sourceOP){
		sourceOPs.add(sourceOP);
	}

	public void addSourceOp(List<ILogicalOperator> sourceOPList){
		sourceOPs.addAll(sourceOPList);
	}
	
	public List<ILogicalOperator> getSourceOpList(){
		return sourceOPs;
	}
	
	public void addSinkOp(ILogicalOperator sinkOP){
		sinkOPs.add(sinkOP);
	}
	
	public List<ILogicalOperator> getSinkOpList(){
		return sinkOPs;
	}

	
	public boolean isOperatorAdded(ILogicalOperator operator){
		if(operatorList.containsKey(operator)){
			return true;
		}else{
			return false;
		}
	}
	
	public void addOperator(ILogicalOperator operator){
		if(!operatorList.containsKey(operator)){
			String operatorName = operator.getName().toLowerCase();
			operatorName = operatorName.replaceAll("[^a-zA-Z0-9]", "");
			
			operatorList.put(operator, operatorName+getUniqueId());
		}
	
	}
	
	public  String getVariable(ILogicalOperator operator){
		if(operatorList.containsKey(operator)){
			return operatorList.get(operator);
		}else{
			return "";
		}
	}

	public  Map<ILogicalOperator, String> getOperatorList(){
		return operatorList;
	}

	
	private synchronized int getUniqueId(){  
		 return uniqueId++;
	}
}
