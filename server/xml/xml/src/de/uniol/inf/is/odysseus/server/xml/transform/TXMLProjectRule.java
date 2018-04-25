package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.XMLProjectPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TXMLProjectRule extends AbstractTransformationRule<ProjectAO>{

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(ProjectAO operator, TransformationConfiguration config) throws RuleException {
		XMLProjectPO<IMetaAttribute> projectPO = new XMLProjectPO<IMetaAttribute>(operator.getAttributes());
		defaultExecute(operator, projectPO, config, true, false);
	}

	@Override
	public boolean isExecutable(ProjectAO operator, TransformationConfiguration config) {
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