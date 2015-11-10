package de.uniol.inf.is.odysseus.codegenerator.jre.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.codegenerator.scheduler.registry.CSchedulerRegistry;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;


/**
 * This class generate codefragments for different objects.
 * object to code transformation (templatebased)
 *  
 * @author MarcPreuschaft
 *
 */
public class CreateJreDefaultCode {
	
	
	/**
	 * create the default init code for the operators. Its create
	 * the SDFSchema for a operator.
	 * 
	 * @param operator
	 * @return
	 */
	public static CodeFragmentInfo getCodeForInitOperator(ILogicalOperator operator){
		CodeFragmentInfo sdfSchema = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		if(operatorVariable != null && !operatorVariable.equals("")){
			sdfSchema.addCodeFragmentInfo(getCodeForSDFSchema(operator.getOutputSchema(),operatorVariable));
		}
				
		return sdfSchema;
	}
	
	
	/**
	 * create code for a sdfAttribute object
	 * template: utils/sdfAttributeList.st
	 * 
	 * @param sdfAttributes
	 * @param operatorVariable
	 * @return
	 */
	public static CodeFragmentInfo getCodeForSDFAttributeList(List<SDFAttribute> sdfAttributes, String operatorVariable){
		CodeFragmentInfo sdfAttributeList = new CodeFragmentInfo();
		
		StringTemplate sdfAttributeListTemplate = new StringTemplate("utils","sdfAttributeList");
		sdfAttributeListTemplate.getSt().add("sdfAttributes", sdfAttributes);
		sdfAttributeListTemplate.getSt().add("operatorVariable",operatorVariable);

		sdfAttributeList.addCode(sdfAttributeListTemplate.getSt().render());
	
		return sdfAttributeList;
	}
	
	/**
	 * create code for the SDFSchema object
	 * template: utils/sdfSchema.st
	 * 
	 * @param schema
	 * @param operatorVariable
	 * @return
	 */
	public static CodeFragmentInfo getCodeForSDFSchema(SDFSchema schema, String operatorVariable){
		CodeFragmentInfo sdfSchema = new CodeFragmentInfo();
		
		String className = schema.getType().getSimpleName()+".class";
		
		StringTemplate sdfSchemaTemplate = new StringTemplate("utils","sdfSchema");
		sdfSchemaTemplate.getSt().add("operatorVariable", operatorVariable);
		sdfSchemaTemplate.getSt().add("schemaURI", schema.getURI());
		sdfSchemaTemplate.getSt().add("className", className);
		sdfSchemaTemplate.getSt().add("sdfAttributes", schema.getAttributes());
		
		//Add framework imports 
		sdfSchema.addFrameworkImport(schema.getType().getName());
		sdfSchema.addFrameworkImport(SDFSchema.class.getName());
		sdfSchema.addFrameworkImport(SDFSchemaFactory.class.getName());
		sdfSchema.addFrameworkImport(SDFAttribute.class.getName());
		sdfSchema.addFrameworkImport(ArrayList.class.getName());
		sdfSchema.addFrameworkImport(SDFDatatype.class.getName());
		
		//add java imports
		sdfSchema.addImport(List.class.getName());
		
		//render the template
		sdfSchema.addCode(sdfSchemaTemplate.getSt().render());

		return sdfSchema;
	}
	
	
	/**
	 * create the code the accessFramework
	 * 
	 * @param protocolHandlerParameter
	 * @param optionMap
	 * @param operator
	 * @param direction
	 * @return
	 */
	public static CodeFragmentInfo getCodeForAccessFramework(ProtocolHandlerParameter protocolHandlerParameter, Map<String,String> optionMap, ILogicalOperator operator, ITransportDirection direction){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
	
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);

		//add import
		codeFragmentInfo.addFrameworkImport(direction.getClass().getName());
		
		//generate code for options
		codeFragmentInfo.addCodeFragmentInfo(getCodeForOptionMap(optionMap,operatorVariable));
		
		//setup transportHandler
		codeFragmentInfo.addCodeFragmentInfo(getCodeForProtocolHandler(protocolHandlerParameter, operatorVariable, direction));
	
