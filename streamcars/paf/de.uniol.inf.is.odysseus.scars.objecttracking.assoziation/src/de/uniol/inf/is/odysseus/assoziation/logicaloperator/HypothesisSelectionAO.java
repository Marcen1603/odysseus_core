package de.uniol.inf.is.odysseus.assoziation.logicaloperator;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HypothesisSelectionAO<M extends IProbability> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	private String ID;
	
	private String oldObjListPath;
	private String newObjListPath;
		
	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return super.getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void initPaths(String oldObjListPath, String newObjListPath) {
		this.oldObjListPath = oldObjListPath;
		this.newObjListPath = newObjListPath;
	}
	
	public int[] getNewObjListPath() {
		this.leftSchema = ((LogicalSubscription[]) this.getSubscriptions().toArray())[0].getSchema();
		return OrAttributeResolver.getAttributePath(leftSchema, this.newObjListPath);
	}

	public int[] getOldObjListPath() {
		this.rightSchema = ((LogicalSubscription[]) this.getSubscriptions().toArray())[1].getSchema();
		return OrAttributeResolver.getAttributePath(rightSchema, this.oldObjListPath);
	}
	
	public SDFAttributeList getLeftSchema() {
		return ((LogicalSubscription[]) this.getSubscriptions().toArray())[0].getSchema();
	}
	
	public SDFAttributeList getRightSchema() {
		return ((LogicalSubscription[]) this.getSubscriptions().toArray())[1].getSchema();
	}

}
