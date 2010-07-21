package de.uniol.inf.is.odysseus.filtering.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class GainFunctionAO <M extends IProbability> extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// TODO Weitere ben�tigte Attribute hier einf�gen
	
	public GainFunctionAO() {
		super();
	}
	
	public GainFunctionAO(GainFunctionAO<M> copy) {
		super(copy);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new GainFunctionAO<M>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		// TODO Falls das Schema nicht ver�ndert wird, kann
		// hier einfach das InputSchema zur�ckgegeben werden
		// (wie in HypothesisEvaluationPO.getOutputSchema())
		// Falls es ver�ndert wird, 
		// HypothesisGenerationAO.getOutputSchema() anschauen.
		return null;
	}

	// TODO Weitere ben�tigte Methoden hier hinzuf�gen.
}
