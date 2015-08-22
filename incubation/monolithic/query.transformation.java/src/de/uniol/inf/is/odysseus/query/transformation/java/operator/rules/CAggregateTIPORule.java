package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateJavaDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractAggregateTIPORule;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;

public class CAggregateTIPORule extends AbstractAggregateTIPORule{

	public CAggregateTIPORule() {
		super(CAggregateTIPORule.class.getName(), "Java");
	}
	
	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo aggregateTIPO = new CodeFragmentInfo();
		
		AggregateAO aggregateAO = (AggregateAO) operator;
		
		String operatorVariable = JavaTransformationInformation.getInstance().getVariable(operator);
		
		aggregateTIPO.addCodeFragmentInfo(CreateJavaDefaultCode.getCodeForSDFSchema(aggregateAO.getInputSchema(), operatorVariable+"Input"));
		
		aggregateTIPO.addCodeFragmentInfo(CreateJavaDefaultCode.getCodeForSDFSchema(aggregateAO.getOutputSchemaIntern(0), operatorVariable+"OutputSchemaIntern"));
		
		aggregateTIPO.addCodeFragmentInfo(CreateJavaDefaultCode.createCodeForSDFAttributeList(aggregateAO.getGroupingAttributes(), operatorVariable+"GroupingAttribute"));
		
	

		
		AggregateAO dummyAggregateAO = new AggregateAO();
		
		List<AggregateItem> aggregateItemList = new ArrayList<AggregateItem>();

		for (AggregateItem item : aggregateAO.getAggregationItems()) {

			String functionName = item.aggregateFunction.getName();
			List<SDFAttribute> inAttributes = item.inAttributes;
			SDFAttribute outAttribute = item.outAttribute;
			
			String outAttributeSourceName = outAttribute.getSourceName();
			String outAttributeAttributeName = outAttribute.getAttributeName();
			String outAttributeSDFDataTypeName = outAttribute.getDatatype().toString();
			
			
			/*
			System.out.println(function);
			new AggregateFunction(function);
			*/
			
			aggregateItemList.add(new AggregateItem(functionName, inAttributes, outAttribute));
			
			StringTemplate aggregateItemsTIPOTemplate = new StringTemplate("utils","aggregateItemList");
			aggregateItemsTIPOTemplate.getSt().add("operatorVariable", operatorVariable+"aggreateItemList");
			aggregateItemsTIPOTemplate.getSt().add("functionName", functionName);
			aggregateItemsTIPOTemplate.getSt().add("sdfAttributeListTest", item.inAttributes);
			aggregateItemsTIPOTemplate.getSt().add("outAttributeSourceName", outAttributeSourceName);
			aggregateItemsTIPOTemplate.getSt().add("outAttributeAttributeName", outAttributeAttributeName);
			aggregateItemsTIPOTemplate.getSt().add("outAttributeSDFDataTypeName", outAttributeSDFDataTypeName);
			
			

			aggregateTIPO.addCode(aggregateItemsTIPOTemplate.getSt().render());
			
		}
		
		dummyAggregateAO.setAggregationItems(aggregateItemList);
		dummyAggregateAO.getAggregations();
		
	
		
		
		
		boolean fastGrouping = aggregateAO.isFastGrouping();
		
		
		StringTemplate aggregateTIPOTemplate = new StringTemplate("operator","aggregateTIPO");
		aggregateTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		aggregateTIPOTemplate.getSt().add("fastGrouping", fastGrouping);
		
		
		aggregateTIPO.addCode(aggregateTIPOTemplate.getSt().render());
		
		aggregateTIPO.addImport(AggregateTIPO.class.getName());
		aggregateTIPO.addImport(ITimeInterval.class.getName());
		aggregateTIPO.addImport(IStreamObject.class.getName());
		aggregateTIPO.addImport(AggregateTIPO.class.getName());
		
		return aggregateTIPO;
	}
}
