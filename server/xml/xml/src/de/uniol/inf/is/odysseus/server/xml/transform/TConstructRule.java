package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.collection.XMLStreanObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.ConstructAO;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.ToXMLAO;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XPathAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.ConstructPO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.ToXMLPO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.ProjectPO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.XPathPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TConstructRule extends AbstractTransformationRule<ConstructAO>{

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(ConstructAO operator, TransformationConfiguration config) throws RuleException {
		ConstructPO<?> constructPO = new ConstructPO<IMetaAttribute>(operator.getExpressions(), operator.getNewExpressions());
		defaultExecute(operator, constructPO, config, true, false);
	}

	@Override
	public boolean isExecutable(ConstructAO operator, TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == XMLStreanObject.class ) &&
				operator.isAllPhysicalInputSet()) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "ConstructAO --> ConstructPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}