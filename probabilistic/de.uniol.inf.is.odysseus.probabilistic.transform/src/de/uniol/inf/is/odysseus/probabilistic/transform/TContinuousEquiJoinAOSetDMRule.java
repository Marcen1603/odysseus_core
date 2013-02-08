package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.interval.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ContinuousProbabilisticEquiJoinMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ContinuousProbabilisticEquiJoinPO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ContinuousProbabilisticEquiJoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class TContinuousEquiJoinAOSetDMRule
		extends
		AbstractTransformationRule<ContinuousProbabilisticEquiJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(
			ContinuousProbabilisticEquiJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> operator,
			TransformationConfiguration config) {

		int[][] joinAttributePos = new int[2][];

		for (int port = 0; port < 2; port++) {
			int otherPort = port ^ 1;
			if (JoinTransformationHelper.checkPhysicalPath(operator
					.getSubscribedToSource(port).getTarget())) {
				Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs = new TreeSet<Pair<SDFAttribute, SDFAttribute>>();

				if (JoinTransformationHelper.checkPredicate(operator
						.getPredicate(), neededAttrs, operator
						.getSubscribedToSource(port).getSchema(), operator
						.getSubscribedToSource(otherPort).getSchema())) {

					SDFSchema schema = operator.getSubscribedToSource(port)
							.getSchema();
					List<SDFAttribute> joinAttributes = new ArrayList<SDFAttribute>();

					for (Pair<SDFAttribute, SDFAttribute> pair : neededAttrs) {
						if (TransformUtil
								.isContinuousProbabilisticAttribute(pair
										.getE2())) {
							joinAttributes.add(pair.getE1());
						}
					}
					joinAttributePos[port] = TransformUtil.getAttributePos(
							schema, joinAttributes);

					List<SDFAttribute> viewAttributes = new ArrayList<SDFAttribute>(
							schema.getAttributes());
					viewAttributes.removeAll(joinAttributes);
					int[] viewPos = TransformUtil.getAttributePos(schema,
							viewAttributes);
				}
			}
		}

//		ContinuousProbabilisticEquiJoinMergeFunction<ITimeIntervalProbabilistic> mergeFunction = new ContinuousProbabilisticEquiJoinMergeFunction<ITimeIntervalProbabilistic>(
//				operator.getOutputSchema().size(), joinAttributePos);
//
//		for (int port = 0; port < 2; port++) {
//			mergeFunction.setBetas(operator.getBetas(port), port);
//			mergeFunction.setSigmas(operator.getSigmas(port), port);
//		}
//		operator.setDataMerge(mergeFunction);
		update(operator);
	}

	@Override
	public boolean isExecutable(
			ContinuousProbabilisticEquiJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> operator,
			TransformationConfiguration config) {
		if (config.getDataTypes().contains(TransformUtil.DATATYPE)) {
			if (operator.getDataMerge() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Insert DataMergeFunction ProbabilisticContinuousJoinPO (Probabilistic)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super ContinuousProbabilisticEquiJoinPO<?, ?>> getConditionClass() {
		return ContinuousProbabilisticEquiJoinPO.class;
	}

}
