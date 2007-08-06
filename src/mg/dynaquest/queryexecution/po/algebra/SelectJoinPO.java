/*
 * Created on 01.02.2005
 *
 */
package mg.dynaquest.queryexecution.po.algebra;


import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * @author  Marco Grawunder
 */
public class SelectJoinPO extends BinaryAlgebraPO{

    /**
	 * @uml.property  name="selJoinAttributes"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFAttributeList selJoinAttributes;
	/**
	 * @uml.property  name="selJoinPred"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SDFPredicate selJoinPred;

	/**
     * @param joinPO
     */
    public SelectJoinPO(SelectJoinPO joinPO) {
        super(joinPO);
        setPOName("SelectJoinPO");
        selJoinAttributes = joinPO.selJoinAttributes;
        selJoinPred = joinPO.selJoinPred;
    }

	public SelectJoinPO(SDFAttributeList selJoinAttributes, SDFPredicate selJoinPred) {
		this.selJoinAttributes = selJoinAttributes;
		this.selJoinPred = selJoinPred;
	}

	public @Override
	SupportsCloneMe cloneMe() {
		return new SelectJoinPO(this);
	}
  
}
