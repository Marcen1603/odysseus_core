/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

package de.uniol.inf.is.odysseus.mining.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.mining.logicaloperator.FrequentItemsetAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.FrequentItemsetAprioriPO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.FrequentItemsetFPGrowthPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen
 * Created at: 14.05.2012
 */
public class TFrequentItemsetAORule extends AbstractTransformationRule<FrequentItemsetAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(FrequentItemsetAO operator, TransformationConfiguration config) {
		FrequentItemsetFPGrowthPO<ITimeInterval> fpg = new FrequentItemsetFPGrowthPO<ITimeInterval>(operator.getMinSupport(), operator.getMaxTransactions());
		AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> po = fpg; 
		CombinedMergeFunction<ITimeInterval> metaDataMerge = new CombinedMergeFunction<ITimeInterval>();
		metaDataMerge.add(new TimeIntervalInlineMetadataMergeFunction());
		fpg.setMetadataMerge(metaDataMerge);		
		if(operator.getAlgorithm().equalsIgnoreCase("APRIORI")){
			po = new FrequentItemsetAprioriPO<ITimeInterval>(operator.getMinSupport());			
		}		
		
		po.setOutputSchema(operator.getOutputSchema(0), 0);
		po.setOutputSchema(operator.getOutputSchema(1), 1);
		replace(operator, po, config);
		retract(operator);
	}

	@Override
	public boolean isExecutable(FrequentItemsetAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "FrequentItemsetAO -> FrequentItemsetPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
