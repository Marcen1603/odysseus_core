package de.uniol.inf.is.odysseus.transform.interval;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.window.SystemTimeIntervalFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.TimestampAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSystemTimestampRule extends AbstractTransformationRule<TimestampAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(TimestampAO timestampAO, TransformationConfiguration transformConfig) {
		SystemTimeIntervalFactory mUpdater = new SystemTimeIntervalFactory();		
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
		if(transformConfig.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")){
			if(operator.isAllPhysicalInputSet() && operator.isUsingSystemTime()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "TimestampAO -> MetadataUpdatePO(system timestamp)";
	}

}
