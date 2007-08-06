/*
 * Created on 07.12.2004
 *
 */
package mg.dynaquest.queryexecution.po.algebra;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.query.WSimplePredicateSet;

/**
 * @author Marco Grawunder
 *
 */
public class SelectPO extends UnaryAlgebraPO{

   	
    public SelectPO(SelectPO po) {
        super(po);
        setPOName(po.getPOName());
    }
        
    public SelectPO(){
    	super();
    	setPOName("SelectPO");
    }
    
    public SelectPO(SDFSimplePredicate predicate) {
    	super();
    	setPredicate(predicate);
    }

	 public SelectPO(WSimplePredicateSet predicatSet) {
		 // Aus der Menge der Prädikate muss nun ein einzelnes 
		 // UND-Verknüpftes Prädikat werden
		 setPredicate(predicatSet.combinePredicates());	
		 setPOName("SelectPO");
	}
	 
	@Override
	public void setPredicate(SDFPredicate predicate) {
		super.setPredicate(predicate);
	}

	@Override
	 public SupportsCloneMe cloneMe() {
		return new SelectPO(this);
	}


}
