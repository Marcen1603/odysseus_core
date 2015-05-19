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
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalPredicate extends AbstractRelationalPredicate<Tuple<?>> {

	Logger logger = LoggerFactory.getLogger(RelationalPredicate.class);

	private static final long serialVersionUID = 1222104352250883947L;

	// protected SDFExpression expression;

	// stores which attributes are needed at which position for
	// variable bindings
	// protected int[] attributePositions;

	// final List<SDFAttribute> neededAttributes;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	// protected boolean[] fromRightChannel;

	// protected Map<SDFAttribute, SDFAttribute> replacementMap = new
	// HashMap<SDFAttribute, SDFAttribute>();

	// protected SDFSchema leftSchema;
	// protected SDFSchema rightSchema;

	public RelationalPredicate(SDFExpression expression) {
		// this.expression = expression;
		// this.neededAttributes = expression.getAllAttributes();
		super(expression);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<IPredicate> splitPredicate() {
		List<IPredicate> result = new LinkedList<IPredicate>();
		if (isAndPredicate()) {
			Stack<IExpression<?>> expressionStack = new Stack<IExpression<?>>();
			expressionStack.push(expression.getMEPExpression());

			while (!expressionStack.isEmpty()) {
				IExpression<?> curExpression = expressionStack.pop();
				if (isAndExpression(curExpression)) {
					expressionStack.push(curExpression.toFunction()
							.getArgument(0));
					expressionStack.push(curExpression.toFunction()
							.getArgument(1));
				} else {
					SDFExpression expr = new SDFExpression(curExpression,
							expression.getAttributeResolver(),
							MEP.getInstance());
					RelationalPredicate relationalPredicate = new RelationalPredicate(
							expr);
					relationalPredicate.init(expression.getSchema(), false);
					result.add(relationalPredicate);
				}
			}
			return result;

		}
		result.add(this);
		return result;
	}



	public RelationalPredicate(RelationalPredicate predicate) {
		super(predicate);
	}

	@Override
	public boolean evaluate(Tuple<?> input) {
		Object[] values;
		try {
			values = new Object[this.attributePositions.length];
		} catch (NullPointerException e) {
			if (attributePositions == null) {
				throw new IllegalStateException("The predicate "
						+ this.expression + " is not initialized!");
			} else {
				throw e;
			}

		}

		for (int i = 0; i < values.length; ++i) {
			values[i] = getValue(input, i);
		}
		this.expression.bindMetaAttribute(input.getMetadata());
		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	protected Object getValue(Tuple<?> input, int i) {
		Object value = null;
		if (this.attributePositions[i].getE1() == -1) {
			value = input.getAttribute(this.attributePositions[i]
					.getE2());
		}else{
			value = input.getMetadata().getValue(attributePositions[i].getE1(), attributePositions[i].getE2());
		}
		return value;
	}

	@Override
	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		// TODO: IMetaAttribute
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = getValue(r,i);
		}
		Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
		additionalContent.putAll(left.getAdditionalContent());
		additionalContent.putAll(right.getAdditionalContent());

		// FIXME Merge meta data
		// this.expression.bindMetaAttribute();
		this.expression.bindAdditionalContent(additionalContent);
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	public boolean evaluate(Tuple<?> input, KeyValueObject<?> additional) {
		Object[] values = new Object[neededAttributes.size()];

		for (int i = 0; i < neededAttributes.size(); ++i) {
			if (!fromRightChannel[i]) {
				
				values[i] = getValue(input,i);
			} else {
				values[i] = additional.getAttribute(neededAttributes.get(i)
						.getURI());
			}
		}
		this.expression.bindMetaAttribute(input.getMetadata());
		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public RelationalPredicate clone() {
		return new RelationalPredicate(this);
	}


	@Override
	public boolean equals(Object other) {
		if (!(other instanceof RelationalPredicate)) {
			return false;
		}
		return this.expression.equals(((RelationalPredicate) other).expression);
	}

	@Override
	public int hashCode() {
		return 23 * this.expression.hashCode();
	}


	@Override
	public boolean isAlwaysTrue() {
		return expression.isAlwaysTrue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<Tuple<?>> and(IPredicate<Tuple<?>> predicate) {
		if (predicate instanceof RelationalPredicate) {
			SDFExpression expr = ((RelationalPredicate) predicate).expression;
			AndOperator and = new AndOperator();
			and.setArguments(new IExpression<?>[] {
					expression.getMEPExpression(), expr.getMEPExpression() });
			RelationalPredicate andPredicate = new RelationalPredicate(
					new SDFExpression(and.toString(), MEP.getInstance()));
			return andPredicate;
		}
		return super.and(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<Tuple<?>> or(IPredicate<Tuple<?>> predicate) {
		if (predicate instanceof RelationalPredicate) {
			SDFExpression expr = ((RelationalPredicate) predicate).expression;
			OrOperator or = new OrOperator();
			or.setArguments(new IExpression<?>[] {
					expression.getMEPExpression(), expr.getMEPExpression() });
			// We need to reparse the expression because of multiple instances
			// of the same variable may exist
			RelationalPredicate orPredicate = new RelationalPredicate(
					new SDFExpression(or.toString(), MEP.getInstance()));
			return orPredicate;
		}
		return super.or(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<Tuple<?>> not() {
		NotOperator not = new NotOperator();
		not.setArguments(new IExpression<?>[] { expression.getMEPExpression() });
		RelationalPredicate notPredicate = new RelationalPredicate(
				new SDFExpression(not, expression.getAttributeResolver(),
						MEP.getInstance()));
		return notPredicate;
	}

	public static void main(String[] args) {
		SDFAttribute a = new SDFAttribute("", "p_out", SDFDatatype.DOUBLE,
				null, null, null);
		SDFSchema schema = SDFSchemaFactory.createNewTupleSchema("", a);
		RelationalPredicate pred = new RelationalPredicate(new SDFExpression(
				"p_out <=0 || isNaN(p_out)", MEP.getInstance()));

		System.out.println(pred.toString());
		pred.init(schema, null, false);
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, 8);
		KeyValueObject<IMetaAttribute> additional = new KeyValueObject<IMetaAttribute>();
		additional.setAttribute("b", 5);
		System.out.println(pred.evaluate(tuple, additional));
	}

}
