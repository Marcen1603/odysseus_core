package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator.AssureHeartbeatAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.SoccerDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.IntermediateSchemaAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;

/**
 * Parser for Query sprints.
 * 
 * 
 * @author Simon Küspert, Pascal Schmedt, Thore Stratmann
 * 
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.TEAM }, name = "sprints", parameters = {
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class SprintsTeamSportsQLParser implements ISportsQLParser {
	
	private static String ATTRIBUTE_SPRINTS_COUNT = "count";

	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException,
			MissingDDCEntryException {
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		SprintsSportsQLParser parser = new SprintsSportsQLParser();
		ILogicalOperator operator = parser.getSprints(session, sportsQL,
				allOperators);

		// 8. Clear Endtimestamp
		TimestampAO timestampAO = OperatorBuildHelper.clearEndTimestamp(operator);
		allOperators.add(timestampAO);

		// 9. Heatbeat every 5 seconds
		AssureHeartbeatAO assureHeartbeatAO = OperatorBuildHelper.createHeartbeat(5000, timestampAO);
		allOperators.add(assureHeartbeatAO);

		// 10. Calulate sprints count
		List<String> resultAggregateFunctions = new ArrayList<String>();
		resultAggregateFunctions.add("COUNT");

		List<String> resultAggregateInputAttributeNames = new ArrayList<String>();
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_SPRINT_TIME);

		List<String> resultAggregateOutputAttributeNames = new ArrayList<String>();
		resultAggregateOutputAttributeNames.add(ATTRIBUTE_SPRINTS_COUNT);

		List<String> resultAggregateGroupBys = new ArrayList<String>();
		resultAggregateGroupBys.add(IntermediateSchemaAttributes.TEAM_ID);

		AggregateAO resultAggregate = OperatorBuildHelper
				.createAggregateAO(resultAggregateFunctions,
						resultAggregateGroupBys,
						resultAggregateInputAttributeNames,
						resultAggregateOutputAttributeNames, null,
						assureHeartbeatAO, 1);
		allOperators.add(resultAggregate);
		
		// 11. Ignore referee
		String selectAOPredicate = IntermediateSchemaAttributes.TEAM_ID + " = " + SoccerDDCAccess.getLeftGoalTeamId() + " OR " + IntermediateSchemaAttributes.TEAM_ID + " = " +SoccerDDCAccess.getRightGoalTeamId();
		SelectAO selectAO = OperatorBuildHelper.createSelectAO(selectAOPredicate, resultAggregate);

		return OperatorBuildHelper.finishQuery(selectAO, allOperators,	sportsQL.getDisplayName());
	}

}
