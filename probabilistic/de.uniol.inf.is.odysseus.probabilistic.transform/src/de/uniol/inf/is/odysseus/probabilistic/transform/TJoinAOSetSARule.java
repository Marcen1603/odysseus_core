package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.interval.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.persistentqueries.HashJoinSweepArea;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticContinuousJoinPO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticContinuousJoinTISweepArea;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TJoinAOSetSARule extends
		AbstractTransformationRule<ProbabilisticContinuousJoinPO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(ProbabilisticContinuousJoinPO joinPO,
			TransformationConfiguration transformConfig) {
		ITimeIntervalSweepArea[] areas = new ITimeIntervalSweepArea[2];

		areas[0] = new ProbabilisticContinuousJoinTISweepArea(
				joinPO.getLeftJoinAttributePos(),
				joinPO.getLeftViewAttributePos());
		areas[1] = new ProbabilisticContinuousJoinTISweepArea(
				joinPO.getRightJoinAttributePos(),
				joinPO.getRightViewAttributePos());

		for (int port = 0; port < 2; port++) {
			int otherPort = port ^ 1;
			if (JoinTransformationHelper.checkPhysicalPath(joinPO
					.getSubscribedToSource(port).getTarget())) {
				Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs = new TreeSet<Pair<SDFAttribute, SDFAttribute>>();

				if (JoinTransformationHelper.checkPredicate(joinPO
						.getPredicate(), neededAttrs, joinPO
						.getSubscribedToSource(port).getSchema(), joinPO
						.getSubscribedToSource(otherPort).getSchema())) {

					List<Pair<SDFAttribute, SDFAttribute>> neededAttrsList = new ArrayList<Pair<SDFAttribute, SDFAttribute>>();
					for (Pair<SDFAttribute, SDFAttribute> pair : neededAttrs) {
						neededAttrsList.add(pair);
					}

					Pair<int[], int[]> restrictLists = JoinTransformationHelper
							.createRestrictLists(joinPO, neededAttrsList, port);
					areas[port] = new HashJoinSweepArea(restrictLists.getE1(),
							restrictLists.getE2());
				}
			}
		}

		joinPO.setAreas(areas);
	}

	@Override
	public boolean isExecutable(ProbabilisticContinuousJoinPO operator,
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
	public Class<? super ProbabilisticContinuousJoinPO> getConditionClass() {
		return ProbabilisticContinuousJoinPO.class;
	}

}
