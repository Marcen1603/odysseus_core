package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.XMLMapAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.XMLMapPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TXMLMapRule extends AbstractTransformationRule<XMLMapAO>
{

	@Override
	public int getPriority()
	{
		return 10;
	}

	@Override
	public void execute(XMLMapAO operator, TransformationConfiguration config) throws RuleException
	{
		IPhysicalOperator mapPO = new XMLMapPO<>(operator);
		defaultExecute(operator, mapPO, config, true, false);
	}

	@Override
	public boolean isExecutable(XMLMapAO operator, TransformationConfiguration config)
	{
		if ((operator.getInputSchema().getType() == XMLStreamObject.class) && operator.isAllPhysicalInputSet())
		{
			return true;
		}
		return false;
	}

	@Override
	public String getName()
	{
		return "XMLMapAO --> XMLMapPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup()
	{
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}