package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.merge;

import de.uniol.inf.is.odysseus.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;


/**
 * This MergeFunction merges metadata according to the
 * prediction based stream processing. The metadata must
 * contain a TimeInterval, a Probability and a Prediction-
 * Function.
 * 
 * @author Andre Bolles
 * 
 */
public class PredictionKeyMetadataMergeFunction<M extends IPredictionFunctionKey<IPredicate>> implements IInlineMetadataMergeFunction<M>{

	
	public PredictionKeyMetadataMergeFunction(){
	}
	
	public PredictionKeyMetadataMergeFunction(PredictionKeyMetadataMergeFunction<M> original){
		super();
	}
	
	public PredictionKeyMetadataMergeFunction<M> clone() {
		return new PredictionKeyMetadataMergeFunction<M>(this);
	}

	/**
	 * This method returns a new AndPredicate, that
	 * has the prediction function key from the left
	 * as left predicate and the key from the right
	 * as right predicate.
	 */
	@Override
	public void mergeInto(M res, M left, M right) {		
		AndPredicate newPred = new AndPredicate();
		newPred.setLeft(left.getPredictionFunctionKey());
		newPred.setRight(right.getPredictionFunctionKey());
		
		res.setPredictionFunctionKey(newPred);
	}

}
