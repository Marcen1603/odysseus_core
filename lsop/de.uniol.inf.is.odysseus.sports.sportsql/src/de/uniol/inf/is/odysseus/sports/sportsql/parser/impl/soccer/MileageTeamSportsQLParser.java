package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator.AssureHeartbeatAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.IntermediateSchemaAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLEvaluationParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * This query calculated the mileage of a whole team.
 * 
 * @author Tobias Brandt
 * @since 24.07.2014
 *
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.TEAM }, name = "mileage", parameters = {
		@SportsQLParameter(name = "evaluation", parameterClass = SportsQLEvaluationParameter.class, mandatory = false),
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class MileageTeamSportsQLParser implements ISportsQLParser {
	
	private static final int HEARTBEAT = 5000;
	private static final String ATTRIBUTE_MILEAGE = "mileage";

	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL) throws NumberFormatException, MissingDDCEntryException {

		// We need this list to initialize all operators
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		ILogicalOperator soccerGameStreamAO = OperatorBuildHelper.createGameSource(session);

		// ---------------------------------------
		// First part (Select for questioned time)
		// ---------------------------------------

		// 1. MAP
		MapAO firstMap = OperatorBuildHelper.createTimeMap(soccerGameStreamAO);
		allOperators.add(firstMap);

		// 2. Correct selection for the time
		SportsQLTimeParameter timeParam = SportsQLParameterHelper
				.getTimeParameter(sportsQL);

		SelectAO timeSelect = OperatorBuildHelper.createTimeSelect(timeParam,
				firstMap);
		allOperators.add(timeSelect);

		// -----------------------------------------
		// Second part (Select for questioned space)
		// -----------------------------------------

		// 1. Map for space select, but with meters
		SportsQLSpaceParameter spaceParam = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);
		SelectAO spaceSelectAO = OperatorBuildHelper.createSpaceSelect(
				spaceParam, true, timeSelect);
		allOperators.add(spaceSelectAO);

		// -----------------------------------------
		// Third part (Select for questioned team)
		// -----------------------------------------

		
		// 3. Select for teams
		SelectAO teamSelect = OperatorBuildHelper.createBothTeamSelectAO(spaceSelectAO);
		
		
		// -----------------------------------------
		// Fourth part (Calculate distance)
		// -----------------------------------------

		// 3. StateMapAO for mileage calculation
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter param2 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID, teamSelect);
		SDFExpressionParameter param3 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.TEAM_ID, teamSelect);
		SDFExpressionParameter param4 = OperatorBuildHelper
				.createExpressionParameter(
						"((sqrt(("+OperatorBuildHelper.ATTRIBUTE_X_METER+"-__last_1."+OperatorBuildHelper.ATTRIBUTE_X_METER+")^2 + ("+OperatorBuildHelper.ATTRIBUTE_Y_METER+"-__last_1."+OperatorBuildHelper.ATTRIBUTE_Y_METER+")^2))/1000)",
						ATTRIBUTE_MILEAGE, teamSelect);

		expressions.add(param2);
		expressions.add(param3);
		expressions.add(param4);
		
		StateMapAO mileageStateMap = OperatorBuildHelper.createStateMapAO(
				expressions, IntermediateSchemaAttributes.ENTITY_ID, teamSelect);
		allOperators.add(mileageStateMap);
		
		// 4. Aggregate for the sum
		
		AggregateAO sumAggregate = OperatorBuildHelper.createAggregateAO(
				"SUM", IntermediateSchemaAttributes.TEAM_ID, ATTRIBUTE_MILEAGE, ATTRIBUTE_MILEAGE, null, mileageStateMap);
		allOperators.add(sumAggregate);
		
		List<SDFAttribute> attr = OperatorBuildHelper.createAttributeList(ATTRIBUTE_MILEAGE, sumAggregate);
		List<SDFAttribute> groupBy = OperatorBuildHelper.createAttributeList(IntermediateSchemaAttributes.TEAM_ID, sumAggregate);
		ChangeDetectAO checkDifference = OperatorBuildHelper.createChangeDetectAO(attr, 0.1, false, groupBy, sumAggregate);
		
		// 5. Clear Endtimestamp
		TimestampAO timestampAO = OperatorBuildHelper.clearEndTimestamp(checkDifference);
		allOperators.add(timestampAO);

		// 6. Assure heatbeat every x seconds
		AssureHeartbeatAO assureHeartbeatAO = OperatorBuildHelper.createHeartbeat(HEARTBEAT, timestampAO);
		allOperators.add(assureHeartbeatAO);

		// 7. Result Aggregate
		List<String> resultAggregateFunctions = new ArrayList<String>();
		resultAggregateFunctions.add("MAX");

		List<String> resultAggregateInputAttributeNames = new ArrayList<String>();
		resultAggregateInputAttributeNames.add(ATTRIBUTE_MILEAGE);

		List<String> resultAggregateOutputAttributeNames = new ArrayList<String>();
		resultAggregateOutputAttributeNames.add(ATTRIBUTE_MILEAGE);

		List<String> resultAggregateGroupBys = new ArrayList<String>();
		resultAggregateGroupBys.add(IntermediateSchemaAttributes.TEAM_ID);
		
				
		AggregateAO resultAggregate = OperatorBuildHelper
				.createAggregateAO(resultAggregateFunctions,
						resultAggregateGroupBys,
						resultAggregateInputAttributeNames,
						resultAggregateOutputAttributeNames, null,
						assureHeartbeatAO, 1);

		allOperators.add(resultAggregate);
		
		return OperatorBuildHelper.finishQuery(resultAggregate, allOperators, sportsQL.getDisplayName(),sportsQL);
	}


}
