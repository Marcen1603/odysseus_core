package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TJoinTIPOInsertDataMergeRule extends AbstractTransformationRule<JoinTIPO<?,?>> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(JoinTIPO<?,?> joinPO, TransformationConfiguration transformConfig) {
		joinPO.setDataMerge(new RelationalMergeFunction(joinPO.getOutputSchema().size()));
		update(joinPO);		
	}

	@Override
	public boolean isExecutable(JoinTIPO<?,?> operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getDataType().equals("relational")){
			if(operator.getDataMerge()==null){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return"Insert DataMergeFunction (Relational)";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

}
