package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Marco Grawunder
 */
public abstract class SDFPredicate extends SDFElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4940130236222593530L;
	public SDFPredicate(String URI) {
		super(URI);
	}
	
	/**
	 * @uml.property  name="allAttributes"
	 * @uml.associationEnd  readOnly="true"
	 */
	public abstract SDFAttributeList getAllAttributes();
	public abstract SDFAttributeList getAllAttributesWithCompareOperator(SDFCompareOperator op);
	public abstract void getAllPredicatesWithCompareOperator(SDFCompareOperator op, List<SDFSimplePredicate> resultList);
	public abstract boolean evaluate(Map<SDFAttribute, Object> attributeAssignment);

	public abstract void getXMLRepresentation(String indent, StringBuffer xmlRetValue);
	public abstract String toSQL();
	public abstract boolean isNegatived();
	public abstract void negate();
}