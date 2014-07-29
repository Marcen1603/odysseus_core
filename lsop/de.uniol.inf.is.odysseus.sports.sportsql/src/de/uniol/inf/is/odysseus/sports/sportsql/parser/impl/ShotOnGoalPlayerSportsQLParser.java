package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Counts the number of shots on the goal from a player
 * 
 * SportsQL could be { "statisticType": "player", "gameType": "soccer",
 * "entityId" : 8, "name": "shotongoal" }
 * 
 * @author Michael (all the heavy PQL stuff), Tobias (only the translation into
 *         logical query)
 *
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.PLAYER }, name = "shotongoal", parameters = { @SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false) })
public class ShotOnGoalPlayerSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {
		// TODO Include what Michael said about this task on Jira:
		// Filter for time and space where it's usefull
		// "global" query without team-or-player selection -> use this for other
		// queries with player or team

		// Do all the steps one till nine
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		ShotOnGoalGlobalSportsQLParser globalParser = new ShotOnGoalGlobalSportsQLParser();
		ILogicalQuery forecastCriteraQuery = globalParser.parse(sportsQL);
		ILogicalOperator forecastCriteria = forecastCriteraQuery
				.getLogicalPlan();

		// -------------------------------------------------------------------
		// Tenth part
		// count the shots
		// output schema: shots [Integer]
		// -------------------------------------------------------------------

		// 1. Time window
		SportsQLTimeParameter timeParam = SportsQLParameterHelper
				.getTimeParameter(sportsQL);
		TimeWindowAO timeWindow = OperatorBuildHelper.createTimeWindowAO(
				Integer.parseInt(timeParam.getTime()), "Minutes",
				forecastCriteria);

		// 2. Select for correct entity
		SelectAO entitySelect = OperatorBuildHelper.createEntityIDSelect(
				sportsQL.getEntityId(), timeWindow);

		// 3. Aggregate (sum the shots for this player)
		AggregateAO out = OperatorBuildHelper.createAggregateAO("sum", "shot",
				"shots", entitySelect);

		// Add to list
		allOperators.add(timeWindow);
		allOperators.add(entitySelect);
		allOperators.add(out);

		return OperatorBuildHelper.finishQuery(out, allOperators,
				sportsQL.getName());
	}

}
