package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
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
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;
/**
 * Parser for SportsQL:
 * Query: Ball contacts of teams.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
	{
	    "displayName":"Ball_Contact_Team",
	    "statisticType":"TEAM",
	    "gameType":"SOCCER",
	    "name":"ball_contact"
	}
 * 
 * @author Thomas Prünie,Marc Wilken
 *
 */

@SportsQL(gameTypes = GameType.SOCCER, statisticTypes = { StatisticType.TEAM }, name = "ball_contact", parameters = {
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class BallContactTeamSportsQLParser implements ISportsQLParser{
	
	private static final int HEARTBEAT = 5000;
	private final int dumpAtValueCount = 1;
	 
	@Override
	public ILogicalQuery parse(ISession session,SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {
		
		ArrayList<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		ArrayList<ILogicalOperator> tempOperators = new ArrayList<ILogicalOperator>();
		
		//Get all ball contacts of every player by using the global parser
		BallContactGlobalOutput ballcontactsGlobal = new BallContactGlobalOutput();	
		ILogicalOperator globalOutput = ballcontactsGlobal.getOutputOperator(OperatorBuildHelper.createGameStreamAO(session), sportsQL, allOperators);
		
		List<String> groupCount = new ArrayList<String>();
		groupCount.add("team_id");
		
		ILogicalOperator countOutput = OperatorBuildHelper.createAggregateAO("count", groupCount, "team_id", "ballContactCount", "Integer", globalOutput, dumpAtValueCount);
		tempOperators.add(countOutput);
		groupCount.clear();
		
		ILogicalOperator allOutput = OperatorBuildHelper.createAggregateAO("SUM", groupCount, "ballContactCount", "ballContactCountAll", "Integer", countOutput, dumpAtValueCount);
		tempOperators.add(allOutput);
		
		List<String> predicates=new ArrayList<>();
		predicates.add("ballContactCount = ballContactCount");
		
		ILogicalOperator sumAndCount = OperatorBuildHelper.createJoinAO(predicates, countOutput, allOutput);
		
		//countOutput / allOutput = percentOutput
		List<SDFExpressionParameter> passesStateMapExpressions = new ArrayList<SDFExpressionParameter>();
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("team_id", "team_id", sumAndCount));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("DoubleToFloat(ROUND((ballContactCount"+"/"+"ballContactCountAll),4)*100)", "ballContactCount", sumAndCount));
		passesStateMapExpressions.add(OperatorBuildHelper.createExpressionParameter("ballContactCount", "ballContactCountAbs", sumAndCount));
		
		MapAO percentageMap = OperatorBuildHelper.createMapAO(passesStateMapExpressions, sumAndCount, 0, 0, true);
		allOperators.add(percentageMap);
		
		// clear timestamp
		ILogicalOperator clearEndTimestamp = OperatorBuildHelper
				.clearEndTimestamp(percentageMap);
		allOperators.add(clearEndTimestamp);

		// Assure heatbeat every x seconds
		AssureHeartbeatAO assureHeartbeatAO = OperatorBuildHelper
				.createHeartbeat(HEARTBEAT, clearEndTimestamp);
		allOperators.add(assureHeartbeatAO);

		// Result Aggregate
		List<String> resultAggregateFunctions = new ArrayList<String>();
		resultAggregateFunctions.add("LAST");

		List<String> resultAggregateInputAttributeNames = new ArrayList<String>();
		resultAggregateInputAttributeNames.add("ballContactCount");

		List<String> resultAggregateOutputAttributeNames = new ArrayList<String>();
		resultAggregateOutputAttributeNames.add("ballContactCount");

		List<String> resultAggregateGroupBys = new ArrayList<String>();
		resultAggregateGroupBys.add(IntermediateSchemaAttributes.TEAM_ID);

		AggregateAO resultAggregate = OperatorBuildHelper
				.createAggregateAO(resultAggregateFunctions,
						resultAggregateGroupBys,
						resultAggregateInputAttributeNames,
						resultAggregateOutputAttributeNames, null,
						assureHeartbeatAO, 1);

		return OperatorBuildHelper.finishQuery(resultAggregate, allOperators,
				sportsQL.getDisplayName());
	}

}
