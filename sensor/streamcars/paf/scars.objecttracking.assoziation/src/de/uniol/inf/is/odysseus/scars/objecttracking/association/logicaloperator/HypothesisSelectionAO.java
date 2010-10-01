package de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HypothesisSelectionAO<M extends IProbability> extends UnaryLogicalOp {

  private static final long serialVersionUID = 1L;

  private String id;

  private String oldObjListPath;
  private String newObjListPath;

  public HypothesisSelectionAO() {
    super();
  }

  public HypothesisSelectionAO(HypothesisSelectionAO<M> copy) {
    super(copy);
    this.id = copy.id;
    
    this.oldObjListPath = copy.oldObjListPath;
    this.newObjListPath = copy.newObjListPath;
  }

  public String getID() {
    return id;
  }

  public void setID(String iD) {
    id = iD;
  }

  public String getNewObjListPath() {
    return this.newObjListPath;
  }

  public String getOldObjListPath() {
    return this.oldObjListPath;
  }

  public void setNewObjListPath(String newObjListPath) {
    this.newObjListPath = newObjListPath;
  }
  
  public void setOldObjListPath(String oldObjListPath) {
    this.oldObjListPath = oldObjListPath;
  }

  @Override
  public SDFAttributeList getOutputSchema() {
    return this.getInputSchema();
  }

  @Override
  public AbstractLogicalOperator clone() {
    return new HypothesisSelectionAO<M>(this);
  }
}
