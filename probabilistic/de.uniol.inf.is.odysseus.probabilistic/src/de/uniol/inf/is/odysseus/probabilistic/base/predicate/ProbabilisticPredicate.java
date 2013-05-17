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

package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticPredicate extends AbstractPredicate<ProbabilisticTuple<?>> implements IProbabilisticPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3159284040915771680L;
	/** The probabilistic expression. */
	private SDFProbabilisticExpression expression;
	/** The attribute postitions. */
	private int[] attributePositions;
	/** Flags indicating the preferred schema for each attribute. */
	private boolean[] fromRightChannel;
	/** The replacement map. */
	private Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

	/**
	 * Constructs a new probabilistic expression with the given expression.
	 * 
	 * @param expression
	 *            The expression
	 */
	public ProbabilisticPredicate(final SDFExpression expression) {
		this(new SDFProbabilisticExpression(expression));
	}

	/**
	 * Constructs a new probabilistic expression with the given probabilistic expression.
	 * 
	 * @param expression
	 *            The probabilistic expression
	 */
	public ProbabilisticPredicate(final SDFProbabilisticExpression expression) {
		this.expression = expression;
	}

	/**
	 * Clone constructor.
	 * 
	 * @param predicate
	 *            The probabilistic predicate
	 */
	public ProbabilisticPredicate(final ProbabilisticPredicate predicate) {
		this.attributePositions = (int[]) predicate.attributePositions.clone();
		this.fromRightChannel = (boolean[]) predicate.fromRightChannel.clone();
		if (predicate.expression == null) {
			this.expression = null;
		} else {
			this.expression = predicate.expression.clone();
		}
		this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(predicate.replacementMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.base.predicate.IProbabilisticPredicate #init(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema, de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	public final void init(final SDFSchema left, final SDFSchema right) {
		init(left, right, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.predicate.IPredicate#evaluate(java.lang .Object)
	 */
	@Override
	public final boolean evaluate(final ProbabilisticTuple<?> input) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(this.attributePositions[i]);
		}
		((SDFProbabilisticExpression) this.expression).bindDistributions(input.getDistributions());
		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.base.predicate.IProbabilisticPredicate #probabilisticEvaluate(de.uniol.inf.is.odysseus.probabilistic.base. ProbabilisticTuple)
	 */
	@Override
	public final double probabilisticEvaluate(final ProbabilisticTuple<?> input) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(this.attributePositions[i]);
		}
		((SDFProbabilisticExpression) this.expression).bindDistributions(input.getDistributions());
		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return this.expression.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.predicate.IPredicate#evaluate(java.lang .Object, java.lang.Object)
	 */
	@Override
	public final boolean evaluate(final ProbabilisticTuple<?> left, final ProbabilisticTuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r;
			if (fromRightChannel[i]) {
				r = right;
			} else {
				r = left;
			}
			values[i] = r.getAttribute(this.attributePositions[i]);
		}
		Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
		additionalContent.putAll(left.getAdditionalContent());
		additionalContent.putAll(right.getAdditionalContent());

		int length = 0;
		if (left.getDistributions() != null) {
			length += left.getDistributions().length;
		}
		if (right.getDistributions() != null) {
			length += right.getDistributions().length;
		}
		NormalDistributionMixture[] distributions = new NormalDistributionMixture[length];
		if (left.getDistributions() != null) {
			System.arraycopy(left.getDistributions(), 0, distributions, 0, left.getDistributions().length);
		}
		if (right.getDistributions() != null) {
			System.arraycopy(right.getDistributions(), 0, distributions, length - right.getDistributions().length, right.getDistributions().length);
		}
		this.expression.bindDistributions(distributions);
		this.expression.bindAdditionalContent(additionalContent);
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.base.predicate.IProbabilisticPredicate #probabilisticEvaluate(de.uniol.inf.is.odysseus.probabilistic.base. ProbabilisticTuple, de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple)
	 */
	@Override
	public final double probabilisticEvaluate(final ProbabilisticTuple<?> left, final ProbabilisticTuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r;
			if (fromRightChannel[i]) {
				r = right;
			} else {
				r = left;
			}
			values[i] = r.getAttribute(this.attributePositions[i]);
		}
		Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
		additionalContent.putAll(left.getAdditionalContent());
		additionalContent.putAll(right.getAdditionalContent());

		int length = 0;
		if (left.getDistributions() != null) {
			length += left.getDistributions().length;
		}
		if (right.getDistributions() != null) {
			length += right.getDistributions().length;
		}
		NormalDistributionMixture[] distributions = new NormalDistributionMixture[length];
		if (left.getDistributions() != null) {
			System.arraycopy(left.getDistributions(), 0, distributions, 0, left.getDistributions().length);
		}
		if (right.getDistributions() != null) {
			System.arraycopy(right.getDistributions(), 0, distributions, length - right.getDistributions().length, right.getDistributions().length);
		}
		this.expression.bindDistributions(distributions);
		this.expression.bindAdditionalContent(additionalContent);
		this.expression.bindVariables(values);
		return this.expression.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate# getAttributes()
	 */
	@Override
	public final List<SDFAttribute> getAttributes() {
		return Collections.unmodifiableList(this.expression.getAllAttributes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.base.predicate.IProbabilisticPredicate #replaceAttribute(de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute, de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute)
	 */
	@Override
	public final void replaceAttribute(final SDFAttribute curAttr, final SDFAttribute newAttr) {
		replacementMap.put(curAttr, newAttr);
	}

	/**
	 * Gets the expression of this predicate.
	 * 
	 * @return The expression
	 */
	public final SDFProbabilisticExpression getExpression() {
		return expression;
	}

	/**
	 * Gets the index of the given attribute in the given schema.
	 * 
	 * @param schema
	 *            The schema
	 * @param attribute
	 *            The attribute
	 * @return The index of the attribute
	 */
	private int indexOf(final SDFSchema schema, final SDFAttribute attribute) {
		SDFAttribute cqlAttr = getReplacement(attribute);
		Iterator<SDFAttribute> iter = schema.iterator();
		for (int i = 0; iter.hasNext(); ++i) {
			SDFAttribute a = iter.next();
			if (cqlAttr.equalsCQL(a)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the replacement for the given attribute.
	 * 
	 * @param attribute
	 *            The attribute
	 * @return The replacement for the given attribute
	 */
	private SDFAttribute getReplacement(final SDFAttribute attribute) {
		SDFAttribute result = attribute;
		SDFAttribute tmp = null;
		while ((tmp = replacementMap.get(result)) != null) {
			result = tmp;
		}
		return result;
	}

	/**
	 * 
	 * @param left
	 *            The schema of the left input tuple
	 * @param right
	 *            The schema of the right input tuple
	 * @param checkRightSchema
	 *            Flag indicating the preferred schema: <code>true</code> use the schema from the right
	 */
	private void init(final SDFSchema left, final SDFSchema right, final boolean checkRightSchema) {
		List<SDFAttribute> attributes = expression.getAllAttributes();
		this.attributePositions = new int[attributes.size()];
		this.fromRightChannel = new boolean[attributes.size()];

		int i = 0;
		for (SDFAttribute curAttribute : attributes) {
			int pos = indexOf(left, curAttribute);
			if (pos == -1) {
				if (right == null && checkRightSchema) {
					throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + left + " and rightSchema is null!");
				}
				if (checkRightSchema) {
					pos = indexOf(right, curAttribute);
					if (pos == -1) {
						throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + right);
					}
				}
				this.fromRightChannel[i] = true;
			}
			this.attributePositions[i++] = pos;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate#clone()
	 */
	@Override
	public final ProbabilisticPredicate clone() {
		return new ProbabilisticPredicate(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return this.expression.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		if (this.expression != null) {
			result += this.expression.hashCode();
		}
		return result;
	}

}
