/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining.transform;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.LatencyMergeFunction;
import de.uniol.inf.is.odysseus.mining.physicaloperator.FrequentItemsetFPGrowthPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Dennis Geesen
 *
 */
public class TFrequentItemsetLatencyRule  extends AbstractTransformationRule<FrequentItemsetFPGrowthPO<?>> {

	@Override
	public int getPriority() {
		return 10;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(FrequentItemsetFPGrowthPO<?> fp, TransformationConfiguration config) throws RuleException {
		((CombinedMergeFunction)fp.getMetadataMerge()).add(new LatencyMergeFunction());		
	}

	@Override
	public boolean isExecutable(FrequentItemsetFPGrowthPO<?> fp, TransformationConfiguration config) {
		if(fp.getMetadataMerge() instanceof CombinedMergeFunction){
			if(config.getMetaTypes().contains(ILatency.class.getCanonicalName())){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return  "FrequentItemsetFPGrowthPO add MetadataMerge (ILatency)";
	}
	
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}
	
	@Override
	public Class<? super FrequentItemsetFPGrowthPO<?>> getConditionClass() {	
		return FrequentItemsetFPGrowthPO.class;
	}

}
