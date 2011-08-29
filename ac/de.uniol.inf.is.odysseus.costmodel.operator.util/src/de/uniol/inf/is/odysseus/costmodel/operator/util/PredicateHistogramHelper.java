package de.uniol.inf.is.odysseus.costmodel.operator.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.mep.Constant;
import de.uniol.inf.is.odysseus.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.Variable;
import de.uniol.inf.is.odysseus.mep.functions.EqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.GreaterEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.GreaterThanOperator;
import de.uniol.inf.is.odysseus.mep.functions.SmallerEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.SmallerThanOperator;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class PredicateHistogramHelper {

	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(PredicateHistogramHelper.class);
		}
		return _logger;
	}

	private class HistAttrValue {
		public IHistogram histogram;
		public SDFAttribute attribute;
		public Double value;

		public HistAttrValue(IHistogram histogram, SDFAttribute attribute, Double value) {
			this.histogram = histogram;
			this.attribute = attribute;
			this.value = value;
		}
	}

	private class TwoHistAttr {
		public IHistogram histogram1;
		public IHistogram histogram2;
		public SDFAttribute attr1;
		public SDFAttribute attr2;

		public TwoHistAttr(IHistogram hist1, SDFAttribute attr1, IHistogram hist2, SDFAttribute attr2) {
			this.histogram1 = hist1;
			this.histogram2 = hist2;
			this.attr1 = attr1;
			this.attr2 = attr2;
		}
	}

	private IPredicate<?> predicate;
	private List<Map<SDFAttribute, IHistogram>> histograms;

	public PredicateHistogramHelper(IPredicate<?> predicate, List<Map<SDFAttribute, IHistogram>> histograms) {
		this.predicate = predicate;
		this.histograms = histograms;
	}

	public Map<SDFAttribute, IHistogram> getHistograms() {

		// merge histograms in one map
		Map<SDFAttribute, IHistogram> histograms = new HashMap<SDFAttribute, IHistogram>();
		for (Map<SDFAttribute, IHistogram> histogramMap : this.histograms) {
			for (SDFAttribute attribute : histogramMap.keySet()) {
				if (histograms.containsKey(attribute))
					getLogger().warn("Two histograms for one attribute: " + attribute);

				histograms.put(attribute, histogramMap.get(attribute).clone());
			}
		}

		try {
			Map<SDFAttribute, IHistogram> sel = evaluatePredicate(predicate, histograms);
			
			if( sel != null ) {
				for( SDFAttribute attr : sel.keySet() ) {
					histograms.put(attr, sel.get(attr));
				}
			}				
			
			return histograms;

		} catch (Exception ex) {
			ex.printStackTrace();
			return histograms;
		}
	}

	private Map<SDFAttribute, IHistogram> evaluatePredicate(IPredicate<?> predicate, Map<SDFAttribute, IHistogram> histograms) {
		if (predicate instanceof RelationalPredicate) {
			return evaluateRelationalPredicate((RelationalPredicate) predicate, histograms);
		} else if (predicate instanceof ComplexPredicate) {
			return evaluateComplexPredicate((ComplexPredicate<?>) predicate, histograms);
		} else {
			getLogger().warn("Unsupported PredicateType :" + predicate.getClass());
			return histograms;
		}

	}

	private Map<SDFAttribute, IHistogram> evaluateComplexPredicate(ComplexPredicate<?> complexPredicate, Map<SDFAttribute, IHistogram> histograms) {

		if (ComplexPredicateHelper.isAndPredicate(complexPredicate)) {
			return evaluateAndPredicate(complexPredicate, histograms);
		} else if (ComplexPredicateHelper.isOrPredicate(complexPredicate)) {
			return evaluateOrPredicate(complexPredicate, histograms);
		} else {
			getLogger().warn("Unsupported ComplexPredicate : " + complexPredicate.getClass());
		}

		return histograms;
	}

	private Map<SDFAttribute, IHistogram> evaluateOrPredicate(ComplexPredicate<?> orPredicate, Map<SDFAttribute, IHistogram> histograms) {
		Map<SDFAttribute, IHistogram> left = evaluatePredicate(orPredicate.getLeft(), histograms);
		Map<SDFAttribute, IHistogram> right = evaluatePredicate(orPredicate.getRight(), histograms);

		if (left == null && right == null)
			return null;

		Map<SDFAttribute, IHistogram> result = new HashMap<SDFAttribute, IHistogram>();
		if (left != null) {
			for (SDFAttribute attribute : left.keySet()) {
				result.put(attribute, left.get(attribute));
			}
		}

		if (right != null) {
			for (SDFAttribute attribute : right.keySet()) {

				if (result.containsKey(attribute)) {
					getLogger().debug("Merging two histograms of " + attribute);

					// merge both histograms
					IHistogram histLeft = result.get(attribute);
					IHistogram histRight = right.get(attribute);

					IHistogram leftRelative = histLeft.toRelative();
					IHistogram rightRelative = histRight.toRelative();

					IHistogram histMoreIntervals = null;
					IHistogram histLessIntervals = null;
					if (leftRelative.getIntervalCount() > rightRelative.getIntervalCount()) {
						histMoreIntervals = leftRelative;
						histLessIntervals = rightRelative;
					} else {
						histMoreIntervals = rightRelative;
						histLessIntervals = leftRelative;
					}

					double[] borders = histMoreIntervals.getIntervalBorders();
					IHistogram histResultRelative = histMoreIntervals.clone();
					for (int i = 0; i < borders.length - 1; i++) {
						double prob1 = histMoreIntervals.getOccurences(borders[i]);
						double prob2 = histLessIntervals.getOccurenceRange(borders[i], borders[i + 1]);
						histResultRelative.setOccurences(i, prob1 + prob2 - (prob1 * prob2));
					}

					double valueCount = histLeft.getValueCount() + histRight.getValueCount();
					result.put(attribute, histResultRelative.normalize().toAbsolute(valueCount));
				} else {
					result.put(attribute, right.get(attribute));
				}
			}
		}

		return result;
	}

	private Map<SDFAttribute, IHistogram> evaluateAndPredicate(ComplexPredicate<?> andPredicate, Map<SDFAttribute, IHistogram> histograms) {
		Map<SDFAttribute, IHistogram> left = evaluatePredicate(andPredicate.getLeft(), histograms);
		Map<SDFAttribute, IHistogram> right = evaluatePredicate(andPredicate.getRight(), histograms);

		if (left == null && right == null)
			return null;

		Map<SDFAttribute, IHistogram> result = new HashMap<SDFAttribute, IHistogram>();
		if (left != null) {
			for (SDFAttribute attribute : left.keySet()) {
				result.put(attribute, left.get(attribute));
			}
		}

		if( right != null ) {
			for (SDFAttribute attribute : right.keySet()) {
	
				if (result.containsKey(attribute)) {
					getLogger().debug("Merging two histograms of " + attribute);
	
					// merge both histograms
					IHistogram histLeft = result.get(attribute);
					IHistogram histRight = right.get(attribute);
	
					IHistogram leftRelative = histLeft.toRelative();
					IHistogram rightRelative = histRight.toRelative();
	
					IHistogram histMoreIntervals = null;
					IHistogram histLessIntervals = null;
					if (leftRelative.getIntervalCount() > rightRelative.getIntervalCount()) {
						histMoreIntervals = leftRelative;
						histLessIntervals = rightRelative;
					} else {
						histMoreIntervals = rightRelative;
						histLessIntervals = leftRelative;
					}
	
					double[] borders = histMoreIntervals.getIntervalBorders();
					IHistogram histResultRelative = histMoreIntervals.clone();
					for (int i = 0; i < borders.length - 1; i++) {
						double prob1 = histMoreIntervals.getOccurences(borders[i]);
						double prob2 = histLessIntervals.getOccurenceRange(borders[i], borders[i + 1]);
						histResultRelative.setOccurences(i, prob1 * prob2);
					}
	
					double valueCount = Math.max(histLeft.getValueCount(), histRight.getValueCount());
					result.put(attribute, histResultRelative.normalize().toAbsolute(valueCount));
				} else {
					result.put(attribute, right.get(attribute));
				}
			}
		}
		
		return result;
	}

	private Map<SDFAttribute, IHistogram> evaluateRelationalPredicate(RelationalPredicate relationalPredicate, Map<SDFAttribute, IHistogram> histograms) {
		getLogger().debug("Evaluate RelationalPredicate " + relationalPredicate);

		SDFExpression expression = relationalPredicate.getExpression();
		IExpression<?> mepExpression = expression.getMEPExpression();

		if (mepExpression instanceof IBinaryOperator) {
			return evaluateBinaryOperator((IBinaryOperator<?>) mepExpression, histograms);
		} else {
			getLogger().warn("Unsupported MEP-Expression: " + mepExpression);
			return histograms;
		}
	}

	private Map<SDFAttribute, IHistogram> evaluateBinaryOperator(IBinaryOperator<?> op, Map<SDFAttribute, IHistogram> histograms) {

		IExpression<?>[] args = op.getArguments();

		if (op instanceof GreaterThanOperator) {
			return evaluateGreaterThanOperator(args[0], args[1], histograms);
		} else if (op instanceof SmallerThanOperator) {
			return evaluateSmallerThanOperator(args[0], args[1], histograms);
		} else if (op instanceof EqualsOperator) {
			return evaluateEqualsOperator(args[0], args[1], histograms);
		} else if (op instanceof GreaterEqualsOperator) {
			return evaluateGreaterThanOperator(args[0], args[1], histograms);
		} else if (op instanceof SmallerEqualsOperator) {
			return evaluateSmallerThanOperator(args[0], args[1], histograms);
		} else {
			getLogger().warn("Unsupported BinaryOperator: " + op);
			return histograms;
		}
	}

	/***********************************************************************/
	/** EQUALS **/
	/***********************************************************************/
	private Map<SDFAttribute, IHistogram> evaluateEqualsOperator(IExpression<?> arg0, IExpression<?> arg1, Map<SDFAttribute, IHistogram> histograms) {
		getLogger().debug("Found EqualsOperator");

		if (isOnlyOneAttribute(arg0, arg1)) {
			if (arg0 instanceof Constant)
				return evaluateEqualsOperator(arg1, arg0, histograms);

			HistAttrValue hav = getHistAttrValue(arg0, arg1, histograms);
			if (hav == null)
				return null;

			// cut histogram
			IHistogram cuttedHistogram = hav.histogram.cutLower(hav.value).cutHigher(hav.value);
			Map<SDFAttribute, IHistogram> resultHistograms = new HashMap<SDFAttribute, IHistogram>();
			resultHistograms.put(hav.attribute, cuttedHistogram);
			return resultHistograms;

		} else {
			TwoHistAttr tha = getTwoHistAttr(arg0, arg1, histograms);
			if (tha == null)
				return null;

			// Calculate new histograms
			IHistogram hist1Relative = tha.histogram1.toRelative();
			IHistogram hist2Relative = tha.histogram2.toRelative();
			
			IHistogram biggerHist = null;
			IHistogram smallerHist = null;
			if( hist1Relative.getIntervalCount() > 
				hist2Relative.getIntervalCount()) {
				biggerHist = hist1Relative;
				smallerHist = hist2Relative;
			} else {
				biggerHist = hist2Relative;
				smallerHist = hist1Relative;
			}
			
			double[] borders = biggerHist.getIntervalBorders();
			IHistogram histResult = biggerHist.clone();
			for( int i = 0; i < borders.length - 1; i++ ) {
				double intervalStart = borders[i];
				double intervalEnd = borders[i + 1];
				
				double prob1 = biggerHist.getOccurences(intervalStart);
				double prob2 = smallerHist.getOccurenceRange(intervalStart, intervalEnd);
				double result = prob1 * prob2;

				histResult.setOccurences(i, result); // set value in
													 // histogram
			}
			
			// make absolute again
			IHistogram hist1Result = histResult.normalize().toAbsolute(tha.histogram1.getValueCount());
			IHistogram hist2Result = histResult.normalize().toAbsolute(tha.histogram2.getValueCount());

			// insert results
			Map<SDFAttribute, IHistogram> resultHistograms = new HashMap<SDFAttribute, IHistogram>();
			resultHistograms.put(tha.attr1, hist1Result);
			resultHistograms.put(tha.attr2, hist2Result);
			return resultHistograms;
		}
	}

	/***********************************************************************/
	/** SMALLER-THAN **/
	/***********************************************************************/
	private Map<SDFAttribute, IHistogram> evaluateSmallerThanOperator(IExpression<?> arg0, IExpression<?> arg1, Map<SDFAttribute, IHistogram> histograms) {
		getLogger().debug("Found SmallerThanOperator");

		if (isOnlyOneAttribute(arg0, arg1)) {
			if (arg0 instanceof Constant)
				return evaluateGreaterThanOperator(arg1, arg0, histograms);

			HistAttrValue hav = getHistAttrValue(arg0, arg1, histograms);
			if (hav == null)
				return null;

			// cut histogram
			IHistogram cuttedHistogram = hav.histogram.cutHigher(hav.value);
			Map<SDFAttribute, IHistogram> resultHistograms = new HashMap<SDFAttribute, IHistogram>();
			resultHistograms.put(hav.attribute, cuttedHistogram);
			return resultHistograms;

		} else {
			TwoHistAttr tha = getTwoHistAttr(arg0, arg1, histograms);
			if (tha == null)
				return null;

			// Calculate new histograms
			IHistogram hist1Relative = tha.histogram1.toRelative();
			IHistogram hist2Relative = tha.histogram2.toRelative();
			IHistogram hist1RelativeResult = hist1Relative.clone();
			IHistogram hist2RelativeResult = hist2Relative.clone();

			double[] borders = hist1Relative.getIntervalBorders();
			for (int i = 0; i < borders.length - 1; i++) {
				double intervalStart = borders[i];

				double prob1 = hist1Relative.getOccurences(intervalStart);
				double prob2 = hist2Relative.getOccurenceRange(intervalStart, hist2Relative.getMaximum());
				double result = prob1 * prob2;

				hist1RelativeResult.setOccurences(i, result); // set value in
																// histogram
			}

			double[] borders2 = hist2Relative.getIntervalBorders();
			for (int i = 0; i < borders2.length - 1; i++) {
				double intervalStart = borders2[i];

				double prob2 = hist2Relative.getOccurences(intervalStart);
				double prob1 = hist1Relative.getOccurenceRange(hist1Relative.getMinimum(), intervalStart);
				double result = prob1 * prob2;

				hist2RelativeResult.setOccurences(i, result); // set value in
																// histogram
			}

			// make absolute again
			IHistogram hist1Result = hist1RelativeResult.toAbsolute(tha.histogram1.getValueCount());
			IHistogram hist2Result = hist2RelativeResult.toAbsolute(tha.histogram2.getValueCount());

			// insert results
			Map<SDFAttribute, IHistogram> resultHistograms = new HashMap<SDFAttribute, IHistogram>();
			resultHistograms.put(tha.attr1, hist1Result);
			resultHistograms.put(tha.attr2, hist2Result);
			return resultHistograms;
		}
	}

	/***********************************************************************/
	/** GREATER-THAN **/
	/***********************************************************************/

	private Map<SDFAttribute, IHistogram> evaluateGreaterThanOperator(IExpression<?> arg0, IExpression<?> arg1, Map<SDFAttribute, IHistogram> histograms) {
		getLogger().debug("Found GreaterThanOperator");

		if (isOnlyOneAttribute(arg0, arg1)) {
			if (arg0 instanceof Constant)
				return evaluateSmallerThanOperator(arg1, arg0, histograms);

			HistAttrValue hav = getHistAttrValue(arg0, arg1, histograms);
			if (hav == null)
				return null;

			// cut histogram
			IHistogram cuttedHistogram = hav.histogram.cutLower(hav.value);
			Map<SDFAttribute, IHistogram> resultHistograms = new HashMap<SDFAttribute, IHistogram>();
			resultHistograms.put(hav.attribute, cuttedHistogram);
			return resultHistograms;

		} else {
			TwoHistAttr tha = getTwoHistAttr(arg0, arg1, histograms);
			if (tha == null)
				return null;

			// Calculate new histograms
			IHistogram hist1Relative = tha.histogram1.toRelative();
			IHistogram hist2Relative = tha.histogram2.toRelative();
			IHistogram hist1RelativeResult = hist1Relative.clone();
			IHistogram hist2RelativeResult = hist2Relative.clone();

			double[] borders = hist1Relative.getIntervalBorders();
			for (int i = 0; i < borders.length - 1; i++) {
				double intervalStart = borders[i];

				double prob1 = hist1Relative.getOccurences(intervalStart);
				double prob2 = hist2Relative.getOccurenceRange(hist2Relative.getMinimum(), intervalStart);
				double result = prob1 * prob2;

				hist1RelativeResult.setOccurences(i, result); // set value in
																// histogram
			}

			double[] borders2 = hist2Relative.getIntervalBorders();
			for (int i = 0; i < borders2.length - 1; i++) {
				double intervalStart = borders2[i];

				double prob2 = hist2Relative.getOccurences(intervalStart);
				double prob1 = hist1Relative.getOccurenceRange(intervalStart, hist1Relative.getMaximum());
				double result = prob1 * prob2;

				hist2RelativeResult.setOccurences(i, result); // set value in
																// histogram
			}

			// make absolute again
			IHistogram hist1Result = hist1RelativeResult.toAbsolute(tha.histogram1.getValueCount());
			IHistogram hist2Result = hist2RelativeResult.toAbsolute(tha.histogram2.getValueCount());

			// insert results
			Map<SDFAttribute, IHistogram> resultHistograms = new HashMap<SDFAttribute, IHistogram>();
			resultHistograms.put(tha.attr1, hist1Result);
			resultHistograms.put(tha.attr2, hist2Result);
			return resultHistograms;
		}
	}

	/***********************************************************************/
	/** HELPER-METHODS **/
	/***********************************************************************/

	private HistAttrValue getHistAttrValue(IExpression<?> arg0, IExpression<?> arg1, Map<SDFAttribute, IHistogram> histograms) {
		try {
			Variable var = (Variable) arg0;
			if (var == null) {
				getLogger().warn("Operator more complex than expected");
				return null;
			}
			Double value = (Double) ((Constant<?>) arg1).getValue();
			if (value == null) {
				getLogger().warn("Could not get value");
				return null;
			}
			SDFAttribute attribute = getAttribute(var.getIdentifier(), histograms.keySet());
			if (attribute == null) {
				getLogger().warn("Could not find attribute with name " + var.getIdentifier());
				return null;
			}
			IHistogram histogram = histograms.get(attribute);
			if (histogram == null) {
				getLogger().warn("Could not find histogram for attribute " + attribute);
				return null;
			}
			return new HistAttrValue(histogram, attribute, value);
		} catch (Exception ex) {
			getLogger().warn("Error during evaluating expressions " + arg0 + " and " + arg1);
			getLogger().warn(ex.toString());
			return null;
		}
	}

	private TwoHistAttr getTwoHistAttr(IExpression<?> arg0, IExpression<?> arg1, Map<SDFAttribute, IHistogram> histograms) {
		Variable var1 = (Variable) arg0;
		Variable var2 = (Variable) arg1;
		if (var1 == null || var2 == null) {
			getLogger().warn("Operator more complex than expected");
			return null;
		}
		SDFAttribute attr1 = getAttribute(var1.getIdentifier(), histograms.keySet());
		if (attr1 == null) {
			getLogger().warn("Could not find attribute with name " + var1.getIdentifier());
			return null;
		}
		SDFAttribute attr2 = getAttribute(var2.getIdentifier(), histograms.keySet());
		if (attr2 == null) {
			getLogger().warn("Could not find attribute with name " + var2.getIdentifier());
			return null;
		}
		IHistogram histogram1 = histograms.get(attr1);
		if (histogram1 == null) {
			getLogger().warn("Could not find histogram for attribute " + attr1);
			return null;
		}
		IHistogram histogram2 = histograms.get(attr2);
		if (histogram2 == null) {
			getLogger().warn("Could not find histogram for attribute " + attr2);
			return null;
		}

		return new TwoHistAttr(histogram1, attr1, histogram2, attr2);
	}

	private SDFAttribute getAttribute(String attribute, Set<SDFAttribute> attributes) {
		for (SDFAttribute attr : attributes) {
			String srcName = attr.getSourceName();
			String attrName = attr.getAttributeName();
			String combined = srcName + "." + attrName;

			if (attribute.equals(attrName) || attribute.equals(combined)) {
				return attr;
			}
		}

		return null;
	}

	private boolean isOnlyOneAttribute(IExpression<?> arg0, IExpression<?> arg1) {
		if (arg0 instanceof Variable && arg1 instanceof Variable)
			return false;
		return true;
	}
}
