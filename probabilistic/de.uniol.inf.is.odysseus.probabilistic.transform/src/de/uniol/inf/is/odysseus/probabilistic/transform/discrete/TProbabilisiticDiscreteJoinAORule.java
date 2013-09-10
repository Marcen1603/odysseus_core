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
package de.uniol.inf.is.odysseus.probabilistic.transform.discrete;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.interval.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.persistentqueries.PersistentTransferArea;
import de.uniol.inf.is.odysseus.probabilistic.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.ProbabilisticDiscreteJoinTIPO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.DefaultProbabilisticTIDummyDataCreation;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class TProbabilisiticDiscreteJoinAORule extends AbstractTransformationRule<JoinAO> {
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
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public final void execute(final JoinAO operator, final TransformationConfiguration config) {
		final ProbabilisticDiscreteJoinTIPO joinPO = new ProbabilisticDiscreteJoinTIPO();
		final IPredicate<?> pred = operator.getPredicate();
		joinPO.setJoinPredicate(pred.clone());
	
		// see TJoinAORule!
		// if in both input paths there is no window, we
		// use a persistent sweep area
		// check the paths
//		boolean windowFound = false;
//		for (int port = 0; port < 2; port++) {
//			if (!JoinTransformationHelper.checkLogicalPath(operator.getSubscribedToSource(port).getTarget())) {
//				windowFound = true;
//				break;
//			}
//		}
//
//		if (!windowFound) {
//			joinPO.setTransferFunction(new PersistentTransferArea());
//		} else {
//			// otherwise we use a LeftJoinTISweepArea
			joinPO.setTransferFunction(new TITransferArea());
//		}

		joinPO.setMetadataMerge(new CombinedMergeFunction());
		joinPO.setCreationFunction(new DefaultProbabilisticTIDummyDataCreation());

		this.defaultExecute(operator, joinPO, config, true, true);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang .Object, java.lang.Object)
	 */
	@Override
	public final boolean isExecutable(final JoinAO operator, final TransformationConfiguration config) {
		final IPredicate<?> predicate = operator.getPredicate();
		if (predicate != null) {
			if (config.getDataTypes().contains(SchemaUtils.DATATYPE)) {
				if (operator.isAllPhysicalInputSet() && !(operator instanceof LeftJoinAO)) {
					final Set<SDFAttribute> attributes = PredicateUtils.getAttributes(predicate);
					if (SchemaUtils.containsDiscreteProbabilisticAttributes(attributes)) {
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
		return "JoinAO -> discrete probabilistic Join";
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
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
	 */
	@Override
	public final Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}

}
