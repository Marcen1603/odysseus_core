package de.uniol.inf.is.odysseus.server.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator.ToKeyValueAO;
import de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator.TupleToKeyValuePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTupleToKeyValueRule extends AbstractTransformationRule<ToKeyValueAO> {


	@Override
	public void execute(ToKeyValueAO ao, TransformationConfiguration config) throws RuleException {
		TupleToKeyValuePO<IMetaAttribute> po = new TupleToKeyValuePO<IMetaAttribute>(ao);
		defaultExecute(ao, po, config, true, false);
	}

	@Override
	public boolean isExecutable(ToKeyValueAO operator, TransformationConfiguration config) {
		if (operator.getInputSchema().getType() == Tuple.class &&
				operator.isAllPhysicalInputSet()) {
			return true;

		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ToKeyValueAO> getConditionClass() {
		return ToKeyValueAO.class;
	}

}
