/**
 * Copyright 2013 The Odysseus Team
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

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.DefaultProbabilisticTIDummyDataCreation;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TProbabilisticContinuousEquiJoinAORule extends
		AbstractTransformationRule<JoinAO> {
	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public final int getPriority() {
		return TransformationConstants.PRIORITY;
	}

	/*
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 * 
	 * 
	 * FIXME Implement Transformation rule to insert Regression and Sample
	 * operator (CK-20130720)
	 */
	@Override
	public final void execute(final JoinAO operator,
			final TransformationConfiguration config) throws RuleException {
		Objects.requireNonNull(operator);
		Objects.requireNonNull(config);
		
		IMetadataMergeFunction<?> metaDataMerge = MetadataRegistry
				.getMergeFunction(operator.getInputSchema(0).getMetaAttributeNames(),
						operator.getInputSchema(1).getMetaAttributeNames());
		final JoinTIPO joinPO = new JoinTIPO(metaDataMerge);

		boolean isCross = false;
		final IPredicate pred = operator.getPredicate();
		if (pred == null) {
			joinPO.setJoinPredicate(TruePredicate.getInstance());
			isCross = true;
		} else {
			joinPO.setJoinPredicate(pred.clone());
		}
		joinPO.setTransferFunction(new TITransferArea());
		joinPO.setCreationFunction(new DefaultProbabilisticTIDummyDataCreation());

		this.defaultExecute(operator, joinPO, config, true, true);
		if (isCross) {
			joinPO.setName("Crossproduct");
		}
	}

	/*
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public final boolean isExecutable(final JoinAO operator,
			final TransformationConfiguration transformConfig) {
		Objects.requireNonNull(operator);
		Objects.requireNonNull(operator.getInputSchema(0));
		Objects.requireNonNull(transformConfig);
		final IPredicate<?> predicate = operator.getPredicate();
		if (predicate != null) {
			if ((operator.getInputSchema(0).getType() == ProbabilisticTuple.class)
					|| (operator.getInputSchema(1).getType() == ProbabilisticTuple.class)) {
				if (operator.isAllPhysicalInputSet()
						&& !(operator instanceof LeftJoinAO)) {

					// if
					// (!SchemaUtils.containsContinuousProbabilisticAttributes(operator.getPredicate().getAttributes()))
					// {
					// return false;
					// }
					final SDFExpression expr = this.getExpression(operator);

					if (PredicateUtils
							.isEquiExpression(expr.getMEPExpression())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public final String getName() {
		return "JoinAO -> Continuous probabilistic Equi-Join";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public final IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	/*
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
	 */
	@Override
	public final Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}

	/**
	 * 
	 * @param operator
	 * @return
	 */
	private SDFExpression getExpression(final ILogicalOperator operator) {
		
		final String mepString; 
		if (operator instanceof IHasPredicate) {
			mepString = ((IHasPredicate)operator).getPredicate().toString();
		}else{
			mepString = "";
		}
		final SDFSchema leftInputSchema = operator.getInputSchema(0);
		final SDFSchema rightInputSchema = operator.getInputSchema(1);

		final SDFSchema inputSchema = SDFSchema.union(leftInputSchema,
				rightInputSchema);
		final IAttributeResolver attrRes = new DirectAttributeResolver(
				inputSchema);
		final SDFExpression expr = new SDFExpression(null, mepString, attrRes,
				MEP.getInstance(),
				AggregateFunctionBuilderRegistry.getAggregatePattern());
		return expr;
	}

}
