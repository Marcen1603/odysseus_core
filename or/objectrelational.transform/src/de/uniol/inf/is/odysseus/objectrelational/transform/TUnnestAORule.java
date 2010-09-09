package de.uniol.inf.is.odysseus.objectrelational.transform;

import java.util.Collection;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking.ObjectTrackingUnnestAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.ObjectTrackingUnnestPO;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUnnestAORule extends AbstractTransformationRule<ObjectTrackingUnnestAO> {

	@Override
	public int getPriority() {		
		return 15;
	}

	@Override
	public void execute(ObjectTrackingUnnestAO unnestAO, TransformationConfiguration transformConfig) {		
		ObjectTrackingUnnestPO<?> unnestPO = new ObjectTrackingUnnestPO(unnestAO.getInputSchema(),unnestAO.getOutputSchema(),unnestAO.getNestAttribute());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(unnestAO, unnestPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}				
		retract(unnestAO);
	}

	@Override
	public boolean isExecutable(ObjectTrackingUnnestAO operator, TransformationConfiguration transformConfig) {
		if(operator.isAllPhysicalInputSet()){
			Set<String> metaTypes = transformConfig.getMetaTypes();
			if(metaTypes.contains(IProbability.class.getCanonicalName())){
				if(metaTypes.contains(ILatency.class.getCanonicalName())){
					if(metaTypes.contains(IPredictionFunctionKey.class.getCanonicalName())){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ObjectTrackingUnnestAO -> ObjectTrackingUnnestPO";
	}

}
