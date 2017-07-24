/**********************************************************************************
 * Copyright 2014 The Odysseus Team
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

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
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
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.AbstractRelationalPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@SuppressWarnings("all")
public class ProbabilisticRelationalPredicate extends AbstractRelationalPredicate<ProbabilisticTuple<?>>
		implements IProbabilisticRelationalPredicate<ProbabilisticTuple<?>> {
	private static final long serialVersionUID = 1222104352250883947L;
	Logger LOG = LoggerFactory.getLogger(ProbabilisticRelationalPredicate.class);

	public ProbabilisticRelationalPredicate(final SDFExpression expression) {
		super(new SDFProbabilisticExpression(expression));
	}

	public ProbabilisticRelationalPredicate(final SDFProbabilisticExpression expression) {
		super(expression);
	}

	public ProbabilisticRelationalPredicate(final ProbabilisticRelationalPredicate predicate) {
		super(predicate);
	}

	/**
	 * @param pred
	 */
	public ProbabilisticRelationalPredicate(final AbstractRelationalPredicate<ProbabilisticTuple<?>> predicate) {
		super(predicate);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<IPredicate> splitPredicate() {
		final List<IPredicate> result = new LinkedList<IPredicate>();
		if (this.isAndPredicate()) {
			final Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
			expressionStack.push(this.expression.getMEPExpression());

			while (!expressionStack.isEmpty()) {
				final IMepExpression<?> curExpression = expressionStack.pop();
				if (this.isAndExpression(curExpression)) {
					expressionStack.push(curExpression.toFunction().getArgument(0));
					expressionStack.push(curExpression.toFunction().getArgument(1));
				} else {
					final SDFExpression expr = new SDFExpression(curExpression.toString(), expression.getAttributeResolver(),
							MEP.getInstance());
					final RelationalExpression<?> relationalPredicate = new RelationalExpression<>(expr);
					relationalPredicate.initVars(this.expression.getSchema());
					result.add(relationalPredicate);
				}
			}
			return result;

		}
		result.add(this);
		return result;
	}

	@Override
	public Boolean evaluate(final ProbabilisticTuple<?> input) {
		Objects.requireNonNull(input);
		final Object[] values = new Object[this.attributePositions.length];
		final IMetaAttribute[] meta = new IMetaAttribute[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			Object attribute = input.getAttribute(this.attributePositions[i].getE2());
			if (attribute.getClass() == ProbabilisticDouble.class) {
				final int index = ((ProbabilisticDouble) attribute).getDistribution();
				attribute = input.getDistribution(index);
			}
			values[i] = attribute;
			meta[i] = input.getMetadata();
		}
		final SDFProbabilisticExpression probabilisticExpression = (SDFProbabilisticExpression) this.expression;

		// probabilisticExpression.bindMetaAttribute(input.getMetadata());
		// probabilisticExpression.bindAdditionalContent(input.getAdditionalContent());
		probabilisticExpression.bindVariables(this.attributePositions, meta, values);

		final Object expr = probabilisticExpression.getValue();
		if (probabilisticExpression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_RESULT)) {
			final ProbabilisticBooleanResult result = (ProbabilisticBooleanResult) expr;
			return result.getProbability() != 0.0;
		} else {
			return (Boolean) expr;
		}
	}

	public ProbabilisticTuple<?> probabilisticEvaluate(final ProbabilisticTuple<?> input) {
		Objects.requireNonNull(input);
		@SuppressWarnings("unchecked")
		final ProbabilisticTuple<IProbabilistic> output = (ProbabilisticTuple<IProbabilistic>) input.clone();

		final Object[] values = new Object[this.attributePositions.length];
		final IMetaAttribute[] meta = new IMetaAttribute[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			Object attribute = input.getAttribute(this.attributePositions[i].getE2());
			if (attribute.getClass() == ProbabilisticDouble.class) {
				final int index = ((ProbabilisticDouble) attribute).getDistribution();
				attribute = input.getDistribution(index);
			}
			values[i] = attribute;
			meta[i] = input.getMetadata();
		}
		final SDFProbabilisticExpression probabilisticExpression = (SDFProbabilisticExpression) this.expression;

		// probabilisticExpression.bindMetaAttribute(input.getMetadata());
		// probabilisticExpression.bindAdditionalContent(input.getAdditionalContent());
		probabilisticExpression.bindVariables(this.attributePositions, meta, values);

		final Object expr = probabilisticExpression.getValue();
		if (this.expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_RESULT)) {
			final ProbabilisticBooleanResult result = (ProbabilisticBooleanResult) expr;
			if (result.getProbability() > 0.0) {
				double probability = result.getProbability();
				for (final IMultivariateDistribution distr : result.getDistributions()) {
					final MultivariateMixtureDistribution distribution = (MultivariateMixtureDistribution) distr;
					final int index = ((ProbabilisticDouble) input.getAttribute(distribution.getAttribute(0)))
							.getDistribution();

					final MultivariateMixtureDistribution outputDistribution = output.getDistribution(index);
					// Adjust the support in case the distribution was
					// modified
					for (int d = 0; d < distribution.getDimension(); d++) {
						// New support is the resulting support of the
						// filtering subtract by the difference of the
						// new and the old mean
						final Interval support = distribution.getSupport(d)
								.subtract(distribution.getMean()[d] - input.getDistribution(index).getMean()[d]);
						if (outputDistribution.getSupport(d).intersects(support)) {
							final Interval intersection = outputDistribution.getSupport(d).intersection(support);
							outputDistribution.setSupport(d, intersection);
						} else {
							probability = 0.0;
							break;
						}
					}
					outputDistribution.setScale(distribution.getScale());

				}
				output.getMetadata().setExistence(output.getMetadata().getExistence() * probability);
				return output;
			}
		}
		return null;
	}

	@Override
	public Boolean evaluate(final ProbabilisticTuple<?> left, final ProbabilisticTuple<?> right) {
		Objects.requireNonNull(left);
		Objects.requireNonNull(right);
		// FIXME 20140319 christian@kuka.cc Restrict tuple before concatenate
		// them

		final MultivariateMixtureDistribution[] newDistributions = this.mergeDistributions(left, right,
				left.getAttributes().length);
		final Object[] newAttributes = this.mergeAttributes(left, right, left.getDistributions().length);

		final Object[] values = new Object[this.attributePositions.length];
		final IMetaAttribute[] meta = new IMetaAttribute[values.length];
		final int[] positions = new int[values.length];
		for (int i = 0; i < values.length; ++i) {
			final int pos = this.fromRightChannel[i] ? left.getAttributes().length + this.attributePositions[i].getE2()
					: this.attributePositions[i].getE2();
			Object attribute = newAttributes[pos];
			if (attribute.getClass() == ProbabilisticDouble.class) {
				final int index = ((ProbabilisticDouble) attribute).getDistribution();
				attribute = newDistributions[index];
			}
			values[i] = attribute;
			meta[i] = this.fromRightChannel[i] ? left.getMetadata() : right.getMetadata();
			positions[i] = pos;
		}

		final SDFProbabilisticExpression probabilisticExpression = (SDFProbabilisticExpression) this.expression;
		probabilisticExpression.bindVariables(positions, values);
		final Object expr = probabilisticExpression.getValue();
		if (probabilisticExpression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_RESULT)) {
			final ProbabilisticBooleanResult result = (ProbabilisticBooleanResult) expr;
			return result.getProbability() != 0.0;
		} else {
			return (Boolean) expr;
		}
	}

	public ProbabilisticTuple<?> probabilisticEvaluate(final ProbabilisticTuple<?> left,
			final ProbabilisticTuple<?> right, final IProbabilisticTimeInterval metadata) {
		Objects.requireNonNull(left);
		Objects.requireNonNull(right);
		// FIXME 20140319 christian@kuka.cc Restrict tuple before concatenate
		// them

		final MultivariateMixtureDistribution[] newDistributions = this.mergeDistributions(left, right,
				left.getAttributes().length);
		final Object[] newAttributes = this.mergeAttributes(left, right, left.getDistributions().length);

		final Object[] values = new Object[this.attributePositions.length];
		final IMetaAttribute[] meta = new IMetaAttribute[values.length];
		final int[] positions = new int[values.length];
		for (int i = 0; i < values.length; ++i) {
			final int pos = this.fromRightChannel[i] ? left.getAttributes().length + this.attributePositions[i].getE2()
					: this.attributePositions[i].getE2();
			Object attribute = newAttributes[pos];
			if (attribute.getClass() == ProbabilisticDouble.class) {
				final int index = ((ProbabilisticDouble) attribute).getDistribution();
				attribute = newDistributions[index];
			}
			values[i] = attribute;
			meta[i] = this.fromRightChannel[i] ? left.getMetadata() : right.getMetadata();
			positions[i] = pos;
		}

		final SDFProbabilisticExpression probabilisticExpression = (SDFProbabilisticExpression) this.expression;

		probabilisticExpression.bindVariables(positions, values);

		final Object expr = probabilisticExpression.getValue();
		if (this.expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_RESULT)) {
			final ProbabilisticBooleanResult result = (ProbabilisticBooleanResult) expr;
			if (result.getProbability() > 0.0) {
				double probability = 1.0;
				for (final IMultivariateDistribution distr : result.getDistributions()) {
					final MultivariateMixtureDistribution distribution = (MultivariateMixtureDistribution) distr;
					final int index = ((ProbabilisticDouble) newAttributes[distribution.getAttribute(0)])
							.getDistribution();

					final MultivariateMixtureDistribution outputDistribution = newDistributions[index];
					// Adjust the support in case the distribution was
					// modified
					MultivariateMixtureDistribution inputDistibution;
					if (distribution.getAttribute(0) > left.size()) {
						inputDistibution = right.getDistribution(((ProbabilisticDouble) (right
								.getAttribute(distribution.getAttribute(0) - right.size()))).getDistribution());
					} else {
						inputDistibution = left.getDistribution(
								((ProbabilisticDouble) (left.getAttribute(distribution.getAttribute(0))))
										.getDistribution());
					}
					for (int d = 0; d < distribution.getDimension(); d++) {
						// New support is the resulting support of the
						// filtering subtract by the difference of the
						// new and the old mean

						final Interval support = distribution.getSupport(d)
								.subtract(distribution.getMean()[d] - inputDistibution.getMean()[d]);
						if (outputDistribution.getSupport(d).intersects(support)) {
							final Interval intersection = outputDistribution.getSupport(d).intersection(support);
							outputDistribution.setSupport(d, intersection);
						} else {
							probability = 0.0;
							break;
						}
					}

					newDistributions[index] = distribution;
				}
				final ProbabilisticTuple<IProbabilistic> outputVal = new ProbabilisticTuple<>(newAttributes,
						newDistributions, true);
				outputVal.setMetadata(metadata.clone());
				outputVal.getMetadata().setExistence(outputVal.getMetadata().getExistence() * probability);
				return outputVal;
			}
		}
		return null;
	}

