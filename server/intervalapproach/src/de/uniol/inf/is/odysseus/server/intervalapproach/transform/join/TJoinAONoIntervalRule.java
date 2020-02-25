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
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.persistentqueries.DirectTransferArea;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TJoinAONoIntervalRule extends AbstractTransformationRule<JoinAO> {

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
			joinPO.setJoinPredicate(pred.clone());
		}
		joinPO.setCardinalities(joinAO.getCard());
		if (joinAO.getLeftSweepAreaName() == null){
			joinAO.setLeftSweepAreaName("HashJoinSA");
		}
		
		joinPO.setSweepAreaName(joinAO.getLeftSweepAreaName());		
		joinPO.setTransferFunction(new DirectTransferArea());
		

		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());

		defaultExecute(joinAO, joinPO, transformConfig, true, true);
		if (isCross) {
			joinPO.setName("Crossproduct");
		}

	}

	@Override
	public boolean isExecutable(JoinAO operator, TransformationConfiguration transformConfig) {
		if (operator instanceof LeftJoinAO){
			return false;
		}
		// Only for cases with no Interval
		for (int i=0; i<operator.getNumberOfInputs();i++){
			SDFSchema s = operator.getInputSchema(i);
			if (s.hasMetatype(ITimeInterval.class)){
				return false;
			}
		}
		return operator.isAllPhysicalInputSet();
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
