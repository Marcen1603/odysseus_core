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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MAP")
public class MapAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2120387285754464451L;
	private List<SDFExpression> expressions;
	private SDFSchema outputSchema = null;

	public MapAO() {
		super();
	}

	public MapAO(MapAO ao) {
		super(ao);
		this.setExpressions(ao.expressions);
	}

	public List<SDFExpression> getExpressions() {
		return Collections.unmodifiableList(expressions);
	}

	private void calcOutputSchema() {
		outputSchema = new SDFSchema(getInputSchema().getURI());
		if (expressions != null) {
			for (SDFExpression expr : expressions) {
				SDFAttribute attr = null;

				IExpression<?> mepExpression = expr.getMEPExpression();
				String exprString = mepExpression.toString();
				// Variable could be source.name oder name, we are looking for
				// name!
				String[] split = SDFElement.splitURI(exprString);
				final SDFElement elem;
				if (split[1] != null && split[1].length() > 0) {
					elem = new SDFElement(split[0], split[1]);
				} else {
					elem = new SDFElement(null, split[0]);
				}

				// If expression is an attribute use this data type
				List<SDFAttribute> inAttribs = expr.getAllAttributes();
				for (SDFAttribute attribute : inAttribs) {
					if (attribute.equalsCQL(elem)) {
						attr = new SDFAttribute(null, exprString,
								attribute.getDatatype());
					}
				}
				// else use the expression data type
				if (attr == null) {
					attr = new SDFAttribute(null, exprString,
							mepExpression.getReturnType());
				}

				outputSchema.add(attr);

				// Alles Quatsch :-)
				//
				// if (expr.isSingleAttribute()){
				// outputSchema.add(expr.getSingleAttribute());
				// }else if (inputSchema.contains(attr)){
				// outputSchema.add(attr);
				// } else{
				// outputSchema.add(new SDFAttribute(expr.toString()));
				// }
			}
		}
	}

	@Parameter(type = SDFExpressionParameter.class, isList = true)
	public void setExpressions(List<SDFExpression> expressions) {
		this.expressions = expressions;
		this.outputSchema = null;
	}

	@Override
	public SDFSchema getOutputSchema() {
		if (outputSchema == null){
			calcOutputSchema();
		}
		return outputSchema;
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
