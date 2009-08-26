package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFPredicates;

public class SDFComplexPredicate extends SDFPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6526047081256267668L;

	private boolean isNegatived = false;

	/**
	 * @uml.property name="left"
	 * @uml.associationEnd
	 */
	private SDFPredicate left;

	/**
	 * @uml.property name="right"
	 * @uml.associationEnd
	 */
	private SDFPredicate right;

	/**
	 * @uml.property name="op"
	 * @uml.associationEnd
	 */
	private SDFLogicalOperator op;

	public SDFComplexPredicate(String URI) {
		super(URI);
	}

	public SDFComplexPredicate(String URI, SDFPredicate left,
			SDFLogicalOperator op, SDFPredicate right) {
		super(URI);
		this.op = op;
		this.left = left;
		this.right = right;
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
		if (this.isNegatived) {
			ret.append("NOT (");
		}
		if (this.left != null)
			ret.append("(" + this.left.toString());
		if (this.op != null)
			ret.append(" " + this.op.toString() + " ");
		if (this.right != null)
			ret.append(this.right.toString() + ")");
		if (this.isNegatived) {
			ret.append(")");
		}
		return ret.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFPredicate#getAllAttributes()
	 */
	public SDFAttributeList getAllAttributes() {
		return SDFAttributeList.union(this.left.getAllAttributes(), this.right
				.getAllAttributes());
	}

	public SDFAttributeList getAllAttributesWithCompareOperator(
			SDFCompareOperator op) {
		return SDFAttributeList.union(left
				.getAllAttributesWithCompareOperator(op), right
				.getAllAttributesWithCompareOperator(op));
	}

	public void getAllPredicatesWithCompareOperator(SDFCompareOperator op,
			List<SDFSimplePredicate> resultList) {
		left.getAllPredicatesWithCompareOperator(op, resultList);
		right.getAllPredicatesWithCompareOperator(op, resultList);
	}

	public boolean evaluate(Map<SDFAttribute, Object> attributeAssignment) {
		boolean val = this.op.evaluate(this.left.evaluate(attributeAssignment),
				this.right.evaluate(attributeAssignment));
		return this.isNegatived ? !val : val;
	}

	@Override
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		xmlRetValue.append(indent);
		xmlRetValue.append("<complexPredicate>\n");
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("<left>\n");
		left.getXMLRepresentation(indent + indent + indent, xmlRetValue);
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
		right.getXMLRepresentation(indent + indent + indent, xmlRetValue);
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("</right>\n");

		xmlRetValue.append("</complexPredicate>\n");
	}

	public String toSQL() {
		return "("
				+ left.toSQL()
				+ (this.op.equals(SDFLogicalOperatorFactory
						.getOperator(SDFPredicates.And)) ? " AND " : " OR ")
				+ right.toSQL() + ")";
	}

	public boolean isNegatived() {
		return isNegatived;
	}

	public void negate() {
		this.isNegatived = !this.isNegatived;
	}
}