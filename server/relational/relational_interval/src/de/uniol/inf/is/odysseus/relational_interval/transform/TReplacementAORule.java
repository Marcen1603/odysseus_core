package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.ReplacementAO;
import de.uniol.inf.is.odysseus.relational_interval.replacement.IReplacement;
import de.uniol.inf.is.odysseus.relational_interval.replacement.RelationalReplacementPO;
import de.uniol.inf.is.odysseus.relational_interval.replacement.ReplacementRegistry;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TReplacementAORule extends AbstractTransformationRule<ReplacementAO> {

	@Override
	public void execute(ReplacementAO operator,
			TransformationConfiguration config) throws RuleException {
		SDFSchema schema = operator.getOutputSchema();
		int timestampAttributePos = operator.getTimestampAttribute()!=null?schema.indexOf(operator.getTimestampAttribute()):-1;
		int valueAttributePos = operator.getValueAttribute()!=null?schema.indexOf(operator.getValueAttribute()): -1;
		int qualityAttributePos = operator.getQualityAttribute()!=null?schema.indexOf(operator.getQualityAttribute()): -1;
		@SuppressWarnings("rawtypes")
		IReplacement replacement = ReplacementRegistry.getReplacement(operator.getReplacementMethod());
		if (replacement == null){
			// TODO: better exception
			throw new RuntimeException("Replacement method "+operator.getReplacementMethod()+" not found!");
		}
		
		@SuppressWarnings("unchecked")
		RelationalReplacementPO po = new RelationalReplacementPO(operator.getInterval(), timestampAttributePos, valueAttributePos, qualityAttributePos, replacement);
		
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(ReplacementAO operator,
			TransformationConfiguration config) {
		if(operator.getInputSchema(0).getType() == Tuple.class && config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
			if(operator.isAllPhysicalInputSet()){
					return true;				
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ReplacementAO --> ReplacementPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
