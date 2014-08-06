package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

/***
 * SportsQL Parser for Corner Kicks. PQL by Pascal Schmedt
 * 
 * @author Carsten Cordes
 *
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.GLOBAL }, name = "cornerkicks", parameters = { })
public class CornerKickGlobalSportsQLParser implements ISportsQLParser {

	public static final String TIME_BETWEEN_GOALLINE_AND_CORNER =  "30000000000000.0";
	public static final String FACTOR_TS_GAMETIME = "60000000000000.0";
	
	/**
	 * Parses sportsQL
	 */
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {
		
		int xMin = OperatorBuildHelper.LOWERLEFT_X;
		int xMax = OperatorBuildHelper.LOWERRIGHT_X;
		int yMin = OperatorBuildHelper.LOWERLEFT_Y;
		int yMax = OperatorBuildHelper.UPPERLEFT_Y;

		String startTS = OperatorBuildHelper.TS_GAME_START;

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
		ILogicalOperator projected = OperatorBuildHelper.createProjectAO(
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
		ArrayList<String> predicates = new ArrayList<String>();
		predicates.add("sid= " + OperatorBuildHelper.BALL_1 + " OR sid= "
				+ OperatorBuildHelper.BALL_2 + " OR sid= "
				+ OperatorBuildHelper.BALL_3 + " OR sid= "
				+ OperatorBuildHelper.BALL_4);
		predicates.add("x >= " + (xMin - 500));
		predicates.add("x <= " + (xMax + 500));
		predicates.add("y >= " + (yMin - 500));
		predicates.add("y <= " + (yMax + 500));
		ILogicalOperator activeBall = OperatorBuildHelper.createSelectAO(
				predicates, projected);
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
				"eif(y < " + OperatorBuildHelper.GOAL_AREA_2_Y + ", 1, 0)",
				"BallBehindGoalline1_left", activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(y > " + OperatorBuildHelper.GOAL_AREA_1_Y + ", 1, 0)",
				"BallBehindGoalline2_right", activeBall));

		ILogicalOperator activeBallBehindGoalline = OperatorBuildHelper
				.createMapAO(expressions, activeBall, 0, 0);
		operatorList.add(activeBallBehindGoalline);

		//
		// activeBallBehindGoalline_change = CHANGEDETECT({
		// attr = ['BallBehindGoalline1_left', 'BallBehindGoalline2_right']
		// }, activeBallBehindGoalline)

		ArrayList<String> changeDetectBallAttributes = new ArrayList<String>();
		changeDetectBallAttributes.add("BallBehindGoalline1_left");
		changeDetectBallAttributes.add("BallBehindGoalline2_right");

		ILogicalOperator activeBallBehindGoallineChange = OperatorBuildHelper
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
				"eif((x <= (" + xMin + " + 2000) OR x >= (" + xMax
						+ " - 2000)) AND y >= (" + yMax + " - 2000) , 1, 0)",
				"BallOnCornerSpot1_bottom", activeBall));
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif((x <= (" + xMin + " + 2000) OR x >= (" + xMax
						+ " - 2000)) AND y <= (" + yMin + " + 2000) , 1, 0)",
				"BallOnCornerSpot2_top", activeBall));
		ILogicalOperator ballOnCornerSpot = OperatorBuildHelper.createMapAO(
				ballExpressions, activeBall, 0, 0);
		operatorList.add(ballOnCornerSpot);
//
		// BallOnCornerSpot_change = CHANGEDETECT({
		// attr = ['BallOnCornerSpot1_bottom', 'BallOnCornerSpot2_top']
		// }, BallOnCornerSpot)
		ArrayList<String> changeDetectCornerAttributes = new ArrayList<String>();
		changeDetectCornerAttributes.add("BallOnCornerSpot1_bottom");
		changeDetectCornerAttributes.add("BallOnCornerSpot2_top");

		ILogicalOperator ballOnCornerSpotChange = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						changeDetectCornerAttributes, ballOnCornerSpot), 0,
						ballOnCornerSpot);
		operatorList.add(ballOnCornerSpotChange);

		// activeBallBehindGoalline_wnd = ELEMENTWINDOW({SIZE = 1, ADVANCE=1},
		// activeBallBehindGoalline_change)
		ILogicalOperator activeBallBehindGoallineWindow = OperatorBuildHelper
				.createElementWindowAO(1, 1, activeBallBehindGoallineChange);
		operatorList.add(activeBallBehindGoallineWindow);

		// BallOnCornerSpot_wnd = ELEMENTWINDOW({SIZE = 1, ADVANCE=1},
		// BallOnCornerSpot_change)
		ILogicalOperator ballOnCornerSpotWindow = OperatorBuildHelper
				.createElementWindowAO(1, 1, ballOnCornerSpotChange);
		operatorList.add(ballOnCornerSpotWindow);

		//
		// corner_join = JOIN({predicate='(BallBehindGoalline1_left = 1 OR
		// BallBehindGoalline2_right = 1) AND ts > (spot_ball_ts -
		// 30000000000000) AND ts < spot_ball_ts AND
		// (BallOnCornerSpot1_bottom = 1 OR BallOnCornerSpot2_top = 1) '},
		// activeBallBehindGoalline_wnd, BallOnCornerSpot_wnd)
		//

		ArrayList<String> joinPredicates = new ArrayList<String>();
		joinPredicates
				.add("BallBehindGoalline1_left = 1 OR BallBehindGoalline2_right = 1");
		joinPredicates.add("ts > (spot_ball_ts - " + TIME_BETWEEN_GOALLINE_AND_CORNER + ")");
		joinPredicates.add("ts < spot_ball_ts");
		joinPredicates
				.add("BallOnCornerSpot1_bottom = 1 OR BallOnCornerSpot2_top = 1");

		ILogicalOperator cornerJoin = OperatorBuildHelper.createJoinAO(
				joinPredicates, activeBallBehindGoallineWindow,
				ballOnCornerSpotWindow);
		operatorList.add(cornerJoin);

		// corners = CHANGEDETECT({
		// attr = ['ts'],
		// }, corner_join)
		ArrayList<String> changeDetectCorners = new ArrayList<String>();
		changeDetectCorners.add("ts");		
		ILogicalOperator corners = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(changeDetectCorners,
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
				.createExpressionParameter("((spot_ball_ts - " + startTS
						+ ") / " + FACTOR_TS_GAMETIME + ")", "timeInMinutes", corners));
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
