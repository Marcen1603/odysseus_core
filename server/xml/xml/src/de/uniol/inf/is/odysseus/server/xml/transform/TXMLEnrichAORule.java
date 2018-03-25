package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XMLEnrichAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.XMLEnrichPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TXMLEnrichAORule extends AbstractTransformationRule<XMLEnrichAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(XMLEnrichAO operator, TransformationConfiguration config) throws RuleException {
		XMLEnrichPO<IMetaAttribute> po = new XMLEnrichPO<IMetaAttribute>(operator.getPredicate(),
				operator.getMinimumSize(), operator.getTargetPath());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(XMLEnrichAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "XMLEnrichAO -> XMLEnrichPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super XMLEnrichAO> getConditionClass() {
		return XMLEnrichAO.class;
	}

}