		return codeFragmentInfo;
	}
	
	/**
	 * create the code for a OptionMap
	 * template: optionMap.st
	 * 
	 * @param optionMap
	 * @param operatorVariable
	 * @return
	 */
	public static CodeFragmentInfo getCodeForOptionMap(Map<String, String> optionMap, String operatorVariable){
		CodeFragmentInfo codeFragmentInfo= new CodeFragmentInfo();
		
		StringTemplate optionMapTemplate = new StringTemplate("utils","optionMap");
		optionMapTemplate.getSt().add("operatorVariable", operatorVariable);
		optionMapTemplate.getSt().add("optionMap", optionMap);
		
		codeFragmentInfo.addCode(optionMapTemplate.getSt().render().replace("\\", "\\\\"));
	
		codeFragmentInfo.addFrameworkImport(OptionMap.class.getName());
		
		return codeFragmentInfo;
	}
	

	/**
	 * create the code for the relationalTimestamp object
	 * template: relationalTimestampAttributeTimeIntervalMFactory.st
	 *  
	 * @param forOperator
	 * @param timestampAO
	 * @return
	 */
	public static CodeFragmentInfo getCodeForRelationalTimestampAttributeTimeIntervalMFactory(ILogicalOperator forOperator, TimestampAO timestampAO){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(forOperator);
		
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
			
			codeFragmentInfo.addFrameworkImport(RelationalTimestampAttributeTimeIntervalMFactory.class.getName());
			codeFragmentInfo.addFrameworkImport(IMetadataInitializer.class.getName());
			
			codeFragmentInfo.addCode(relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().render());
			
			}
		}
	
		return codeFragmentInfo;
		
	}
	
	/**
	 * create the start code for a given scheduler
	 * 
	 * @param queryAnalyseInform
	 * @param scheduler
	 * @return
	 */
	public static CodeFragmentInfo getCodeForStartStreams(QueryAnalyseInformation queryAnalyseInform, String scheduler){
		return getCodeForStartStreams(queryAnalyseInform.getSinkOpList(),queryAnalyseInform.getSourceOpList(),queryAnalyseInform.getIterableSources(), scheduler);
	}
	
	/**
	 * create the start code for a given scheduler
	 * 
	 * @param sinkOPs
	 * @param sourceOPs
	 * @param iterableSources
	 * @param scheduler
	 * @return
	 */
	public static CodeFragmentInfo getCodeForStartStreams(List<ILogicalOperator> sinkOPs, List<ILogicalOperator> sourceOPs,List<ILogicalOperator> iterableSources, String scheduler){
		CodeFragmentInfo startFragment = new CodeFragmentInfo();
		
		String firstOP = DefaultCodegeneratorStatus.getInstance().getVariable(sourceOPs.get(0));
		List<String> sinkOpList = new ArrayList<String>();
		
		//Open on sink ops
		for(ILogicalOperator sinkOp : sinkOPs){
			sinkOpList.add(DefaultCodegeneratorStatus.getInstance().getVariable(sinkOp));
		}
		
		StringTemplate startCodeTemplate = new StringTemplate("java","startCode");
		startCodeTemplate.getSt().add("firstOP", firstOP);
		startCodeTemplate.getSt().add("operatorList",DefaultCodegeneratorStatus.getInstance().getOperatorList());
		startCodeTemplate.getSt().add("sinkOpList",sinkOpList);
		startCodeTemplate.getSt().add("sourceOP",DefaultCodegeneratorStatus.getInstance().getVariable(sourceOPs.get(0)));
		
		startFragment.addCode(startCodeTemplate.getSt().render());
		
		if(!iterableSources.isEmpty()){
			CodeFragmentInfo schedulerCode = CSchedulerRegistry.getScheduler("JRE", scheduler).getStartCode(iterableSources);
			startFragment.addCodeFragmentInfo(schedulerCode);
		}
		
		//add framework imports
		startFragment.addFrameworkImport(IPhysicalOperator.class.getName());
		startFragment.addFrameworkImport(ArrayList.class.getName());
		startFragment.addFrameworkImport(IPhysicalOperator.class.getName());
		startFragment.addFrameworkImport(PhysicalQuery.class.getName());
		startFragment.addFrameworkImport(IOperatorOwner.class.getName());
	
		return startFragment;
	}
	
	/**
	 * create the subscription code
	 * template: subscribeToSource.st
	 * 
	 * @param operator
	 * @param transformationInformation
	 * @return
	 */
	public static CodeFragmentInfo getCodeForSubscription(ILogicalOperator operator, QueryAnalyseInformation transformationInformation) {
		CodeFragmentInfo codeFragmentInfo =  new CodeFragmentInfo();
		
		String operatorVariable = transformationInformation.getVariable(operator);
		
		if(operatorVariable != null && !(operatorVariable.equals(""))){
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
			   
			 if(DefaultCodegeneratorStatus.getInstance().allOperatorExistForSubscriptions(neededOperator)){
				StringTemplate startCodeTemplate = new StringTemplate("utils","subscribeToSource");
				startCodeTemplate.getSt().add("operatorVariable", operatorVariable);  
				startCodeTemplate.getSt().add("targetOpMap", targetOpMap);  
					
				codeFragmentInfo.addCode(startCodeTemplate.getSt().render());
				
				return codeFragmentInfo;
			 }
		} 
		 return null;
	}
	
	

	
	
	
	/**
	 * create the code for a protocalHandler
	 * template: utils/protocolHandler.st
	 * 
	 * @param protocolHandlerParameter
	 * @param operatorVariable
	 * @param direction
	 * @return
	 */
	public static CodeFragmentInfo getCodeForProtocolHandler(ProtocolHandlerParameter protocolHandlerParameter, String operatorVariable, ITransportDirection direction){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();

		boolean openwrapper = false;
		
		String wrapper = "";
		if(protocolHandlerParameter.getWrapper().equals("GenericPull")){
			 wrapper = "IAccessPattern.PULL";
			 openwrapper = true;
			
		}else{
			 wrapper = "IAccessPattern.PUSH";
		}
		codeFragmentInfo.addFrameworkImport(IAccessPattern.class.getName());
		
	
		StringTemplate protocolHandlerTemplate = new StringTemplate("utils","protocolHandler");
	
		protocolHandlerTemplate.getSt().add("operatorVariable", operatorVariable);
		protocolHandlerTemplate.getSt().add("protocolHandlerParameter", protocolHandlerParameter);
		protocolHandlerTemplate.getSt().add("wrapper", wrapper);
		protocolHandlerTemplate.getSt().add("openwrapper", openwrapper);
		protocolHandlerTemplate.getSt().add("direction", direction);
		
		//add imports
		codeFragmentInfo.addFrameworkImport(IProtocolHandler.class.getName());
		codeFragmentInfo.addFrameworkImport(ITransportHandler.class.getName());

		//add dynamic transportHandler as import
		codeFragmentInfo.addFrameworkImport(TransportHandlerRegistry.getITransportHandlerClass(protocolHandlerParameter.getTransportHandler()).getClass().getName());
		
		//render the template
		codeFragmentInfo.setCode(protocolHandlerTemplate.getSt().render());

		return codeFragmentInfo;
	}
	


	
	/**
	 * create the code for the OSGi-registry emulation
	 * 
	 * @param odysseusPath
	 * @param transformationInforamtion
	 * @return
	 */
	public static CodeFragmentInfo getCodeForOSGIBinds(String odysseusPath, QueryAnalyseInformation transformationInforamtion){
		CodeFragmentInfo osgiBinds = new CodeFragmentInfo();
		
		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("registerAggregateFunctionBuilder",transformationInforamtion.getNeededAggregateFunctionBuilder(), AggregateFunctionBuilderRegistry.class));
		
		
		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("registerFunction",transformationInforamtion.getNeededMEPFunctions(), MEP.class));
		
		
		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("registerDataHandler",transformationInforamtion.getNeededDataHandler(), DataHandlerRegistry.class));
	

		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("register",transformationInforamtion.getNeededTransportHandler(), TransportHandlerRegistry.class));
		

		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("register",transformationInforamtion.getNeededProtocolHandler(), ProtocolHandlerRegistry.class));
		
	
		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("addMetadataType",transformationInforamtion.getNeededMetaDataTypes(), MetadataRegistry.class));
		
		
		osgiBinds.addCodeFragmentInfo(getCodeFragmentForRegistry("register",transformationInforamtion.getNeededSweepAreas(), SweepAreaRegistry.class));
		
	
		return osgiBinds;

	}
	
	/**
	 * create the code for a given registry
	 * template: utils/registryHandler.st
	 * 
	 * @param registerFunctionName
	 * @param registerList
	 * @param registryClass
	 * @return
	 */
	private static CodeFragmentInfo getCodeFragmentForRegistry(String registerFunctionName, Map<String, String> registerList, Class<?> registryClass){
		CodeFragmentInfo registryCode = new CodeFragmentInfo();
	
		List<String> functionNameList = new ArrayList<String>();
		for (Map.Entry<String, String> entry : registerList.entrySet())
		{
			registryCode.addFrameworkImport(entry.getKey());
			functionNameList.add(entry.getValue());

		}
		
		if(!functionNameList.isEmpty()){

			StringTemplate registryTemplate = new StringTemplate("utils","registryHandler");
			registryTemplate.getSt().add("clazzName", registryClass.getSimpleName());
			registryTemplate.getSt().add("clazzSimpleName", registryClass.getSimpleName().toLowerCase());
			registryTemplate.getSt().add("handlerList", functionNameList);
			registryTemplate.getSt().add("registerFunctionName",registerFunctionName);
			
			registryCode.addCode(registryTemplate.getSt().render());
			
			//add framework import
			registryCode.addFrameworkImport(registryClass.getName());
		
		}
		return registryCode;
	}
}
