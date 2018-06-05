package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public abstract class AbstractIntervalTransformationRule<T extends ILogicalOperator>
		extends AbstractTransformationRule<T> {

	@Override
	public boolean isExecutable(T operator, TransformationConfiguration config) {
		if ((getStreamType() == null || operator.getInputSchema(0).getType() == getStreamType()) ||
				(getStreamType2() != null && operator.getInputSchema(0).getType() == getStreamType2())){
			// Remark ALL INPUT SCHEMAS need ITimeInterval			
			for (int i=0; i<operator.getNumberOfInputs();i++){
				SDFSchema s = operator.getInputSchema(i);
				if (!s.hasMetatype(ITimeInterval.class)){
					return false;
				}
			}
			return operator.isAllPhysicalInputSet();
			
		}
		return false;
	}

	protected Class<? extends IStreamObject<?>> getStreamType(){
		return null;
	}

	protected Class<? extends IStreamObject<?>> getStreamType2(){
		return null;
	}

}
