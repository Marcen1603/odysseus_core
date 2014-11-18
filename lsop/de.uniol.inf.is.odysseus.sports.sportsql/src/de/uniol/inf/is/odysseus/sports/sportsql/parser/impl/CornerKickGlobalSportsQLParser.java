package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SoccerGameAttributes;
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
	public ILogicalQuery parse(ISession session,SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {

		double xMin = AbstractSportsDDCAccess.getFieldXMin();
		double xMax = AbstractSportsDDCAccess.getFieldXMax();
		double yMin = AbstractSportsDDCAccess.getFieldYMin();
		double yMax = AbstractSportsDDCAccess.getFieldYMax();

		ArrayList<ILogicalOperator> operatorList = new ArrayList<ILogicalOperator>();
		ArrayList<String> attributes = new ArrayList<String>();
		
		StreamAO source = OperatorBuildHelper.createGameStreamAO(session);

		operatorList.add(source);

		ArrayList<String> projectionAttributes = new ArrayList<String>();
		projectionAttributes.add(SoccerGameAttributes.ENTITY_ID);
		projectionAttributes.add(SoccerGameAttributes.TS);
		projectionAttributes.add(SoccerGameAttributes.X);
		projectionAttributes.add(SoccerGameAttributes.Y);

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
		Iterator<Integer> ballSensorIterator = AbstractSportsDDCAccess.getBallEntityIds().iterator();
		while(ballSensorIterator.hasNext()) {
			int entityId = ballSensorIterator.next();
			predicate += SoccerGameAttributes.ENTITY_ID + " = " + entityId;
			if(ballSensorIterator.hasNext()) {
				predicate += " OR ";
			}
		}
		predicate += ")";
		
		predicate += " AND (" + SoccerGameAttributes.X + " >= " + (xMin - FIELD_TOLERANCE) + ")";
		predicate += " AND (" + SoccerGameAttributes.X + " <= " + (xMax + FIELD_TOLERANCE) + ")";
		predicate += "AND (" + SoccerGameAttributes.Y + " >= " + (yMin - FIELD_TOLERANCE) + ")";
		predicate += "AND (" + SoccerGameAttributes.Y + " <= " + (yMax + FIELD_TOLERANCE) + ")";
		
		
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
		expressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.ENTITY_ID,
				activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TS,
				activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.X,
				activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.Y,
				activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.X + " < " + SoccerDDCAccess.getGoalareaLeftX() + ", 1, 0)",
				"BallBehindGoalline1_left", activeBall));
		expressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + SoccerGameAttributes.X + " > " + SoccerDDCAccess.getGoalareaRightX() + ", 1, 0)",
				"BallBehindGoalline2_right", activeBall));

		MapAO activeBallBehindGoalline = OperatorBuildHelper
				.createMapAO(expressions, activeBall, 0, 0, false);
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
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter(SoccerGameAttributes.TS,
				"spot_ball_ts", activeBall));
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif((" + SoccerGameAttributes.Y + " <= (" + (yMin + CORNER_SPOT_TOLERANCE) + ") OR " + SoccerGameAttributes.Y + "  >= ("
						+ (yMax - CORNER_SPOT_TOLERANCE) + ")) AND " + SoccerGameAttributes.X + " >= ("
						+ (xMax - CORNER_SPOT_TOLERANCE) + ") , 1, 0)",
				"BallOnCornerSpot1_bottom", activeBall));
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif((" + SoccerGameAttributes.Y + " <= (" + (yMin + CORNER_SPOT_TOLERANCE) + ") OR " + SoccerGameAttributes.Y + " >= ("
						+ (yMax - CORNER_SPOT_TOLERANCE) + ")) AND " + SoccerGameAttributes.X + " <= ("
						+ (xMin + CORNER_SPOT_TOLERANCE) + ") , 1, 0)",
				"BallOnCornerSpot2_top", activeBall));
		MapAO ballOnCornerSpot = OperatorBuildHelper.createMapAO(
				ballExpressions, activeBall, 0, 0, false);
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
		
		
		// time windows for goal behind line and ball on corner spot
		TimeWindowAO ball_on_cornerspot_window = OperatorBuildHelper.createTimeWindowAO(30, "SECONDS", ballOnCornerSpotChange);
		operatorList.add(ball_on_cornerspot_window);
		
		TimeWindowAO behind_goalline_window = OperatorBuildHelper.createTimeWindowAO(30, "SECONDS", activeBallBehindGoallineChange);
		operatorList.add(behind_goalline_window);
		
		// join these to windows to detect corners
		JoinAO corners_join = OperatorBuildHelper.createJoinAO("((BallBehindGoalline1_left = 1 AND (BallOnCornerSpot1_bottom = 1 OR BallOnCornerSpot2_top = 1)) OR "
				+ "(BallBehindGoalline2_right = 1 AND (BallOnCornerSpot1_bottom = 1 OR BallOnCornerSpot2_top = 1))) AND "
				+ "ts < spot_ball_ts", "MANY_MANY", behind_goalline_window, ball_on_cornerspot_window);
		operatorList.add(corners_join);

		// only one corner for each ball behind line
		attributes.add("ts");
		ChangeDetectAO corner_cd = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						attributes, corners_join), 0.0, 100, corners_join);
		corner_cd.setDeliverFirstElement(true);
		operatorList.add(corner_cd);
		attributes.clear();
		
		// return only 1 / 0 for each side of game field
		MapAO corners_lr = OperatorBuildHelper.createMapAO(
				getExpressionForLeftRightCorner(corner_cd),
				corner_cd, 0, 0, false);
		operatorList.add(corners_lr);
		

		// TODO: add an aggregate or similar to sum corners for each team

		return OperatorBuildHelper.finishQuery(corners_lr, operatorList,
				sportsQL.getName());
	}
	
	private List<SDFExpressionParameter> getExpressionForLeftRightCorner(
			ILogicalOperator source) throws NumberFormatException, MissingDDCEntryException {
		
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper
				.createExpressionParameter("ts", "corner_ts", source);
		SDFExpressionParameter ex2 = OperatorBuildHelper
				.createExpressionParameter("toInteger(eif(BallBehindGoalline1_left = 1, 1, 0))", "CornerLeft", source);
		SDFExpressionParameter ex3 = OperatorBuildHelper
				.createExpressionParameter("toInteger(eif(BallBehindGoalline2_right = 1, 1, 0))", "CornerRight", source);	
		expressions.add(ex1);
		expressions.add(ex2);
		expressions.add(ex3);

		return expressions;
	}

}
