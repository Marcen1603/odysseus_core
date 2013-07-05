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
package de.uniol.inf.is.odysseus.probabilistic.transform.continuous;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.interval.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.persistentqueries.PersistentTransferArea;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ContinuousProbabilisticEquiJoinPO;
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

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(final JoinAO joinAO, final TransformationConfiguration transformConfig) {

		final IPredicate<?> pred = joinAO.getPredicate();

		final ContinuousProbabilisticEquiJoinPO joinPO = new ContinuousProbabilisticEquiJoinPO();
		joinPO.setJoinPredicate(pred == null ? new TruePredicate() : pred.clone());
		boolean windowFound = false;
		for (int port = 0; port < 2; port++) {
			if (!JoinTransformationHelper.checkLogicalPath(joinAO.getSubscribedToSource(port).getTarget())) {
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
		((CombinedMergeFunction) joinPO.getMetadataMerge()).add(new ProbabilisticMergeFunction());
		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());

		this.defaultExecute(joinAO, joinPO, transformConfig, true, true);

	}

	@Override
	public boolean isExecutable(final JoinAO operator, final TransformationConfiguration transformConfig) {
		if (((operator.getPredicate() != null) && (operator.isAllPhysicalInputSet()) && !(operator instanceof LeftJoinAO))) {
			if (!SchemaUtils.containsContinuousProbabilisticAttributes(operator.getPredicate().getAttributes())) {
				return false;
			}
			if (!transformConfig.getMetaTypes().contains(IProbabilistic.class.getCanonicalName())) {
				return false;
			}

			final String mepString = operator.getPredicate().toString();
			final SDFSchema leftInputSchema = operator.getInputSchema(0);
			final SDFSchema rightInputSchema = operator.getInputSchema(1);

			final SDFSchema inputSchema = SDFSchema.union(leftInputSchema, rightInputSchema);
			final IAttributeResolver attrRes = new DirectAttributeResolver(inputSchema);
			final SDFExpression expr = new SDFExpression(null, mepString, attrRes, MEP.getInstance());

			if (SchemaUtils.isEquiExpression(expr.getMEPExpression())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "JoinAO -> continuous probabilistic Equi Join";
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
