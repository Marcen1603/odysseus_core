package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator.HypothesisGenerationAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator.HypothesisGenerationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class THypothesisGenarationAORule extends AbstractTransformationRule<HypothesisGenerationAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public void execute(HypothesisGenerationAO operator, TransformationConfiguration config) {
		System.out.println("DROOLS: THypothesisGeneration.drl");
		System.out.println("CREATE HypothesisGenerationPO.");
		HypothesisGenerationPO genPO = new HypothesisGenerationPO();
		genPO.setNewObjListPath(operator.getNewObjListPath());
		genPO.setOldObjListPath(operator.getOldObjListPath());
		genPO.setOutputSchema(operator.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, genPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}

		insert(genPO);
		retract(operator);
		System.out.println("CREATE HypothesisGenerationPO finished.");

	}

	@Override
	public boolean isExecutable(HypothesisGenerationAO operator, TransformationConfiguration config) {
		if (config.getMetaTypes().contains(ILatency.class.getCanonicalName()) && config.getMetaTypes().contains(IProbability.class.getCanonicalName())
				&& config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) && operator.isAllPhysicalInputSet()) {
			return true;
		}

		return false;

		// DRL-Code
		// trafo : TransformationConfiguration( metaTypes contains
		// "de.uniol.inf.is.odysseus.latency.ILatency" &&
		// metaTypes contains
		// "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" &&
		// metaTypes contains
		// "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
		// $genAO : HypothesisGenerationAO( allPhysicalInputSet == true )

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "HypothesisGenerationAO -> HypothesisGenerationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
