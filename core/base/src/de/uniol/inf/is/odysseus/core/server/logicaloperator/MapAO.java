/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MAP")
public class MapAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2120387285754464451L;
	private List<NamedExpressionItem> namedExpressions;
	private List<SDFExpression> expressions;

	public MapAO() {
		super();
	}

	public MapAO(MapAO ao) {
		super(ao);
		this.setExpressions(ao.namedExpressions);
	}

	public List<SDFExpression> getExpressions() {
		return Collections.unmodifiableList(expressions);
	}

	private void calcOutputSchema() {
		if (namedExpressions != null) {
			List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
			for (NamedExpressionItem expr : namedExpressions) {
				SDFAttribute attr = null;
				IExpression<?> mepExpression = expr.expression
						.getMEPExpression();
				String exprString;
				boolean isOnlyAttribute = false;

				exprString = expr.expression.toString();
				// Variable could be source.name oder name, we are looking
				// for
				// name!
				String lastString = null;
				String toSplit;
				if (exprString.startsWith("__last_")) {
					toSplit = exprString.substring(exprString.indexOf(".") + 1);
					lastString = exprString.substring(0,
							exprString.indexOf(".") - 1);
				} else {
					toSplit = exprString;
				}
				String[] split = SDFElement.splitURI(toSplit);
				final SDFElement elem;
				if (split[1] != null && split[1].length() > 0) {
					elem = new SDFElement(split[0], split[1]);
				} else {
					elem = new SDFElement(null, split[0]);
				}

				// If expression is an attribute use this data type
				List<SDFAttribute> inAttribs = expr.expression
						.getAllAttributes();
				for (SDFAttribute attributeToCheck : inAttribs) {
					SDFAttribute attribute;
					String attributeURI = attributeToCheck.getURI();
					if (attributeURI.startsWith("__last_")) {
						String realAttributeName = attributeURI
								.substring(attributeURI.indexOf(".") + 1);
						split = SDFElement.splitURI(realAttributeName);
						if (split.length > 1) {
							attribute = new SDFAttribute(split[0], split[1],
									attributeToCheck);
						} else {
							attribute = new SDFAttribute(null, split[0],
									attributeToCheck);
						}
					} else {
						attribute = attributeToCheck;
					}
					if (attribute.equalsCQL(elem)) {
						if (lastString != null) {
							String attrName = elem.getURIWithoutQualName() != null ? elem
									.getURIWithoutQualName()
									+ "."
									+ elem.getQualName() : elem.getQualName();
							attr = new SDFAttribute(lastString, attrName,
									attribute.getDatatype());
						} else {
							attr = new SDFAttribute(
									elem.getURIWithoutQualName(),
									elem.getQualName(), attribute.getDatatype());
						}
						isOnlyAttribute = true;
					}
				}

				// Expression is an attribute and name is set --> keep Attribute
				// type
				if (isOnlyAttribute) {
					if (!"".equals(expr.name)) {
						if (!attr.getSourceName().startsWith("__last_")) {
							attr = new SDFAttribute(attr.getSourceName(),
									expr.name, attr);
						} else {
							attr = new SDFAttribute(null, expr.name, attr);
						}
					}
				}

				// else use the expression data type
				if (attr == null) {
					attr = new SDFAttribute(null, expr.name != null ? expr.name
							: exprString, mepExpression.getReturnType());
				}
				attrs.add(attr);

			}
			setOutputSchema(new SDFSchema(getInputSchema().getURI(), attrs));
		}
	}

	@Parameter(type = SDFExpressionParameter.class, isList = true)
	public void setExpressions(List<NamedExpressionItem> namedExpressions) {
		this.namedExpressions = namedExpressions;
		expressions = new ArrayList<>();
		for (NamedExpressionItem e : namedExpressions) {
			expressions.add(e.expression);
		}
		setOutputSchema(null);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		calcOutputSchema();
		return getOutputSchema();
	}

	@Override
	public void initialize() {
		calcOutputSchema();
	}

	@Override
	public MapAO clone() {
		return new MapAO(this);
	}

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = super.hashCode();
	// result = prime * result
	// + ((expressions == null) ? 0 : expressions.hashCode());
	// return result;
	// }

	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (!super.equals(obj))
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// MapAO other = (MapAO) obj;
	// if (expressions == null) {
	// if (other.expressions != null)
	// return false;
	// } else if (!expressions.equals(other.expressions))
	// return false;
	// return true;
	// }

}
