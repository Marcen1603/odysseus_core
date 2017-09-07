package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.ToXMLAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.ToXMLPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TToXMLRule extends AbstractTransformationRule<ToXMLAO>{

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(ToXMLAO operator, TransformationConfiguration config) throws RuleException {
		ToXMLPO<?> toxmlPO = new ToXMLPO<IMetaAttribute>(operator.getExpressions(), operator.getAttributes());
		defaultExecute(operator, toxmlPO, config, true, false);
	}

	@Override
	public boolean isExecutable(ToXMLAO operator, TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == Tuple.class ) &&
				operator.isAllPhysicalInputSet()) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "ToXMLAO --> ToXMLPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}