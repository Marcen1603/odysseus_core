package de.uniol.inf.is.odysseus.filtering.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CorrectStateEstimateFunctionAO <M extends IProbability> extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String functionID;
	
	private String oldObjListPath;
	
	private String newObjListPath;

	public CorrectStateEstimateFunctionAO(String functionId) {
		super();
		this.functionID = functionId;
	}
	
	public CorrectStateEstimateFunctionAO(CorrectStateEstimateFunctionAO<M> copy) {
		super(copy);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new CorrectStateEstimateFunctionAO<M>(this);
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

	public void initPaths(String oldObjListPath, String newObjListPath) {
		this.oldObjListPath = oldObjListPath;
		this.newObjListPath = newObjListPath;
	}

	public int[] getNewObjListPath() {
		//this.leftSchema = ((LogicalSubscription[]) this.getSubscriptions().toArray())[0].getSchema();
		return OrAttributeResolver.getAttributePath(this.getInputSchema(), this.newObjListPath);
	}

	public int[] getOldObjListPath() {
		//this.rightSchema = ((LogicalSubscription[]) this.getSubscriptions().toArray())[1].getSchema();
		return OrAttributeResolver.getAttributePath(this.getInputSchema(), this.oldObjListPath);
	}
	
	public String getFunctionID() {
		return this.functionID;
	}

	public void setFunctionID(String functionID) {
		this.functionID = functionID;
	}
	
	// TODO Weitere benötigte Methoden hier hinzufügen.
}
