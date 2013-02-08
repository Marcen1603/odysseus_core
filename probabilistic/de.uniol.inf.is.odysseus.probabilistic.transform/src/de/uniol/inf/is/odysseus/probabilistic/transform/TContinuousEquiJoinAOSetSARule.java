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
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ContinuousProbabilisticEquiJoinPO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ContinuousProbabilisticEquiJoinTISweepArea;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TContinuousEquiJoinAOSetSARule
		extends
		AbstractTransformationRule<ContinuousProbabilisticEquiJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(ContinuousProbabilisticEquiJoinPO joinPO,
			TransformationConfiguration transformConfig) {
		ContinuousProbabilisticEquiJoinTISweepArea[] areas = new ContinuousProbabilisticEquiJoinTISweepArea[2];

		for (int port = 0; port < 2; port++) {
			int otherPort = port ^ 1;
			if (JoinTransformationHelper.checkPhysicalPath(joinPO
					.getSubscribedToSource(port).getTarget())) {
				Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs = new TreeSet<Pair<SDFAttribute, SDFAttribute>>();

				if (JoinTransformationHelper.checkPredicate(joinPO
						.getPredicate(), neededAttrs, joinPO
						.getSubscribedToSource(port).getSchema(), joinPO
						.getSubscribedToSource(otherPort).getSchema())) {

					SDFSchema schema = joinPO.getSubscribedToSource(port)
							.getSchema();
					List<SDFAttribute> joinAttributes = new ArrayList<SDFAttribute>();

					for (Pair<SDFAttribute, SDFAttribute> pair : neededAttrs) {
						if (TransformUtil
								.isContinuousProbabilisticAttribute(pair
										.getE2())) {
							joinAttributes.add(pair.getE1());
						}
					}
					int[] joinPos = TransformUtil.getAttributePos(schema,
							joinAttributes);

					List<SDFAttribute> viewAttributes = new ArrayList<SDFAttribute>(
							schema.getAttributes());
					viewAttributes.removeAll(joinAttributes);
					int[] viewPos = TransformUtil.getAttributePos(schema,
							viewAttributes);
					areas[port] = new ContinuousProbabilisticEquiJoinTISweepArea(
							joinPos, viewPos);
					joinPO.setBetas(areas[port].getBetas(), port);
				}
			}
		}

		joinPO.setAreas(areas);

	}

	@Override
	public boolean isExecutable(ContinuousProbabilisticEquiJoinPO operator,
			TransformationConfiguration transformConfig) {
		if ((transformConfig.getDataTypes().contains(TransformUtil.DATATYPE))
				&& transformConfig.getMetaTypes().contains(
						IProbabilistic.class.getCanonicalName())) {
			if (operator.getAreas() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ProbabilisticContinuousJoinPO set SweepArea";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super ContinuousProbabilisticEquiJoinPO> getConditionClass() {
		return ContinuousProbabilisticEquiJoinPO.class;
	}

}
