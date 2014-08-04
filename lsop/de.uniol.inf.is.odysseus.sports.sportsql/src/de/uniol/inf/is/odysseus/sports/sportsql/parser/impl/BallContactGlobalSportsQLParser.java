package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Parser for SportsQL: Query: Ball contacts of all player.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
 * { "statisticType": "global", "gameType": "soccer", "name": "ball_contact",
 * "parameters": { "time": { "start": 10753295594424116, "end" :
 * 9999999999999999, } "space": { "startx":-50, "starty":-33960 "endx":52489
 * "endy":33965 } } }
 * 
 * @author Thomas Prünie
 *
 */
@SportsQL(gameTypes = GameType.SOCCER, statisticTypes = { StatisticType.GLOBAL }, name = "ball_contact", parameters = {
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
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
	 * Radius constant to describe the maximal distance between a player and the
	 * ball
	 */
	private final String radius = "400";

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) {

		// List for all operators, which will be used in this query plan
		ArrayList<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		ILogicalOperator output = getOutputOperator(
				OperatorBuildHelper.createGameStreamAO(),
				OperatorBuildHelper.createMetadataStreamAO(), sportsQL,
				allOperators);

		return OperatorBuildHelper.finishQuery(output, allOperators,
				sportsQL.getName());
	}

	/**
	 * Return list of expressions for the ball
	 * 
	 * @param source
	 * @return
	 */
	private List<SDFExpressionParameter> getMapExpressionForBallPosition(
			ILogicalOperator source) {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex = OperatorBuildHelper
				.createExpressionParameter("ToPoint(x,y,z)", "ball_pos", source);

		expressions.add(ex);
		return expressions;
	}

	/**
	 * Return list of expressions for the position of the players
	 * 
	 * @param source
	 * @return
	 */
	private List<SDFExpressionParameter> getMapExpressionForPlayerPosition(
			ILogicalOperator source) {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper
				.createExpressionParameter("ToPoint(x,y,z)", "player_pos",
						source);
		SDFExpressionParameter ex2 = OperatorBuildHelper
				.createExpressionParameter("sid", "sid", source);

		expressions.add(ex1);
		expressions.add(ex2);
		return expressions;
	}

	/**
	 * If you want the unfinished global query for ball contacts
	 * 
	 * @param soccerGameStreamAO
	 *            Stream of the game
	 * @param metadataStreamAO
	 *            Stream of the metadata
	 * @param sportsQL
	 * @param allOperators
	 * @return
	 */
	public ILogicalOperator getOutputOperator(
			ILogicalOperator soccerGameStreamAO,
			ILogicalOperator metadataStreamAO, SportsQLQuery sportsQL,
			List<ILogicalOperator> allOperators) {

		// List of predicates to use in single operators
		ArrayList<String> predicates = new ArrayList<String>();

		// List of attributes to use in single operators
		ArrayList<String> attributes = new ArrayList<String>();

		// get the time parameter
		SportsQLTimeParameter timeParameter = SportsQLParameterHelper
				.getTimeParameter(sportsQL);

		// get the space parameter
		SportsQLSpaceParameter spaceParameter = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);

		// count ball contacts with the beginning of the game
		ILogicalOperator game_after_start = OperatorBuildHelper
				.createTimeMapAndSelect(timeParameter, soccerGameStreamAO);
		allOperators.add(game_after_start);

		ILogicalOperator game_space = OperatorBuildHelper.createSpaceSelect(
				spaceParameter, false, game_after_start);
		allOperators.add(game_space);

		predicates.add("sid=4 OR sid=8 OR sid=10 OR sid=12");
		ILogicalOperator split_balls = OperatorBuildHelper.createRouteAO(
				predicates, game_space);
		allOperators.add(split_balls);
		predicates.clear();

		// Detect changes of the velocity of the ball
		attributes.add("vx");
		attributes.add("vy");
		attributes.add("vz");

		ArrayList<String> groupBy = new ArrayList<String>();
		groupBy.add("sid");

		ILogicalOperator ball_velocity_changes = OperatorBuildHelper
				.createChangeDetectAO(attributes, OperatorBuildHelper
						.createAttributeList(groupBy, split_balls),
						relativeTolerance, velocityChange, split_balls);
		allOperators.add(ball_velocity_changes);
		attributes.clear();

		// Get position of active ball in game
		ILogicalOperator ball_position_map = OperatorBuildHelper.createMapAO(
				getMapExpressionForBallPosition(ball_velocity_changes),
				ball_velocity_changes, 0, 0);
		allOperators.add(ball_position_map);

		// window size = 1, advance = 1
		ILogicalOperator ball_window = OperatorBuildHelper.createTupleWindowAO(
				1, 1, ball_position_map);
		allOperators.add(ball_window);

		// Get position of players in game
		ILogicalOperator players_position_map = OperatorBuildHelper
				.createMapAO(getMapExpressionForPlayerPosition(split_balls),
						split_balls, 0, 1);
		allOperators.add(players_position_map);

		// window size = 1, advance = 1
		ILogicalOperator players_window = OperatorBuildHelper
				.createTupleWindowAO(1, 1, players_position_map);
		allOperators.add(players_window);

		// Join the sources and show only values if ball is near to a player
		predicates.add("SpatialDistance(ball_pos,player_pos)<" + radius);
		ILogicalOperator proximity_join = OperatorBuildHelper.createJoinAO(
				predicates, players_window, ball_window);
		allOperators.add(proximity_join);
		predicates.clear();

		// Delete duplicates
		attributes.add("sid");
		ILogicalOperator delete_duplicates = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						attributes, proximity_join), 0.0, proximity_join);
		allOperators.add(delete_duplicates);
		attributes.clear();

		// enrich data
		ILogicalOperator enriched_proximity = OperatorBuildHelper
				.createEnrichAO("sid=sensorid", delete_duplicates,
						metadataStreamAO);
		allOperators.add(enriched_proximity);

		// Detect changes in entity_id if another player hits the ball
		attributes.add("entity_id");
		ILogicalOperator output = OperatorBuildHelper.createChangeDetectAO(
				OperatorBuildHelper.createAttributeList(attributes,
						enriched_proximity), 0.0, enriched_proximity);
		allOperators.add(output);
		attributes.clear();

		return output;
	}
}
