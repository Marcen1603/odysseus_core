package de.uniol.inf.is.odysseus.hmm.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.hmm.logicaloperator.VectorquantizationAO;
import de.uniol.inf.is.odysseus.hmm.physicaloperator.VectorquantizationPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TVectorquantizationAORule extends
		AbstractTransformationRule<VectorquantizationAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(VectorquantizationAO operator, TransformationConfiguration config) throws RuleException {
		AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> po;

		int numCluster = operator.getNumCluster();
		po = new VectorquantizationPO<>(numCluster);
		po.setOutputSchema(operator.getOutputSchema());
		replace(operator, po, config);
		retract(operator);
		insert(po);
		System.out.println("debug Tvector: " + numCluster);
//		defaultExecute(operator, new VectorquantizationOrientationPO(), config, true, true);
	}

	@Override
	public boolean isExecutable(VectorquantizationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "VectorquantizationAO -> VectorquantizationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super VectorquantizationAO> getConditionClass() {
		return VectorquantizationAO.class;
	}
}
