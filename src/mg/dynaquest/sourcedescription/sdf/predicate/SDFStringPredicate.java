package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.Map;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;

/**
 * @author  Marco Grawunder
 */
public class SDFStringPredicate extends SDFSimplePredicate {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8943576842223255937L;

	/**
	 * @uml.property  name="compareOp"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFStringCompareOperator compareOp;

    /**
	 * @uml.property  name="value"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFConstant value;

	public SDFStringPredicate(String URI, SDFAttribute attribute,
			SDFStringCompareOperator compareOp, SDFConstant value) {
		super(URI, attribute);
		this.compareOp = compareOp;
		this.value = value;
	}

    public SDFStringPredicate(SDFStringPredicate pred) {
		super(pred.getURI(false), pred.getAttribute());
		this.compareOp = pred.compareOp;
		this.value = pred.value;
	}

	/**
     * 
     * @uml.property name="compareOp"
     */
    public void setCompareOp(SDFStringCompareOperator compareOp) {
        this.compareOp = compareOp;
    }


	/**
	 * @return  the compareOp
	 * @uml.property  name="compareOp"
	 */
	public SDFCompareOperator getCompareOp() {
		return compareOp;
	}

    /**
     * 
     * @uml.property name="value"
     */
    public void setValue(SDFConstant value) {
        this.value = value;
    }

    /**
     * 
     * @uml.property name="value"
     */
    public SDFConstant getValue() {
        return value;
    }

	public String toString() {
		StringBuffer buffer = new StringBuffer(this.getAttribute()+" ");      
        buffer.append(compareOp);
        buffer.append(" \"" + value);
        buffer.append("\"");
        return buffer.toString();
	}

    /* (non-Javadoc)
     * @see mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate#evaluate(java.util.HashMap)
     */
    public boolean evaluate(Map<SDFAttribute, SDFConstant> attributeAssignment) {
        // Wird zu True ausgewertet, wenn die Belegung des "eigenen" Attributs
        // mit dem Wert übereinstimmt.
        SDFConstant valueToCompare = attributeAssignment.get(this.getAttribute());
        String value =  valueToCompare.getString();
        return getCompareOp().evaluate(value, getValue().getString());
     }

	@Override
	public boolean evaluateDirect(Map<SDFAttribute, Object> attributeAssignment) {
		String value = (String)attributeAssignment.get(this.getAttribute());
		return getCompareOp().evaluate(value, getValue().getString());
	}
    
	@Override
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		xmlRetValue.append(indent);
		xmlRetValue.append("<stringPredicate>\n");
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("<attribute>");
		xmlRetValue.append(getAttribute().getURI(false));
		xmlRetValue.append("</attribute>\n");
		
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("<operator>\n");
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);	
		xmlRetValue.append(compareOp.getXMLRepresentation());
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("</operator>\n");

		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("<value>");
		value.getXMLRepresentation("", xmlRetValue);
		xmlRetValue.append("</value>\n");
		
		xmlRetValue.append(indent);
		xmlRetValue.append("</stringPredicate>\n");
	}

	public void setCompareOp(SDFCompareOperator operator) {
		this.compareOp = (SDFStringCompareOperator) operator;
	}    
}