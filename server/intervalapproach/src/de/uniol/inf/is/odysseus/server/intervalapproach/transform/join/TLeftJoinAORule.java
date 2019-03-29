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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.persistentqueries.DirectTransferArea;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalLeftMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.server.intervalapproach.LeftJoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.AbstractIntervalTransformationRule;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TLeftJoinAORule extends AbstractIntervalTransformationRule<LeftJoinAO> {

	@Override
	public int getPriority() {
		// must be higher than TJoinAORule, since
		// LeftJoinAO extends JoinAO
		return 5;

	}

	@Override
	public void execute(LeftJoinAO joinAO, TransformationConfiguration transformConfig) throws RuleException {

		List<String> leftMeta = joinAO.getInputSchema(0).getMetaAttributeNames();
		List<String> rightMeta = joinAO.getInputSchema(1).getMetaAttributeNames();

		IMetadataMergeFunction<?> metaDataMerge = MetadataRegistry.getMergeFunction(leftMeta, rightMeta);

		if (metaDataMerge == null) {
			throw new RuntimeException(
					"Cannot find a meta data merge function for left=" + leftMeta + " and right=" + rightMeta);
		}

		LeftJoinTIPO joinPO = new LeftJoinTIPO(metaDataMerge);
		boolean isCross = false;
		IPredicate pred = joinAO.getPredicate();
		if (pred == null) {
			joinPO.setJoinPredicate(TruePredicate.getInstance());
			isCross = true;
		} else {
			joinPO.setJoinPredicate(pred.clone());
		}
		joinPO.setCardinalities(joinAO.getCard());
		joinPO.setSweepAreaName(joinAO.getSweepAreaName());

		if (joinAO.isAssureOrder() == null || joinAO.isAssureOrder()) {
			joinPO.setTransferFunction(new TITransferArea());
		} else {
			joinPO.setTransferFunction(new DirectTransferArea());
		}
		
		int leftSize = joinAO.getInputSchema(0).size();
		int rightSize = joinAO.getInputSchema(1).size();
		joinPO.setDataMerge(new RelationalLeftMergeFunction(leftSize, rightSize, leftSize + rightSize));

		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());

		defaultExecute(joinAO, joinPO, transformConfig, true, true);
		if (isCross) {
			joinPO.setName("Crossproduct");
		}

	}

	@Override
	public String getName() {
		return "JoinAO -> JoinTIPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super LeftJoinAO> getConditionClass() {
		return LeftJoinAO.class;
	}

}
