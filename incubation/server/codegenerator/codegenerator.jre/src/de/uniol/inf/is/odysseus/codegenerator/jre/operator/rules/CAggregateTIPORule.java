package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.codegenerator.jre.model.CAggregateItemModel;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractAggregateTIPORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;


public class CAggregateTIPORule extends AbstractAggregateTIPORule<AggregateAO>{
	
	public CAggregateTIPORule() {
		super(CAggregateTIPORule.class.getName());
	}
	
	@Override
	public CodeFragmentInfo getCode(AggregateAO operator) {
		
		
		
		CodeFragmentInfo aggregateTIPO = new CodeFragmentInfo();
		
		AggregateAO aggregateAO = (AggregateAO) operator;
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		aggregateTIPO.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFSchema(aggregateAO.getInputSchema(), operatorVariable+"Input"));
		
		aggregateTIPO.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFSchema(aggregateAO.getOutputSchemaIntern(0), operatorVariable+"OutputSchemaIntern"));
		
		aggregateTIPO.addCodeFragmentInfo(CreateJreDefaultCode.getCodeForSDFAttributeList(aggregateAO.getGroupingAttributes(), operatorVariable+"GroupingAttribute"));
		

		List<String> metaAttributeNames = aggregateAO.getInputSchema().getMetaAttributeNames();
		

		
		StringTemplate aggregateMetaDataTemplate = new StringTemplate("utils","metaDataMergeFunction");
		aggregateMetaDataTemplate.getSt().add("operatorVariable", operatorVariable);
		aggregateMetaDataTemplate.getSt().add("metaAttributeNames", metaAttributeNames);

		aggregateTIPO.addCode(aggregateMetaDataTemplate.getSt().render());
		
	
	
		List<CAggregateItemModel> cAggregateItemModelList = new ArrayList<CAggregateItemModel>();
	
		
		
		List<AggregateItem> aggregateItemList = new ArrayList<AggregateItem>();
		
		
		for (Entry<SDFSchema, Map<AggregateFunction, SDFAttribute>> entry : aggregateAO.getAggregations().entrySet())
		{
		    for (Entry<AggregateFunction, SDFAttribute> entryNeu : entry.getValue().entrySet())
			{
		    	SDFAttribute outAttribute = entryNeu.getValue();
		    	SDFAttribute attribute =entry.getKey().get(0);
		    
		    	AggregateFunction function = entryNeu.getKey();
		    	
		    	aggregateItemList.add(new AggregateItem(function.getName(), attribute, outAttribute));

			}
		  
		}
		
		
	
		if(aggregateItemList!=null){
			for (AggregateItem item : aggregateItemList) {

				String functionName = item.aggregateFunction.getName();
				List<SDFAttribute> inAttributes = item.inAttributes;
				SDFAttribute outAttribute = item.outAttribute;
				
				String outAttributeSourceName = outAttribute.getSourceName();
				String outAttributeAttributeName = outAttribute.getAttributeName();
				String outAttributeSDFDataTypeName = outAttribute.getDatatype().toString();
				
				cAggregateItemModelList.add(new CAggregateItemModel(functionName, inAttributes,outAttribute,outAttributeSourceName,outAttributeAttributeName,outAttributeSDFDataTypeName));
			
			}
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
		
		aggregateTIPO.addFrameworkImport(AggregateTIPO.class.getName());
		aggregateTIPO.addFrameworkImport(ITimeInterval.class.getName());
		aggregateTIPO.addFrameworkImport(IStreamObject.class.getName());
		aggregateTIPO.addFrameworkImport(AggregateItem.class.getName());
		aggregateTIPO.addFrameworkImport(IMetadataMergeFunction.class.getName());
		aggregateTIPO.addFrameworkImport(AggregateAO.class.getName());
		aggregateTIPO.addFrameworkImport(AggregateFunction.class.getName());
		aggregateTIPO.addFrameworkImport(RelationalAggregateFunctionBuilder.class.getName());
		aggregateTIPO.addFrameworkImport(IAggregateFunctionBuilder.class.getName());
		aggregateTIPO.addFrameworkImport(IAggregateFunction.class.getName());
		aggregateTIPO.addFrameworkImport(FESortedClonablePair.class.getName());
		aggregateTIPO.addFrameworkImport(RelationalGroupProcessor.class.getName());
		aggregateTIPO.addFrameworkImport(AggregateTISweepArea.class.getName());
		aggregateTIPO.addFrameworkImport(AggregateFunctionBuilderRegistry.class.getName());
		
	
		aggregateTIPO.addImport(java.util.Map.Entry.class.getCanonicalName());
		aggregateTIPO.addImport(java.util.Map.class.getName());
		
		
		return aggregateTIPO;
	}
	
	

}