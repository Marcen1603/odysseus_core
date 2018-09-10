package de.uniol.inf.is.odysseus.hmm.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.hmm.logicaloperator.HmmAO;
import de.uniol.inf.is.odysseus.hmm.physicaloperator.HmmRecognitionPO;
import de.uniol.inf.is.odysseus.hmm.physicaloperator.HmmTrainingPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class THmmAORule extends
		AbstractTransformationRule<HmmAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 * 
	 * Choose the correct physical operator by comparing the given parameter from the odysseus query
	 */
	@Override
	public void execute(HmmAO operator, TransformationConfiguration config) throws RuleException {
		AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> po;
		if (operator.getMode().equalsIgnoreCase("RECOGNITION")) {
			po = new HmmRecognitionPO();
			po.setOutputSchema(operator.getOutputSchema());
			replace(operator, po, config);
			retract(operator);
			insert(po);
		} else if (operator.getMode().equalsIgnoreCase("TRAINING")) {
			po = new HmmTrainingPO(operator.getGesture());
			po.setOutputSchema(operator.getOutputSchema());
			replace(operator, po, config);
			retract(operator);
			insert(po);
		}
//		defaultExecute(operator, new HmmRecognitionPO(), config, true, true);

	}

	@Override
	public boolean isExecutable(HmmAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "HmmAO -> HmmPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super HmmAO> getConditionClass() {
		return HmmAO.class;
	}
}
