/*
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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.TIMergeFunction;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.ProbabilisticDiscreteJoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TProbabilisticDiscreteJoinAOSetSARule extends AbstractTransformationRule<JoinTIPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>> {
	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public int getPriority() {
		return 11;
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void execute(final JoinTIPO joinPO, final TransformationConfiguration transformConfig) {
		final ProbabilisticDiscreteJoinTISweepArea[] areas = new ProbabilisticDiscreteJoinTISweepArea[2];

		final IDataMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic> dataMerge = new ProbabilisticMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic>(joinPO.getOutputSchema().size());
		IMetadataMergeFunction metadataMerge;
		if (transformConfig.getMetaTypes().size() > 1) {
			metadataMerge = new CombinedMergeFunction();
			((CombinedMergeFunction) metadataMerge).add(new TimeIntervalInlineMetadataMergeFunction());
		} else {
			metadataMerge = TIMergeFunction.getInstance();
		}
		final List<SDFAttribute> attributes = SchemaUtils.getDiscreteProbabilisticAttributes(joinPO.getPredicate().getAttributes());
		final SDFSchema leftSchema = joinPO.getSubscribedToSource(0).getSchema();
		final SDFSchema rightSchema = joinPO.getSubscribedToSource(1).getSchema();

		final List<SDFAttribute> leftAttributes = new ArrayList<SDFAttribute>(leftSchema.getAttributes());
		leftAttributes.retainAll(attributes);

		final List<SDFAttribute> rightAttributes = new ArrayList<SDFAttribute>(rightSchema.getAttributes());
		rightAttributes.retainAll(attributes);
		rightAttributes.removeAll(leftAttributes);

		final int[] rightProbabilisticAttributePos = SchemaUtils.getAttributePos(rightSchema, rightAttributes);
		final int[] leftProbabilisticAttributePos = SchemaUtils.getAttributePos(leftSchema, leftAttributes);

		for (int port = 0; port < 2; port++) {
			areas[port] = new ProbabilisticDiscreteJoinTISweepArea(rightProbabilisticAttributePos, leftProbabilisticAttributePos, dataMerge, metadataMerge);
		}

		joinPO.setAreas(areas);

	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(final JoinTIPO operator, final TransformationConfiguration transformConfig) {
		if ((transformConfig.getDataTypes().contains(SchemaUtils.DATATYPE)) && transformConfig.getMetaTypes().contains(IProbabilistic.class.getCanonicalName())) {
			if (operator.getAreas() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public String getName() {
		return "ProbabilisticDiscreteJoinPO set SweepArea";
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
	 */
	@Override
	public Class<? super JoinTIPO> getConditionClass() {
		return JoinTIPO.class;
	}

}
