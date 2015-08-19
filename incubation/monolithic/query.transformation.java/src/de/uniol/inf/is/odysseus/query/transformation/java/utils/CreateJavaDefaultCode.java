package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.query.transformation.executor.registry.ExecutorRegistry;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;

public class CreateJavaDefaultCode {
	
	public static CodeFragmentInfo initOperator(ILogicalOperator operator){
		CodeFragmentInfo sdfSchema = new CodeFragmentInfo();
		
		String operatorVariable = JavaTransformationInformation.getInstance().getVariable(operator);
		
		sdfSchema.addCodeFragmentInfo(getCodeForSDFSchema(operator.getOutputSchema(),operatorVariable));
				
		return sdfSchema;
	}
	
	public static CodeFragmentInfo codeForAccessFramework(ProtocolHandlerParameter protocolHandlerParameter, Map<String,String> optionMap, ILogicalOperator operator, ITransportDirection direction){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
	
		String operatorVariable = JavaTransformationInformation.getInstance().getVariable(operator);

		//add import
		codeFragmentInfo.addImport(ITransportDirection.class.getName());
		
		//generate code for options
		codeFragmentInfo.addCodeFragmentInfo(getCodeForParameterInfoNeu(optionMap,operatorVariable));
		
		//setup transportHandler
		codeFragmentInfo.addCodeFragmentInfo(getCodeForProtocolHandlerNeu(protocolHandlerParameter, operatorVariable, direction));
	

		return codeFragmentInfo;
	}
	

	public static CodeFragmentInfo codeForRelationalTimestampAttributeTimeIntervalMFactory(ILogicalOperator forOperator, TimestampAO timestampAO){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		String operatorVariable = JavaTransformationInformation.getInstance().getVariable(forOperator);
		
		SDFSchema schema = timestampAO.getInputSchema();
		boolean clearEnd = timestampAO.isClearEnd();
		int pos = schema.indexOf(timestampAO.getStartTimestamp());

		if (Tuple.class.isAssignableFrom(timestampAO.getInputSchema().getType())) {
			if (pos >= 0) {
				int posEnd = timestampAO.hasEndTimestamp() ? timestampAO
						.getInputSchema()
						.indexOf(timestampAO.getEndTimestamp()) : -1;
				
			StringTemplate relationalTimestampAttributeTimeIntervalMFactoryTemplate = new StringTemplate("utils","relationalTimestampAttributeTimeIntervalMFactory");
			
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("operatorVariable", operatorVariable);			
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("pos", pos);	
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("posEnd", posEnd);	
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("clearEnd", clearEnd);	
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("timestampAO", timestampAO);	
			
			codeFragmentInfo.addImport(RelationalTimestampAttributeTimeIntervalMFactory.class.getName());
			codeFragmentInfo.addImport(IMetadataInitializer.class.getName());
			
			codeFragmentInfo.addCode(relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().render());
			
			}
			
		}
	
		return codeFragmentInfo;
		
	}
	
