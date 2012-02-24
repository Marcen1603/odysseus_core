/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.interval.transform.join;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.LeftJoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TLeftJoinTIPOAddMetadataMergeRule extends AbstractTransformationRule<LeftJoinTIPO<?, ?>> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(LeftJoinTIPO<?, ?> joinPO, TransformationConfiguration transformConfig) {
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
	public boolean isExecutable(LeftJoinTIPO<?, ?> operator, TransformationConfiguration transformConfig) {
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
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return LeftJoinTIPO.class;
	}

}

