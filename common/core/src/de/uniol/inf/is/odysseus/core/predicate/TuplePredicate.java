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
package de.uniol.inf.is.odysseus.core.predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class TuplePredicate {

	Logger logger = LoggerFactory.getLogger(TuplePredicate.class);

	private SDFExpression expression;

	// stores which attributes are needed at which position for
	// variable bindings
	protected int[] attributePositions;

	final List<SDFAttribute> neededAttributes;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	protected boolean[] fromRightChannel;

	protected Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

	protected SDFSchema leftSchema;
	protected SDFSchema rightSchema;

	public TuplePredicate(SDFExpression expression) {
		this.expression = expression;
		this.neededAttributes = expression.getAllAttributes();
	}


	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		init(leftSchema, rightSchema, true);
	}

	public void init(SDFSchema leftSchema, SDFSchema rightSchema, boolean checkRightSchema) {
		// logger.debug("Init ("+this+"): Left "+leftSchema+" Right "+rightSchema);
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;

		List<SDFAttribute> neededAttributes = expression.getAllAttributes();
		this.attributePositions = new int[neededAttributes.size()];
		this.fromRightChannel = new boolean[neededAttributes.size()];

		int i = 0;
		for (SDFAttribute curAttribute : neededAttributes) {
			if (curAttribute == null) {
				throw new IllegalArgumentException("Needed attribute for expression "+expression+" may not be null!");
			}
			int pos = indexOf(leftSchema, curAttribute);
			if (pos == -1) {
				if (rightSchema == null && checkRightSchema) {
					throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + leftSchema + " and rightSchema is null!");
				}
				if (checkRightSchema) {
					// if you get here, there is an attribute
					// in the predicate that does not exist
					// in the left schema, so there must also be
					// a right schema
					pos = indexOf(rightSchema, curAttribute);
					if (pos == -1) {
						throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + rightSchema);
					}
				}
				this.fromRightChannel[i] = true;
			}
			this.attributePositions[i++] = pos;
		}
	}

	private int indexOf(SDFSchema schema, SDFAttribute attr) {
		SDFAttribute cqlAttr = getReplacement(attr);
		Iterator<SDFAttribute> it = schema.iterator();
		for (int i = 0; it.hasNext(); ++i) {
			SDFAttribute a = it.next();
			if (cqlAttr.equalsCQL(a)) {
				return i;
			}
		}
		return -1;
	}

	private SDFAttribute getReplacement(SDFAttribute a) {
		SDFAttribute ret = a;
		SDFAttribute tmp = null;
		while ((tmp = replacementMap.get(ret)) != null) {
			ret = tmp;
		}
		return ret;
	}

	public TuplePredicate(TuplePredicate predicate) {
		this.attributePositions = predicate.attributePositions == null ? null : (int[]) predicate.attributePositions.clone();
		this.fromRightChannel = predicate.fromRightChannel == null ? null : (boolean[]) predicate.fromRightChannel.clone();
		this.expression = predicate.expression == null ? null : predicate.expression.clone();
		this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(predicate.replacementMap);
		this.neededAttributes = new ArrayList<SDFAttribute>(predicate.neededAttributes);
		// logger.debug("Cloned "+this+ " "+attributePositions);
	}


	public boolean evaluate(Tuple<?> input) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(this.attributePositions[i]);
		}
//		this.expression.bindMetaAttribute(input.getMetadata());
//		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}


	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = r.getAttribute(this.attributePositions[i]);
		}

		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

//	public boolean evaluate(Tuple<?> input, KeyValueObject<?> additional) {
//		Object[] values = new Object[neededAttributes.size()];
//
//		for (int i = 0; i < neededAttributes.size(); ++i) {
//			if (!fromRightChannel[i]) {
//				values[i] = input.getAttribute(this.attributePositions[i]);
//			} else {
//				values[i] = additional.getAttribute(neededAttributes.get(i).getURI());
//			}
//		}
////		this.expression.bindMetaAttribute(input.getMetadata());
////		this.expression.bindAdditionalContent(input.getAdditionalContent());
//		this.expression.bindVariables(values);
//		return (Boolean) this.expression.getValue();
//	}

	@Override
	public TuplePredicate clone() {
		return new TuplePredicate(this);
	}

	@Override
	public String toString() {
		return this.expression.toString();
	}

	public List<SDFAttribute> getAttributes() {
		return Collections.unmodifiableList(this.expression.getAllAttributes());
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TuplePredicate)) {
			return false;
		}
		return this.expression.equals(((TuplePredicate) other).expression);
	}

	@Override
	public int hashCode() {
		return 23 * this.expression.hashCode();
	}

	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		if (!curAttr.equals(newAttr)) {
			replacementMap.put(curAttr, newAttr);
		} else {
			logger.warn("Replacement " + curAttr + " --> " + newAttr + " not added because they are equal!");
		}
	}


	public SDFExpression getExpression() {
		return expression;
	}


}
