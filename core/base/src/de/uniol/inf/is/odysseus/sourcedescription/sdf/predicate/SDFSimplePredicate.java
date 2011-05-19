/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * Diese Klasse verwaltet ein Praedikat
 */
public class SDFSimplePredicate extends SDFPredicate implements IClone {

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
	public SDFSimplePredicate clone() {
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