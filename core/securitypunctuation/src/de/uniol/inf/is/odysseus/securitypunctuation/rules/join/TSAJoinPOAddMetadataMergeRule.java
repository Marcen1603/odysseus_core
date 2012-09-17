package de.uniol.inf.is.odysseus.securitypunctuation.rules.join;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.TIMergeFunction;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAJoinPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TSAJoinPOAddMetadataMergeRule extends AbstractTransformationRule<SAJoinPO<?, ?>> {

	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	public void execute(SAJoinPO joinPO, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().size()>1){
			((CombinedMergeFunction) joinPO.getMetadataMerge()).add(new TimeIntervalInlineMetadataMergeFunction());
		}else{
			joinPO.setMetadataMerge(TIMergeFunction.getInstance());
		}
		/*
		 * # no update, because otherwise # other rules may overwrite this rule
		 * # example: rule with priority 5 setting the areas has been #
		 * processed, update causes rule engine to search for other # rules
		 * applicable for the updated object. The rule with # priority 5 cannot
		 * be processed because of no-loop term, however # other rules with
		 * lower priority could be used of the updated # objects fulfills the
		 * when clause. However, these lower priority # rules should not be used
		 * because of the high priority rule # # do not use retract also,
		 * because # other fields of the object should still be modified
		 */
	}

	@Override
	public boolean isExecutable(SAJoinPO<?, ?> operator, TransformationConfiguration transformConfig) {
		if (transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (operator.getMetadataMerge() != null) {
				if (operator.getMetadataMerge() instanceof CombinedMergeFunction) {
					if (transformConfig.getOption("isSecurityAware") != null) {
						if (transformConfig.getOption("isSecurityAware")) {
							return true;
						}
					}
				}
			}

		}
		return false;
	}

	@Override
	public String getName() {
		return "SAJoinPO add MetadataMerge (ITimeInterval)";
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