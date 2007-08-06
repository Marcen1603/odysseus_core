package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.Map;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author  Marco Grawunder
 */
public class SDFNumberPredicate extends SDFSimplePredicate {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4200722688860871826L;

	/**
	 * @uml.property  name="compareOp"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFNumberCompareOperator compareOp;

    /**
	 * @uml.property  name="value"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFExpression value;
	
	public SDFNumberPredicate(String URI, SDFAttribute attribute,
			SDFNumberCompareOperator compareOp, SDFExpression value) {
		super(URI, attribute);
		this.compareOp = compareOp;
		this.value = value;
	}

    public SDFNumberPredicate(SDFNumberPredicate pred) {
		super(pred.getURI(false), pred.getAttribute());
		this.compareOp = pred.compareOp;
		this.value = pred.value;
	}

	/**
     * 
     * @uml.property name="compareOp"
     */
    public void setCompareOp(SDFNumberCompareOperator compareOp) {
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
    public void setValue(SDFExpression value) {
        this.value = value;
    }

    /**
     * 
     * @uml.property name="value"
     */
    public SDFExpression getValue() {
        return value;
    }

	public String toString() {
		return getAttribute() + " " + compareOp + " " + value;
	}

    /* (non-Javadoc)
     * @see mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate#evaluate(java.util.HashMap)
     */
    public boolean evaluate(Map<SDFAttribute, SDFConstant> attributeAssignment) {
        // Wird zu True ausgewertet, wenn die Belegung des "eigenen" Attributs
        // mit dem Wert übereinstimmt.
        SDFConstant valueToCompare = attributeAssignment.get(this.getAttribute());
        double value =  valueToCompare.getDouble();

        this.value.bindVariables(attributeAssignment);
        
        return getCompareOp().evaluate(value, getValue().getDouble());
    }
    
	@Override
	public boolean evaluateDirect(Map<SDFAttribute, Object> attributeAssignment) {
		double value = (Double)attributeAssignment.get(this.getAttribute());
		return getCompareOp().evaluate(value, getValue().getDouble());
	}

	@Override
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		xmlRetValue.append(indent);
		xmlRetValue.append("<numberPredicate>\n");
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
		xmlRetValue.append("</numberPredicate>\n");
	}

	@Override
	public void setCompareOp(SDFCompareOperator operator) {
		this.compareOp = (SDFNumberCompareOperator) operator;
		
	}

}