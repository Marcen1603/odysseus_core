package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.ProtocolHandlerParameter;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class CreateDefaultCode {
	
	public static CodeFragmentInfo initOperator(ILogicalOperator operator){
		CodeFragmentInfo sdfSchema = new CodeFragmentInfo();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		sdfSchema.addCodeFragmentInfo(TransformSDFSchema.getCodeForSDFSchema(operator.getOutputSchema(),operatorVariable));
				
		return sdfSchema;
	}
	
	
	
	
	public static CodeFragmentInfo codeForAccessFrameworkNeu(ProtocolHandlerParameter protocolHandlerParameter, Map<String,String> optionMap, ILogicalOperator operator, ITransportDirection direction){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
	
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);

		//add import
		codeFragmentInfo.addImport(ITransportDirection.class.getName());
		
		//generate code for options
		codeFragmentInfo.addCodeFragmentInfo(TransformCSVParameter.getCodeForParameterInfoNeu(optionMap,operatorVariable));
		
		//setup transportHandler
		codeFragmentInfo.addCodeFragmentInfo(TransformProtocolHandler.getCodeForProtocolHandlerNeu(protocolHandlerParameter, operatorVariable, direction));
	

		return codeFragmentInfo;
	}
	
	
	
	public static CodeFragmentInfo codeForRelationalTimestampAttributeTimeIntervalMFactory(ILogicalOperator forOperator, TimestampAO timestampAO){
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		
		StringBuilder code = new StringBuilder();
		
		String operatorVariable = TransformationInformation.getInstance().getVariable(forOperator);
		
		SDFSchema schema = timestampAO.getInputSchema();
		boolean clearEnd = timestampAO.isClearEnd();
		int pos = schema.indexOf(timestampAO.getStartTimestamp());

		if (Tuple.class.isAssignableFrom(timestampAO.getInputSchema().getType())) {
			if (pos >= 0) {
				int posEnd = timestampAO.hasEndTimestamp() ? timestampAO
						.getInputSchema()
						.indexOf(timestampAO.getEndTimestamp()) : -1;
						
			code.append("RelationalTimestampAttributeTimeIntervalMFactory "+operatorVariable+"MetaUpdater = new RelationalTimestampAttributeTimeIntervalMFactory("+pos+", "+posEnd+","+ clearEnd+","+ timestampAO.getDateFormat()+","+timestampAO.getTimezone()+","+ timestampAO.getLocale()+","+timestampAO.getFactor()+","+ timestampAO.getOffset()+");");
			code.append("\n");
			code.append("((IMetadataInitializer) "+operatorVariable+"PO).addMetadataUpdater("+operatorVariable+"MetaUpdater);");
			}
			
		}
		codeFragmentInfo.addImport(RelationalTimestampAttributeTimeIntervalMFactory.class.getName());
		codeFragmentInfo.addImport(IMetadataInitializer.class.getName());
		
		codeFragmentInfo.addCode(code.toString());
		

	
		return codeFragmentInfo;
		
	}
	
	public static CodeFragmentInfo codeForStartStreams(List<ILogicalOperator> sinkOPs, List<ILogicalOperator> sourceOPs){
		CodeFragmentInfo startFragment = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();
		code.append("\n");
		code.append("\n");
		code.append("ArrayList<IPhysicalOperator> physicalOperatorList = new ArrayList<IPhysicalOperator>();");
		code.append("\n");
		code.append("physicalOperatorList.add("+TransformationInformation.getInstance().getVariable(sourceOPs.get(0))+"PO);");
		code.append("\n");
		code.append("IOperatorOwner operatorOwner = new PhysicalQuery(physicalOperatorList);");
		code.append("\n");
		code.append("\n");
	
		//add owner to op
		for (Entry<ILogicalOperator, String> entry : TransformationInformation.getInstance().getOperatorList().entrySet())
		{
			code.append(entry.getValue()+"PO.addOwner(operatorOwner);");
			code.append("\n");
		}
	
		//Open on sink ops
		for(ILogicalOperator sinkOp : sinkOPs){
				code.append(TransformationInformation.getInstance().getVariable(sinkOp)+"PO.open(operatorOwner);");
				code.append("\n");
		}
		
		code.append("\n");
		code.append("while("+TransformationInformation.getInstance().getVariable(sourceOPs.get(0))+"PO.hasNext()){");
		code.append("\n");
		code.append(TransformationInformation.getInstance().getVariable(sourceOPs.get(0))+"PO.transferNext();");
		code.append("\n");
		code.append("}");
		code.append("\n");
		code.append("\n");
		
		startFragment.addCode(code.toString());
		
		startFragment.addImport(IPhysicalOperator.class.getName());
		startFragment.addImport(ArrayList.class.getName());
		startFragment.addImport(IPhysicalOperator.class.getName());
		startFragment.addImport(PhysicalQuery.class.getName());
		startFragment.addImport(IOperatorOwner.class.getName());
	
		
		return startFragment;
		
	}
	
	
	public static CodeFragmentInfo generateSubscription(ILogicalOperator operator) {
		CodeFragmentInfo codeFragmentInfo =  new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();
	
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		Collection<LogicalSubscription> subscriptionSourceList = operator.getSubscribedToSource();
			
		   if(!subscriptionSourceList.isEmpty()){
			   for(LogicalSubscription sub : subscriptionSourceList){
					  String targetOp =  TransformationInformation.getInstance().getVariable(sub.getTarget());
					  if(!targetOp.equals("")){
						  code.append("\n");
							code.append("\n");
							code.append(operatorVariable+"PO.subscribeToSource("+targetOp+"PO, "+sub.getSinkInPort()+", "+sub.getSourceOutPort()+", "+targetOp+"PO.getOutputSchema());");
							code.append("\n");
					  }	
				   }
		   }
		

		code.append("\n");
		code.append("\n");
		code.append("\n");
		
		codeFragmentInfo.addCode(code.toString());
		
		return codeFragmentInfo;
	}
	
	

}