	public static CodeFragmentInfo codeForStartStreams(List<ILogicalOperator> sinkOPs, List<ILogicalOperator> sourceOPs, String executor){
		CodeFragmentInfo startFragment = new CodeFragmentInfo();
		
		String firstOP = JavaTransformationInformation.getInstance().getVariable(sourceOPs.get(0));
		
		List<String> sinkOpList = new ArrayList<String>();
		List<String> iiterableSources = new ArrayList<String>();
	

		//Open on sink ops
		for(ILogicalOperator sinkOp : sinkOPs){
					sinkOpList.add(JavaTransformationInformation.getInstance().getVariable(sinkOp));
		}
		
	
		StringTemplate startCodeTemplate = new StringTemplate("java","startCode");
		startCodeTemplate.getSt().add("firstOP", firstOP);
		startCodeTemplate.getSt().add("operatorList",JavaTransformationInformation.getInstance().getOperatorList());
		startCodeTemplate.getSt().add("sinkOpList",sinkOpList);
		startCodeTemplate.getSt().add("sourceOP",JavaTransformationInformation.getInstance().getVariable(sourceOPs.get(0)));
		
		startFragment.addCode(startCodeTemplate.getSt().render());
		
		
		for(ILogicalOperator op : sourceOPs){
			if(op instanceof IIterableSource){
				iiterableSources.add(JavaTransformationInformation.getInstance().getVariable(op));
			}
		}
		
		
		if(!iiterableSources.isEmpty()){
			CodeFragmentInfo executorCode = ExecutorRegistry.getExecutor("Java", executor).getStartCode(sourceOPs);
			startFragment.addCodeFragmentInfo(executorCode);
		}
	
	
		startFragment.addImport(IPhysicalOperator.class.getName());
		startFragment.addImport(ArrayList.class.getName());
		startFragment.addImport(IPhysicalOperator.class.getName());
		startFragment.addImport(PhysicalQuery.class.getName());
		startFragment.addImport(IOperatorOwner.class.getName());
	
		
		return startFragment;
	}
	
	
	public static CodeFragmentInfo generateSubscription(ILogicalOperator operator, QueryAnalyseInformation transformationInformation) {
		CodeFragmentInfo codeFragmentInfo =  new CodeFragmentInfo();
		
		String operatorVariable = transformationInformation.getVariable(operator);
		
		Collection<LogicalSubscription> subscriptionSourceList = operator.getSubscribedToSource();
		
		Map<String,LogicalSubscription> targetOpMap = new HashMap<String,LogicalSubscription>();
		
		List<ILogicalOperator> neededOperator = new ArrayList<ILogicalOperator>();
		
		   if(!subscriptionSourceList.isEmpty()){
			   for(LogicalSubscription sub : subscriptionSourceList){
				   
					ILogicalOperator  targetLogicalOP;
					
					//TODO workaround for renameAO
					if( sub.getTarget() instanceof RenameAO){
						targetLogicalOP =sub.getTarget().getSubscribedToSource().iterator().next().getTarget();
					}else{
						targetLogicalOP = sub.getTarget();
					}
			
					 String  targetOp =  transformationInformation.getVariable(targetLogicalOP);
					 neededOperator.add(targetLogicalOP);
					  if(!targetOp.equals("")){
						  targetOpMap.put(targetOp,sub);		
					  }	
				   }
		   }
		   
		 if(JavaTransformationInformation.getInstance().allOperatorExistForSubscriptions(neededOperator)){
			 
	
			StringTemplate startCodeTemplate = new StringTemplate("utils","subscribeToSource");
			startCodeTemplate.getSt().add("operatorVariable", operatorVariable);  
			startCodeTemplate.getSt().add("targetOpMap", targetOpMap);  
				
			codeFragmentInfo.addCode(startCodeTemplate.getSt().render());
			
			return codeFragmentInfo;
		 }
		 
		 return null;
	}
	
	
	/*
    OptionMap csvOptions = new OptionMap();
    csvOptions.setOption("delimiter", ",");
    csvOptions.setOption("filename", "F:\\Dropbox\\Dropbox\\Studium\\Masterarbeit\\Odysseus\\Data\\soccergame_10_20.csv");
    csvOptions.setOption("basetimeunit", "MICROSECONDS");
    csvOptions.setOption("dumpeachline", "1000000");
    csvOptions.setOption("measureeachline", "100000");
    csvOptions.setOption("lastline", "49576078");
    
    */
	
	

	public static CodeFragmentInfo getCodeForParameterInfoNeu(Map<String, String> optionMap, String operatorVariable){
		CodeFragmentInfo codeFragmentInfo= new CodeFragmentInfo();
		
		StringTemplate optionMapTemplate = new StringTemplate("utils","optionMap");
		optionMapTemplate.getSt().add("operatorVariable", operatorVariable);
		optionMapTemplate.getSt().add("optionMap", optionMap);
		
		codeFragmentInfo.addCode(optionMapTemplate.getSt().render().replace("\\", "\\\\"));
	
		codeFragmentInfo.addImport(OptionMap.class.getName());
		
		return codeFragmentInfo;
	}
	
	
	
	/*
	IProtocolHandler cSVProtocolHandlerNeu =  protocolHandlerRegistry.getInstance("SimpleCSV", ITransportDirection.IN, IAccessPattern.PULL, csvOptions,  dataHandlerRegistry.getDataHandler("Tuple", sourceSchema));
    ITransportHandler transportHandler = new FileHandler(cSVProtocolHandlerNeu, csvOptions);
    cSVProtocolHandlerNeu.setTransportHandler(transportHandler);
        transportHandler.processInOpen();
	 
	 */


	public static CodeFragmentInfo getCodeForProtocolHandlerNeu(ProtocolHandlerParameter protocolHandlerParameter, String operatorVariable, ITransportDirection direction){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		Set<String> imports = new HashSet<String>();

		boolean openwrapper = false;
		
		String wrapper = "";
		if(protocolHandlerParameter.getWrapper().equals("GenericPull")){
			 wrapper = "IAccessPattern.PULL";
			 openwrapper = true;
			
		}else{
			 wrapper = "IAccessPattern.PUSH";
		}
		imports.add(IAccessPattern.class.getName());
		
	
		StringTemplate protocolHandlerTemplate = new StringTemplate("utils","protocolHandler");
	
		protocolHandlerTemplate.getSt().add("operatorVariable", operatorVariable);
		protocolHandlerTemplate.getSt().add("protocolHandlerParameter", protocolHandlerParameter);
		protocolHandlerTemplate.getSt().add("wrapper", wrapper);
		protocolHandlerTemplate.getSt().add("openwrapper", openwrapper);
		protocolHandlerTemplate.getSt().add("direction", direction);
		
		//add imports
		imports.add(IProtocolHandler.class.getName());
		imports.add(ITransportHandler.class.getName());
		imports.add(FileHandler.class.getName());
	 
		codeFragmentInfo.setCode(protocolHandlerTemplate.getSt().render());
		codeFragmentInfo.setImports(imports);
		
		return codeFragmentInfo;
	}
	
