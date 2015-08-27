package de.uniol.inf.is.odysseus.query.codegenerator.modell;

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
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;

public class QueryAnalyseInformation {

	private Map<String, String> dataHandler = new HashMap<String, String>();
	private Map<String, String> mepFunctions = new HashMap<String, String>();
	private Map<String, String> protocolHandler = new HashMap<String, String>();
	private Map<String, String> transportHandler = new HashMap<String, String>();
	private Map<String, String> metaDataTypes = new HashMap<String, String>();
	private Map<String, String> sweepAreas = new HashMap<String, String>();
	
	private List<ILogicalOperator> iIterableSource = new ArrayList<ILogicalOperator>();
	
	
	private Map<ILogicalOperator, String> operatorList  = new HashMap<ILogicalOperator, String>();

	private Map<ILogicalOperator,Map<String,String>> operatorConfigurationList =  new HashMap<ILogicalOperator,Map<String,String>>();
	

	private List<ILogicalOperator> sourceOPs = new ArrayList<ILogicalOperator>();
	private List<ILogicalOperator> sinkOPs = new ArrayList<ILogicalOperator>();;
	
	private int uniqueId = 0;
	
	
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
		
		String fullClassName = MetadataRegistry.getMetadataType(metaData).getClass().getName();
		String simpleClassName = MetadataRegistry.getMetadataType(metaData).getClass().getSimpleName();
		
		if (!metaDataTypes.containsKey(fullClassName)) {
			metaDataTypes.put(fullClassName, simpleClassName);
		}
	}
	
	public Map<String, String> getNeededMetaDataTypes() {
		return metaDataTypes;
	}
	
	
	public void addDataHandler(String datatype){
		String fullClassName = DataHandlerRegistry.getIDataHandlerClass(datatype).getClass().getName();
		String simpleClassName = DataHandlerRegistry.getIDataHandlerClass(datatype).getClass().getSimpleName();
		
		if (!dataHandler.containsKey(fullClassName)) {
			dataHandler.put(fullClassName, simpleClassName);
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
		
	String fullClassName =	mepExpression.toFunction().getClass().getName();
	String simpleClassName = mepExpression.toFunction().getClass().getSimpleName();

		if (!mepFunctions.containsKey(fullClassName)) {
			mepFunctions.put(fullClassName, simpleClassName);
		}

	}
	

	public Map<String, String> getNeededMEPFunctions() {
		return mepFunctions;
	}
	

	public void addProtocolHandler(String protocolHandlerString) {
	if(protocolHandlerString !=null ){
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
		if(transportHandlerString != null){
			
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
	


	public boolean isOperatorAdded(ILogicalOperator operator){
		if(operatorList.containsKey(operator)){
			return true;
		}else{
			return false;
		}
	}
	
	public void addOperator(ILogicalOperator operator){
		
		if(!operatorList.containsKey(operator)){
			operatorList.put(operator, operator.getName().toLowerCase()+getUniqueId());
		}
	
	}
	
	public  String getVariable(ILogicalOperator operator){
		if(operatorList.containsKey(operator)){
			return operatorList.get(operator);
		}else{
			return "";
		}
	}


	private synchronized int getUniqueId()
	{  
		 return uniqueId++;
	   
	}
	
	public  Map<ILogicalOperator, String> getOperatorList(){
		return operatorList;
	}

}
