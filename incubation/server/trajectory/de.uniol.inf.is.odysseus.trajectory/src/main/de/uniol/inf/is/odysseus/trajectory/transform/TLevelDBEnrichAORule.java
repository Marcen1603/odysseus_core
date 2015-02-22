/*
 * Copyright 2015 Marcus Behrendt
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
 *
**/

package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.UseLeftInputMetadata;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.LevelDBEnrichAO;
import de.uniol.inf.is.odysseus.trajectory.physicaloperator.LevelDBEnrichPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This Rule creates a <tt>LevelDBEnrichPO</tt> for a <tt>LevelDBEnrichAO</tt>.
 * @author marcus
 *
 */
public class TLevelDBEnrichAORule extends AbstractTransformationRule<LevelDBEnrichAO> {

	@Override
	public void execute(final LevelDBEnrichAO logical,
			final TransformationConfiguration config) throws RuleException {
		
		final IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMergeFunction = new RelationalMergeFunction<ITimeInterval>(
				logical.getOutputSchema().size() + 1);

		final IMetadataMergeFunction<ITimeInterval> metaMerge = new UseLeftInputMetadata<>();

		final int[] uniqueKeys = logical.getUniqueKeysAsArray();

		final LevelDBEnrichPO<ITimeInterval> physical = new LevelDBEnrichPO<ITimeInterval>(
				null, dataMergeFunction, metaMerge, uniqueKeys, 
				logical.getLevelDBPath(), 
				logical.getInputSchema().findAttributeIndex(logical.getIn().getAttributeName()),
				logical.getInputSchema().findAttributeIndex(logical.getOut().getAttributeName()));

		physical.setOutputSchema(logical.getOutputSchema());
		this.replace(logical, physical, config);
		this.retract(logical);
	}

	@Override
	public boolean isExecutable(final LevelDBEnrichAO operator,
			final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}
	
	@Override
	public String getName() {
		return "LevelDBEnrichAO -> LevelDBEnrichPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
