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
package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.EqualsPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.interval.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.persistentqueries.PersistentTransferArea;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticContinuousJoinPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TContinuousEquiJoinAORule extends AbstractTransformationRule<JoinAO> {
	@Override
	public int getPriority() {
		return 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(JoinAO joinAO,
			TransformationConfiguration transformConfig) {

		IPredicate<?> pred = joinAO.getPredicate();

		SDFSchema leftInputSchema = joinAO.getInputSchema(0);
		SDFSchema rightInputSchema = joinAO.getInputSchema(1);
		List<SDFAttribute> attributes = pred.getAttributes();
		SDFAttribute leftAttribute = null;
		SDFAttribute rightAttribute = null;

		if (attributes.size() == 2) {
			if (leftInputSchema.contains(attributes.get(0))) {
				leftAttribute = attributes.get(0);
			} else {
				leftAttribute = attributes.get(1);
			}
			if (rightInputSchema.contains(attributes.get(0))) {
				rightAttribute = attributes.get(0);
			} else {
				rightAttribute = attributes.get(1);
			}
			int[] leftJoinAttributePos = new int[] {};
			int[] rightJoinAttributePos = new int[] {};
			if (leftAttribute.getDatatype() instanceof SDFProbabilisticDatatype) {
				SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) leftAttribute
						.getDatatype();
				if (datatype.isContinuous()) {
					rightJoinAttributePos = new int[] { rightInputSchema
							.indexOf(rightAttribute) };
				}
			}
			if (rightAttribute.getDatatype() instanceof SDFProbabilisticDatatype) {
				SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) rightAttribute
						.getDatatype();
				if (datatype.isContinuous()) {
					leftJoinAttributePos = new int[] { leftInputSchema
							.indexOf(leftAttribute) };
				}
			}
			int[] leftViewAttributePos = new int[leftInputSchema
					.getAttributes().size()];
			int[] rightViewAttributePos = new int[rightInputSchema
					.getAttributes().size()];
			for (int i = 0; i < leftInputSchema.getAttributes().size(); i++) {
				if (i != leftJoinAttributePos[0]) {
					leftViewAttributePos[i] = i;
				}
			}
			for (int i = 0; i < rightInputSchema.getAttributes().size(); i++) {
				if (i != rightJoinAttributePos[0]) {
					rightViewAttributePos[i] = i;
				}
			}

			ProbabilisticContinuousJoinPO joinPO = new ProbabilisticContinuousJoinPO(
					leftViewAttributePos, leftJoinAttributePos,
					rightViewAttributePos, rightJoinAttributePos);
			joinPO.setJoinPredicate(pred == null ? new TruePredicate() : pred
					.clone());
			boolean windowFound = false;
			for (int port = 0; port < 2; port++) {
				if (!JoinTransformationHelper.checkLogicalPath(joinAO
						.getSubscribedToSource(port).getTarget())) {
					windowFound = true;
					break;
				}
			}

			if (!windowFound) {
				joinPO.setTransferFunction(new PersistentTransferArea());
			} else {
				joinPO.setTransferFunction(new TITransferArea());
			}

			joinPO.setMetadataMerge(new CombinedMergeFunction());
			joinPO.setCreationFunction(new DefaultTIDummyDataCreation());

			defaultExecute(joinAO, joinPO, transformConfig, true, true);
		}
		// ((CombinedMergeFunction) joinPO.getMetadataMerge())
		// .add(new ProbabilisticMergeFunction());
	}

	@Override
	public boolean isExecutable(JoinAO operator,
			TransformationConfiguration transformConfig) {
		if (operator.isAllPhysicalInputSet()
				&& !(operator instanceof LeftJoinAO)) {
			if (!(operator.getPredicate() instanceof EqualsPredicate)) {
				return false;
			}
			if (operator.getPredicate().getAttributes().size() != 2) {
				return false;
			}
			if (transformConfig.getMetaTypes().contains(
					IProbabilistic.class.getCanonicalName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "JoinTIPO add MetadataMerge (IProbabilistic)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}
}
