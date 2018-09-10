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

import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.MiningAlgorithmRegistry;
import de.uniol.inf.is.odysseus.mining.frequentitem.IFrequentPatternMiner;
import de.uniol.inf.is.odysseus.mining.logicaloperator.FrequentPatternMiningAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.FrequentPatternMiningPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen Created at: 14.05.2012
 */
public class TFrequentPatternMiningAORule extends AbstractTransformationRule<FrequentPatternMiningAO> {

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(FrequentPatternMiningAO operator, TransformationConfiguration config) throws RuleException {

		IMetadataMergeFunction<?> metaDataMerge = MetadataRegistry
				.getMergeFunction(operator.getInputSchema(0).getMetaAttributeNames());
	

		IFrequentPatternMiner<ITimeInterval> fpm = MiningAlgorithmRegistry.getInstance().getFrequentPatternMiner(operator.getLearner());
		fpm.setOptions(operator.getOptionsMap());
		fpm.init(operator.getInputSchema(0), operator.getMinSupport());
		ITimeIntervalSweepArea sa;
		try {
			sa = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuleException(e);
		}
		// TODO: ITimeIntervalSweepArea sa = new DefaultTISweepArea(new FastLinkedList());
		FrequentPatternMiningPO po = new FrequentPatternMiningPO(fpm, operator.getMaxTransactions(), metaDataMerge, sa);
		defaultExecute(operator, po, config, false, false);
		po.setOutputSchema(operator.getOutputSchema(0), 0);
		po.setOutputSchema(operator.getOutputSchema(1), 1);
		retract(operator);
		insert(po);

	}

	@Override
	public boolean isExecutable(FrequentPatternMiningAO operator, TransformationConfiguration config) {
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
