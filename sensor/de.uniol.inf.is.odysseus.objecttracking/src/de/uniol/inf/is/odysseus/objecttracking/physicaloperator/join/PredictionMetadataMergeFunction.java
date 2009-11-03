package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join;

import de.uniol.inf.is.odysseus.metadata.base.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


/**
 * This MergeFunction merges metadata according to the
 * prediction based stream processing. The metadata must
 * contain a TimeInterval, a Probability and a Prediction-
 * Function.
 * 
 * @author Andre Bolles
 * 
 */
public class PredictionMetadataMergeFunction<M extends IPredictionFunction<? extends RelationalTuple<M>, M>> implements IInlineMetadataMergeFunction<M>{

	
	/**
	 * This is the covariance between left and right input
	 */
	double[][] covLeftRight;
	
	public PredictionMetadataMergeFunction(){
	}

	@Override
	public void mergeInto(M res, M left, M right) {		
		res.setPredictionFunction(null);
	}

}
