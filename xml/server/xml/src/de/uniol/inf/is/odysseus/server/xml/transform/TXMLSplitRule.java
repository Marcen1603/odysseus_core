package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XMLSplitAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.XMLSplitPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TXMLSplitRule extends AbstractTransformationRule<XMLSplitAO>{

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(XMLSplitAO operator, TransformationConfiguration config) throws RuleException {
		XMLSplitPO<IMetaAttribute> projectPO = new XMLSplitPO<IMetaAttribute>(operator.getAttributes());
		defaultExecute(operator, projectPO, config, true, false);
	}

	@Override
	public boolean isExecutable(XMLSplitAO operator, TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == XMLStreamObject.class ) &&
				operator.isAllPhysicalInputSet()) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "ProjectAO --> XMLProjectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}