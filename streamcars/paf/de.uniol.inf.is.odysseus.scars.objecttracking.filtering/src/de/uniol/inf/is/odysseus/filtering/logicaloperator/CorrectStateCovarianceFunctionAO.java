package de.uniol.inf.is.odysseus.filtering.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CorrectStateCovarianceFunctionAO  <M extends IProbability> extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// TODO Weitere benötigte Attribute hier einfügen
	
	public CorrectStateCovarianceFunctionAO() {
		super();
	}
	
	public CorrectStateCovarianceFunctionAO(CorrectStateCovarianceFunctionAO<M> copy) {
		super(copy);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new CorrectStateCovarianceFunctionAO<M>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		// TODO Falls das Schema nicht verändert wird, kann
		// hier einfach das InputSchema zurückgegeben werden
		// (wie in HypothesisEvaluationPO.getOutputSchema())
		// Falls es verändert wird, 
		// HypothesisGenerationAO.getOutputSchema() anschauen.
		return null;
	}
}
