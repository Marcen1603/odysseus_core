package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.MapPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMapRule extends AbstractTransformationRule<MapAO>
{

	@Override
	public int getPriority()
	{
		return 10;
	}

	@Override
	public void execute(MapAO operator, TransformationConfiguration config) throws RuleException
	{
		MapPO<?> xmlmapPO = new MapPO<IMetaAttribute>(operator.getSource(), operator.getTarget());
		defaultExecute(operator, xmlmapPO, config, true, false);
	}

	@Override
	public boolean isExecutable(MapAO operator, TransformationConfiguration config)
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