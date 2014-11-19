package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Counts the number of shots on the goal from a player
 * 
 * SportsQL could be { "statisticType": "player", "gameType": "soccer",
 * "entityId" : 8, "name": "shotongoal" }
 * 
 * @author Michael (all the hard PQL stuff), Tobias (only the translation into
 *         logical query)
 *
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.PLAYER }, name = "shot_on_goal_player", parameters = {
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class ShotOnGoalPlayerSportsQLParser implements ISportsQLParser {

	/**
	 * Length of game in minutes
	 */
	private static final int GAME_LENGTH = 90;

	@Override
	public ILogicalQuery parse(ISession session,SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {

		// Do all the steps one till nine
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		ShotOnGoalGlobalOutput globalParser = new ShotOnGoalGlobalOutput();
		ILogicalOperator forecastCriteria = globalParser.createGlobalOutput(
				session,sportsQL, allOperators);

		// -------------------------------------------------------------------
		// Tenth part
		// count the shots
		// output schema: shots [Integer]
		// -------------------------------------------------------------------

		// 1. Time window
		TimeWindowAO timeWindow = OperatorBuildHelper.createTimeWindowAO(
				GAME_LENGTH, "Minutes", forecastCriteria);

		// 2. Select for correct entity
		SelectAO entitySelect = OperatorBuildHelper.createEntityIDSelect(
				sportsQL.getEntityId(), timeWindow);

		// 3. Aggregate (sum the shots for this player)
		//AggregateAO out = OperatorBuildHelper.createAggregateAO("sum", "shot",
		//		"shots", entitySelect);
		
		List<String> groupCount = new ArrayList<String>();
		groupCount.add("entity_id");
		
		ILogicalOperator out = OperatorBuildHelper.createAggregateAO("sum", groupCount, "shot", "shots", "Integer", entitySelect, 1);
		

		// Add to list
		allOperators.add(timeWindow);
		allOperators.add(entitySelect);
		allOperators.add(out);

		return OperatorBuildHelper.finishQuery(out, allOperators,
				sportsQL.getName());
	}

}
