package de.uniol.inf.is.odysseus.securitypunctuation.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.persistentqueries.DirectTransferArea;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SAJoinAO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAJoinPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.AbstractIntervalTransformationRule;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TSAJoinAORule extends AbstractIntervalTransformationRule<SAJoinAO> {
	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(SAJoinAO SAjoinAO, TransformationConfiguration transformConfig) throws RuleException {

		List<String> leftMeta = SAjoinAO.getInputSchema(0).getMetaAttributeNames();
		List<String> rightMeta = SAjoinAO.getInputSchema(1).getMetaAttributeNames();

		IMetadataMergeFunction<?> metaDataMerge = MetadataRegistry.getMergeFunction(leftMeta, rightMeta);

		if (metaDataMerge == null) {
			throw new RuntimeException(
					"Cannot find a meta data merge function for left=" + leftMeta + " and right=" + rightMeta);
		}

		SAJoinPO SAjoinPO = new SAJoinPO(metaDataMerge,SAjoinAO.getTupleRangeAttribute());
		boolean isCross = false;
		IPredicate pred = SAjoinAO.getPredicate();
		if (pred == null) {
			SAjoinPO.setJoinPredicate(TruePredicate.getInstance());
			isCross = true;
		} else {
			setJoinPredicate(SAjoinPO, SAjoinAO);
		}
		SAjoinPO.setCardinalities(SAjoinAO.getCard());
		SAjoinPO.setSweepAreaName(SAjoinAO.getSweepAreaName());
		SAjoinPO.setTupleRangeAttribute(SAjoinAO.getTupleRangeAttribute());
		if (SAjoinAO.isAssureOrder()) {
			SAjoinPO.setTransferFunction(new TITransferArea());
		} else {
			SAjoinPO.setTransferFunction(new DirectTransferArea());
		}
		// }

		SAjoinPO.setCreationFunction(new DefaultTIDummyDataCreation());

		defaultExecute(SAjoinAO, SAjoinPO, transformConfig, true, true);
		if (isCross) {
			SAjoinPO.setName("SAJoin");
		}

	}

	protected void setJoinPredicate(JoinTIPO joinPO, JoinAO joinAO) {
		joinPO.setJoinPredicate(joinAO.getPredicate().clone());
	}

	@Override
	public boolean isExecutable(SAJoinAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SAJoinAO> getConditionClass() {
		return SAJoinAO.class;
	}

}
