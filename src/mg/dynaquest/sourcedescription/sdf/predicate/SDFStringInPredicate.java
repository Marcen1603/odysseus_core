package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

public class SDFStringInPredicate extends SDFInPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8623711134364384479L;

	/**
	 * @uml.property name="compOp"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private SDFCompareOperator compOp = null;

	/**
	 * Soll beim Vergleich die Groß und Kleinschreibung unterschieden werden?
	 * 
	 * @uml.property name="ignoreCase"
	 */
	private boolean ignoreCase;

	/**
	 * Soll bei der Auswertung des Prädikates die Groß- und Kleinschreibung
	 * missachtet werden?
	 * 
	 * @param ignoreCase
	 *            The ignoreCase to set.
	 * 
	 * @uml.property name="ignoreCase"
	 */
	public synchronized void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public SDFStringInPredicate(String URI, SDFAttribute attribute,
			SDFConstantList elements) {
		super(URI, attribute, elements);
		this.compOp = SDFCompareOperatorFactory
				.getCompareOperator(SDFPredicates.Equal);
	}

	public String getElement(int index) {
		return super.getObjectElement(index).getString();
	}

	/**
	 * 
	 * @uml.property name="compOp"
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate#getCompareOp()
	 */
	public SDFCompareOperator getCompareOp() {
		return this.compOp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate#evaluate(java.util.HashMap)
	 */
	public boolean evaluate(
			Map<SDFAttribute, SDFConstant> attributeAssignment) {
		// Wird zu True ausgewertet, wenn die Belegung des "eigenen" Attributs
		// mit einem Element der Liste übereinstimmt.
		SDFConstant valueToCompare = attributeAssignment.get(this
				.getAttribute());
		String value = valueToCompare.getString();
		for (int i = 0; i < this.getNoOfElements(); i++) {
			if ((ignoreCase && value.compareToIgnoreCase(getElement(i)) == 0)
					|| (!ignoreCase && value.compareTo(getElement(i)) == 0)) {
				return true;
			}

		}
		return false;
	}
	
	

	
	
	public void setCompareOp(SDFCompareOperator operator) {
		this.compOp = operator;
	}

	@Override
	public SDFAttributeList getAllAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean evaluateDirect(Map<SDFAttribute, Object> attributeAssignment) {
		throw new NotImplementedException();
	}

}