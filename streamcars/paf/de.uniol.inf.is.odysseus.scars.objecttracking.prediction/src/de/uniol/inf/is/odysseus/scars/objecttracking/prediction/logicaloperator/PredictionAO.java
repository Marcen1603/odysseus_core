package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PredictionAO<T extends ITimeInterval & IProbability & ILatency & IPredictionFunctionKey<K>, K> extends BinaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	private Map<IPredicate<? super T>, IPredictionFunction> predictionFunctions;
	
	public PredictionAO() {
		predictionFunctions = new HashMap<IPredicate<? super T>, IPredictionFunction>();
	}
	
	public PredictionAO(PredictionAO<T, K> copy) {
		this.predictionFunctions = copy.predictionFunctions;
	}
	

	public void setPredictionFunctions(Map<IPredicate<? super T>, IPredictionFunction> predictionFunctions) {
		this.predictionFunctions = predictionFunctions;
	}
	
	public Map<IPredicate<? super T>, IPredictionFunction> getPredictionFunctions() {
		return predictionFunctions;
	}


	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(RIGHT);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PredictionAO<T, K>(this);
	}
}
