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
package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionParser;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Jonas Jacobi
 */
public class SDFExpression implements Serializable, IClone {

	private static final long serialVersionUID = 8658794141096208317L;
	// Für P2P als transient gekennzeichnet
	transient ArrayList<Variable> variableArrayList = new ArrayList<Variable>();

	/** The additional content */
	private Map<String, Serializable> additionalContent;

	/** The meta attributes */
	private IMetaAttribute[] metaAttribute;
	private int varCounter;
	private IExpression<?> expression;

	private String expressionString;

	private Object value;

	private List<SDFAttribute> attributes;

	/**
	 * Using prediction functions you know in advance of query processing, which
	 * attributes have to be used and where they are located in the schema. So
	 * the attribute positions can be initialized in advance.
	 */
	private int[] attributePositions;

	private IAttributeResolver attributeResolver;
	/** The schema */
	private List<SDFSchema> schema;

	private transient IExpressionParser expressionParser;
	
	Pattern pattern = null;


	/**
	 * @param URI
	 * @param value
	 * @param attributeResolver
	 * @throws ParseException
	 */
	public SDFExpression(String URI, String value,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser, Pattern aggregatePattern)
			throws SDFExpressionParseException {
		this.pattern = aggregatePattern;
		init(null, value, attributeResolver, expressionParser);
	}

	/**
	 * @param URI
	 * @param value
	 * @param schema
	 * @throws ParseException
	 */
	// public SDFExpression(String URI, String value, SDFSchema schema,
	// IExpressionParser expressionParser)
	// throws SDFExpressionParseException {
	// init(null, value, schema, expressionParser);
	// }

	/**
	 * @param URI
	 * @param value
	 * @param attributeResolver
	 * @throws ParseException
	 */
	public SDFExpression(String value, IExpressionParser expressionParser)
			throws SDFExpressionParseException {
		init(null, value, null, expressionParser);
	}

	public SDFExpression(SDFExpression expression)
			throws SDFExpressionParseException {
		init(expression.expression, expression.expressionString,
				expression.attributeResolver, expression.expressionParser);

		if (expression.attributePositions != null) {
			this.attributePositions = new int[expression.attributePositions.length];
			for (int i = 0; i < expression.attributePositions.length; i++) {
				this.attributePositions[i] = expression.attributePositions[i];
			}
		}
	}

	public SDFExpression(IExpression<?> expression,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser) {
		init(expression, null, attributeResolver, expressionParser);
	}

	// public SDFExpression(IExpression<?> expression, SDFSchema schema,
	// IExpressionParser expressionParser) {
	// init(expression, null, schema, expressionParser);
	// }

	public SDFExpression(IExpression<?> expression,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser, String expressionString) {
		init(expression, null, attributeResolver, expressionParser);
		this.expressionString = expressionString;
	}

	// public SDFExpression(IExpression<?> expression, SDFSchema schema,
	// IExpressionParser expressionParser,
	// String expressionString) {
	// init(expression, null, schema, expressionParser);
	// this.expressionString = expressionString;
	// }

	// private void init(IExpression<?> expre, String value, SDFSchema schema,
	// IExpressionParser expressionParser) {
	// }

