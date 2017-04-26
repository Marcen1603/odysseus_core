package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XPathAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.XMLStreamObjectProjectPO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.XPathPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TXPathRule extends AbstractTransformationRule<XPathAO>{

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(XPathAO operator, TransformationConfiguration config) throws RuleException {
		XPathPO<?> xpathPO = new XPathPO<IMetaAttribute>(operator.getAttributes());
		defaultExecute(operator, xpathPO, config, true, false);
	}

	@Override
	public boolean isExecutable(XPathAO operator, TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == XMLStreamObject.class ) &&
				operator.isAllPhysicalInputSet()) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "XPathAO --> XPathPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}