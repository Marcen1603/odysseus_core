package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RouteAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLTimeParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

/**
 * Query plan for ball contacts of every player. <br>
 *  
 * @author Thomas
 *
 */
@SportsQL(gameTypes = GameType.SOCCER, statisticTypes = { StatisticType.GLOBAL }, name = "ball_contact")
public class BallContactGlobalSportsQLParser implements ISportsQLParser {
	
	/**
	 * Percentage change of velocity
	 */
	private final double velocityChange = 0.15;
	
	/**
	 * Boolean which describes the relative tolerance
	 */
	private final boolean relativeTolerance = true;
	
	/**
	 * Radius constant to describe the maximal distance between a player and the ball
	 */
	private final String radius = "400";
	
	/**
	 * Min x of the game field
	 */
	private final String minX = "-50";
	
	/**
	 * Min y of the game field
	 */
	private final String minY = "-33960";
	
	/**
	 * Max x of the game field
	 */
	private final String maxX = "52489";

	/**
	 * Max y of the game field
	 */
	private final String maxY = "33965";
	

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) {
		
		// List for all operators, which will be used in this query plan
		ArrayList<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		//List of predicates to use in single operators
		ArrayList<String> predicates = new ArrayList<String>();
		
		//List of attributes to use in single operators
		ArrayList<String> attributes = new ArrayList<String>();
		
		//get the time parameter 
		SportsQLTimeParameter timeParameter = SportsQLParameterHelper
				.getTimeParameter(sportsQL);
		
		//get the space parameter
		SportsQLSpaceParameter spaceParameter = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);

		
		String soccerGameSourceName = OperatorBuildHelper.MAIN_STREAM_NAME;
		String metadataSourceName = OperatorBuildHelper.METADATA_STREAM_NAME;
		
		//game source
		AccessAO soccerGameAccessAO = OperatorBuildHelper
				.createAccessAO(soccerGameSourceName);
		
		// metadata source
		AccessAO metadataAccessAO = OperatorBuildHelper.createAccessAO(metadataSourceName);
		
		
		//count ball contacts with the beginning of the game
		SelectAO game_after_start = OperatorBuildHelper.createTimeSelect(timeParameter, soccerGameAccessAO);
		allOperators.add(game_after_start);
		
		SelectAO game_space = OperatorBuildHelper.createSpaceSelect(spaceParameter, game_after_start);
		allOperators.add(game_space);

		predicates.add("sid=4 OR sid=8 OR sid=10 OR sid=12");
		RouteAO split_balls = OperatorBuildHelper.createRouteAO(predicates, game_space);
		allOperators.add(split_balls);
		predicates.clear();
		
		//Detect changes of the velocity of the ball
		attributes.add("vx");
		attributes.add("vy");
		attributes.add("vz");
		
		ArrayList<String> groupBy = new ArrayList<String>();
		groupBy.add("sid");
		
		
		ChangeDetectAO ball_velocity_changes = OperatorBuildHelper.createChangeDetectAO(attributes, OperatorBuildHelper.createAttributeList(groupBy,split_balls),relativeTolerance, velocityChange, split_balls);
		allOperators.add(ball_velocity_changes);
		attributes.clear();
		
		//Get the current ball in the game
		predicates.add("x>"+minX+" AND x<"+maxX+" AND y>"+minY+" AND y<"+maxY);
		SelectAO ball_in_game = OperatorBuildHelper.createSelectAO(predicates, ball_velocity_changes);
		allOperators.add(ball_in_game);
		predicates.clear();
		
		//Get position of active ball in game
		MapAO ball_position = OperatorBuildHelper.createMapAO(getMapExpressionForBallPosition(ball_in_game), ball_in_game, 0, 0);
		allOperators.add(ball_position);
		
		TimeValueItem windowSize = new TimeValueItem(1, null);
		TimeValueItem windowAdvance = new TimeValueItem(1, null);
	
		WindowAO ball_window = OperatorBuildHelper.createWindowAO(windowSize, WindowType.TUPLE, windowAdvance, ball_position);
		allOperators.add(ball_window);
		
		//Get position of players in game
		MapAO players_position = OperatorBuildHelper.createMapAO(getMapExpressionForPlayerPosition(split_balls), split_balls, 0, 1);
		allOperators.add(players_position);
		
		WindowAO players_window = OperatorBuildHelper.createWindowAO(windowSize, WindowType.TUPLE, windowAdvance, players_position);
		allOperators.add(players_position);
		
		//Join the sources and show only values if ball is near to a player
		predicates.add("SpatialDistance(ball_pos,player_pos)<"+ radius);
		JoinAO proximity = OperatorBuildHelper.createJoinAO(predicates, players_window, ball_window);
		allOperators.add(proximity);
		predicates.clear();
	
		//Delete duplicates
		attributes.add("sid");
		ChangeDetectAO delete_duplicates = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(attributes,proximity), 0.0, proximity);
		allOperators.add(delete_duplicates);
		attributes.clear();
		
		//enrich data
		EnrichAO enriched_proximity = OperatorBuildHelper.createEnrichAO("sid=sensorid", delete_duplicates, metadataAccessAO);
		allOperators.add(enriched_proximity);
		
		//Detect changes in entity_id if another player hits the ball
		attributes.add("entity_id");
		ChangeDetectAO output = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(attributes,enriched_proximity), 0.0, enriched_proximity);
		allOperators.add(output);
		attributes.clear();

		return OperatorBuildHelper.finishQuery(output, allOperators, sportsQL.getName());
	}
	
	/**
	 * Return list of expressions for the ball
	 * @param source
	 * @return
	 */
	private List<SDFExpressionParameter> getMapExpressionForBallPosition(ILogicalOperator source) {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex = OperatorBuildHelper.createExpressionParameter("ToPoint(x,y,z)", "ball_pos", source);	
		
		expressions.add(ex);
		return expressions;
	}
	
	/**
	 * Return list of expressions for the position of the players
	 * @param source
	 * @return
	 */
	private List<SDFExpressionParameter> getMapExpressionForPlayerPosition(ILogicalOperator source) {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper.createExpressionParameter("ToPoint(x,y,z)", "player_pos", source);
		SDFExpressionParameter ex2 = OperatorBuildHelper.createExpressionParameter("sid","sid", source);
		
		expressions.add(ex1);
		expressions.add(ex2);
		return expressions;
	}		
}
