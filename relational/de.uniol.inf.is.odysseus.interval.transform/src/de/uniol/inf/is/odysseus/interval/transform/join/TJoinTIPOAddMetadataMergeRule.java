package de.uniol.inf.is.odysseus.interval.transform.join;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.metadata.base.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TJoinTIPOAddMetadataMergeRule extends AbstractTransformationRule<JoinTIPO<?, ?>> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(JoinTIPO<?, ?> joinPO, TransformationConfiguration transformConfig) {
		((CombinedMergeFunction) joinPO.getMetadataMerge()).add(new TimeIntervalInlineMetadataMergeFunction());
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
	public boolean isExecutable(JoinTIPO<?, ?> operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (transformConfig.getMetaTypes().size() > 1) {
				if (operator.getMetadataMerge() != null) {
					if (operator.getMetadataMerge() instanceof CombinedMergeFunction) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "JoinTIPO add MetadataMerge (ITimeInterval)";
	}

}