	/*
    //AccessPO schema erstellen
    List<SDFAttribute> sourceSchemaList = new ArrayList(); 
    SDFAttribute sid = new SDFAttribute(null, "a", SDFDatatype.INTEGER, null, null,null);
    SDFAttribute ts = new SDFAttribute(null, "ts", SDFDatatype.STRING, null, null,null);
    sourceSchemaList.add(sid);
    sourceSchemaList.add(ts);
    
	SDFSchema sourceSchema = SDFSchemaFactory.createNewSchema("soccergame", Tuple.class, sourceSchemaList);
*/
	public static CodeFragmentInfo getCodeForSDFSchema(SDFSchema schema, String operatorVariable){
		CodeFragmentInfo sdfSchema = new CodeFragmentInfo();
		
		String className = schema.getType().getSimpleName()+".class";
		
		StringBuilder code = new StringBuilder();
		
	
		StringTemplate sdfSchemaTemplate = new StringTemplate("utils","sdfSchema");
		sdfSchemaTemplate.getSt().add("operatorVariable", operatorVariable);
		sdfSchemaTemplate.getSt().add("schemaURI", schema.getURI());
		sdfSchemaTemplate.getSt().add("className", className);
		sdfSchemaTemplate.getSt().add("sdfAttributes", schema.getAttributes());
		
	
		
		code.append(sdfSchemaTemplate.getSt().render());
		

		
		//Add imports 
		sdfSchema.addImport(schema.getType().getName());
		sdfSchema.addImport(SDFSchema.class.getName());
		sdfSchema.addImport(SDFSchemaFactory.class.getName());
		
		
		sdfSchema.addImport(SDFAttribute.class.getName());
		sdfSchema.addImport(List.class.getName());
		sdfSchema.addImport(ArrayList.class.getName());
		sdfSchema.addImport(SDFDatatype.class.getName());
		
		
		
		sdfSchema.addCode(code.toString());

		return sdfSchema;
	}
	
	
	public static CodeFragmentInfo getCodeForOSGIBinds(String odysseusPath, de.uniol.inf.is.odysseus.query.transformation.modell.QueryAnalyseInformation transformationInforamtion){
		CodeFragmentInfo osgiBinds = new CodeFragmentInfo();
		
		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("registerFunction",transformationInforamtion.getNeededMEPFunctions(), MEP.class));
		
		
		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("registerDataHandler",transformationInforamtion.getNeededDataHandler(), DataHandlerRegistry.class));
	

		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("register",transformationInforamtion.getNeededTransportHandler(), TransportHandlerRegistry.class));
		

		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("register",transformationInforamtion.getNeededProtocolHandler(), ProtocolHandlerRegistry.class));
		
	
		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("addMetadataType",transformationInforamtion.getNeededMetaDataTypes(), MetadataRegistry.class));
		
		
		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("register",transformationInforamtion.getNeededSweepAreas(), SweepAreaRegistry.class));
		
	
		return osgiBinds;

	}
	
	private static CodeFragmentInfo getCodeFragmentForRegistry(String registerFunctionName, Map<String, String> registerList, Class<?> registryClass){
		
		CodeFragmentInfo registryCode = new CodeFragmentInfo();
		

		List<String> functionNameList = new ArrayList<String>();
		for (Map.Entry<String, String> entry : registerList.entrySet())
		{
			registryCode.addImport(entry.getKey());
			functionNameList.add(entry.getValue());

		}

		StringTemplate mepRegistryTemplate = new StringTemplate("utils","registryHandler");
		mepRegistryTemplate.getSt().add("clazzName", registryClass.getSimpleName());
		mepRegistryTemplate.getSt().add("clazzSimpleName", registryClass.getSimpleName().toLowerCase());
		mepRegistryTemplate.getSt().add("handlerList", functionNameList);
		mepRegistryTemplate.getSt().add("registerFunctionName",registerFunctionName);
		
		registryCode.addCode(mepRegistryTemplate.getSt().render());
		registryCode.addImport(registryClass.getName());
		
		
		return registryCode;
	}
	
	

}
