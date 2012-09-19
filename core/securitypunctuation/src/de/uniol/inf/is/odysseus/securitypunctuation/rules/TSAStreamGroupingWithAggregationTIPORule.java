package de.uniol.inf.is.odysseus.securitypunctuation.rules;

import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAAggregateTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSAStreamGroupingWithAggregationTIPORule extends AbstractTransformationRule<AggregateAO> {

	@Override
	public int getPriority() {
		return 100;
	}
	
	@Override
	public void execute(AggregateAO aggregateAO, TransformationConfiguration transformConfig) {
		SAAggregateTIPO<ITimeInterval, MetaAttributeContainer<ITimeInterval>, MetaAttributeContainer<ITimeInterval>> po = new SAAggregateTIPO<ITimeInterval, MetaAttributeContainer<ITimeInterval>, MetaAttributeContainer<ITimeInterval>>(aggregateAO.getInputSchema(), aggregateAO.getOutputSchemaIntern(0) , aggregateAO.getGroupingAttributes(),
				aggregateAO.getAggregations());
		po.setDumpAtValueCount(aggregateAO.getDumpAtValueCount());
		po.setMetadataMerge(new CombinedMergeFunction<ITimeInterval>());
		// ACHTUNG: Die Zeit-Metadaten werden manuell in der Aggregation gesetzt!!
		//((CombinedMergeFunction) po.getMetadataMerge()).add(new TimeIntervalInlineMetadataMergeFunction());
		defaultExecute(aggregateAO, po, transformConfig, true, true);
		
	}

	@Override
	public boolean isExecutable(AggregateAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				if (transformConfig.getOption("isSecurityAware") != null) {
					if (transformConfig.getOption("isSecurityAware")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AggregateAO -> SAAggregateTIPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super AggregateAO> getConditionClass() {	
		return AggregateAO.class;
	}
}
