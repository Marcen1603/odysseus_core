package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
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
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLEvaluationParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Counts the number of shots on the goal from a player
 * 
 * SportsQL could be 
 * { 
 * "statisticType": "player", 
 * "gameType": "soccer",
 * "name": "shotongoal" 
 * }
 * 
 * @author Michael (all the hard PQL stuff), Tobias (only the translation into
 *         logical query), Pascal
 *
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.PLAYER }, name = "shotongoal", parameters = {
		@SportsQLParameter(name = "evaluation", parameterClass = SportsQLEvaluationParameter.class, mandatory = false),
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class ShotOnGoalPlayerSportsQLParser implements ISportsQLParser {

	private static final int HEARTBEAT = 5000;

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

		// 1. Clear Endtimestamp
		TimestampAO timestampAO = OperatorBuildHelper.clearEndTimestamp(forecastCriteria);
		allOperators.add(timestampAO);

		// 2. Assure heatbeat every x seconds
		AssureHeartbeatAO assureHeartbeatAO = OperatorBuildHelper
			.createHeartbeat(HEARTBEAT, timestampAO);
		allOperators.add(assureHeartbeatAO);
				
		// 3. Aggregate
		List<String> groupCount = new ArrayList<String>();
		groupCount.add(IntermediateSchemaAttributes.ENTITY_ID);
		ILogicalOperator out = OperatorBuildHelper.createAggregateAO("sum", groupCount, ShotOnGoalGlobalOutput.ATTRIBUTE_SHOT, ShotOnGoalGlobalOutput.ATTRIBUTE_SHOTS, "Integer", assureHeartbeatAO, 1);
		allOperators.add(out);

		return OperatorBuildHelper.finishQuery(out, allOperators,
				sportsQL.getDisplayName(),sportsQL);
	}

}
