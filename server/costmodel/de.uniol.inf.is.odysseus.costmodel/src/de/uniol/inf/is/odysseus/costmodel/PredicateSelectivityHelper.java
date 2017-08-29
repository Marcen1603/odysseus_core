/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.costmodel;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.expression.IRelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepConstant;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.EqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.GreaterEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.GreaterThanOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.SmallerEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.SmallerThanOperator;

public class PredicateSelectivityHelper {

	private static final Logger LOG = LoggerFactory.getLogger(PredicateSelectivityHelper.class);

	private class HistogramPair {
		public IHistogram first;
		public IHistogram second;

		public HistogramPair(IHistogram h1, IHistogram h2) {
			first = h1;
			second = h2;
		}
	}

	private class HistogramValuePair {
		public IHistogram histogram;
		public Double value;

		public HistogramValuePair(IHistogram histogram, Double value) {
			this.histogram = histogram;
			this.value = value;
		}
	}

	private IPredicate<?> predicate;
	private Map<SDFAttribute, IHistogram> histograms;

	public PredicateSelectivityHelper(IPredicate<?> predicate, Map<SDFAttribute, IHistogram> histograms) {
		this.predicate = predicate;
		this.histograms = histograms;
	}

	public Optional<Double> getSelectivity() {

		try {
			return evaluatePredicate(predicate);
		} catch (Exception ex) {
			LOG.error("Could not estimate selectivity of predicate {}", predicate, ex);
			return Optional.absent();
		}
	}

	private Optional<Double> evaluatePredicate(IPredicate<?> predicate) {
		if (predicate instanceof IRelationalExpression) {
			return evaluateRelationalPredicate((RelationalExpression<?>) predicate);
		} else if (predicate instanceof ComplexPredicate) {
			return evaluateComplexPredicate((ComplexPredicate<?>) predicate);
		} else if (predicate != null) {
			LOG.warn("Unsupported PredicateType :" + predicate.getClass());
			return Optional.absent();
		} else {
			return Optional.absent();
		}

	}

	private Optional<Double> evaluateComplexPredicate(ComplexPredicate<?> complexPredicate) {

		if (ComplexPredicateHelper.isAndPredicate(complexPredicate)) {
			return evaluateAndPredicate(complexPredicate);
		} else if (ComplexPredicateHelper.isOrPredicate(complexPredicate)) {
			return evaluateOrPredicate(complexPredicate);
		} else {
			LOG.warn("Unsupported ComplexPredicate : " + complexPredicate.getClass());
		}

		return Optional.absent();
	}

	private Optional<Double> evaluateOrPredicate(ComplexPredicate<?> orPredicate) {
		Optional<Double> left = evaluatePredicate(orPredicate.getLeft());
		Optional<Double> right = evaluatePredicate(orPredicate.getRight());
		
		if( left.isPresent() && right.isPresent() ) {
			return Optional.of(left.get() + right.get() - (left.get() * right.get()));
		}
		
		return Optional.absent();
	}

	private Optional<Double> evaluateAndPredicate(ComplexPredicate<?> andPredicate) {
		Optional<Double> left = evaluatePredicate(andPredicate.getLeft());
		Optional<Double> right = evaluatePredicate(andPredicate.getRight());
		if( left.isPresent() && right.isPresent() ) {
			return Optional.of(left.get() * right.get());
		}
		
		return Optional.absent();
	}

	private Optional<Double> evaluateRelationalPredicate(RelationalExpression<?> relationalPredicate) {
		LOG.debug("Evaluate RelationalPredicate " + predicate);

		IMepExpression<?> mepExpression = relationalPredicate.getMEPExpression();

		if (mepExpression instanceof IBinaryOperator) {
			return evaluateBinaryOperator((IBinaryOperator<?>) mepExpression);
		}
		if( relationalPredicate.isAlwaysTrue()) {
			return Optional.of(1.0);
		}
		if( relationalPredicate.isAlwaysFalse() ) {
			return Optional.of(0.0);
		}
		
		LOG.warn("Unsupported MEP-Expression: " + mepExpression);
		return Optional.absent();
	}

