package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.base.MapAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMapAORule extends AbstractTransformationRule<MapAO> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(MapAO mapAO, TransformationConfiguration transformConfig) {
		RelationalMapPO mapPO = new RelationalMapPO(mapAO.getInputSchema(), mapAO.getExpressions().toArray(new SDFExpression[0]));
		mapPO.setOutputSchema(mapAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(mapAO,mapPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(mapAO);		
	}

	@Override
	public boolean isExecutable(MapAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getDataType().equals("relational")){
			if(operator.getPhysSubscriptionTo()!=null){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "MapAO -> RelationalMapPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
