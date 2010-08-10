package de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HypothesisSelectionAO<M extends IProbability> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private String ID;

	private String oldObjListPath;
	private String newObjListPath;

	public HypothesisSelectionAO() {
		super();
	}

	public HypothesisSelectionAO(HypothesisSelectionAO<M> copy) {
		super(copy);
		this.ID = copy.getID();
		this.oldObjListPath = copy.oldObjListPath;
		this.newObjListPath = copy.newObjListPath;
	}

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
		return new HypothesisSelectionAO<M>(this);
	}

	public void initPaths(String oldObjListPath, String newObjListPath) {
		this.oldObjListPath = oldObjListPath;
		this.newObjListPath = newObjListPath;
	}

	public int[] getNewObjListPath() {
		SchemaHelper sh = new SchemaHelper(this.getInputSchema());
		return sh.getSchemaIndexPath(this.newObjListPath).toArray();
	}

	public int[] getOldObjListPath() {
		SchemaHelper sh = new SchemaHelper(this.getInputSchema());
		return sh.getSchemaIndexPath(this.oldObjListPath).toArray();
	}
}
