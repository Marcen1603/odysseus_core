package de.uniol.inf.is.odysseus.objectrelational.transform;

import java.util.Collection;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking.ObjectTrackingNestAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.ObjectTrackingNestPO;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TNestAORule extends AbstractTransformationRule<ObjectTrackingNestAO> {

	@Override
	public int getPriority() {		
		return 15;
	}

	@Override
	public void transform(ObjectTrackingNestAO nestAO, TransformationConfiguration transformConfig) {
		ObjectTrackingNestPO<?> nestPO = new ObjectTrackingNestPO(nestAO.getInputSchema(),nestAO.getOutputSchema(),nestAO.getNestAttribute(),nestAO.getGroupingAttributes());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(nestAO, nestPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}				
		retract(nestAO);		
	}

	@Override
	public boolean isExecutable(ObjectTrackingNestAO operator, TransformationConfiguration transformConfig) {
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
		return "ObjectTrackingNestAO -> ObjectTrackingNestPO";
	}

}