	private Optional<Double> evaluateBinaryOperator(IBinaryOperator<?> op) {

		IMepExpression<?>[] args = op.getArguments();

		if (op instanceof GreaterThanOperator) {
			return evaluateGreaterThanOperator(args[0], args[1]);
		} else if (op instanceof SmallerThanOperator) {
			return evaluateSmallerThanOperator(args[0], args[1]);
		} else if (op instanceof EqualsOperator) {
			return evaluateEqualsOperator(args[0], args[1]);
		} else if (op instanceof GreaterEqualsOperator) {
			return evaluateGreaterThanOperator(args[0], args[1]);
		} else if (op instanceof SmallerEqualsOperator) {
			return evaluateSmallerThanOperator(args[0], args[1]);
		} else {
			LOG.warn("Unsupported BinaryOperator: " + op);
			return Optional.absent();
		}
	}

	/***********************************************************************/
	/** EQUALS **/
	/***********************************************************************/
	private Optional<Double> evaluateEqualsOperator(IMepExpression<?> arg0, IMepExpression<?> arg1) {
		LOG.debug("Found EqualsOperator");

		if (isOnlyOneAttribute(arg0, arg1)) {
			if (arg0 instanceof IMepConstant) {
				return evaluateEqualsOperator(arg1, arg0);
			}

			Optional<HistogramValuePair> optHvp = getHistogramAndValue(arg0, arg1);
			if (!optHvp.isPresent()) {
				return Optional.absent();
			}

			HistogramValuePair hvp = optHvp.get();
			double valueCount = hvp.histogram.getValueCount();
			double occs = hvp.histogram.getOccurences(hvp.value);
			return Optional.of(occs / valueCount);

		}

		Optional<HistogramPair> optHistPair = getRelativeHistograms(arg0, arg1);
		if (!optHistPair.isPresent()) {
			return Optional.absent();
		}

		HistogramPair histPair = optHistPair.get();
		double[] borders = histPair.first.getIntervalBorders();
		double selectivity = 0.0;
		for (int i = 0; i < borders.length - 1; i++) {
			double intervalStart = borders[i];
			double intervalEnd = borders[i + 1];

			double prob1 = histPair.first.getOccurences(intervalStart);
			double prob2 = histPair.second.getOccurenceRange(intervalStart, intervalEnd);
			double prob = prob1 * prob2;
			selectivity += prob;
		}

		return Optional.of(selectivity);
	}

	/***********************************************************************/
	/** SMALLER-THAN **/
	/***********************************************************************/
	private Optional<Double> evaluateSmallerThanOperator(IMepExpression<?> arg0, IMepExpression<?> arg1) {
		LOG.debug("Found SmallerThanOperator");

		if (isOnlyOneAttribute(arg0, arg1)) {
			if (arg0 instanceof IMepConstant) {
				return evaluateGreaterThanOperator(arg1, arg0);
			}

			Optional<HistogramValuePair> optHvp = getHistogramAndValue(arg0, arg1);
			if (!optHvp.isPresent()) {
				return Optional.absent();
			}

			HistogramValuePair hvp = optHvp.get();
			double valueCount = hvp.histogram.getValueCount();
			double occs = hvp.histogram.getOccurenceRange(hvp.histogram.getMinimum(), hvp.value);
			return Optional.of(occs / valueCount);

		}

		Optional<HistogramPair> optHistPair = getRelativeHistograms(arg0, arg1);
		if (!optHistPair.isPresent()) {
			return Optional.absent();
		}

		HistogramPair histPair = optHistPair.get();
		double[] borders = histPair.first.getIntervalBorders();

		double selectivity = 0.0;
		for (int i = 0; i < borders.length - 1; i++) {
			double borderValue = borders[i];

			double prob2 = histPair.second.getOccurenceRange(borderValue, histPair.second.getMaximum());
			double prob1 = histPair.first.getOccurences(borderValue);

			selectivity += (prob1 * prob2);
		}

		return Optional.of(selectivity);
	}

	/***********************************************************************/
	/** GREATER-THAN **/
	/***********************************************************************/

