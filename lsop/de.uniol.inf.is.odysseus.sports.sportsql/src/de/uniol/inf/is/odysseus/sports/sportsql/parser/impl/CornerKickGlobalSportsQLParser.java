package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.SoccerDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

/***
 * SportsQL Parser for Corner Kicks. PQL by Pascal Schmedt
 * 
 * @author Carsten Cordes
 *
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.GLOBAL }, name = "cornerkicks", parameters = {})
public class CornerKickGlobalSportsQLParser implements ISportsQLParser {

	public static final String TIME_BETWEEN_GOALLINE_AND_CORNER = "30000000000000.0";
	public static final String FACTOR_TS_GAMETIME = "60000000000000.0";

	// Tolerance in which we say that the ball is active (makes the field a bit
	// bigger)
	private static final int FIELD_TOLERANCE = 500;

	// Corner spot tolerance (square around the corner spot)
	private static final int CORNER_SPOT_TOLERANCE = 2000;

	/**
	 * Parses sportsQL
	 * @throws MissingDDCEntryException 
	 * @throws NumberFormatException 
	 */
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {

		double xMin = AbstractSportsDDCAccess.getFieldXMin();
		double xMax = AbstractSportsDDCAccess.getFieldXMax();
		double yMin = AbstractSportsDDCAccess.getFieldYMin();
		double yMax = AbstractSportsDDCAccess.getFieldYMax();

		ArrayList<ILogicalOperator> operatorList = new ArrayList<ILogicalOperator>();

		StreamAO source = OperatorBuildHelper.createGameStreamAO();

		operatorList.add(source);

		ArrayList<String> projectionAttributes = new ArrayList<String>();
		projectionAttributes.add("sid");
		projectionAttributes.add("ts");
		projectionAttributes.add("x");
		projectionAttributes.add("y");

		// projected = PROJECT({attributes = ['sid', 'ts', 'x',
		// 'y']},soccergame)
		ProjectAO projected = OperatorBuildHelper.createProjectAO(
				projectionAttributes, source);
		operatorList.add(projected);

		// Deleted RENAME Operator, because it's not needed.

		// activeBall = SELECT({
		// predicate = '(sid = 4 OR sid = 8 OR sid = 10 OR sid = 12) AND x >=
		// ${x_min}-500 AND x <= ${x_max}+500 AND y >= ${y_min}-500 AND y <=
		// ${y_max}+500'
		// },
		// renamed
		// )
		//
		
		// build ball sensor IDs predicate
		String predicate = "(";
		Iterator<Integer> ballSensorIterator = AbstractSportsDDCAccess.getBallSensorIds().iterator();
		while(ballSensorIterator.hasNext()) {
			int sensorId = ballSensorIterator.next();
			predicate += "sid = " + sensorId;
			if(ballSensorIterator.hasNext()) {
				predicate += " OR ";
			}
		}
		predicate += ")";
		
		predicate += " AND (x >= " + (xMin - FIELD_TOLERANCE) + ")";
		predicate += " AND (x <= " + (xMax + FIELD_TOLERANCE) + ")";
		predicate += "AND (y >= " + (yMin - FIELD_TOLERANCE) + ")";
		predicate += "AND (y <= " + (yMax + FIELD_TOLERANCE) + ")";
		
		
		SelectAO activeBall = OperatorBuildHelper.createSelectAO(
				predicate, projected);
		operatorList.add(activeBall);

		//
		// activeBallBehindGoalline = MAP({
		// expressions = [
		// 'sid',
		// 'ts',
		// 'x',
		// 'y',
		// ['eif(y < ${goalline1}, 1, 0)', 'BallBehindGoalline1_left'],
		// ['eif(y > ${goalline2}, 1, 0)', 'BallBehindGoalline2_right']
		// ]}, activeBall)
		ArrayList<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		expressions.add(OperatorBuildHelper.createExpressionParameter("sid",
				activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter("ts",
				activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter("x",
				activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter("y",
				activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(x < " + SoccerDDCAccess.getGoalareaLeftX() + ", 1, 0)",
				"BallBehindGoalline1_left", activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(x > " + SoccerDDCAccess.getGoalareaRightX() + ", 1, 0)",
				"BallBehindGoalline2_right", activeBall));

		MapAO activeBallBehindGoalline = OperatorBuildHelper
				.createMapAO(expressions, activeBall, 0, 0);
		operatorList.add(activeBallBehindGoalline);

		//
		// activeBallBehindGoalline_change = CHANGEDETECT({
		// attr = ['BallBehindGoalline1_left', 'BallBehindGoalline2_right']
		// }, activeBallBehindGoalline)

		ArrayList<String> changeDetectBallAttributes = new ArrayList<String>();
		changeDetectBallAttributes.add("BallBehindGoalline1_left");
		changeDetectBallAttributes.add("BallBehindGoalline2_right");

		ChangeDetectAO activeBallBehindGoallineChange = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						changeDetectBallAttributes, activeBallBehindGoalline),
						0, activeBallBehindGoalline);
		operatorList.add(activeBallBehindGoallineChange);

		// BallOnCornerSpot = MAP({
		// expressions = [
		// ['ts', 'spot_ball_ts'],
		// ['eif((x <= (${x_min} + 2000) OR x >= (${x_max} - 2000)) AND y >=
		// (${y_max} - 2000) , 1, 0)', 'BallOnCornerSpot1_bottom'],
		// ['eif((x <= (${x_min} + 2000) OR x >= (${x_max} - 2000)) AND y <=
		// (${y_min} + 2000) , 1, 0)', 'BallOnCornerSpot2_top']
		// ]}, activeBall)
		ArrayList<SDFExpressionParameter> ballExpressions = new ArrayList<SDFExpressionParameter>();
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter("ts",
				"spot_ball_ts", activeBall));
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif((y <= (" + (yMin + CORNER_SPOT_TOLERANCE) + ") OR y >= ("
						+ (yMax - CORNER_SPOT_TOLERANCE) + ")) AND x >= ("
						+ (xMax - CORNER_SPOT_TOLERANCE) + ") , 1, 0)",
				"BallOnCornerSpot1_bottom", activeBall));
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif((y <= (" + (yMin + CORNER_SPOT_TOLERANCE) + ") OR y >= ("
						+ (yMax - CORNER_SPOT_TOLERANCE) + ")) AND x <= ("
						+ (xMin + CORNER_SPOT_TOLERANCE) + ") , 1, 0)",
				"BallOnCornerSpot2_top", activeBall));
		MapAO ballOnCornerSpot = OperatorBuildHelper.createMapAO(
				ballExpressions, activeBall, 0, 0);
		operatorList.add(ballOnCornerSpot);
		//
		// BallOnCornerSpot_change = CHANGEDETECT({
		// attr = ['BallOnCornerSpot1_bottom', 'BallOnCornerSpot2_top']
		// }, BallOnCornerSpot)
		ArrayList<String> changeDetectCornerAttributes = new ArrayList<String>();
		changeDetectCornerAttributes.add("BallOnCornerSpot1_bottom");
		changeDetectCornerAttributes.add("BallOnCornerSpot2_top");

		ChangeDetectAO ballOnCornerSpotChange = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						changeDetectCornerAttributes, ballOnCornerSpot), 0,
						ballOnCornerSpot);
		operatorList.add(ballOnCornerSpotChange);

		// activeBallBehindGoalline_wnd = ELEMENTWINDOW({SIZE = 1, ADVANCE=1},
		// activeBallBehindGoalline_change)
		ElementWindowAO activeBallBehindGoallineWindow = OperatorBuildHelper
				.createElementWindowAO(1, 1, activeBallBehindGoallineChange);
		operatorList.add(activeBallBehindGoallineWindow);

		// BallOnCornerSpot_wnd = ELEMENTWINDOW({SIZE = 1, ADVANCE=1},
		// BallOnCornerSpot_change)
		ElementWindowAO ballOnCornerSpotWindow = OperatorBuildHelper
				.createElementWindowAO(1, 1, ballOnCornerSpotChange);
		operatorList.add(ballOnCornerSpotWindow);

		//
		// corner_join = JOIN({predicate='(BallBehindGoalline1_left = 1 OR
		// BallBehindGoalline2_right = 1) AND ts > (spot_ball_ts -
		// 30000000000000) AND ts < spot_ball_ts AND
		// (BallOnCornerSpot1_bottom = 1 OR BallOnCornerSpot2_top = 1) '},
		// activeBallBehindGoalline_wnd, BallOnCornerSpot_wnd)
		//

//		ArrayList<String> joinPredicates = new ArrayList<String>();
//		joinPredicates
//				.add("BallBehindGoalline1_left = 1 OR BallBehindGoalline2_right = 1");
//		joinPredicates.add("ts > (spot_ball_ts - "
//				+ TIME_BETWEEN_GOALLINE_AND_CORNER + ")");
//		joinPredicates.add("ts < spot_ball_ts");
//		joinPredicates
//				.add("BallOnCornerSpot1_bottom = 1 OR BallOnCornerSpot2_top = 1");

		String joinPredicates = "(BallBehindGoalline1_left = 1 OR BallBehindGoalline2_right = 1) AND (BallOnCornerSpot1_bottom = 1 OR BallOnCornerSpot2_top = 1) AND (ts > (spot_ball_ts - " + TIME_BETWEEN_GOALLINE_AND_CORNER + ")) AND (ts < spot_ball_ts)";
		JoinAO cornerJoin = OperatorBuildHelper.createJoinAO(
				joinPredicates, null, activeBallBehindGoallineWindow,
				ballOnCornerSpotWindow);
		operatorList.add(cornerJoin);

		// corners = CHANGEDETECT({
		// attr = ['ts'],
		// }, corner_join)
		ArrayList<String> changeDetectCorners = new ArrayList<String>();
		changeDetectCorners.add("ts");
		ChangeDetectAO corners = OperatorBuildHelper.createChangeDetectAO(
				OperatorBuildHelper.createAttributeList(changeDetectCorners,
						cornerJoin), 0, true, cornerJoin);
		operatorList.add(corners);

		// corners_time = MAP({
		// expressions = [
		// 'BallBehindGoalline1_left',
		// 'BallBehindGoalline2_right',
		// ['((spot_ball_ts - ${STARTTS}) / 60000000000000.0)',
		// 'timeInMinutes'],
		// 'BallOnCornerSpot1_bottom',
		// 'BallOnCornerSpot2_top'
		// ]}, corners)

		ArrayList<SDFExpressionParameter> cornersTimeExpressions = new ArrayList<SDFExpressionParameter>();
		cornersTimeExpressions
				.add(OperatorBuildHelper.createExpressionParameter(
						"BallBehindGoalline1_left", corners));
		cornersTimeExpressions
				.add(OperatorBuildHelper.createExpressionParameter(
						"BallBehindGoalline2_right", corners));
		cornersTimeExpressions.add(OperatorBuildHelper
				.createExpressionParameter("((spot_ball_ts) / " + FACTOR_TS_GAMETIME + ")", "timeInMinutes",
						corners));
		cornersTimeExpressions
				.add(OperatorBuildHelper.createExpressionParameter(
						"BallOnCornerSpot1_bottom", corners));
		cornersTimeExpressions.add(OperatorBuildHelper
				.createExpressionParameter("BallOnCornerSpot2_top", corners));

		MapAO cornersTime = OperatorBuildHelper.createMapAO(
				cornersTimeExpressions, corners, 0, 0);
		operatorList.add(cornersTime);

		return OperatorBuildHelper.finishQuery(cornersTime, operatorList,
				sportsQL.getName());
	}

}
