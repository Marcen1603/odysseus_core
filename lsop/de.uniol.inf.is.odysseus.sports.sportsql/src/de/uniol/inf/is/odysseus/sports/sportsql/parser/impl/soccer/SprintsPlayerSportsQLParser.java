package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator.AssureHeartbeatAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLEvaluationParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;
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
		@SportsQLParameter(name = "evaluation", parameterClass = SportsQLEvaluationParameter.class, mandatory = false),
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
		ILogicalOperator operator = parser.getSprints(session, sportsQL,allOperators);
		
		// 9. Clear EndTimeStamp
		TimestampAO timestampAO = OperatorBuildHelper.clearEndTimestamp(operator);
		allOperators.add(timestampAO);

		// 10. Assure that tuples are sent every x seconds
		AssureHeartbeatAO assureHeartbeatAO = OperatorBuildHelper.createHeartbeat(HEARTBEAT, timestampAO);
		allOperators.add(assureHeartbeatAO);
		
		// 11. Final aggregate
		List<String> resultAggregateFunctions = new ArrayList<String>();
		resultAggregateFunctions.add("AVG");
		resultAggregateFunctions.add("AVG");
		resultAggregateFunctions.add("MAX");
		resultAggregateFunctions.add("SUM");
		resultAggregateFunctions.add("SUM");
		resultAggregateFunctions.add("SUM");
		resultAggregateFunctions.add("SUM");
		resultAggregateFunctions.add("SUM");

		List<String> resultAggregateInputAttributeNames = new ArrayList<String>();
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_SPRINT_DISTANCE);
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_AVG_SPEED);
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_MAX_SPEED);
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_1_10);
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_11_20);
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_21_30);
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_31_40);
		resultAggregateInputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_41_X);

		List<String> resultAggregateOutputAttributeNames = new ArrayList<String>();
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_AVG_DISTANCE);
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_AVG_SPEED);
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_MAX_SPEED);
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_1_10);
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_11_20);
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_21_30);
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_31_40);
		resultAggregateOutputAttributeNames.add(SprintsSportsQLParser.ATTRIBUTE_DIST_41_X);

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
		
		return OperatorBuildHelper.finishQuery(resultAggregate, allOperators,sportsQL.getDisplayName(),sportsQL);
	}

}
