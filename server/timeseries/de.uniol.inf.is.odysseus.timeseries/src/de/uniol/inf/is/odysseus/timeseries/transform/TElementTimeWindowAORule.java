package de.uniol.inf.is.odysseus.timeseries.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MergeAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.timeseries.logicaloperator.ElementTimeWindowAO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * Rule transform an ElementTimeWindow in a query plan with standard operators
 * (map --> route -->timewindow --> merge)
 * 
 * IMPORTANT:
 * only experimental usage.
 * 
 * @author Christoph Schröer
 *
 */
public class TElementTimeWindowAORule extends AbstractTransformationRule<ElementTimeWindowAO> {

	@Override
	public void execute(final ElementTimeWindowAO operator, final TransformationConfiguration config)
			throws RuleException {

		// current implementation of previous value-method
		// fist Map
		// to identify, which time size has to be set
		SDFSchema inputSchema = operator.getInputSchema();
		List<SDFAttribute> inputAttributes = inputSchema.getAttributes();

		String[] expressionNameInput = new String[inputAttributes.size()];
		String[] expressionInput = new String[inputAttributes.size()];
		int expressionIndex = 0;
		for (SDFAttribute sdfAttribute : inputAttributes) {
			expressionNameInput[expressionIndex] = sdfAttribute.getAttributeName();
			expressionInput[expressionIndex] = sdfAttribute.getAttributeName();
			expressionIndex++;
		}

		String[] expressionNameIfExpr = new String[1];
		String[] expressionIfExpr = new String[1];

		expressionNameIfExpr[0] = "in_day_true";
		expressionIfExpr[0] = this.getIfExpression(operator);

		String[] expressionName = Stream.concat(Arrays.stream(expressionNameInput), Arrays.stream(expressionNameIfExpr))
				.toArray(String[]::new);
		String[] expression = Stream.concat(Arrays.stream(expressionInput), Arrays.stream(expressionIfExpr))
				.toArray(String[]::new);

		MapAO mapAo = this.insertMapAO(operator.getName() + "MapAO", expressionName, expression, operator);

		// Route
		// to the corresponding time window
		RouteAO routeAo = this.insertRouteO(operator.getName() + "RouteAO", mapAo);

		// Timewindows
		TimeWindowAO dayTimeWindow = this.insertDayTimeWindowAO("DayTimeWindowAO", operator, routeAo);

		TimeWindowAO nightTimeWindow = this.insertNightTimeWindowAO("NightTimeWindowAO", operator, routeAo);

		MapAO mapAoDayWindowUndo = this.insertMapAO(operator.getName() + "MapAOUndo1", expressionNameInput,
				expressionInput, dayTimeWindow);

		MapAO mapAoNightWindowUndo = this.insertMapAO(operator.getName() + "MapAOUndo2", expressionNameInput,
				expressionInput, nightTimeWindow);

		// Merge
		// to one datastream
		MergeAO merge = new MergeAO();
		merge.setName(operator.getName() + "MergeAO");

		LogicalPlan.insertOperatorBefore(merge, mapAoDayWindowUndo, 0, 0, 0);
		LogicalPlan.insertOperatorBefore(merge, mapAoNightWindowUndo, 1, 0, 0);
		insert(merge);


		LogicalPlan.removeOperator(operator, false);
		retract(operator);

	}

	private String getIfExpression(final ElementTimeWindowAO operator) {

		String expression = "";
//		int countElem = operator.getElementSize();
//		long regularTimeMillis = this.getTimeAsMilliseconds(operator.getRegularWindowSize());

		String timeEnd = operator.getTimeEnd();

operator.getNightDuration().getTime();

//		long timeWindowDiff = (long) ((countElem * regularTimeMillis)
//				+ Math.floor(countElem * (double) regularTimeMillis / (double) dayDuration) * nightDuration);
		
		long timeWindowSize = this.getTimeWindowSize(operator);

		/// FIXME: start in metadata!
		expression += "eif(start +";
		expression += "(" + timeWindowSize + ")";
		expression += "<=";
		expression += "Millisecond(toString(toDate(start + ";
		expression += "(" + timeWindowSize + ")";
		expression += "), \"d.M.y\")+\" " + timeEnd + "\", \"d.M.y H:m\")";
		expression += ",1,0)";

		return expression;
	}

	/**
	 * 
	 * @param operatorName
	 * @param expressionName
	 * @param expression
	 * @param operatorBefore
	 * @return
	 */
	private MapAO insertMapAO(final String operatorName, final String[] expressionName, final String[] expression,
			final UnaryLogicalOp operatorBefore) {
		final MapAO mapAO = new MapAO();
		mapAO.setName(operatorName);

		final List<NamedExpression> namedExpressions = new ArrayList<>(expressionName.length);

		for (int i = 0; i < expression.length; ++i) {
			SDFExpression sdfExpression = new SDFExpression(expression[i], null, MEP.getInstance());

			final NamedExpression namedExpression = new NamedExpression(expressionName[i], sdfExpression, null);
			namedExpressions.add(namedExpression);
		}

		mapAO.setExpressions(namedExpressions);
		System.out.println("mapAO: " + mapAO);

		LogicalPlan.insertOperatorBefore(mapAO, operatorBefore);
		insert(mapAO);

		return mapAO;
	}

