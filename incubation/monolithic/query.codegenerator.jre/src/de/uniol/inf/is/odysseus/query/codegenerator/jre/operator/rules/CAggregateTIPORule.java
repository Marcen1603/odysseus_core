package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.model.CAggregateItemModel;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJavaDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractAggregateTIPORule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;


public class CAggregateTIPORule extends AbstractAggregateTIPORule{
	
	public CAggregateTIPORule() {
		super(CAggregateTIPORule.class.getName());
	}
	
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		
		
		
		CodeFragmentInfo aggregateTIPO = new CodeFragmentInfo();
		
		AggregateAO aggregateAO = (AggregateAO) operator;
		
		String operatorVariable = JavaTransformationInformation.getInstance().getVariable(operator);
		
		aggregateTIPO.addCodeFragmentInfo(CreateJavaDefaultCode.getCodeForSDFSchema(aggregateAO.getInputSchema(), operatorVariable+"Input"));
		
		aggregateTIPO.addCodeFragmentInfo(CreateJavaDefaultCode.getCodeForSDFSchema(aggregateAO.getOutputSchemaIntern(0), operatorVariable+"OutputSchemaIntern"));
		
		aggregateTIPO.addCodeFragmentInfo(CreateJavaDefaultCode.createCodeForSDFAttributeList(aggregateAO.getGroupingAttributes(), operatorVariable+"GroupingAttribute"));
		

		List<String> metaAttributeNames = aggregateAO.getInputSchema().getMetaAttributeNames();
		

		
		StringTemplate aggregateMetaDataTemplate = new StringTemplate("utils","metaDataMergeFunction");
		aggregateMetaDataTemplate.getSt().add("operatorVariable", operatorVariable);
		aggregateMetaDataTemplate.getSt().add("metaAttributeNames", metaAttributeNames);

		aggregateTIPO.addCode(aggregateMetaDataTemplate.getSt().render());
		
	
		

	
		List<CAggregateItemModel> cAggregateItemModelList = new ArrayList<CAggregateItemModel>();

		for (AggregateItem item : aggregateAO.getAggregationItems()) {

			String functionName = item.aggregateFunction.getName();
			List<SDFAttribute> inAttributes = item.inAttributes;
			SDFAttribute outAttribute = item.outAttribute;
			
			String outAttributeSourceName = outAttribute.getSourceName();
			String outAttributeAttributeName = outAttribute.getAttributeName();
			String outAttributeSDFDataTypeName = outAttribute.getDatatype().toString();
			

			
			cAggregateItemModelList.add(new CAggregateItemModel(functionName, inAttributes,outAttribute,outAttributeSourceName,outAttributeAttributeName,outAttributeSDFDataTypeName));
		
	
		
	
		}
		
		
		StringTemplate aggregateItemsTIPOTemplate = new StringTemplate("utils","aggregateItemListNeu");
		aggregateItemsTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		aggregateItemsTIPOTemplate.getSt().add("cAggregateItemModelList", cAggregateItemModelList);
	

		aggregateTIPO.addCode(aggregateItemsTIPOTemplate.getSt().render());
		
		
		
		
		boolean fastGrouping = aggregateAO.isFastGrouping();
		
		
		StringTemplate aggregateTIPOTemplate = new StringTemplate("operator","aggregateTIPO");
		aggregateTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		aggregateTIPOTemplate.getSt().add("fastGrouping", fastGrouping);
		
		
		aggregateTIPO.addCode(aggregateTIPOTemplate.getSt().render());
		
		aggregateTIPO.addImport(AggregateTIPO.class.getName());
		aggregateTIPO.addImport(ITimeInterval.class.getName());
		aggregateTIPO.addImport(IStreamObject.class.getName());
		aggregateTIPO.addImport(AggregateItem.class.getName());
		aggregateTIPO.addImport(IMetadataMergeFunction.class.getName());
		aggregateTIPO.addImport(AggregateAO.class.getName());
		aggregateTIPO.addImport(AggregateFunction.class.getName());
		aggregateTIPO.addImport(RelationalAggregateFunctionBuilder.class.getName());
		aggregateTIPO.addImport(IAggregateFunctionBuilder.class.getName());
		aggregateTIPO.addImport(IAggregateFunction.class.getName());
		aggregateTIPO.addImport(FESortedClonablePair.class.getName());
		aggregateTIPO.addImport(java.util.Map.class.getName());
		aggregateTIPO.addImport(RelationalGroupProcessor.class.getName());
		aggregateTIPO.addImport(java.util.Map.Entry.class.getCanonicalName());
		aggregateTIPO.addImport(AggregateTISweepArea.class.getName());
		
	
		
	
		aggregateTIPO.addImport(AggregateFunctionBuilderRegistry.class.getName());
		
		
		
		return aggregateTIPO;
	}
	
	

}
