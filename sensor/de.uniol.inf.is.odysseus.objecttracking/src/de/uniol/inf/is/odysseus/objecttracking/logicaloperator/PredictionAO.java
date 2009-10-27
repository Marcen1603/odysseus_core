package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class PredictionAO<T> extends UnaryLogicalOp{

	private Map<SDFExpression[], IPredicate<? super T>> predictionFunctions;
	
	private SDFExpression[] defaultPredictionFunction;
	
	public SDFExpression[] getDefaultPredictionFunction() {
		return defaultPredictionFunction;
	}

	public void setDefaultPredictionFunction(SDFExpression[] defaultPredictionFunction) {
		this.defaultPredictionFunction = defaultPredictionFunction;
	}

	public PredictionAO() {
		this.predictionFunctions = new HashMap<SDFExpression[], IPredicate<? super T>>();
		this.defaultPredictionFunction = null;
	}
	
	public PredictionAO(PredictionAO predictionAO) {
		super(predictionAO);
		this.predictionFunctions = new TreeMap<SDFExpression[], IPredicate<? super T>>();
		Set<Entry<SDFExpression[], IPredicate<? super T>>> entries = predictionAO.predictionFunctions.entrySet();
		for(Map.Entry<SDFExpression[], IPredicate<? super T>> curPredFunction: entries){
			this.predictionFunctions.put(curPredFunction.getKey(), curPredFunction.getValue());
		}
	}
	
	public Map<SDFExpression[], IPredicate<? super T>> getPredictionFunctions() {
		return predictionFunctions;
	}

	public void setPriorities(Map<SDFExpression[], IPredicate<? super T>> predictionFunctions) {
		this.predictionFunctions = predictionFunctions;
	}
	
	public void setPredictionFunction(SDFExpression[] expressions, IPredicate<? super T> predicate) {
		if (this.predictionFunctions.containsKey(expressions)) {
			throw new IllegalArgumentException("predictionFunction already exists: " + expressions);
		}
		this.predictionFunctions.put(expressions, predicate);
	}

	@Override
	public PredictionAO clone() {
		return new PredictionAO(this);
	}
}