	private Optional<Double> evaluateGreaterThanOperator(IMepExpression<?> arg0, IMepExpression<?> arg1) {
		LOG.debug("Found GreaterThanOperator");

		if (isOnlyOneAttribute(arg0, arg1)) {
			if (arg0 instanceof IMepConstant) {
				return evaluateSmallerThanOperator(arg1, arg0);
			}

			Optional<HistogramValuePair> optHvp = getHistogramAndValue(arg0, arg1);
			if (!optHvp.isPresent()) {
				return Optional.absent();
			}

			HistogramValuePair hvp = optHvp.get();
			double valueCount = hvp.histogram.getValueCount();
			double occs = hvp.histogram.getOccurenceRange(hvp.value, hvp.histogram.getMaximum());
			return Optional.of(occs / valueCount);
		}

		Optional<HistogramPair> optHistPair = getRelativeHistograms(arg0, arg1);
		if (!optHistPair.isPresent()) {
			return Optional.absent();
		}

		HistogramPair histogramPair = optHistPair.get();
		double[] borders = histogramPair.first.getIntervalBorders();

		double selectivity = 0.0;
		for (int i = 0; i < borders.length - 1; i++) {
			double borderValue = borders[i];

			double prob2 = histogramPair.second.getOccurenceRange(histogramPair.second.getMinimum(), borderValue);
			double prob1 = histogramPair.first.getOccurences(borderValue);

			selectivity += (prob1 * prob2);
		}
		return Optional.of(selectivity);
	}

	/***********************************************************************/
	/** HELPER-METHODS **/
	/***********************************************************************/

	private Optional<HistogramPair> getRelativeHistograms(IMepExpression<?> arg0, IMepExpression<?> arg1) {
		// Get Attributes
		String firstAttribute = ((IMepVariable) arg0).getIdentifier();
		String secondAttribute = ((IMepVariable) arg1).getIdentifier();

		Optional<IHistogram> optFirstHist = getHistogram(firstAttribute);
		Optional<IHistogram> optSecondHist = getHistogram(secondAttribute);

		if (!optFirstHist.isPresent()) {
			LOG.warn("No histogram for attribute " + firstAttribute + " found ");
			return Optional.absent();
		} else if (!optSecondHist.isPresent()) {
			LOG.warn("No histogram for attribute " + firstAttribute + " found ");
			return Optional.absent();
		}

		IHistogram firstHistRelative = optFirstHist.get().toRelative();
		IHistogram secondHistRelative = optSecondHist.get().toRelative();
		return Optional.of(new HistogramPair(firstHistRelative, secondHistRelative));
	}

	public Optional<HistogramValuePair> getHistogramAndValue(IMepExpression<?> arg0, IMepExpression<?> arg1) {
		String attribute = null;
		Double value = null;

		try {
			attribute = ((IMepVariable) arg0).getIdentifier();
			//Changed from typecast to call of toNumeric to avoid ClassCast Exceptions
			value = toNumeric(((IMepConstant<?>) arg1).getValue());
		} catch (Throwable ex) {
			LOG.warn("Operator is more complex than expected here...", ex);
			return Optional.absent();
		}

		Optional<IHistogram> optHistogram = getHistogram(attribute);

		if (value == null) {
			LOG.warn("Cannot estimate value of expression " + arg1);
			return Optional.absent();
		}
		if (!optHistogram.isPresent()) {
			LOG.warn("Cannot find histogram for attribute " + attribute);
			return Optional.absent();
		}

		return Optional.of(new HistogramValuePair(optHistogram.get(), value));
	}

	private Optional<IHistogram> getHistogram(String attribute) {
		for (SDFAttribute attr : histograms.keySet()) {
			String srcName = attr.getSourceName();
			String attrName = attr.getAttributeName();
			String combined = srcName + "." + attrName;

			if (attribute.equals(attrName) || attribute.equals(combined)) {
				return Optional.of(histograms.get(attr));
			}
		}

		return Optional.absent();
	}

	private static boolean isOnlyOneAttribute(IMepExpression<?> arg0, IMepExpression<?> arg1) {
		if (arg0 instanceof IMepVariable && arg1 instanceof IMepVariable)
			return false;
		return true;
	}
	

	private static Double toNumeric(Object value) {
		if( value == null ) {
			return null;
		}
		
		if (value instanceof Double) {
			return ((Double) value);
		}
		if (value instanceof Float) {
			return ((Float) value).doubleValue();
		}
		if (value instanceof Long) {
			return ((Long) value).doubleValue();
		}
		if (value instanceof Byte) {
			return ((Byte) value).doubleValue();
		}
		if (value instanceof Integer) {
			return ((Integer) value).doubleValue();
		}

		LOG.error("Could not sample value '{}'. Is it not numeric?", value);
		return null;
	}

}
