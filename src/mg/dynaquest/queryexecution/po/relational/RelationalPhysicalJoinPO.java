/*
 * Created on 09.06.2006
 *
 */
package mg.dynaquest.queryexecution.po.relational;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.base.BinaryPlanOperator;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTupleCorrelator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFComplexPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Marco Grawunder
 */
public abstract class RelationalPhysicalJoinPO extends BinaryPlanOperator {

	
    /**
	 * Das zu verbindende Attributenpaar
	 * @uml.property  name="compareAttrs"
	 * @uml.associationEnd  
	 */
	private RelationalTupleCorrelator compareAttrs = null;
    
    public RelationalPhysicalJoinPO(RelationalPhysicalJoinPO operator) {
        super(operator);
    }

    public RelationalPhysicalJoinPO() {
        super();
    }
    

    public RelationalPhysicalJoinPO(JoinPO joinPO){
        super(joinPO);
    }

//	public JoinPO getJoinPO() {
//		return (JoinPO)getAlgebraPO();
//	}

	public void setJoinPO(JoinPO joinPO) {
		setAlgebraPO(joinPO);
	}

	/**
	 * @param compareAttrs  the compareAttrs to set
	 * @uml.property  name="compareAttrs"
	 */
	protected void setCompareAttrs(RelationalTupleCorrelator compareAttrs) {
		this.compareAttrs = compareAttrs;
	}

	/**
	 * @return  the compareAttrs
	 * @uml.property  name="compareAttrs"
	 */
	protected RelationalTupleCorrelator getCompareAttrs() {
		if (compareAttrs == null){
			compareAttrs = calculateCompareAttributes();
		}
		return compareAttrs;
	}

	private RelationalTupleCorrelator calculateCompareAttributes() {
		SDFAttributeList left = ((NAryPlanOperator)getLeftInput()).getOutElements();
		SDFAttributeList right = ((NAryPlanOperator)getRightInput()).getOutElements();
		RelationalTupleCorrelator ca = new RelationalTupleCorrelator(Math.max(left.size(), right.size()));
		
		// TODO: Im Moment hat JoinPredicate nur zwei Attribute 
		// muss dann auch hier noch angepasst werden.
		SDFComplexPredicate jp = ((JoinPO)getAlgebraPO()).getJoinPredicate();
		
//		ca.setPair(0, left.indexOf(jp.getLeftElement()), 
//					right.indexOf(jp.getRightElement()));
					
		
		throw new NotImplementedException();
		//return ca;
	}
    


}
