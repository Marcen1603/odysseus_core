package mg.dynaquest.sourcedescription.sdf.query;

import java.util.ArrayList;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFComplexPredicateFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

public class WSimplePredicateSet {

    /**
	 * @uml.property  name="predicates"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="mg.dynaquest.sourcedescription.sdf.query.WeightedSimplePredicate"
	 */
    private ArrayList<WeightedSimplePredicate> predicates;

	public WSimplePredicateSet() {
		predicates = new ArrayList<WeightedSimplePredicate>();
	}

	public void addPredicate(WeightedSimplePredicate predicate) {
		predicates.add(predicate);
	}

	public WeightedSimplePredicate getPredicate(int pos) {
		return (WeightedSimplePredicate) predicates.get(pos);
	}
	
	public void removePredicate(WeightedSimplePredicate predicate)
	{
		this.predicates.remove(predicate);
	}

	public int getPredicateCount() {
		return predicates.size();
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        for (WeightedSimplePredicate p: predicates){
            ret.append(" "+p.toString());
        }
        return ret.toString();
    }

    /* gibt alle Pr�dikate zur�ck */
    public ArrayList<WeightedSimplePredicate> getPredicates() {
    	return this.predicates;
    }
    
    /** Mit Hilfe dieser Methode werden alle Pr�dikate der Menge unabh�ngig von
     * ihrer Gewichtung zu einem komplexen UND-verkn�pften Pr�dikat zusammengefasst
     * @return UND-Verkn�pfung aller enthaltenen Pr�dikate
     */
	public SDFPredicate combinePredicates() {
		if (predicates.size() == 0){
			return null;
		}
		if (predicates.size() == 1){
			return predicates.get(0).getPredicate();
		}
		// Ansonsten sind mindestens 2 Pr�dikate enthalten
		// Jetzt von links nach rechts verkn�pfen
		SDFPredicate ret = 
			SDFComplexPredicateFactory.combinePredicates(SDFPredicates.AndPredicate, 
				predicates.get(0).getPredicate(), predicates.get(1).getPredicate());
		
		for (int i=2;i<predicates.size();i++){
			SDFComplexPredicateFactory.combinePredicates(SDFPredicates.AndPredicate, 
					ret, predicates.get(i).getPredicate());
		}
		
		return ret;
	}
    
    

}