	private void init(IExpression<?> expre, String value,
			IAttributeResolver attributeResolver,
			IExpressionParser expressionParser) {
		this.expressionParser = expressionParser;
		if (expre != null) {
			this.expression = expre;
			this.expressionString = expression.toString();
		} else {
			expressionString = value.trim();
		}
		this.varCounter = 0;
		this.variableArrayList = new ArrayList<Variable>();
		this.attributes = new ArrayList<SDFAttribute>();
		if (attributeResolver != null) {
			this.attributeResolver = attributeResolver.clone();
			this.schema = this.attributeResolver.getSchema();
		} else {
			// Try to determine own attribute resolver from expression
			try {
				IExpression<?> tmpExpression = expressionParser.parse(
						expressionString);
				this.attributeResolver = new DirectAttributeResolver(
						tmpExpression.getVariables());
				this.schema = this.attributeResolver.getSchema();
				// IExpression<?> tmpExpression =
				// expressionParser.parse(expressionString, null);
				// List<SDFAttribute> attribs = new ArrayList<SDFAttribute>();
				// for (Variable var : tmpExpression.getVariables()) {
				// String[] split =
				// var.getIdentifier().split(Pattern.quote("."));
				// if (split.length > 2) {
				// attribs.add(new SDFAttribute(null, split[split.length -
				// 1], var.getReturnType()));
				// }
				// else if (split.length == 2) {
				// attribs.add(new SDFAttribute(split[0], split[1],
				// var.getReturnType()));
				// }
				// else if (split.length == 1) {
				// attribs.add(new SDFAttribute(null, split[0],
				// var.getReturnType()));
				// }
				// }
				// this.schema = new SDFSchema("", attribs);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		Map<String, String> aliasToAggregationAttributeMapping = new HashMap<String, String>();

		if (expression == null) {
			String result = substituteAggregations(this.expressionString,
					aliasToAggregationAttributeMapping);
			try {
				this.expression = expressionParser.parse(result, this.schema);
				expressionString = expression.toString();
			} catch (Throwable e) {
				System.err.println("Expr: " + this.expressionString);
				e.printStackTrace();
				throw new SDFExpressionParseException(e);
			}
		}

		initVariables(this.expression.getVariables(),
				aliasToAggregationAttributeMapping);

		if (this.expression instanceof Constant) {
			setValue(expression.getValue());
		}
		if (this.expression instanceof IFunction) {
			IFunction<?> function = (IFunction<?>) expression;
	        function.propagateAdditionalContentReference(function.getAdditionalContents());
	        function.propagateMetadataReference(function.getMetaAttributeContainer());
			setAdditionalContent(function.getAdditionalContents());
			setMetaAttribute(function.getMetaAttributeContainer());
		}
	}

	public List<SDFSchema> getSchema() {
		return this.schema;
	}
	
	private String substituteAggregations(String value,
			Map<String, String> inverseAliasMappings) {
		String result = "";
		{
			if (pattern == null)
				return value;
			Matcher m2 = pattern.matcher(value);
			Map<String, String> aliasMappings = new HashMap<String, String>();
			int start = 0;
			while (m2.find()) {
				String group = m2.group(1);
				if (group == null) {
					continue;
				}

				SDFAttribute attribute = this.attributeResolver
						.getAttribute(group);
				// SDFAttribute attribute = this.schema.findAttribute(group);
				if (attribute == null) {
					System.err.println("no such attribute: " + group);
					throw new SDFExpressionParseException("No such attribute: "
							+ group);
				}
				String attributeName = attribute.getURI();
				String aliasName = aliasMappings.get(attributeName);
				if (aliasName == null) {
					aliasName = "__V" + ++this.varCounter;
					aliasMappings.put(attributeName, aliasName);
					inverseAliasMappings.put(aliasName, attributeName);
				}

				result += value.substring(start, m2.start(1));
				result += aliasName;
				start = m2.end(1);
			}
			result += value.substring(start);
		}
		return result;
	}

	private void initVariables(Set<Variable> variables2,
			Map<String, String> inverseAliasMappings) {
		for (Variable var : variables2) {
			String name = var.getIdentifier();
			if (inverseAliasMappings.containsKey(name)) {
				name = inverseAliasMappings.get(name);
			}
			SDFAttribute curAttribute = this.attributeResolver
					.getAttribute(name);
			// SDFAttribute curAttribute = this.schema.findAttribute(name);
			if (curAttribute == null && name == "t") {
				this.attributes.add(new SDFAttribute(null, "t",
						SDFDatatype.OBJECT, null, null, null));
			} else {
				this.attributes.add(curAttribute);
			}
			this.variableArrayList.add(var);
		}
	}

	public void initAttributePositions(SDFSchema schema) {
		this.attributePositions = new int[this.attributes.size()];
		int j = 0;
		DirectAttributeResolver dar = new DirectAttributeResolver(schema);
		for (SDFAttribute curAttribute : this.attributes) {
			SDFAttribute resolvedAttribute = dar.getAttribute(curAttribute
					.getAttributeName());
			this.attributePositions[j++] = schema.indexOf(resolvedAttribute);
		}
	}

	public int[] getAttributePositions() {
		return this.attributePositions;
	}

	public SDFDatatype getType() {
		return this.expression.getReturnType();
	}

	private void setValue(Object object) {
		this.value = object;
	}

	/**
	 * Get the additional content under the given name (key)
	 * 
	 * @param name
	 *            The name (key) of the additional content
	 * @return The additional content under that name or <code>null</code> if no
	 *         additional content under that name exists
	 */
	public Serializable getAdditionalContent(String name) {
		return this.additionalContent.get(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) this.value;
	}

	@Override
	public SDFExpression clone() {
		return new SDFExpression(this);
	}

	public List<SDFAttribute> getAllAttributes() {
		return this.attributes;
	}

	@Override
	public String toString() {
		return this.expressionString;
	}

	@SuppressWarnings("unchecked")
	public void bindVariables(Object... values) {
		if (expression instanceof Constant) {
			return;
		}

		if (values.length != variableArrayList.size()) {
			throw new IllegalArgumentException(
					"illegal variable bindings in expression");
		}
		
		for (int i = 0; i < values.length; ++i) {
			Variable variable = variableArrayList.get(i);
//			if(variable.isArray()) {
//				variable.bind(((List<Object>)values[i]).get(variable.getArrayIndex()), null, -1);
//			} else {
				variable.bind(values[i], null, -1);
//			}
		}

		setValue(expression.getValue());
	}

    @SuppressWarnings("unchecked")
	public void bindVariables(IMetaAttribute[] metadata, Object... values) {
        if (expression instanceof Constant) {
            return;
        }

        if (values.length != variableArrayList.size()) {
            throw new IllegalArgumentException("illegal variable bindings in expression");
        }

        for (int i = 0; i < values.length; ++i) {
			Variable variable = variableArrayList.get(i);
//			if(variable.isArray()) {
//				variable.bind(((List<Object>)values[i]).get(variable.getArrayIndex()), metadata[i], -1);
//			} else {
				variable.bind(values[i], metadata[i], -1);
//			}
        }

        setValue(expression.getValue());
    }
	   
	@SuppressWarnings("unchecked")
	public void bindVariables(int[] positions, IMetaAttribute[] metadata, Object... values) {
		if (expression instanceof Constant) {
			return;
		}

		if (values.length != variableArrayList.size()) {
			throw new IllegalArgumentException(
					"illegal variable bindings in expression");
		}

		for (int i = 0; i < values.length; ++i) {
			Variable variable = variableArrayList.get(i);
//			if(variable.isArray()) {
//				variable.bind(((List<Object>)values[i]).get(variable.getArrayIndex()), metadata[i], positions[i]);
//			} else {
				variable.bind(values[i], metadata[i], positions[i]);
//			}
		}

		setValue(expression.getValue());
	}

	/**
	 * Bind the additional content to this expression
	 * 
	 * @param additionalContent
	 *            The additional content to bind
	 */
	public void bindAdditionalContent(
			Map<String, Serializable> additionalContent) {
		if ((getMEPExpression() instanceof Constant)
				|| (getMEPExpression() instanceof Variable)) {
			return;
		}

		this.additionalContent.clear();
		this.additionalContent.putAll(additionalContent);
	}

	/**
	 * Bind the meta attribute to this expression
	 * 
	 * @param metaAttribute
	 *            The meta attributes to bind
	 */
	public void bindMetaAttribute(IMetaAttribute metaAttribute) {
		if ((getMEPExpression() instanceof Constant)
				|| (getMEPExpression() instanceof Variable)) {
			return;
		}

		this.metaAttribute[0] = metaAttribute;
	}

	public ArrayList<Variable> getVariables() {
		return this.variableArrayList;
	}

	@Override
	public int hashCode() {
		final int prime = 19;
		int result = 1;
		result = prime
				* result
				+ ((expressionString == null) ? 0 : expressionString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SDFExpression other = (SDFExpression) obj;

		if (expressionString == null) {
			if (other.expressionString != null)
				return false;
		} else if (!expressionString.equals(other.expressionString))
			return false;
		return true;
	}

	/**
	 * This method is need to be able to determine which kind of SweepArea has
	 * to be used in a join. For this, we check whether this expression contains
	 * other operators than == or not.
	 */
	public IExpression<?> getMEPExpression() {
		return this.expression;
	}

	/**
	 * 
	 * @return The attribute resolver
	 */
	public IAttributeResolver getAttributeResolver() {
		return attributeResolver;
	}

	/**
	 * 
	 * @return The expression parser
	 */
	public IExpressionParser getExpressionParser() {
		return expressionParser;
	}

	/**
	 * 
	 * @return The expression string
	 */
	public String getExpressionString() {
		return expressionString;
	}

	/**
	 * Set the additional content
	 * 
	 * @param additionalContent
	 *            The additional content
	 */
	private void setAdditionalContent(
			Map<String, Serializable> additionalContent) {
		this.additionalContent = additionalContent;
	}

	/**
	 * Set the meta attribute
	 * 
	 * @param metaAttribute
	 *            The meta attribute
	 */
	private void setMetaAttribute(IMetaAttribute[] metaAttribute) {
		this.metaAttribute = metaAttribute;
	}

	public boolean isAlwaysTrue() {
		if (getMEPExpression() instanceof Constant){
			Object o = getMEPExpression().getValue();
			if (o instanceof Boolean){
				return (boolean)o;
			}
		}
		return false;
	}
}
