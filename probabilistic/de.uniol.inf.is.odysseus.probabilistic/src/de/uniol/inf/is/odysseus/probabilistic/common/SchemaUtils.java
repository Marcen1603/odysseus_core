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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.EqualsOperator;
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
     * Returns true if this list contains a probabilistic attribute. More
     * formally, returns true if and only if this list contains at least one
     * attribute that is an {@link SDFProbabilisticDatatype probabilistic
     * attribute}
     * 
     * @param attributes
     *            The {@link Collection attributes}
     * @return <code>true</code> if this list contains a
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean containsProbabilisticAttributes(final Collection<SDFAttribute> attributes) {
    	Objects.requireNonNull(attributes);
    	boolean containsProbabilisticAttribute = false;
        for (final SDFAttribute attribute : attributes) {
            if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
                containsProbabilisticAttribute = true;
            }
        }
        return containsProbabilisticAttribute;
    }

    /**
     * Returns true if this schema contains a continuous probabilistic
     * attribute. More formally, returns true if and only if this schema
     * contains at least one attribute that is an
     * {@link SDFProbabilisticDatatype probabilistic attribute} that is
     * continuous
     * 
     * @param schema
     *            The {@link SDFSchema schema}
     * @return <code>true</code> if this schema contains a continuous
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean containsContinuousProbabilisticAttributes(final SDFSchema schema) {
    	Objects.requireNonNull(schema);
    	Objects.requireNonNull(schema.getAttributes());
            for (final SDFAttribute attribute : schema.getAttributes()) {
                if (SchemaUtils.isContinuousProbabilisticAttribute(attribute)) {
                    return true;
                }
            }
        return false;
    }

    /**
     * Returns true if this list contains a continuous probabilistic attribute.
     * More formally, returns true if and only if this list contains at least
     * one attribute that is an {@link SDFProbabilisticDatatype probabilistic
     * attribute} that is continuous
     * 
     * @param attributes
     *            The {@link Collection attributes}
     * @return <code>true</code> if this list contains a continuous
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean containsContinuousProbabilisticAttributes(final Collection<SDFAttribute> attributes) {
     	Objects.requireNonNull(attributes);
            for (final SDFAttribute attribute : attributes) {
                if (SchemaUtils.isContinuousProbabilisticAttribute(attribute)) {
                    return true;
                }
            }
        return false;
    }

    /**
     * Returns true if this attribute is a continuous probabilistic attribute.
     * More formally, returns true if and only if this attribute is an
     * {@link SDFProbabilisticDatatype probabilistic attribute} that is
     * continuous
     * 
     * @param attribute
     *            The {@link SDFAttribute attribute}
     * @return <code>true</code> if this attribute is a continuous
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean isContinuousProbabilisticAttribute(final SDFAttribute attribute) {
     	Objects.requireNonNull(attribute);
    	if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
            final SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute.getDatatype();
            if (datatype.isContinuous()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this schema contains a discrete probabilistic attribute.
     * More formally, returns true if and only if this schema contains at least
     * one attribute that is an {@link SDFProbabilisticDatatype probabilistic
     * attribute} that is discrete
     * 
     * @param schema
     *            The {@link SDFSchema schema}
     * @return <code>true</code> if this schema contains a discrete
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean containsDiscreteProbabilisticAttributes(final SDFSchema schema) {
     	Objects.requireNonNull(schema);
     	Objects.requireNonNull(schema.getAttributes());
            for (final SDFAttribute attribute : schema.getAttributes()) {
                if (SchemaUtils.isDiscreteProbabilisticAttribute(attribute)) {
                    return true;
                }
            }
        return false;
    }

    /**
     * Returns true if this list contains a discrete probabilistic attribute.
     * More formally, returns true if and only if this list contains at least
     * one attribute that is an {@link SDFProbabilisticDatatype probabilistic
     * attribute} that is discrete
     * 
     * @param attributes
     *            The {@link Collection attributes}
     * @return <code>true</code> if this list contains a discrete
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean containsDiscreteProbabilisticAttributes(final Collection<SDFAttribute> attributes) {
     	Objects.requireNonNull(attributes);
            for (final SDFAttribute attribute : attributes) {
                if (SchemaUtils.isDiscreteProbabilisticAttribute(attribute)) {
                    return true;
                }
            }
        return false;
    }

    /**
     * Returns true if this attribute is a discrete probabilistic attribute.
     * More formally, returns true if and only if this attribute is an
     * {@link SDFProbabilisticDatatype probabilistic attribute} that is discrete
     * 
     * @param attribute
     *            The {@link SDFAttribute attribute}
     * @return <code>true</code> if this attribute is a discrete
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean isDiscreteProbabilisticAttribute(final SDFAttribute attribute) {
     	Objects.requireNonNull(attribute);
    	if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
            final SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute.getDatatype();
            if (datatype.isDiscrete()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this attribute is a discrete probabilistic or continuous
     * probabilistic attribute. More formally, returns true if and only if this
     * attribute is an {@link SDFProbabilisticDatatype probabilistic attribute}
     * that is discrete or continous.
     * 
     * @param attribute
     *            The {@link SDFAttribute attribute}
     * @return <code>true</code> if this attribute is a discrete or continous
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean isProbabilisticAttribute(final SDFAttribute attribute) {
     	Objects.requireNonNull(attribute);
    	return SchemaUtils.isDiscreteProbabilisticAttribute(attribute) || SchemaUtils.isContinuousProbabilisticAttribute(attribute);
    }

    /**
     * Returns the position indexes all continuous probabilistic attributes in
     * the schema.
     * 
     * @param schema
     *            The {@link SDFSchema schema}
     * @return An array of all attribute indexes in the schema that are
     *         continuous {@link SDFProbabilisticDatatype probabilistic
     *         attributes}
     */
    public static int[] getContinuousProbabilisticAttributePos(final SDFSchema schema) {
     	Objects.requireNonNull(schema);
    	final Collection<SDFAttribute> attributes = SchemaUtils.getContinuousProbabilisticAttributes(schema);
        final int[] pos = new int[attributes.size()];
        int i = 0;
        for (final SDFAttribute attribute : attributes) {
            pos[i++] = schema.indexOf(attribute);
        }
        return pos;
    }

    /**
     * Returns the position indexes all discrete probabilistic attributes in the
     * schema.
     * 
     * @param schema
     *            The {@link SDFSchema schema}
     * @return An array of all attribute indexes in the schema that are discrete
     *         {@link SDFProbabilisticDatatype probabilistic attributes}
     */
    public static int[] getDiscreteProbabilisticAttributePos(final SDFSchema schema) {
     	Objects.requireNonNull(schema);
    	final Collection<SDFAttribute> attributes = SchemaUtils.getDiscreteProbabilisticAttributes(schema);
        final int[] pos = new int[attributes.size()];
        int i = 0;
        for (final SDFAttribute attribute : attributes) {
            pos[i++] = schema.indexOf(attribute);
        }
        return pos;
    }

    /**
     * Returns all attributes from the schema that are continuous probabilistic
     * attributes.
     * 
     * @param schema
     *            The {@link SDFSchema schema}
     * @return A list of all attributes in the schema that are continuous
     *         {@link SDFProbabilisticDatatype probabilistic attributes}
     */
    public static Collection<SDFAttribute> getContinuousProbabilisticAttributes(final SDFSchema schema) {
     	Objects.requireNonNull(schema);
     	Objects.requireNonNull(schema.getAttributes());
    	final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
        for (final SDFAttribute attribute : schema.getAttributes()) {
            if (SchemaUtils.isContinuousProbabilisticAttribute(attribute)) {
                attributes.add(attribute);
            }
        }
        return attributes;
    }

    /**
     * Returns all attributes from the list that are continuous probabilistic
     * attributes.
     * 
     * @param attributes
     *            The {@link Collection attributes}
     * @return A list of all attributes in the list that are continuous
     *         {@link SDFProbabilisticDatatype probabilistic attributes}
     */
    public static Collection<SDFAttribute> getContinuousProbabilisticAttributes(final Collection<SDFAttribute> attributes) {
     	Objects.requireNonNull(attributes);
    	final List<SDFAttribute> result = new ArrayList<SDFAttribute>();
        for (final SDFAttribute attribute : attributes) {
            if (SchemaUtils.isContinuousProbabilisticAttribute(attribute)) {
                result.add(attribute);
            }
        }
        return result;
    }

    /**
     * Returns all attributes from the schema that are discrete probabilistic
     * attributes.
     * 
     * @param schema
     *            The {@link SDFSchema schema}
     * @return A list of all attributes in the schema that are discrete
     *         {@link SDFProbabilisticDatatype probabilistic attributes}
     */

    public static Collection<SDFAttribute> getDiscreteProbabilisticAttributes(final SDFSchema schema) {
     	Objects.requireNonNull(schema);
     	Objects.requireNonNull(schema.getAttributes());
    	final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
        for (final SDFAttribute attribute : schema.getAttributes()) {
            if (SchemaUtils.isDiscreteProbabilisticAttribute(attribute)) {
                attributes.add(attribute);
            }
        }
        return attributes;
    }

    /**
     * Returns all attributes from the list that are discrete probabilistic
     * attributes.
     * 
     * @param attributes
     *            The {@link Collection attributes}
     * @return A list of all attributes in the list that are discrete
     *         {@link SDFProbabilisticDatatype probabilistic attributes}
     */

    public static Collection<SDFAttribute> getDiscreteProbabilisticAttributes(final Collection<SDFAttribute> attributes) {
     	Objects.requireNonNull(attributes);
    	final List<SDFAttribute> result = new ArrayList<SDFAttribute>();
        for (final SDFAttribute attribute : attributes) {
            if (SchemaUtils.isDiscreteProbabilisticAttribute(attribute)) {
                result.add(attribute);
            }
        }
        return result;
    }

    /**
     * Returns the indexes of the given list of attributes in the given schema.
     * 
     * @param schema
     *            The schema
     * @param attributes
     *            The {@link Collection attributes}
     * @return An array with the indexes of the attributes in the schema
     */
    public static int[] getAttributePos(final SDFSchema schema, final Collection<SDFAttribute> attributes) {
     	Objects.requireNonNull(schema);
     	Objects.requireNonNull(attributes);
    	final int[] pos = new int[attributes.size()];
        int i = 0;
        for (final SDFAttribute attribute : attributes) {
			Preconditions.checkArgument(schema.contains(attribute));
			pos[i] = schema.indexOf(attribute);
			i++;
        }
        return pos;
    }

    /**
     * Returns the index of the given attribute in the given schema.
     * 
     * @param schema
     *            The schema
     * @param attribute
     *            The attribute
     * @return The index of the attribute in the schema
     */
    public static int getAttributePos(final SDFSchema schema, final SDFAttribute attribute) {
		Objects.requireNonNull(schema);
		Objects.requireNonNull(attribute);
		Preconditions.checkArgument(schema.contains(attribute));
		return schema.indexOf(attribute);
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
    	Objects.requireNonNull(expression);
    	if (expression instanceof AndOperator) {
            return SchemaUtils.isEquiExpression(((AndOperator) expression).getArgument(0)) && SchemaUtils.isEquiExpression(((AndOperator) expression).getArgument(1));

        }
        if (expression instanceof EqualsOperator) {
            final EqualsOperator eq = (EqualsOperator) expression;
            final IExpression<?> arg1 = eq.getArgument(0);
            final IExpression<?> arg2 = eq.getArgument(1);
            if ((arg1 instanceof Variable) && (arg2 instanceof Variable)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the map of attributes used in a equi expression.
     * 
     * @param expression
     *            The expression
     * @param resolver
     *            The attribute resolver
     * @return The map of attributes
     */
    public static Map<SDFAttribute, List<SDFAttribute>> getEquiExpressionAtributes(final IExpression<?> expression, IAttributeResolver resolver) {
    	Objects.requireNonNull(expression);
    	Objects.requireNonNull(resolver);
        Map<SDFAttribute, List<SDFAttribute>> attributes = new HashMap<SDFAttribute, List<SDFAttribute>>();
        if (expression instanceof AndOperator) {
            Map<SDFAttribute, List<SDFAttribute>> leftAttributes = SchemaUtils.getEquiExpressionAtributes(((AndOperator) expression).getArgument(0), resolver);
            for (SDFAttribute key : leftAttributes.keySet()) {
                if (!attributes.containsKey(key)) {
                    attributes.put(key, new ArrayList<SDFAttribute>());
                }
                attributes.get(key).addAll(leftAttributes.get(key));
            }
            Map<SDFAttribute, List<SDFAttribute>> rigthAttributes = SchemaUtils.getEquiExpressionAtributes(((AndOperator) expression).getArgument(1), resolver);
            for (SDFAttribute key : rigthAttributes.keySet()) {
                if (!attributes.containsKey(key)) {
                    attributes.put(key, new ArrayList<SDFAttribute>());
                }
                attributes.get(key).addAll(rigthAttributes.get(key));
            }
        }
        if (expression instanceof EqualsOperator) {
            final EqualsOperator eq = (EqualsOperator) expression;
            final IExpression<?> arg1 = eq.getArgument(0);
            final IExpression<?> arg2 = eq.getArgument(1);
            if ((arg1 instanceof Variable) && (arg2 instanceof Variable)) {
                SDFAttribute key = resolver.getAttribute(((Variable) arg1).getIdentifier());
                if (!attributes.containsKey(key)) {
                    attributes.put(key, new ArrayList<SDFAttribute>());
                }
                attributes.get(key).add(resolver.getAttribute(((Variable) arg2).getIdentifier()));
            }
        }
        return attributes;
    }

    /**
     * Return true if the given relational predicate is of the form:
     * 
     * A.x=B.y AND A.y=B.z AND * ...
     * 
     * @param predicate
     *            The relational predicate
     * @return <code>true</code> iff the relational predicate is of the given
     *         form
     */
    public static boolean isEquiPredicate(final RelationalPredicate predicate) {
    	Objects.requireNonNull(predicate);
    	Objects.requireNonNull(predicate.getExpression());
    	Objects.requireNonNull(predicate.getExpression().getMEPExpression());
		final IExpression<?> expression = predicate.getExpression().getMEPExpression();
        return SchemaUtils.isEquiExpression(expression);
    }

    /**
     * Utility constructor.
     */
    private SchemaUtils() {
        throw new UnsupportedOperationException();
    }
}
