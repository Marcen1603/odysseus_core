package de.uniol.inf.is.odysseus.securitypunctuation.rules.join;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAJoinPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSAJoinPOInsertDataMergeRule extends AbstractTransformationRule<SAJoinPO<ITimeInterval, Tuple<ITimeInterval>>> {

	@Override
	public int getPriority() {	
		return 100;
	}

	@Override
	public void execute(SAJoinPO<ITimeInterval, Tuple<ITimeInterval>> joinPO, TransformationConfiguration transformConfig) {
		joinPO.setDataMerge(new RelationalMergeFunction<ITimeInterval>(joinPO.getOutputSchema().size()));
		update(joinPO);	
	}

	@Override
	public boolean isExecutable(SAJoinPO<ITimeInterval, Tuple<ITimeInterval>> operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getDataType().equals("relational")){
			if(operator.getDataMerge()==null){
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
		return"Insert DataMergeFunction SAJoinPO (Relational)";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}
	
	@Override
	public Class<? super SAJoinPO<?, ?>> getConditionClass() {	
		return SAJoinPO.class;
	}

}
