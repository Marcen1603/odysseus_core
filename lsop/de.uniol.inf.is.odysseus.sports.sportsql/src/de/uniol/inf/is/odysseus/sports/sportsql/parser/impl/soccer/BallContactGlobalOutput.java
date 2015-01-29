package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Parser for SportsQL:
 * Query: Ball contacts overall
 * 
 * 
 * @author Thomas Prünie, Marc Wilken
 *
 */

public class BallContactGlobalOutput {

	/**
	 * Percentage change of velocity
	 */
	private final double velocityChange = 0.10;

	/**
	 * String of the ball position from input schema
	 */
	private final String ball_pos = "ball_pos";
	
	/**
	 * String of the player position from input schema
	 */
	private final String player_pos = "player_pos";
	
	/**
	 * Heartbeat for changedetect
	 */
	private final int heartbeat = 100;
	
	/**
	 * Sink in port for player and ball
	 */
	private final int sinkInPort = 0;
	
	/**
	 * Sink out port for ball
	 */
	private final int sinkOutPortBall = 0;
	/**
	 * Sink out port for player
	 */
	private final int sinkOutPortPlayer = 1;
	
	/**
	 * Size of the used windows
	 */
	private final int windowSize = 1;
	
	/**
	 * Advance for the used windows
	 * The window will be moved after @windowAdvance steps
	 */
	private final int windowAdvance = 1;
	
	/**
	 * Zero tolerance for changedetect
	 */
	private final double zeroTolerance = 0.0;
	
	/**
	 * Boolean which describes the relative tolerance
	 */
	private final boolean relativeTolerance = true;

	/**
	 * Radius constant to describe the maximal distance between a player and the
	 * ball
	 */
	private final String radius = "400";
	
	private static String ATTRIBUTE_V= "v";

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
				.createExpressionParameter("ToPoint(x,y,z)", ball_pos, source);
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
				.createExpressionParameter("ToPoint(x,y,z)", player_pos,
						source);
		SDFExpressionParameter ex2 = OperatorBuildHelper
				.createExpressionParameter("entity_id", "entity_id", source);

		SDFExpressionParameter ex3 = OperatorBuildHelper
				.createExpressionParameter("team_id", "team_id", source);

		expressions.add(ex1);
		expressions.add(ex2);
		expressions.add(ex3);
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
	 * @throws MissingDDCEntryException
	 * @throws NumberFormatException
	 */
	@SuppressWarnings("rawtypes")
	public ILogicalOperator getOutputOperator(
			ILogicalOperator soccerGameStreamAO, SportsQLQuery sportsQL,
			List<ILogicalOperator> allOperators) throws NumberFormatException,
			MissingDDCEntryException {

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

		ILogicalOperator game_space = OperatorBuildHelper
				.createSpaceSelect(spaceParameter, false, game_after_start);
		allOperators.add(game_space);

		// get only ball sensors
		List<IPredicate> ballPredicates = new ArrayList<IPredicate>();
		for (int sensorId : AbstractSportsDDCAccess.getBallEntityIds()) {
			IPredicate ballPredicate = OperatorBuildHelper
					.createRelationalPredicate("entity_id = " + sensorId);
			ballPredicates.add(ballPredicate);
		}
		List<IPredicate<?>> predicatesBall = new ArrayList<IPredicate<?>>();
		predicatesBall.add(OperatorBuildHelper
				.createOrPredicate(ballPredicates));

		ILogicalOperator split_balls = OperatorBuildHelper
				.createRoutePredicatesAO(predicatesBall, game_space);
		allOperators.add(split_balls);
		predicates.clear();

		ArrayList<String> groupBy = new ArrayList<String>();
		groupBy.add("entity_id");
		
		attributes.add(ATTRIBUTE_V);

		ILogicalOperator ball_velocity_changes = OperatorBuildHelper
				.createChangeDetectAO(attributes, OperatorBuildHelper
						.createAttributeList(groupBy, split_balls),
							relativeTolerance, velocityChange, split_balls, heartbeat);
		allOperators.add(ball_velocity_changes);
		attributes.clear();

		// Get position of active ball in game
		ILogicalOperator ball_position_map = OperatorBuildHelper
				.createMapAO(getMapExpressionForBallPosition(ball_velocity_changes),
						ball_velocity_changes, sinkInPort, sinkOutPortBall, true);
		allOperators.add(ball_position_map);

		// window size = 1, advance = 1
		ILogicalOperator ball_window = OperatorBuildHelper
				.createElementWindowAO(windowSize, windowAdvance, ball_position_map);
		allOperators.add(ball_window);

		// Get position of players in game
		ILogicalOperator players_position_map = OperatorBuildHelper
				.createMapAO(getMapExpressionForPlayerPosition(split_balls),
						split_balls, sinkInPort, sinkOutPortPlayer, false);
		allOperators.add(players_position_map);

		// window size = 1, advance = 1
		ILogicalOperator players_window = OperatorBuildHelper
				.createElementWindowAO(windowSize, windowAdvance, players_position_map);
		allOperators.add(players_window);

		// Join the sources and show only values if ball is near to a player
		predicates.add("SpatialDistance(ball_pos,player_pos)<" + radius);
		ILogicalOperator proximity_join = OperatorBuildHelper
				.createJoinAO(predicates, players_window, ball_window);
		allOperators.add(proximity_join);
		predicates.clear();

		// Delete duplicates
		attributes.add("entity_id");
		ILogicalOperator delete_duplicates = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper
						.createAttributeList(attributes, proximity_join), 
							zeroTolerance, proximity_join);
		allOperators.add(delete_duplicates);
		attributes.clear();

		// Detect changes in entity_id if another player hits the ball
		attributes.add("entity_id");
		ILogicalOperator ballContactDetected = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper
						.createAttributeList(attributes, delete_duplicates), 
							zeroTolerance, delete_duplicates);
		allOperators.add(ballContactDetected);
		attributes.clear();

		ILogicalOperator clearEndTimestamp = OperatorBuildHelper
				.clearEndTimestamp(ballContactDetected);
		allOperators.add(clearEndTimestamp);

		return clearEndTimestamp;
	}
	
}