	/**
	 * 
	 * @param operatorName
	 * @param elementWindowAO
	 * @param operatorBefore
	 * @return
	 */
	private TimeWindowAO insertDayTimeWindowAO(final String operatorName, final ElementTimeWindowAO elementWindowAO,
			final UnaryLogicalOp operatorBefore) {
		final TimeWindowAO timeWindowAO = new TimeWindowAO();
		timeWindowAO.setName(operatorName);

//		int countElem = elementWindowAO.getElementSize();
//		long regularTimeMillis = this.getTimeAsMilliseconds(elementWindowAO.getRegularWindowSize());
//
//		long dayDuration = elementWindowAO.getDayDuration().getTime();
//		long nightDuration = elementWindowAO.getNightDuration().getTime();
//
//		double nightFactor = Math.floor(countElem * (double) regularTimeMillis / (double) dayDuration);
//		long timeWindowDiff = (long) ((countElem * regularTimeMillis) + nightFactor * nightDuration);
//
//		if (nightFactor > 0) {
//			double frac = (double) dayDuration / (double) regularTimeMillis;
//			double floored = Math.floor(frac);
//			double relationEvenNumber = frac - floored;
//
//			double moduloFactor = 1.0 / relationEvenNumber;
//			if (nightFactor % moduloFactor == 0 || relationEvenNumber == 0) {
//				relationEvenNumber = nightFactor;
//				relationEvenNumber = (nightFactor - relationEvenNumber);
//			}
//
//			long subtract = (long) (regularTimeMillis * (relationEvenNumber));
//			timeWindowDiff = (long) (timeWindowDiff - (1 * subtract));
//		}
		
		long windowSizeTime = this.getTimeWindowSize(elementWindowAO);

		TimeValueItem windowSize = new TimeValueItem(windowSizeTime, TimeUnit.MILLISECONDS);
		timeWindowAO.setWindowSize(windowSize);

		TimeValueItem windowAdvance = elementWindowAO.getRegularWindowSize();
		timeWindowAO.setWindowAdvance(windowAdvance);

		System.out.println("dayTimeWindowAO: " + timeWindowAO);

		LogicalPlan.insertOperatorBefore(timeWindowAO, operatorBefore, 0, 0, 0);
		insert(timeWindowAO);

		return timeWindowAO;
	}

	/**
	 * 
	 * @param operatorName
	 * @param elementWindowAO
	 * @param operatorBefore
	 * @return
	 */
	private TimeWindowAO insertNightTimeWindowAO(final String operatorName, final ElementTimeWindowAO elementWindowAO,
			final UnaryLogicalOp operatorBefore) {
		final TimeWindowAO timeWindowAO = new TimeWindowAO();
		timeWindowAO.setName(operatorName);

this.getTimeAsMilliseconds(elementWindowAO.getRegularWindowSize());
elementWindowAO.getNightDuration().getTime();

//		double nightsFactor = Math.floor(countElem * (double) regularTimeMillis / (double) dayDuration);
//		long timeWindow = (long) ((countElem * regularTimeMillis));
//		timeWindow += (long) (nightsFactor * nightDuration);
//
//		double regularsInDay = (double) dayDuration / (double) regularTimeMillis;
//		double floored = Math.floor(regularsInDay);
//		double relationEvenNumber = regularsInDay - floored;
//
//		double moduloFactor = 1.0 / relationEvenNumber;
//		if (nightsFactor % moduloFactor == 0 || relationEvenNumber == 0) {
//			// reason of modulo-condition: after the 2. night the night-duration
//			// is on the even hour.
//			//
//
//			relationEvenNumber = 1;
//			//
//			// relationEvenNumber = (nightsFactor-relationEvenNumber);
//		}
//
//		long subtract = (long) (regularTimeMillis * (relationEvenNumber));
//		// timeWindow -= subtract;
//		timeWindow = (long) (timeWindow + (nightDuration - (subtract)));
		
		long windowSizeTime = this.getTimeWindowSize(elementWindowAO);
		windowSizeTime += this.getNightDuration(elementWindowAO);

		TimeValueItem windowSize = new TimeValueItem(windowSizeTime, TimeUnit.MILLISECONDS);
		timeWindowAO.setWindowSize(windowSize);

		TimeValueItem windowAdvance = elementWindowAO.getRegularWindowSize();
		timeWindowAO.setWindowAdvance(windowAdvance);

		System.out.println("nightTimeWindowAO: " + timeWindowAO);

		LogicalPlan.insertOperatorBefore(timeWindowAO, operatorBefore, 0, 1, 0);
		insert(timeWindowAO);

		return timeWindowAO;
	}

