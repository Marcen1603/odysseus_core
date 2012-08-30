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
package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferedFilterAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.BufferedFilterPO;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalLeftMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBufferedFilterAORule extends AbstractTransformationRule<BufferedFilterAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(BufferedFilterAO operator, TransformationConfiguration config) {
		IDataMergeFunction<?> dataMerge = new RelationalLeftMergeFunction<ITimeInterval>(operator.getInputSchema(0), operator.getInputSchema(1), operator.getOutputSchema());
		// IMetadataMergeFunction<?> metaDataMerge = new UseLeftInputMetadata();
		CombinedMergeFunction metaDataMerge = new CombinedMergeFunction();
		metaDataMerge.add(new TimeIntervalInlineMetadataMergeFunction());

		BufferedFilterPO po = new BufferedFilterPO(operator.getPredicate(), operator.getBufferTime(), operator.getDeliverTime(), dataMerge, metaDataMerge);
		defaultExecute(operator, po, config, true, true);		
	}

	@Override
	public boolean isExecutable(BufferedFilterAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "BufferedFilterAO --> BufferedFilterPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super BufferedFilterAO> getConditionClass() {
		return BufferedFilterAO.class;
	}

}
