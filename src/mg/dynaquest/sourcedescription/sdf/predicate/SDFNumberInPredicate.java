package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.Map;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SDFNumberInPredicate extends SDFInPredicate {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2292260799582521058L;
	/**
	 * @uml.property  name="compOp"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFCompareOperator compOp = null;

    /**
     * 
     * @uml.property name="compOp"
     */
    /* (non-Javadoc)
     * @see mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate#getCompareOp()
     */
    public SDFCompareOperator getCompareOp() {
        return compOp;
    }

    
	public SDFNumberInPredicate(String URI, SDFAttribute attribute,
			SDFConstantList elements) {
		super(URI, attribute, elements);
		this.compOp = SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal);
	}

	public double getElement(int index) {
		return super.getObjectElement(index).getDouble();
	}


    /* (non-Javadoc)
     * @see mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate#evaluate(java.util.HashMap)
     */
    public boolean evaluate(Map<SDFAttribute, SDFConstant> attributeAssignment) {
        // Wird zu True ausgewertet, wenn die Belegung des "eigenen" Attributs
        // mit einem Element der Liste übereinstimmt.
        SDFConstant valueToCompare = attributeAssignment.get(this.getAttribute());
        double value =  valueToCompare.getDouble();
        for (int i=0;i<this.getNoOfElements();i++){
            // Hier jetzt ein Vergleich von Double-Werten ...
            // TODO ist das gut so?
            if (Double.compare(value, getElement(i)) == 0){
                return true;
            }
        }          
        return false;
    }
    
	@Override
	public boolean evaluateDirect(Map<SDFAttribute, Object> attributeAssignment) {
		throw new NotImplementedException();
	}

	public void setCompareOp(SDFCompareOperator operator) {
		this.compOp = operator;
	}






    
    
}