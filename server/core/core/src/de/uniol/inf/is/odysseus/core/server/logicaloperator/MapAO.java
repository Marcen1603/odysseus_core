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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MAP", doc = "Performs a mapping of incoming attributes to out-coming attributes using map functions. Odysseus also provides a wide range of mapping functions. Hint: Map is stateless. To used Map in a statebased fashion see: StateMap", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Map+operator", category = {
		LogicalOperatorCategory.BASE })
public class MapAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2120387285754464451L;
	private List<NamedExpression> namedExpressions;
	private List<SDFExpression> expressions;
	/** The number of threads used for processing the expressions. */
	private int threads = 0;
	private boolean allowNullValue = true;
	private boolean suppressErrors = false;

	private boolean keepInput = false;
	private List<SDFAttribute> removeAttributes;

	// Punctuation handling
	private boolean evaluateOnPunctuation = false;
	private boolean expressionsUpdateable = false;

	public MapAO() {
		super();
	}

	public MapAO(MapAO ao) {
		super(ao);
		this.setExpressions(ao.namedExpressions);
		this.threads = ao.threads;
		this.evaluateOnPunctuation = ao.evaluateOnPunctuation;
		this.expressionsUpdateable = ao.isExpressionsUpdateable();
		this.allowNullValue = ao.allowNullValue;
		this.suppressErrors = ao.suppressErrors;
		this.keepInput = ao.keepInput;
		this.removeAttributes = ao.removeAttributes;
	}

	public List<SDFExpression> getExpressionList() {
		return Collections.unmodifiableList(expressions);
	}

	private void calcOutputSchema() {

		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		SDFSchema inputSchema = getInputSchema();

		if (keepInput) {
			if (removeAttributes == null || removeAttributes.size() == 0) {
				attrs.addAll(inputSchema.getAttributes());
			} else {
				for (SDFAttribute keepAttribute : inputSchema.getAttributes()) {
					if (!removeAttributes.contains(keepAttribute)) {
						attrs.add(keepAttribute);
					}
				}
			}
		}

		/*
		 * The expressions are null, which should not happen, they should at least be an
		 * empty list. Stop here.
		 */
		if (this.namedExpressions == null) {
			return;
		}

		for (NamedExpression expr : namedExpressions) {

			// TODO: Maybe an attribute resolver should be used here?

			SDFAttribute attr = null;
			IMepExpression<?> mepExpression = expr.expression.getMEPExpression();
			String exprString;
			boolean isOnlyAttribute = false;

			exprString = expr.expression.toString();
			// Replace '(' and ')' so the expression will not be recognized
			// as expression in the next operator (e.g. if it is a map)
			exprString = SDFAttribute.replaceSpecialChars(exprString);

			// Variable could be source.name oder name, we are looking for name!
			String lastString = null;
			String toSplit;

			toSplit = exprString;

			String[] split = SDFElement.splitURI(toSplit);
			final SDFElement elem;
			if (split[1] != null && split[1].length() > 0) {
				elem = new SDFElement(split[0], split[1]);
			} else {
				elem = new SDFElement(null, split[0]);
			}

			// If expression is an attribute use this data type
			List<SDFAttribute> inAttribs = expr.expression.getAllAttributes();
			for (SDFAttribute attributeToCheck : inAttribs) {
				SDFAttribute attribute;
				String attributeURI = attributeToCheck.getURI();
				if (attributeURI.startsWith("__")) {
					String realAttributeName = attributeURI.substring(attributeURI.indexOf(".") + 1);
					split = SDFElement.splitURI(realAttributeName);
					if (split.length > 1) {
						attribute = new SDFAttribute(split[0], split[1], attributeToCheck);
					} else {
						attribute = new SDFAttribute(null, split[0], attributeToCheck);
					}
				} else {
					attribute = attributeToCheck;
				}
				if (attribute.equalsCQL(elem)) {
					if (lastString != null) {
						String attrName = elem.getURIWithoutQualName() != null
								? elem.getURIWithoutQualName() + "." + elem.getQualName()
								: elem.getQualName();
						attr = new SDFAttribute(lastString, attrName, attribute.getDatatype(), attribute.getUnit(),
								attribute.getDtConstraints());
					} else {
						attr = new SDFAttribute(elem.getURIWithoutQualName(), elem.getQualName(),
								attribute.getDatatype(), attribute.getUnit(), attribute.getDtConstraints());
					}
					isOnlyAttribute = true;
				}
			}

			// Expression is an attribute and name is set --> keep attribute type
			if (isOnlyAttribute) {
				if (!"".equals(expr.name)) {
					if (attr != null && attr.getSourceName() != null && !attr.getSourceName().startsWith("__")) {
						attr = new SDFAttribute(attr.getSourceName(), expr.name, attr);
					} else {
						attr = new SDFAttribute(null, expr.name, attr);
					}
				}
			}

			// else use the expression data type
			if (attr == null) {
				// Special handling if return type is tuple
				if (mepExpression.getReturnType() == SDFDatatype.TUPLE) {
					int card = mepExpression.getReturnTypeCard();
					for (int i = 0; i < card; i++) {
						String name = !"".equals(expr.name) ? expr.name : exprString;

						// Calculate the constraints for the output
						Collection<SDFConstraint> allConstraints = getExpressionConstraints(expr);

						attr = new SDFAttribute(null, name + "_" + i, mepExpression.getReturnType(i), null,
								allConstraints, null);
						attrs.add(attr);
					}

				} else {
					SDFDatatype retType = expr.datatype != null ? expr.datatype : mepExpression.getReturnType();

					// Calculate the constraints for the output
					Collection<SDFConstraint> allConstraints = getExpressionConstraints(expr);

					attr = new SDFAttribute(null, !"".equals(expr.name) ? expr.name : exprString, retType, null,
							allConstraints, null);
					attrs.add(attr);
				}
			} else {
				attrs.add(attr);
			}

		}

		SDFSchema s = SDFSchema.changeSourceName(SDFSchemaFactory.createNewWithAttributes(attrs, inputSchema),
				inputSchema.getURI(), false);
		// check if all attributes are distinct
		Set<String> amNames = s.checkNames();
		if (amNames.size() > 0) {
			throw new TransformationException(
					"Output schema of " + this.getName() + " contains multiple occurences of attributes " + amNames);
		}

		setOutputSchema(s);
	}

	/**
	 * The constraints by the expression.
	 * 
	 * @param expression
	 *            The expression from which the added constraints are needed
	 * @return The constraints added by the expression
	 */
	private Collection<SDFConstraint> getExpressionConstraints(NamedExpression expression) {

		/*
		 * The expressions themselves can add constraints. For example to tell that
		 * their output is a temporal type.
		 */
		return expression.expression.getMEPExpression().getConstraintsToAdd();
	}

	@Parameter(type = NamedExpressionParameter.class, name = "EXPRESSIONS", aliasname = "kvExpressions", isList = true, optional = false, doc = "A list of expressions.")
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

	@Parameter(type = BooleanParameter.class, name = "keepInput", optional = true, doc = "If set to true, all attributes of the input are also part of the output, so there is no need to repeat all attributes.")
	public void setKeepInput(boolean keepInput) {
		this.keepInput = keepInput;
	}

	public boolean isKeepInput() {
		return keepInput;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "removeAttributes", optional = true, isList = true, doc = "If keepInput is set to true, you can here provides attributes that should not be part of the output.")
	public void setRemoveAttributes(List<SDFAttribute> removeAttributes) {
		this.removeAttributes = removeAttributes;
	}

	public List<SDFAttribute> getRemoveAttributes() {
		return removeAttributes;
	}

	/**
	 * Sets the number of threads used for processing the expressions. A value
	 * greater than 1 indicates the number of simultaneous processing. A value equal
	 * to 1 or 0 indicates that no threads should be used. And a value lower than 0
	 * indicates automatic threads number selection based on the number of
	 * expressions and the number of available processors.
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

	@Parameter(type = BooleanParameter.class, name = "allowNull", optional = true, doc = "If set to true (default) and an error occurs in calculation a null value is added to the element. Else the element is skipped and no output is produced. Default is true.")
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

	public boolean isExpressionsUpdateable() {
		return expressionsUpdateable;
	}

	@Parameter(type = BooleanParameter.class, name = "expressionsUpdateable", optional = true, doc = "If set to true, the expressions can be updated with punctuations Does not work in threaded mode.")
	public void setExpressionsUpdateable(boolean expressionsUpdateable) {
		this.expressionsUpdateable = expressionsUpdateable;
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

	@Override
	public boolean isValid() {
		boolean valid = true;
		if (removeAttributes != null && removeAttributes.size() > 0 && !keepInput) {
			addError("When using removeAttributes, keepInput must be set to true!");
			valid = false;
		}

		return valid && super.isValid();
	}

	/**
	 * There should be no restriction for stateless operators
	 */
	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return InputOrderRequirement.NONE;
	}

}
