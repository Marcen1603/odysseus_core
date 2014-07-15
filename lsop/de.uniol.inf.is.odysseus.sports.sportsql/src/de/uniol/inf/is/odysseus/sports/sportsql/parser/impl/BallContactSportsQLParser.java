package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
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
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = GameType.SOCCER, statisticTypes = { StatisticType.GLOBAL }, name = "ball_contact")
public class BallContactSportsQLParser implements ISportsQLParser {

	private final double velocityChange = 0.15;
	private final boolean relativeTolerance = true;
	private final String game_start = "10753295594424116";
	private final String[] ball_sids = {"4","8","10","12"};
	private final String radius = "400";
	private final String minX = "-50";
	private final String minY = "-33960";
	private final String maxX = "52489";
	private final String maxY = "33965";
	

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) {
		
		ArrayList<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		ArrayList<String> predicates = new ArrayList<String>();
		ArrayList<String> attributes = new ArrayList<String>();
		
		//count ball contacts with the beginning of the game
		predicates.add("ts>="+ game_start);
		SelectAO game_after_start = OperatorBuildHelper.createSelectAO(predicates, null); //TODO Where can I get the source? current value = null
		allOperators.add(game_after_start);
		predicates.clear();
		
		//Split between ball ids and other ids
		for (String ball_sid : ball_sids) {
			predicates.add(ball_sid);
		}

		RouteAO split_balls = OperatorBuildHelper.createRouteAO(predicates, game_after_start);
		allOperators.add(split_balls);
		predicates.clear();
		
		//Detect changes of the velocity of the ball
		attributes.add("vx");
		attributes.add("vy");
		attributes.add("vz");
		
		ArrayList<String> groupBy = new ArrayList<String>();
		groupBy.add("sid");
		
		ChangeDetectAO ball_velocity_changes = OperatorBuildHelper.createChangeDetectAO(attributes, OperatorBuildHelper.createAttributeList(groupBy),relativeTolerance, velocityChange, split_balls);
		allOperators.add(ball_velocity_changes);
		attributes.clear();
		
		//Get the current ball in the game
		predicates.add("x>"+minX+" AND x<"+maxX+" AND y>"+minY+" AND y<"+maxY);
		SelectAO ball_in_game = OperatorBuildHelper.createSelectAO(predicates, ball_velocity_changes);
		allOperators.add(ball_in_game);
		predicates.clear();
		
		//Get position of active ball in game
		MapAO ball_position = OperatorBuildHelper.createMapAO(getMapExpressionForBallPosition(), ball_in_game);
		allOperators.add(ball_position);
		
		TimeValueItem windowSize = new TimeValueItem(1, TimeUnit.SECONDS);
		TimeValueItem windowAdvance = new TimeValueItem(1, TimeUnit.SECONDS);
	
		WindowAO ball_window = OperatorBuildHelper.createWindowAO(windowSize, WindowType.TUPLE, windowAdvance, ball_position);
		allOperators.add(ball_window);
		
		//Get position of players in game
		MapAO players_position = OperatorBuildHelper.createMapAO(getMapExpressionForPlayerPosition(), split_balls);
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
		ChangeDetectAO delete_duplicates = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(attributes), 0.0, proximity);
		allOperators.add(delete_duplicates);
		attributes.clear();
		
		//enrich data
		EnrichAO enriched_proximity = OperatorBuildHelper.createEnrichAO("sid=sensorid", delete_duplicates, null);
		allOperators.add(enriched_proximity);
		
		//Detect changes in entity_id if another player hits the ball
		attributes.add("entity_id");
		ChangeDetectAO output = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(attributes), 0.0, enriched_proximity);
		allOperators.add(output);
		attributes.clear();
		
		OperatorBuildHelper.initializeOperators(allOperators);

		// Create plan
		ILogicalQuery query = new LogicalQuery();
		query.setLogicalPlan(output, true);

		return query;
	}
	
	private List<SDFExpressionParameter> getMapExpressionForBallPosition() {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex = OperatorBuildHelper.createExpressionParameter("ToPoint(x,y,z)", "ball_pos");	
		
		expressions.add(ex);
		return expressions;
	}
	
	private List<SDFExpressionParameter> getMapExpressionForPlayerPosition() {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter ex1 = OperatorBuildHelper.createExpressionParameter("ToPoint(x,y,z)", "player_pos");
		SDFExpressionParameter ex2 = OperatorBuildHelper.createExpressionParameter("sid","sid");
		
		expressions.add(ex1);
		expressions.add(ex2);
		return expressions;
	}		
}
