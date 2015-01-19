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
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.PLAYER }, name = "sprints", parameters = {
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class SprintsPlayerSportsQLParser implements ISportsQLParser {
	
	private static final int HEARTBEAT = 5000;
	

	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException,
			MissingDDCEntryException {
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		SprintsSportsQLParser parser = new SprintsSportsQLParser();
		ILogicalOperator operator = parser.getSprints(session, sportsQL,
				allOperators);

		// 9. Clear Endtimestamp
		TimestampAO timestampAO = OperatorBuildHelper.clearEndTimestamp(operator);
		allOperators.add(timestampAO);

		// 10. Assure heatbeat every x seconds
		AssureHeartbeatAO assureHeartbeatAO = OperatorBuildHelper
				.createHeartbeat(HEARTBEAT, timestampAO);
		allOperators.add(assureHeartbeatAO);

		// 111. Calculate sprints count, average sprint distance and max speed
		List<String> resultAggregateFunctions = new ArrayList<String>();
		resultAggregateFunctions.add("COUNT");
		resultAggregateFunctions.add("AVG");
		resultAggregateFunctions.add("MAX");

		List<String> resultAggregateInputAttributeNames = new ArrayList<String>();
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_SPRINT_DISTANCE);
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_SPRINT_DISTANCE);
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_MAX_SPEED);

		List<String> resultAggregateOutputAttributeNames = new ArrayList<String>();
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_SPRINTS_COUNT);
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_AVG_DISTANCE);
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_MAX_SPEED);

		List<String> resultAggregateGroupBys = new ArrayList<String>();
		resultAggregateGroupBys.add(IntermediateSchemaAttributes.ENTITY_ID);
		resultAggregateGroupBys.add(IntermediateSchemaAttributes.TEAM_ID);

		AggregateAO resultAggregate = OperatorBuildHelper
				.createAggregateAO(resultAggregateFunctions,
						resultAggregateGroupBys,
						resultAggregateInputAttributeNames,
						resultAggregateOutputAttributeNames, null,
						assureHeartbeatAO, 1);
		allOperators.add(resultAggregate);

		
		// 12. Ignore referee
		String selectAOPredicate = IntermediateSchemaAttributes.TEAM_ID + " = " + SoccerDDCAccess.getLeftGoalTeamId() + " OR " + IntermediateSchemaAttributes.TEAM_ID + " = " +SoccerDDCAccess.getRightGoalTeamId();
		SelectAO selectAO = OperatorBuildHelper.createSelectAO(selectAOPredicate, resultAggregate);

		return OperatorBuildHelper.finishQuery(selectAO, allOperators,
				sportsQL.getDisplayName());
	}

}
