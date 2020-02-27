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
package de.uniol.inf.is.odysseus.server.intervalapproach.transform.join;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.persistentqueries.DirectTransferArea;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.AbstractIntervalTransformationRule;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TJoinAORule extends AbstractIntervalTransformationRule<JoinAO> {

	@Override
	public void execute(JoinAO joinAO, TransformationConfiguration transformConfig) throws RuleException {

		List<String> leftMeta = joinAO.getInputSchema(0).getMetaAttributeNames();
		List<String> rightMeta = joinAO.getInputSchema(1).getMetaAttributeNames();

		IMetadataMergeFunction<?> metaDataMerge = MetadataRegistry.getMergeFunction(leftMeta, rightMeta);

		if (metaDataMerge == null) {
			throw new RuntimeException(
					"Cannot find a meta data merge function for left=" + leftMeta + " and right=" + rightMeta);
		}

		JoinTIPO joinPO = new JoinTIPO(metaDataMerge);
		boolean isCross = false;
		IPredicate pred = joinAO.getPredicate();
		if (pred == null) {
			joinPO.setJoinPredicate(TruePredicate.getInstance());
			isCross = true;
		} else {
			setJoinPredicate(joinPO, joinAO);
		}
		joinPO.setCardinalities(joinAO.getCard());
		setSweepArea(joinPO, joinAO);

		if (joinAO.isAssureOrder() == null || joinAO.isAssureOrder()) {
			joinPO.setTransferFunction(new TITransferArea());
		} else {
			joinPO.setTransferFunction(new DirectTransferArea());
		}
		
		if (joinAO.getInputSchema(0).isInOrder() && joinAO.getInputSchema(1).isInOrder()) {
			joinPO.setOutOfOrder(false);
		}else {
			joinPO.setOutOfOrder(true);
		}

		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());
		
		// For the internal element window approach
		joinPO.setElementSizes(joinAO.getElementSizePort1(), joinAO.getElementSizePort2());
		
		final List<SDFAttribute> groupingAttributesPort0 = joinAO.getGroupingAttributesPort0();
		final int[] groupingAttributesIndicesPort0 = new int[groupingAttributesPort0.size()];
		for (int i = 0; i < groupingAttributesPort0.size(); ++i) {
			groupingAttributesIndicesPort0[i] = joinAO.getInputSchema(0).indexOf(groupingAttributesPort0.get(i));
		}
		
		final List<SDFAttribute> groupingAttributesPort1 = joinAO.getGroupingAttributesPort1();
		final int[] groupingAttributesIndicesPort1 = new int[groupingAttributesPort1.size()];
		for (int i = 0; i < groupingAttributesPort1.size(); ++i) {
			groupingAttributesIndicesPort1[i] = joinAO.getInputSchema(1).indexOf(groupingAttributesPort1.get(i));
		}
		
		joinPO.setGroupingIndices(groupingAttributesIndicesPort0, groupingAttributesIndicesPort1);
		joinPO.setKeepEndTimestamp(joinAO.keepEndTimestamp());
		
		joinPO.setOptions(joinAO.getOptionsMap());

		defaultExecute(joinAO, joinPO, transformConfig, true, true);
		if (isCross && !joinAO.isNameSet()) {
			joinPO.setName("Crossproduct");
		}
	}
	
	protected void setSweepArea(JoinTIPO joinPO, JoinAO joinAO) {
		joinPO.setSweepAreaName(joinAO.getLeftSweepAreaName(), joinAO.getRightSweepAreaName());
	}

	protected void setJoinPredicate(JoinTIPO joinPO, JoinAO joinAO) {
		joinPO.setJoinPredicate(joinAO.getPredicate().clone());
	}

	@Override
	public boolean isExecutable(JoinAO operator, TransformationConfiguration transformConfig) {
		if (super.isExecutable(operator, transformConfig)) {
			return !(operator instanceof LeftJoinAO);
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}


}
