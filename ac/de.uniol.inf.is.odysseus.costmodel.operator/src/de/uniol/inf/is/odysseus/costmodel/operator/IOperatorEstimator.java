package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public interface IOperatorEstimator<T extends IPhysicalOperator> {

	public Class<T> getOperatorClass();
	
	public OperatorEstimation estimateOperator( T instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms );
	
}
