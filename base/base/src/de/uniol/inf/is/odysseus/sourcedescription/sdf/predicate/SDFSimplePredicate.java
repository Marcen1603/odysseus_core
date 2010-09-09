package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * Diese Klasse verwaltet ein Praedikat
 */
public class SDFSimplePredicate extends SDFPredicate {

	private static final long serialVersionUID = -5438161522051021911L;

	private boolean isNegatived = false;

	private SDFExpression leftExpression;

	private SDFExpression rightExpression;

	private SDFCompareOperator compareOp;

	private SDFAttributeList attributes;

	public SDFSimplePredicate(String URI, SDFExpression left,
			SDFCompareOperator op, SDFExpression right) {
		super(URI);
		this.leftExpression = left;
		this.rightExpression = right;
		this.compareOp = op;

		this.attributes = new SDFAttributeList();
		this.attributes.addAll(this.leftExpression.getAllAttributes());
		this.attributes.addAll(this.rightExpression.getAllAttributes());
	}

	public SDFSimplePredicate(SDFSimplePredicate predicate) {
		super(predicate.getURI(false));
		this.leftExpression = (SDFExpression) predicate.leftExpression.clone();
		this.rightExpression = (SDFExpression) predicate.rightExpression
				.clone();
		this.compareOp = predicate.compareOp;
		this.attributes = predicate.attributes;
	}

	public void setCompareOp(SDFCompareOperator operator) {
		this.compareOp = operator;
	}

	/**
	 * @return
	 * @uml.property name="compareOp"
	 * @uml.associationEnd
	 */
	public SDFCompareOperator getCompareOperator() {
		return this.compareOp;
	}

	@Override
	public Object clone() {
		return new SDFSimplePredicate(this);
	}

	@Override
	public SDFAttributeList getAllAttributesWithCompareOperator(
			SDFCompareOperator op) {
		if (getCompareOperator().equals(op)) {
			return this.getAllAttributes();
		}
		return null;
	}

	@Override
	public void getAllPredicatesWithCompareOperator(SDFCompareOperator op,
			List<SDFSimplePredicate> resultList) {
		if (getCompareOperator().equals(op)) {
			resultList.add(this);
		}
	}

	@Override
	public boolean evaluate(Map<SDFAttribute, Object> attributeAssignment) {
		this.leftExpression.bindVariables(attributeAssignment);
		this.rightExpression.bindVariables(attributeAssignment);
		boolean result = this.compareOp.evaluate(this.leftExpression.getValue(),
				this.rightExpression.getValue());
		return this.isNegatived ? !result : result;
	}

	@Override
	public SDFAttributeList getAllAttributes() {
		return this.attributes;
	}

	@Override
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		// TODO
	}

	public SDFExpression getLeftExpression() {
		return this.leftExpression;
	}

	public SDFExpression getRightExpression() {
		return this.rightExpression;
	}

	@Override
	public String toString() {
		return (this.isNegatived ? "NOT (" : "") + this.leftExpression.toString() + " " + this.compareOp.toString()
				+ " " + this.rightExpression.toString()+(this.isNegatived ? ")" : "");
	}

	@Override
	public String toSQL() {
		return toString();
	}

	@Override
	public boolean isNegatived() {
		return this.isNegatived;
	}

	@Override
	public void negate() {
		this.isNegatived = !this.isNegatived;
	}
}