	/**
	 * 
	 * @param operatorName
	 * @param operatorBefore
	 * @return
	 */
	private RouteAO insertRouteO(final String operatorName, final UnaryLogicalOp operatorBefore) {
		final RouteAO routeAo = new RouteAO();
		routeAo.setName(operatorName);

		String expressionStr = "(in_day_true) = (1)";
		SDFExpression expression1 = new SDFExpression(expressionStr, null, MEP.getInstance());
		IPredicate<?> rel = new RelationalExpression<ITimeInterval>(expression1);

		String expressionStr2 = "(in_day_true) = (0)";
		SDFExpression expression2 = new SDFExpression(expressionStr2, null, MEP.getInstance());
		IPredicate<?> rel2 = new RelationalExpression<ITimeInterval>(expression2);

		List<IPredicate<?>> predicates = new LinkedList<>();
		predicates.add(rel);
		predicates.add(rel2);
		routeAo.setPredicates(predicates);

		LogicalPlan.insertOperatorBefore(routeAo, operatorBefore);
		insert(routeAo);

		return routeAo;
	}

	@Override
	public boolean isExecutable(final ElementTimeWindowAO operator, final TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

	@Override
	public String getName() {
		return "ElementTimeWindowAO ->  Map -> Route -> Timewindow -> Merge";
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	private long getTimeAsMilliseconds(TimeValueItem time) {
		long regularTimeMillis = 0;

		switch (time.getUnit()) {
		case MINUTES:
			regularTimeMillis = time.getTime() * 60000;
			break;
		case MILLISECONDS:
			regularTimeMillis = time.getTime();
		default:
			throw new IllegalArgumentException("Time Unit Conversion not implemented yet.");
		}

		return regularTimeMillis;

	}

	private long getTimeWindowSize(final ElementTimeWindowAO elementWindowAO) {

		int countElem = elementWindowAO.getElementSize();
		long regularTimeMillis = this.getTimeAsMilliseconds(elementWindowAO.getRegularWindowSize());

		long timeWindowSize = (long) (countElem * regularTimeMillis);

		long plusNights = this.getNightsDurations(elementWindowAO);
		timeWindowSize += plusNights;

		return timeWindowSize;

	}

	private long getNightsDurations(final ElementTimeWindowAO elementWindowAO) {

		System.out.println("nightTimeWindowAO: ");

		int countElem = elementWindowAO.getElementSize();
		long regularTimeMillis = this.getTimeAsMilliseconds(elementWindowAO.getRegularWindowSize());

		long dayDuration = elementWindowAO.getDayDuration().getTime();
		long nightDuration = this.getNightDuration(elementWindowAO);

		double nightFactorPrecision = countElem * (double) regularTimeMillis / (double) dayDuration;
		double nightFactor = Math.floor(nightFactorPrecision);

		long nightTimeWindowSize = (long) (nightFactor * nightDuration);

		return nightTimeWindowSize;

		// TODO Example: end at 17:30 the next time interval should end at 09:00

		// if (nightFactor > 0) {
		// double frac = (double) dayDuration / (double) regularTimeMillis;
		// double floored = Math.floor(frac);
		// double relationEvenNumber = frac - floored;
		//
		// double moduloFactor = 1.0 / relationEvenNumber;
		// if (nightFactor % moduloFactor == 0 || relationEvenNumber == 0) {
		// relationEvenNumber = nightFactor;
		// relationEvenNumber = (nightFactor - relationEvenNumber);
		// }
		//
		// long subtract = (long) (regularTimeMillis * (relationEvenNumber));
		// timeWindowDiff = (long) (timeWindowDiff - (1 * subtract));
		// }

	}

	/**
	 * duration in milliseconds for one night.
	 * 
	 * @return
	 */
	private long getNightDuration(final ElementTimeWindowAO elementWindowAO) {
		
		System.out.println("nightTimeWindowAO: ");

		long regularTimeMillis = this.getTimeAsMilliseconds(elementWindowAO.getRegularWindowSize());

		long nightDuration = elementWindowAO.getNightDuration().getTime();
		long nightDurationWithoutRegularSize = nightDuration - regularTimeMillis;

		return nightDurationWithoutRegularSize;
		
	}
	
	@SuppressWarnings("unused")
	private long getWeekendsDurations(final ElementTimeWindowAO elementWindowAO){
		return 0;
	}
	
	@SuppressWarnings("unused")
	private long getWeekendDuration(final ElementTimeWindowAO elementWindowAO){
		return 0;
	}

}
