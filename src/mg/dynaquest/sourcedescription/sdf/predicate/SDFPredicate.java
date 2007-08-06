package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.Map;

import mg.dynaquest.sourcedescription.sdf.SDFElement;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;

/**
 * @author  Marco Grawunder
 */
public abstract class SDFPredicate extends SDFElement {
	public SDFPredicate(String URI) {
		super(URI);
	}
	
	/**
	 * @uml.property  name="allAttributes"
	 * @uml.associationEnd  readOnly="true"
	 */
	public abstract SDFAttributeList getAllAttributes();
	public abstract SDFAttributeList getAllAttributesWithCompareOperator(SDFCompareOperator op);
	public abstract boolean evaluate(Map<SDFAttribute, SDFConstant> attributeAssignment);
	public abstract boolean evaluateDirect(Map<SDFAttribute, Object> attributeAssignment);

	public abstract void getXMLRepresentation(String indent, StringBuffer xmlRetValue);
}