package mg.dynaquest.sourcedescription.sdf.query;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;

public class WeightedSimplePredicate extends Weighted {

	/* TODO: Diese Klasse sollte IMHO besser von SDFSimplePredicate abgeleitet werden und dann mit einer Gewichtung versehen werden (TH)*/
	
    /**
	 * @uml.property  name="predicate"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFSimplePredicate predicate;


	public WeightedSimplePredicate(SDFSimplePredicate predicate, float weighting) {
		super(weighting);
		this.predicate = predicate;
	}

	public SDFSimplePredicate getSimplePredicate() {
		return this.predicate;
	}
	
    /**
     * 
     * @uml.property name="predicate"
     */
    public SDFSimplePredicate getPredicate() {
        return (SDFSimplePredicate) predicate;
    }

    /**
     * 
     * @uml.property name="predicate"
     */
    public void setPredicate(SDFSimplePredicate predicate) {
        this.predicate = predicate;
    }

	public String toString() {
		return "(" + predicate.toString() + ", " + this.getWeighting() + ")";
	}

}