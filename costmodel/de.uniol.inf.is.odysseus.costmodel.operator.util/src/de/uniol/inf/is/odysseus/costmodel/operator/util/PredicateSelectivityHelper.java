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
package de.uniol.inf.is.odysseus.costmodel.operator.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.EqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.GreaterEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.GreaterThanOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.SmallerEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.SmallerThanOperator;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * Hilfsklasse, um die Selektivität zu einem gegebenen Prädikat mit gegebenen 
 * Histogrammen zu ermitteln.
 * 
 * @author Timo Michelsen
 * 
 */public class PredicateSelectivityHelper {

	private static final Double STD_SELECTIVITY = 0.5;
	
	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(PredicateSelectivityHelper.class);
		}
		return _logger;
	}

	// interne Klasse
	private class HistogramPair {
		public IHistogram first;
		public IHistogram second;

		public HistogramPair(IHistogram h1, IHistogram h2) {
			first = h1;
			second = h2;
		}
	}

	// interne Klasse
	private class HistogramValuePair {
		public IHistogram histogram;
		public Double value;

		public HistogramValuePair(IHistogram histogram, Double value) {
			this.histogram = histogram;
			this.value = value;
		}
	}

	private IPredicate<?> predicate;
	private List<Map<SDFAttribute, IHistogram>> histograms;

	/**
	 * Konstruktor. Erstellt eine neue {@link PredicateSelectivityHelper}-Instanz. Dabei werden die
	 * Histogramme sowie das zu untersuchende Prädikat übergeben.
	 * 
	 * @param predicate Prädikat
	 * @param histograms Histogramme, die nach dem Prädikat angepasst werden sollen.
	 */
	public PredicateSelectivityHelper(IPredicate<?> predicate, List<Map<SDFAttribute, IHistogram>> histograms) {
		this.predicate = predicate;
		this.histograms = histograms;
	}

	/**
	 * Liefert die Selektivität auf Basis der im Kosntruktor übergebenen Histogramme
	 * in Abhängigkeit zum gegebenen Prädikat.
	 * 
	 * @return Selektivität
	 */
	public double getSelectivity() {

		try {
			Double sel = evaluatePredicate(predicate);
			getLogger().debug("Final estimated selectivity for Predicate '" + predicate + "' : "+ sel);
			return sel;
		} catch (Exception ex) {
			ex.printStackTrace();
			return STD_SELECTIVITY;
		}
	}

	private double evaluatePredicate(IPredicate<?> predicate) {
		if (predicate instanceof RelationalPredicate) {
			return evaluateRelationalPredicate((RelationalPredicate) predicate);
		} else if (predicate instanceof ComplexPredicate) {
			return evaluateComplexPredicate((ComplexPredicate<?>) predicate);
		} else {
			getLogger().warn("Unsupported PredicateType :" + predicate.getClass());
			return STD_SELECTIVITY;
		}

	}

	private double evaluateComplexPredicate(ComplexPredicate<?> complexPredicate) {

		if (ComplexPredicateHelper.isAndPredicate(complexPredicate)) {
			return evaluateAndPredicate(complexPredicate);
		} else if (ComplexPredicateHelper.isOrPredicate(complexPredicate)) {
			return evaluateOrPredicate(complexPredicate);
		} else {
			getLogger().warn("Unsupported ComplexPredicate : " + complexPredicate.getClass());
		}

		return STD_SELECTIVITY;
	}

	private double evaluateOrPredicate(ComplexPredicate<?> orPredicate) {
		double left = evaluatePredicate(orPredicate.getLeft());
		double right = evaluatePredicate(orPredicate.getRight());
		return left + right - (left * right);
	}

	private double evaluateAndPredicate(ComplexPredicate<?> andPredicate) {
		double left = evaluatePredicate(andPredicate.getLeft());
		double right = evaluatePredicate(andPredicate.getRight());
		return left * right;
	}

	private double evaluateRelationalPredicate(RelationalPredicate relationalPredicate) {
		getLogger().debug("Evaluate RelationalPredicate " + predicate);

		SDFExpression expression = relationalPredicate.getExpression();
		IExpression<?> mepExpression = expression.getMEPExpression();

		if (mepExpression instanceof IBinaryOperator) {
			return evaluateBinaryOperator((IBinaryOperator<?>) mepExpression);
		}
        getLogger().warn("Unsupported MEP-Expression: " + mepExpression);
        return STD_SELECTIVITY;
	}

	private double evaluateBinaryOperator(IBinaryOperator<?> op) {

		IExpression<?>[] args = op.getArguments();

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
			getLogger().warn("Unsupported BinaryOperator: " + op);
			return STD_SELECTIVITY;
		}
	}

	/***********************************************************************/
	/** EQUALS                                                            **/
	/***********************************************************************/
	private double evaluateEqualsOperator(IExpression<?> arg0, IExpression<?> arg1) {
		getLogger().debug("Found EqualsOperator");

		double selectivity = 0.0;
		if (isOnlyOneAttribute(arg0, arg1)) {
			if (arg0 instanceof Constant)
				return evaluateEqualsOperator(arg1, arg0);

			HistogramValuePair hvp = getHistogramAndValue(arg0, arg1);
			if (hvp == null)
				return STD_SELECTIVITY;

			// get selectivity
			double valueCount = hvp.histogram.getValueCount();
			double occs = hvp.histogram.getOccurences(hvp.value);
			selectivity = occs / valueCount;

		} else {
			HistogramPair histPair = getRelativeHistograms(arg0, arg1);
			if (histPair == null)
				return STD_SELECTIVITY;

			// Calcualte Selectivity
			double[] borders = histPair.first.getIntervalBorders();
			for( int i = 0; i < borders.length - 1; i++ ) {
				double intervalStart = borders[i];
				double intervalEnd = borders[i+1];
				
				double prob1 = histPair.first.getOccurences(intervalStart);
				double prob2 = histPair.second.getOccurenceRange(intervalStart, intervalEnd);
				double prob = prob1 * prob2;
				selectivity += prob;
			}
		}
		
		getLogger().debug(" Estimated selectivity : " + selectivity);
		return selectivity;
	}

	/***********************************************************************/
	/** SMALLER-THAN                                                      **/
	/***********************************************************************/
	private double evaluateSmallerThanOperator(IExpression<?> arg0, IExpression<?> arg1) {
		getLogger().debug("Found SmallerThanOperator");

		double selectivity = 0.0;
		if (isOnlyOneAttribute(arg0, arg1)) {
			if (arg0 instanceof Constant)
				return evaluateGreaterThanOperator(arg1, arg0);

			HistogramValuePair hvp = getHistogramAndValue(arg0, arg1);
			if (hvp == null)
				return STD_SELECTIVITY;

			// get selectivity
			double valueCount = hvp.histogram.getValueCount();
			double occs = hvp.histogram.getOccurenceRange(hvp.histogram.getMinimum(), hvp.value);
			selectivity = occs / valueCount;

		} else {
			HistogramPair histPair = getRelativeHistograms(arg0, arg1);
			if (histPair == null)
				return STD_SELECTIVITY;

			double[] borders = histPair.first.getIntervalBorders();

			for (int i = 0; i < borders.length - 1; i++) {
				double borderValue = borders[i];

				double prob2 = histPair.second.getOccurenceRange(borderValue, histPair.second.getMaximum());
				double prob1 = histPair.first.getOccurences(borderValue);

				selectivity += (prob1 * prob2);
			}
		}
		
		getLogger().debug(" Estimated selectivity : " + selectivity);
		return selectivity;
	}
	/***********************************************************************/
	/** GREATER-THAN                                                      **/
	/***********************************************************************/

	private double evaluateGreaterThanOperator(IExpression<?> arg0, IExpression<?> arg1) {
		getLogger().debug("Found GreaterThanOperator");

		double selectivity = 0.0;
		if (isOnlyOneAttribute(arg0, arg1)) {
			if (arg0 instanceof Constant)
				return evaluateSmallerThanOperator(arg1, arg0);

			HistogramValuePair hvp = getHistogramAndValue(arg0, arg1);
			if (hvp == null)
				return STD_SELECTIVITY;

			// get selectivity
			double valueCount = hvp.histogram.getValueCount();
			double occs = hvp.histogram.getOccurenceRange(hvp.value, hvp.histogram.getMaximum());
			selectivity = occs / valueCount;

		} else {
			HistogramPair histPair = getRelativeHistograms(arg0, arg1);
			if (histPair == null)
				return STD_SELECTIVITY;

			// Calcualte Selectivity
			double[] borders = histPair.first.getIntervalBorders();

			for (int i = 0; i < borders.length - 1; i++) {
				double borderValue = borders[i];

				double prob2 = histPair.second.getOccurenceRange(histPair.second.getMinimum(), borderValue);
				double prob1 = histPair.first.getOccurences(borderValue);

				selectivity += (prob1 * prob2);
			}
		}
		getLogger().debug(" Estimated selectivity : " + selectivity);

		return selectivity;
	}

	/***********************************************************************/
	/** HELPER-METHODS                                                    **/
	/***********************************************************************/
	
	private HistogramPair getRelativeHistograms(IExpression<?> arg0, IExpression<?> arg1) {
		// Get Attributes
		String firstAttribute = ((Variable) arg0).getIdentifier();
		String secondAttribute = ((Variable) arg1).getIdentifier();

		IHistogram firstHist = getHistogram(firstAttribute);
		IHistogram secondHist = getHistogram(secondAttribute);

		if (firstHist == null) {
			getLogger().warn("No histogram for attribute " + firstAttribute + " found ");
			return null;
		} else if (secondHist == null) {
			getLogger().warn("No histogram for attribute " + firstAttribute + " found ");
			return null;
		}

		// Make them relative
		IHistogram firstHistRelative = firstHist.toRelative();
		IHistogram secondHistRelative = secondHist.toRelative();
		return new HistogramPair(firstHistRelative, secondHistRelative);
	}

	public HistogramValuePair getHistogramAndValue(IExpression<?> arg0, IExpression<?> arg1) {
		String attribute = null;
		Double value = null;
		
		try {
			attribute = ((Variable) arg0).getIdentifier();
			value = (Double) ((Constant<?>) arg1).getValue();
		} catch (Throwable ex) {
			getLogger().warn("Operator is more complex than expected here...", ex);
			return null;
		}

		// get relevant histogram
		IHistogram histogram = getHistogram(attribute);

		if (value == null) {
			getLogger().warn("Cannot estimate value of expression " + arg1);
			return null;
		}
		if (histogram == null) {
			getLogger().warn("Cannot find histogram for attribute " + attribute);
			return null;
		}

		return new HistogramValuePair(histogram, value);
	}

	private IHistogram getHistogram(String attribute) {
		for (Map<SDFAttribute, IHistogram> hists : histograms) {
			for (SDFAttribute attr : hists.keySet()) {
				String srcName = attr.getSourceName();
				String attrName = attr.getAttributeName();
				String combined = srcName + "." + attrName;

				if (attribute.equals(attrName) || attribute.equals(combined)) {
					return hists.get(attr);
				}
			}
		}

		return null;
	}

	private static boolean isOnlyOneAttribute(IExpression<?> arg0, IExpression<?> arg1) {
		if (arg0 instanceof Variable && arg1 instanceof Variable)
			return false;
		return true;
	}
}
