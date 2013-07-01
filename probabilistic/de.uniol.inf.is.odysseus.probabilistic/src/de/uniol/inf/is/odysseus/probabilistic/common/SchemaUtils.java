/*
 * Copyright 2013 The Odysseus Team
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

package de.uniol.inf.is.odysseus.probabilistic.common;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.EqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticPredicate;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * Utility class for transformation rules.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public final class SchemaUtils {
	/** Data type used in transformation. */
	public static final String DATATYPE = "probabilistic";

	/**
	 * Returns true if this list contains a probabilistic attribute. More formally, returns true if and only if this list contains at least one attribute that is an {@link SDFProbabilisticDatatype probabilistic attribute}
	 * 
	 * @param attributes
	 *            The list of {@link SDFAttribute attributes}
	 * @return <code>true</code> if this list contains a {@link SDFProbabilisticDatatype probabilistic attribute}
	 */
	public static boolean containsProbabilisticAttributes(final List<SDFAttribute> attributes) {
		boolean containsProbabilisticAttribute = false;
		for (final SDFAttribute attribute : attributes) {
			if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
				containsProbabilisticAttribute = true;
			}
		}
		return containsProbabilisticAttribute;
	}

	/**
	 * Returns true if this list contains a continuous probabilistic attribute. More formally, returns true if and only if this list contains at least one attribute that is an {@link SDFProbabilisticDatatype probabilistic attribute} that is continuous
	 * 
	 * @param attributes
	 *            The list of {@link SDFAttribute attributes}
	 * @return <code>true</code> if this list contains a continuous {@link SDFProbabilisticDatatype probabilistic attribute}
	 */
	public static boolean containsContinuousProbabilisticAttributes(final List<SDFAttribute> attributes) {
		if (attributes != null) {
			for (final SDFAttribute attribute : attributes) {
				if (isContinuousProbabilisticAttribute(attribute)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if this attribute is a continuous probabilistic attribute. More formally, returns true if and only if this attribute is an {@link SDFProbabilisticDatatype probabilistic attribute} that is continuous
	 * 
	 * @param attribute
	 *            The {@link SDFAttribute attribute}
	 * @return <code>true</code> if this attribute is a continuous {@link SDFProbabilisticDatatype probabilistic attribute}
	 */
	public static boolean isContinuousProbabilisticAttribute(final SDFAttribute attribute) {
		if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
			SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute.getDatatype();
			if (datatype.isContinuous()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this list contains a discrete probabilistic attribute. More formally, returns true if and only if this list contains at least one attribute that is an {@link SDFProbabilisticDatatype probabilistic attribute} that is discrete
	 * 
	 * @param attributes
	 *            The list of {@link SDFAttribute attributes}
	 * @return <code>true</code> if this list contains a discrete {@link SDFProbabilisticDatatype probabilistic attribute}
	 */
	public static boolean containsDiscreteProbabilisticAttributes(final List<SDFAttribute> attributes) {
		if (attributes != null) {
			for (final SDFAttribute attribute : attributes) {
				if (isDiscreteProbabilisticAttribute(attribute)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if this attribute is a discrete probabilistic attribute. More formally, returns true if and only if this attribute is an {@link SDFProbabilisticDatatype probabilistic attribute} that is discrete
	 * 
	 * @param attribute
	 *            The {@link SDFAttribute attribute}
	 * @return <code>true</code> if this attribute is a discrete {@link SDFProbabilisticDatatype probabilistic attribute}
	 */
	public static boolean isDiscreteProbabilisticAttribute(final SDFAttribute attribute) {
		if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
			SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute.getDatatype();
			if (datatype.isDiscrete()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the position indexes all continuous probabilistic attributes in the schema.
	 * 
	 * @param schema
	 *            The {@link SDFSchema schema}
	 * @return An array of all attribute indexes in the schema that are continuous {@link SDFProbabilisticDatatype probabilistic attributes}
	 */
	public static int[] getContinuousProbabilisticAttributePos(final SDFSchema schema) {
		List<SDFAttribute> attributes = getContinuousProbabilisticAttributes(schema);
		int[] pos = new int[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			SDFAttribute attribute = attributes.get(i);
			pos[i] = schema.indexOf(attribute);
		}
		return pos;
	}

	/**
	 * Returns the position indexes all discrete probabilistic attributes in the schema.
	 * 
	 * @param schema
	 *            The {@link SDFSchema schema}
	 * @return An array of all attribute indexes in the schema that are discrete {@link SDFProbabilisticDatatype probabilistic attributes}
	 */
	public static int[] getDiscreteProbabilisticAttributePos(final SDFSchema schema) {
		List<SDFAttribute> attributes = getDiscreteProbabilisticAttributes(schema);
		int[] pos = new int[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			SDFAttribute attribute = attributes.get(i);
			pos[i] = schema.indexOf(attribute);
		}
		return pos;
	}

	/**
	 * Returns all attributes from the schema that are continuous probabilistic attributes.
	 * 
	 * @param schema
	 *            The {@link SDFSchema schema}
	 * @return A list of all attributes in the schema that are continuous {@link SDFProbabilisticDatatype probabilistic attributes}
	 */
	public static List<SDFAttribute> getContinuousProbabilisticAttributes(final SDFSchema schema) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute attribute : schema.getAttributes()) {
			if (isContinuousProbabilisticAttribute(attribute)) {
				attributes.add(attribute);
			}
		}
		return attributes;
	}

	/**
	 * Returns all attributes from the schema that are discrete probabilistic attributes.
	 * 
	 * @param schema
	 *            The {@link SDFSchema schema}
	 * @return A list of all attributes in the schema that are discrete {@link SDFProbabilisticDatatype probabilistic attributes}
	 */

	public static List<SDFAttribute> getDiscreteProbabilisticAttributes(final SDFSchema schema) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute attribute : schema.getAttributes()) {
			if (isDiscreteProbabilisticAttribute(attribute)) {
				attributes.add(attribute);
			}
		}
		return attributes;
	}

	/**
	 * Returns the indexes of the given list of attributes in the given schema.
	 * 
	 * @param schema
	 *            The schema
	 * @param attributes
	 *            The list of attributes
	 * @return An array with the idnexes of the attributes in the schema
	 */
	public static int[] getAttributePos(final SDFSchema schema, final List<SDFAttribute> attributes) {
		int[] pos = new int[attributes.size()];
		int i = 0;
		for (SDFAttribute attribute : attributes) {
			if (!schema.contains(attribute)) {
				throw new IllegalArgumentException("No such attribute " + attribute + " in schema " + schema);
			} else {
				pos[i] = schema.indexOf(attribute);
				i++;
			}
		}
		return pos;
	}

	/**
	 * Return true if the given expression is of the form:
	 * 
	 * A.x=B.y AND A.y=B.z AND * ...
	 * 
	 * @param expression
	 *            The expression
	 * @return <code>true</code> iff the expression is of the given form
	 */
	public static boolean isEquiExpression(final IExpression<?> expression) {
		if (expression instanceof AndOperator) {
			return isEquiExpression(((AndOperator) expression).getArgument(0)) && isEquiExpression(((AndOperator) expression).getArgument(1));

		}
		if (expression instanceof EqualsOperator) {
			EqualsOperator eq = (EqualsOperator) expression;
			IExpression<?> arg1 = eq.getArgument(0);
			IExpression<?> arg2 = eq.getArgument(1);
			if ((arg1 instanceof Variable) && (arg2 instanceof Variable)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return true if the given relational predicate is of the form:
	 * 
	 * A.x=B.y AND A.y=B.z AND * ...
	 * 
	 * @param predicate
	 *            The relational predicate
	 * @return <code>true</code> iff the relational predicate is of the given form
	 */
	public static boolean isEquiPredicate(final RelationalPredicate predicate) {
		IExpression<?> expression = predicate.getExpression().getMEPExpression();
		return isEquiExpression(expression);
	}

	/**
	 * Return true if the given relational predicate is of the form:
	 * 
	 * A.x=B.y AND A.y=B.z AND * ...
	 * 
	 * @param predicate
	 *            The relational predicate
	 * @return <code>true</code> iff the relational predicate is of the given form
	 */
	public static boolean isEquiPredicate(final ProbabilisticPredicate predicate) {
		IExpression<?> expression = predicate.getExpression().getMEPExpression();
		return isEquiExpression(expression);
	}

	/**
	 * Utility constructor.
	 */
	private SchemaUtils() {
		throw new UnsupportedOperationException();
	}
}
