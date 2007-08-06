package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.Map;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;

public class SDFComplexPredicate extends SDFPredicate {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6526047081256267668L;
	
	private boolean isNegation = false;

	/**
	 * @uml.property  name="left"
	 * @uml.associationEnd  
	 */
    private SDFPredicate left;

    /**
	 * @uml.property  name="right"
	 * @uml.associationEnd  
	 */
    private SDFPredicate right;

    /**
	 * @uml.property  name="op"
	 * @uml.associationEnd  
	 */
    private SDFLogicalOperator op;


	public SDFComplexPredicate(String URI) {
		super(URI);
	}

    /**
     * 
     * @uml.property name="left"
     */
    public void setLeft(SDFPredicate left) {
        this.left = left;
    }

    /**
     * 
     * @uml.property name="left"
     */
    public SDFPredicate getLeft() {
        return left;
    }

    /**
     * 
     * @uml.property name="right"
     */
    public void setRight(SDFPredicate right) {
        this.right = right;
    }

    /**
     * 
     * @uml.property name="right"
     */
    public SDFPredicate getRight() {
        return right;
    }

    /**
     * 
     * @uml.property name="op"
     */
    public void setOp(SDFLogicalOperator op) {
        this.op = op;
    }

    /**
     * 
     * @uml.property name="op"
     */
    public SDFLogicalOperator getOp() {
        return op;
    }

	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + " ");
		if (this.left != null)
			ret.append("(" + this.left.toString());
		if (this.op != null)
			ret.append(" " + this.op.toString() + " ");
		if (this.right != null)
			ret.append(this.right.toString() + ")");
		return ret.toString();
	}

    /* (non-Javadoc)
     * @see mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate#getAllAttributes()
     */
    public SDFAttributeList getAllAttributes() {
        return SDFAttributeList.union(this.left.getAllAttributes(), this.right.getAllAttributes());
    }
    
    public SDFAttributeList getAllAttributesWithCompareOperator(SDFCompareOperator op){
    	return SDFAttributeList.union(left.getAllAttributesWithCompareOperator(op),
    			right.getAllAttributesWithCompareOperator(op));
    }
    
    /**
     * Überprüft für eine gegebene Belegung von Attributen, ob das Prädikat erfüllt
     * ist oder nicht
     * @param attributeAssignment
     * @return true, wenn das Prädikat erfüllt wird, false wenn nicht
     */
    public boolean evaluate(Map<SDFAttribute, SDFConstant> attributeAssignment){
        boolean val = this.op.evaluate(this.left.evaluate(attributeAssignment), this.right.evaluate(attributeAssignment));
        if (isNegation){ 
        	return !val;
        }else{
        	return val;
        }
    }

    public boolean evaluateDirect(Map<SDFAttribute, Object> attributeAssignment){
    	boolean val = this.op.evaluate(this.left.evaluateDirect(attributeAssignment), this.right.evaluateDirect(attributeAssignment));
        if (isNegation){ 
        	return !val;
        }else{
        	return val;
        }
    }

	@Override
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		xmlRetValue.append(indent);
		xmlRetValue.append("<complexPredicate>\n");
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("<left>\n");
		left.getXMLRepresentation(indent+indent+indent, xmlRetValue);
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("</left>\n");
		
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("<operator>\n");
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);	
		xmlRetValue.append(op.getURI(false));
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("</operator>\n");

		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("<right>\n");
		right.getXMLRepresentation(indent+indent+indent, xmlRetValue);
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("</right>\n");

		xmlRetValue.append("</complexPredicate>\n");
	}

	public boolean isNegation() {
		return isNegation;
	}

	public void setNegation(boolean isNegation) {
		this.isNegation = isNegation;
	}
}