/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.latency.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferedFilterAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.BufferedFilterPO;
import de.uniol.inf.is.odysseus.latency.LatencyMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen
 * Created at: 29.05.2012
 */
public class TBufferedFilterLatencyRule extends AbstractTransformationRule<BufferedFilterPO<?,?>> {

	@Override
	public int getPriority() {
		return 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(BufferedFilterPO<?,?> bf, TransformationConfiguration config) {
		((CombinedMergeFunction)bf.getMetadataMerge()).add(new LatencyMergeFunction());		
	}

	@Override
	public boolean isExecutable(BufferedFilterPO<?,?> bf, TransformationConfiguration config) {
		if(bf.getMetadataMerge() instanceof CombinedMergeFunction){
			if(config.getMetaTypes().contains(ILatency.class.getCanonicalName())){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return  "BufferedFilter add MetadataMerge (ILatency)";
	}
	
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return BufferedFilterAO.class;
	}

}
