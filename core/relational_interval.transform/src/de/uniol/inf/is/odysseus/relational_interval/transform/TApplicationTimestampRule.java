package de.uniol.inf.is.odysseus.relational_interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TApplicationTimestampRule extends AbstractTransformationRule<TimestampAO> {

	@Override 
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(TimestampAO timestampAO, TransformationConfiguration transformConfig) {
		int pos = timestampAO.getInputSchema().indexOf(timestampAO.getStartTimestamp());
		int posEnd = timestampAO.hasEndTimestamp() ? timestampAO.getInputSchema().indexOf(timestampAO.getEndTimestamp()) : -1;
		RelationalTimestampAttributeTimeIntervalMFactory mUpdater = new RelationalTimestampAttributeTimeIntervalMFactory(pos, posEnd); 
	 
		MetadataUpdatePO po = new MetadataUpdatePO(mUpdater);
		po.setOutputSchema(timestampAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(timestampAO, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(timestampAO);		
	}

	@Override
	public boolean isExecutable(TimestampAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
			if(operator.isAllPhysicalInputSet()){
				if(!operator.isUsingSystemTime()){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "TimestampAO -> MetadataUpdatePO(application timestamp/relational)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
