package de.uniol.inf.is.odysseus.server.xml.transform;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XMLTransformAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.XMLTransformPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TXMLTransformRule extends AbstractTransformationRule<XMLTransformAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(XMLTransformAO operator, TransformationConfiguration config) throws RuleException {
		try {
			XMLTransformPO<?> constructPO = new XMLTransformPO<IMetaAttribute>(
					operator.getXsdFile(),
					operator.getXsdString(), 
					operator.isDynamic(),
					operator.getOptionsMap()
				);
			defaultExecute(operator, constructPO, config, true, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isExecutable(XMLTransformAO operator, TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == XMLStreamObject.class) && operator.isAllPhysicalInputSet()) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "XMLTransformAO --> XMLTransformPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}