//	public boolean evaluate(final ProbabilisticTuple<?> input, final KeyValueObject<?> additional) {
//		final Object[] values = new Object[this.neededAttributes.size()];
//
//		for (int i = 0; i < this.neededAttributes.size(); ++i) {
//			if (!this.fromRightChannel[i]) {
//				values[i] = input.getAttribute(this.attributePositions[i].getE2());
//			} else {
//				values[i] = additional.getAttribute(this.neededAttributes.get(i).getURI());
//			}
//		}
//		// this.expression.bindMetaAttribute(input.getMetadata());
//		// this.expression.bindAdditionalContent(input.getAdditionalContent());
//		this.expression.bindVariables(values);
//		return (Boolean) this.expression.getValue();
//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<ProbabilisticTuple<?>> and(IPredicate<ProbabilisticTuple<?>> predicate) {
		if (predicate instanceof AbstractRelationalPredicate) {
			SDFExpression expr = ((AbstractRelationalPredicate<?>) predicate).getExpression();
			AndOperator and = new AndOperator();
			and.setArguments(new IMepExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
			return new ProbabilisticRelationalPredicate(
					new SDFExpression(and.toString(), expression.getAttributeResolver(), expression.getExpressionParser()));
		}
		return super.and(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<ProbabilisticTuple<?>> or(IPredicate<ProbabilisticTuple<?>> predicate) {
		if (predicate instanceof AbstractRelationalPredicate) {
			SDFExpression expr = ((AbstractRelationalPredicate<?>) predicate).getExpression();
			OrOperator or = new OrOperator();
			or.setArguments(new IMepExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
			return new ProbabilisticRelationalPredicate(
					new SDFExpression(or.toString(), expression.getAttributeResolver(), expression.getExpressionParser()));
		}
		return super.or(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<ProbabilisticTuple<?>> not() {
		NotOperator not = new NotOperator();
		not.setArguments(new IMepExpression<?>[] { expression.getMEPExpression() });
		return new ProbabilisticRelationalPredicate(
				new SDFExpression(not.toString(), expression.getAttributeResolver(), expression.getExpressionParser()));

	}

	@Override
	public ProbabilisticRelationalPredicate clone() {
		return new ProbabilisticRelationalPredicate(this);
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof ProbabilisticRelationalPredicate)) {
			return false;
		}
		return this.expression.equals(((ProbabilisticRelationalPredicate) other).expression);
	}

	@Override
	public int hashCode() {
		return 23 * this.expression.hashCode();
	}

	private MultivariateMixtureDistribution[] mergeDistributions(final ProbabilisticTuple<?> left,
			final ProbabilisticTuple<?> right, final int offset) {
		int length = 0;
		int start = 0;
		if (left.getDistributions() != null) {
			length += left.getDistributions().length;
			start = left.getDistributions().length;
		}
		if (right.getDistributions() != null) {
			length += right.getDistributions().length;
		}
		final MultivariateMixtureDistribution[] newDistributions = new MultivariateMixtureDistribution[length];
		if (left.getDistributions() != null) {
			for (int i = 0; i < left.getDistributions().length; i++) {
				newDistributions[i] = left.getDistributions()[i].clone();
			}
			// System.arraycopy(leftDistributions, 0, newDistributions, 0,
			// leftDistributions.length);
		}
		if (right.getDistributions() != null) {
			for (int i = 0; i < right.getDistributions().length; i++) {
				newDistributions[left.getDistributions().length + i] = right.getDistributions()[i].clone();
			}
			// System.arraycopy(rightDistributions, 0, newDistributions,
			// leftDistributions.length, rightDistributions.length);
		}
		for (int i = start; i < length; i++) {
			final int dimension = newDistributions[i].getDimension();

			final int[] newAttrsPos = new int[dimension];
			for (int j = 0; j < dimension; j++) {
				newAttrsPos[j] = newDistributions[i].getAttribute(j) + offset;
			}
			newDistributions[i].setAttributes(newAttrsPos);
		}
		return newDistributions;
	}

	private Object[] mergeAttributes(final ProbabilisticTuple<?> left, final ProbabilisticTuple<?> right,
			final int offset) {
		int length = 0;
		int start = 0;
		if (left.getAttributes() != null) {
			length += left.getAttributes().length;
			start = left.getAttributes().length;
		}
		if (right.getAttributes() != null) {
			length += right.getAttributes().length;
		}
		final Object[] newAttributes = new Object[length];
		if (left.getAttributes() != null) {
			System.arraycopy(left.getAttributes(), 0, newAttributes, 0, left.getAttributes().length);
		}
		if (right.getAttributes() != null) {
			System.arraycopy(right.getAttributes(), 0, newAttributes, left.getAttributes().length,
					right.getAttributes().length);
		}
		for (int i = start; i < length; i++) {
			if (newAttributes[i].getClass() == ProbabilisticDouble.class) {
				final ProbabilisticDouble value = ((ProbabilisticDouble) newAttributes[i]);
				newAttributes[i] = new ProbabilisticDouble(value.getDistribution() + offset);
			}
		}
		return newAttributes;
	}

	public static void main(final String[] args) {
		final SDFAttribute a = new SDFAttribute("", "p_out", SDFDatatype.DOUBLE, null, null, null);
		final SDFSchema schema = SDFSchemaFactory.createNewTupleSchema("", a);
		final RelationalExpression<IMetaAttribute> pred = new RelationalExpression<>(
				new SDFExpression("p_out <=0 || isNaN(p_out)", null, MEP.getInstance()));

		System.out.println(pred.toString());
		pred.initVars(schema);
		final Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, 8);
//		final KeyValueObject<IMetaAttribute> additional = new KeyValueObject<IMetaAttribute>();
//		additional.setAttribute("b", 5);
		// REMARK: COULD THIS EVER WORK CORRECTLY??
		//System.out.println(pred.evaluate(tuple, additional));
	}

}
