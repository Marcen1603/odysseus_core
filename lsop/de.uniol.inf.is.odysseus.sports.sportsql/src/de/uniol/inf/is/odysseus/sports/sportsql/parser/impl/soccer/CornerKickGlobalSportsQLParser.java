package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator.AssureHeartbeatAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.IntermediateSchemaAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.SoccerDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLEvaluationParameter;

/**
 * SportsQL Parser for Corner Kicks
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.GLOBAL }, name = "cornerkicks", 
parameters = {
		@SportsQLParameter(name = "evaluation", parameterClass = SportsQLEvaluationParameter.class, mandatory = false)
})
public class CornerKickGlobalSportsQLParser implements ISportsQLParser {

	// corner spot radius in millimeters
	private static final int CORNER_SPOT_RADIUS 	= 1500;
	// time window size for goalline window in seconds
	private static final int TIME_GOALLINE_WINDOW	= 30;
	// time window size for corner spot window
	private static final int TIME_CORNERSPOT_WINDOW	= 30;
	// ids for teams
	private static final int TEAM_ID_1 = 1;
	private static final int TEAM_ID_2 = 2;
	
	// attributes
	private static final String ATTR_BALL_BEHIND_GOALLINE_LEFT 		= "BallBehindGoallineLeft";
	private static final String ATTR_BALL_BEHIND_GOALLINE_RIGHT 	= "BallBehindGoallineRight";
	private static final String ATTR_BALL_ON_CORNERSPOT_LEFT 		= "BallOnCornerSpotLeft";
	private static final String ATTR_BALL_ON_CORNERSPOT_RIGHT		= "BallOnCornerSpotRight";
	private static final String ATTR_SUM_BALL_ON_CORNERSPOT_LEFT 	= "sum_BallOnCornerSpotLeft";
	private static final String ATTR_SUM_BALL_ON_CORNERSPOT_RIGHT	= "sum_BallOnCornerSpotRight";
	private static final String ATTR_GOALLINE_TS					= "goalline_ts";
	private static final String ATTR_SPOT_BALL_TS					= "spot_ball_ts";
	private static final String ATTR_CORNERS_TEAM_1					= "team_1";
	private static final String ATTR_CORNERS_TEAM_2					= "team_2";
	

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
		
		ILogicalOperator source = OperatorBuildHelper.createGameSource(session);
		//operatorList.add(source);

		ArrayList<String> projectionAttributes = new ArrayList<String>();
		projectionAttributes.add(IntermediateSchemaAttributes.ENTITY_ID);
		projectionAttributes.add(IntermediateSchemaAttributes.TS);
		projectionAttributes.add(IntermediateSchemaAttributes.X);
		projectionAttributes.add(IntermediateSchemaAttributes.Y);
		ProjectAO projected = OperatorBuildHelper.createProjectAO(
				projectionAttributes, source);
		operatorList.add(projected);
		
		
		/*
		 * select only the ball
		 */
		
		List<String> ballEntities = new ArrayList<String>();
		Iterator<Integer> ballSensorIterator = AbstractSportsDDCAccess.getBallEntityIds().iterator();
		while(ballSensorIterator.hasNext()) {
			int entityId = ballSensorIterator.next();
			ballEntities.add( IntermediateSchemaAttributes.ENTITY_ID + " = " + entityId );
		}
		SelectAO activeBall = OperatorBuildHelper.createSelectAO(ballEntities, projected);
		activeBall.setHeartbeatRate(10);
		operatorList.add(activeBall);
		
		SelectAO activeBall_2 = OperatorBuildHelper.createSelectAO(ballEntities, projected);
		activeBall_2.setHeartbeatRate(10);
		operatorList.add(activeBall_2);

		
		/*
		 * detect ball behind goal line
		 */
		
		ArrayList<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		expressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID,
				activeBall_2));
		expressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.TS, 
				ATTR_GOALLINE_TS, activeBall_2));
		expressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.X + " < " + SoccerDDCAccess.getFieldXMin() + ", 1, 0)",
				ATTR_BALL_BEHIND_GOALLINE_LEFT, activeBall_2));
		expressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + IntermediateSchemaAttributes.X + " > " + SoccerDDCAccess.getFieldXMax() + ", 1, 0)",
				ATTR_BALL_BEHIND_GOALLINE_RIGHT, activeBall_2));
		MapAO activeBallBehindGoalline = OperatorBuildHelper.createMapAO(expressions, activeBall_2, 0, 0, false);
		operatorList.add(activeBallBehindGoalline);

		
		ArrayList<String> changeDetectBallAttributes = new ArrayList<String>();
		changeDetectBallAttributes.add(ATTR_BALL_BEHIND_GOALLINE_LEFT);
		changeDetectBallAttributes.add(ATTR_BALL_BEHIND_GOALLINE_RIGHT);
		ChangeDetectAO activeBallBehindGoallineChange = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						changeDetectBallAttributes, activeBallBehindGoalline),
						0, activeBallBehindGoalline);
		operatorList.add(activeBallBehindGoallineChange);

		
		/*
		 * detect ball on corner spot
		 */
	
		ArrayList<SDFExpressionParameter> ballExpressions = new ArrayList<SDFExpressionParameter>();
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter(IntermediateSchemaAttributes.TS,
				ATTR_SPOT_BALL_TS, activeBall));
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif( (" + IntermediateSchemaAttributes.X + " > " + xMin + " AND "
						+ IntermediateSchemaAttributes.X + " < " + (xMin + CORNER_SPOT_RADIUS) + " AND "
						+ "((" + IntermediateSchemaAttributes.Y + " < " + yMax + " AND "
						+ 		IntermediateSchemaAttributes.Y + " > " + (yMax - CORNER_SPOT_RADIUS) + ") OR "
						+ "(" + IntermediateSchemaAttributes.Y + " < " + (yMin + CORNER_SPOT_RADIUS) + " AND "
						+ 		IntermediateSchemaAttributes.Y + " > " + yMin + ")) ) , 1, 0)",
				ATTR_BALL_ON_CORNERSPOT_LEFT, activeBall));
		ballExpressions.add(OperatorBuildHelper.createExpressionParameter(
				"eif( (" + IntermediateSchemaAttributes.X + " < " + xMax + " AND "
						+ IntermediateSchemaAttributes.X + " > " + (xMax - CORNER_SPOT_RADIUS) + " AND "
						+ "((" + IntermediateSchemaAttributes.Y + " < " + yMax + " AND "
						+ 		IntermediateSchemaAttributes.Y + " > " + (yMax - CORNER_SPOT_RADIUS) + ") OR "
						+ "(" + IntermediateSchemaAttributes.Y + " < " + (yMin + CORNER_SPOT_RADIUS) + " AND "
						+ 		IntermediateSchemaAttributes.Y + " > " + yMin + ")) ) , 1, 0)",
				ATTR_BALL_ON_CORNERSPOT_RIGHT, activeBall));
		MapAO ballOnCornerSpot = OperatorBuildHelper.createMapAO(ballExpressions, activeBall, 0, 0, false);
		operatorList.add(ballOnCornerSpot);
		
		
		ArrayList<String> changeDetectCornerAttributes = new ArrayList<String>();
		changeDetectCornerAttributes.add(ATTR_BALL_ON_CORNERSPOT_LEFT);
		changeDetectCornerAttributes.add(ATTR_BALL_ON_CORNERSPOT_RIGHT);
		ChangeDetectAO ballOnCornerSpotChange = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						changeDetectCornerAttributes, ballOnCornerSpot), 0,
						ballOnCornerSpot);
		operatorList.add(ballOnCornerSpotChange);
		
		
		/*
		 * detect corners
		 */
		
		TimeWindowAO ball_on_cornerspot_window = OperatorBuildHelper.createTimeWindowAO(TIME_CORNERSPOT_WINDOW, "SECONDS", ballOnCornerSpotChange);
		operatorList.add(ball_on_cornerspot_window);
		
		TimeWindowAO behind_goalline_window = OperatorBuildHelper.createTimeWindowAO(TIME_GOALLINE_WINDOW, "SECONDS", activeBallBehindGoallineChange);
		operatorList.add(behind_goalline_window);
		
		JoinAO corners_join = OperatorBuildHelper.createJoinAO("(((" + ATTR_BALL_ON_CORNERSPOT_LEFT + " = 1 AND " 
				+ ATTR_BALL_BEHIND_GOALLINE_LEFT + " = 1) OR ("
				+ ATTR_BALL_ON_CORNERSPOT_RIGHT + " = 1 AND " 
				+ ATTR_BALL_BEHIND_GOALLINE_RIGHT + " = 1)) AND "
				+ ATTR_GOALLINE_TS + " < " + ATTR_SPOT_BALL_TS + ")", "ONE_ONE", behind_goalline_window, ball_on_cornerspot_window);
		operatorList.add(corners_join);

		attributes.add(ATTR_SPOT_BALL_TS);
		ChangeDetectAO corner_cd = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
			attributes, corners_join), 0.0, 100, corners_join);
		corner_cd.setDeliverFirstElement(true);
		operatorList.add(corner_cd);
		attributes.clear();
		
		
		/*
		 * aggregate goals
		 */
		
		TimestampAO clearEndTS = OperatorBuildHelper.clearEndTimestamp(corner_cd);
		operatorList.add(clearEndTS);

		AssureHeartbeatAO heart = OperatorBuildHelper.createHeartbeat(5000, clearEndTS);
		operatorList.add(heart);

		AggregateAO goals_aggregate = createAggregate(heart);
		operatorList.add(goals_aggregate);
		
		
		/*
		 * match teams to field side
		 */
		
		ArrayList<SDFExpressionParameter> sideExpression = new ArrayList<SDFExpressionParameter>();
		sideExpression.add(OperatorBuildHelper.createExpressionParameter(
				"eif( " + SoccerDDCAccess.getLeftGoalTeamId() + " = " + TEAM_ID_1 + ", " + ATTR_SUM_BALL_ON_CORNERSPOT_RIGHT + ", " + ATTR_SUM_BALL_ON_CORNERSPOT_LEFT + ")",
				ATTR_CORNERS_TEAM_1, goals_aggregate));
		sideExpression.add(OperatorBuildHelper.createExpressionParameter(
				"eif( " + SoccerDDCAccess.getRightGoalTeamId() + " = " + TEAM_ID_2 + ", " + ATTR_SUM_BALL_ON_CORNERSPOT_LEFT + ", " + ATTR_SUM_BALL_ON_CORNERSPOT_RIGHT + ")", 
				ATTR_CORNERS_TEAM_2, goals_aggregate));
		MapAO sideMap = OperatorBuildHelper.createMapAO(sideExpression, goals_aggregate, 0, 0, false);
		operatorList.add(sideMap);
		

		return OperatorBuildHelper.finishQuery(sideMap, operatorList,
				sportsQL.getDisplayName(),sportsQL);
	}
	
	private AggregateAO createAggregate(ILogicalOperator input) {

		List<String> functions3 = new ArrayList<String>();
		functions3.add("SUM");
		functions3.add("SUM");

		List<String> inputAttributeNames3 = new ArrayList<String>();
		inputAttributeNames3.add(ATTR_BALL_ON_CORNERSPOT_LEFT);
		inputAttributeNames3.add(ATTR_BALL_ON_CORNERSPOT_RIGHT);

		List<String> outputAttributeNames3 = new ArrayList<String>();
		outputAttributeNames3.add(ATTR_SUM_BALL_ON_CORNERSPOT_LEFT);
		outputAttributeNames3.add(ATTR_SUM_BALL_ON_CORNERSPOT_RIGHT);

		List<String> outputTypes = new ArrayList<String>();
		outputTypes.add("Integer");
		outputTypes.add("Integer");

		return OperatorBuildHelper.createAggregateAO(functions3, null,
				inputAttributeNames3, outputAttributeNames3, outputTypes,
				input, 1);
	}

}
