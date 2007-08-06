/*
 * Created on 07.12.2004
 *
 */
package mg.dynaquest.queryexecution.po.algebra;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFComplexPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;

/**
 * @author Marco Grawunder
 *
 */
public class JoinPO extends BinaryAlgebraPO{



    public JoinPO(){
        super();
        setPOName("JoinPO");
    }
    
    /**
     * @param joinPredicate
     */
    public JoinPO(SDFPredicate joinPredicate) 
    {
    	super();
        this.setPredicate(joinPredicate);
        setPOName("JoinPO");
    }
    
    /**
     * @param joinPO
     */
    public JoinPO(JoinPO joinPO) {
        super(joinPO);
        setPOName("JoinPO");
    }


    /**
     * @return Returns the joinPredicate.
     */
    public synchronized SDFComplexPredicate getJoinPredicate() {
        return (SDFComplexPredicate) getPredicate();
    }
        
    public synchronized void setPredicate(SDFPredicate joinPredicate){
    	super.setPredicate(joinPredicate);
    }

	public @Override
	SupportsCloneMe cloneMe() {
		return new JoinPO(this);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString());
		ret.append(" Predicate "+getPredicate());
		return ret.toString();
	}
	
	


}
