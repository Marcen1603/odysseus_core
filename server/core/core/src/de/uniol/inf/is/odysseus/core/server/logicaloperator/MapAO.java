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

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.UncheckedExpressionParamter;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MAP", doc = "Performs a mapping of incoming attributes to out-coming attributes using map functions. Odysseus also provides a wide range of mapping functions. Hint: Map is stateless. To used Map in a statebased fashion see: StateMap", url = "http://odysseus.offis.uni-oldenburg.de:8090/display/ODYSSEUS/Map+operator", category = { LogicalOperatorCategory.BASE })
public class MapAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2120387285754464451L;
	private List<NamedExpression> namedExpressions;
	// Expressions used for KeyValueMap
	private List<String[]> kvExpressions;
	private List<SDFExpression> expressions;
	/** The number of threads used for processing the expressions. */
	private int threads = 0;
	private boolean evaluateOnPunctuation = false;
	private boolean allowNullValue = false;
	private boolean suppressErrors = false;
	private boolean printNull = false;

	public MapAO() {
		super();
	}

	public MapAO(MapAO ao) {
		super(ao);
		this.setExpressions(ao.namedExpressions);
		this.kvExpressions = ao.kvExpressions;
		this.threads = ao.threads;
		this.evaluateOnPunctuation = ao.evaluateOnPunctuation;
		this.allowNullValue = ao.allowNullValue;
		this.suppressErrors = ao.suppressErrors;
	}

	public List<SDFExpression> getExpressionList() {
		return Collections.unmodifiableList(expressions);
	}

	private void calcOutputSchema() {
		if (namedExpressions != null) {
			List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
			for (NamedExpression expr : namedExpressions) {

				// TODO: Maybe here should an attribute resolver be used?

				SDFAttribute attr = null;
				IExpression<?> mepExpression = expr.expression
						.getMEPExpression();
				String exprString;
				boolean isOnlyAttribute = false;

				exprString = expr.expression.toString();
				// Replace '(' and ')' so the expression will not be recognized
				// as expression in the next operator (e.g. if it is a map)
				exprString = exprString.replace('(', '_').replace(')', '_');

				// Variable could be source.name oder name, we are looking
				// for
				// name!
				String lastString = null;
				String toSplit;
				// if (exprString.startsWith("__")) {
				// toSplit = exprString.substring(exprString.indexOf(".") + 1);
				// if (exprString.indexOf(".") > -1) {
				// lastString = exprString.substring(0,
				// exprString.indexOf(".") - 1);
				// }
				// } else {
				toSplit = exprString;
				// }
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
					if (attributeURI.startsWith("__")) {
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
									attribute.getDatatype(),
									attribute.getUnit(),
									attribute.getDtConstraints());
						} else {
							attr = new SDFAttribute(
									elem.getURIWithoutQualName(),
									elem.getQualName(),
									attribute.getDatatype(),
									attribute.getUnit(),
									attribute.getDtConstraints());
						}
						isOnlyAttribute = true;
					}
				}

				// Expression is an attribute and name is set --> keep Attribute
				// type
				if (isOnlyAttribute) {
					if (!"".equals(expr.name)) {
						if (attr != null && attr.getSourceName() != null
								&& !attr.getSourceName().startsWith("__")) {
							attr = new SDFAttribute(attr.getSourceName(),
									expr.name, attr);
						} else {
							attr = new SDFAttribute(null, expr.name, attr);
						}
					}
				}

				// else use the expression data type
				if (attr == null) {
					// Special Handling if return type if tuple
					if (mepExpression.getReturnType() == SDFDatatype.TUPLE) {
						int card = mepExpression.getReturnTypeCard();
						for (int i = 0; i < card; i++) {
							String name = !"".equals(expr.name) ? expr.name
									: exprString;
							attr = new SDFAttribute(null, name + "_" + i,
									mepExpression.getReturnType(i), null, null,
									null);
							attrs.add(attr);
						}

					} else {
						SDFDatatype retType = mepExpression.getReturnType();
						attr = new SDFAttribute(null,
								!"".equals(expr.name) ? expr.name : exprString,
								retType, null, null, null);
						attrs.add(attr);
					}
				} else {
					attrs.add(attr);
				}

			}
			SDFSchema s = SDFSchema.changeSourceName(SDFSchemaFactory
					.createNewWithAttributes(attrs, getInputSchema()),
					getInputSchema().getURI(), false);
			setOutputSchema(s);
		} else if (kvExpressions != null) {
			SDFSchema s = SDFSchemaFactory.createNewWithAttributes(null, getInputSchema());
			setOutputSchema(s);
		}
	}

	@Parameter(type = SDFExpressionParameter.class, name = "EXPRESSIONS", isList = true, optional = true, doc = "A list of expressions.")
	public void setExpressions(List<NamedExpression> namedExpressions) {
		this.namedExpressions = namedExpressions;
		expressions = new ArrayList<>();
		if (namedExpressions != null) {
			for (NamedExpression e : namedExpressions) {
				expressions.add(e.expression);
			}
		}
		setOutputSchema(null);
	}

	public List<NamedExpression> getExpressions() {
		return this.namedExpressions;
	}

	@Parameter(type = UncheckedExpressionParamter.class, name = "KVEXPRESSIONS", isList = true, optional = true, doc = "A list of expressions for use with key value objects.")
	public void setKVExpressions(List<String[]> kvExpressions) {
		this.kvExpressions = kvExpressions;
		setOutputSchema(null);
	}

	public List<String[]> getKVExpressions() {
		return this.kvExpressions;
	}

	/**
	 * Sets the number of threads used for processing the expressions. A value
	 * greater than 1 indicates the number of simultaneous processing. A value
	 * equal to 1 or 0 indicates that no threads should be used. And a value
	 * lower than 0 indicates automatic threads number selection based on the
	 * number of expressions and the number of available processors.
	 * 
	 * @param threads
	 *            The number of threads
	 */
	@Parameter(type = IntegerParameter.class, name = "threads", optional = true, doc = "Number of threads used to calculate the result.")
	public void setThreads(int threads) {
		this.threads = threads;
	}

	/**
	 * Gets the number of threads used for processing the expressions
	 * 
	 * @return The number of threads
	 */
	public int getThreads() {
		return threads;
	}

	public boolean isEvaluateOnPunctuation() {
		return evaluateOnPunctuation;
	}

	@Parameter(type = BooleanParameter.class, name = "evaluateOnPunctuation", optional = true, doc = "If set to true, map will also create an output (with the last read element) when it receives a punctuation.")
	public void setEvaluateOnPunctuation(boolean evaluateOnPunctuation) {
		this.evaluateOnPunctuation = evaluateOnPunctuation;
	}

	@Parameter(type = BooleanParameter.class, name = "allowNull", optional = true, doc = "If set to true and an error occurs in calculation a null value is added to the element. Else the element is skipped and no output is produced.")
	public void setAllowNullValue(boolean allowNullValue) {
		this.allowNullValue = allowNullValue;
	}

	public boolean isAllowNullValue() {
		return allowNullValue;
	}

	public boolean isSuppressErrors() {
		return suppressErrors;
	}

	@Parameter(type = BooleanParameter.class, name = "suppressErrors", optional = true, doc = "If set to true calculation errors will not appear in log or console. Could be helpful in scenarios where null values are allowed.")
	public void setSuppressErrors(boolean suppressErrors) {
		this.suppressErrors = suppressErrors;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		calcOutputSchema();
		return getOutputSchema();
	}

	@Override
	public boolean isValid() {
		if ((getInputSchema().getType() == KeyValueObject.class || getInputSchema()
				.getType() == NestedKeyValueObject.class)) {
			if ((this.kvExpressions != null && !this.kvExpressions.isEmpty())) {
				return true;
			} else {
				addError("Parameter KVEXPRESSIONS has to be set.");
				return false;
			}
		} else if ((this.namedExpressions != null && !this.namedExpressions
				.isEmpty())) {
			return true;
		} else {
			addError("Parameter EXPRESSIONS has to be set.");
			return false;
		}
	}

	@Override
	public void initialize() {
		calcOutputSchema();
	}

	@Override
	public MapAO clone() {
		return new MapAO(this);
	}

}
