package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XMLToTupleAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.XMLToTuplePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TXMLToTupleRule extends AbstractTransformationRule<XMLToTupleAO> {

	@Override
	public void execute(XMLToTupleAO operator, TransformationConfiguration config) throws RuleException {
		ISource<?> inputPO = new XMLToTuplePO<IMetaAttribute>(operator);
		defaultExecute(operator, inputPO, config, true, false);
	}

	@Override
	public boolean isExecutable(XMLToTupleAO operator, TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == XMLStreamObject.class) && operator.isAllPhysicalInputSet()) {
			return true;
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getName() {
		return "XMLToTupleAO --> XMLToTuplePO";
	}

}
