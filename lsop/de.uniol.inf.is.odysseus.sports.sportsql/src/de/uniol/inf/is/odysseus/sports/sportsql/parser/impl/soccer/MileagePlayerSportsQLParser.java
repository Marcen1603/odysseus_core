package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
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

@SportsQL(
		gameTypes = { GameType.SOCCER },
		statisticTypes = { StatisticType.PLAYER },
		name = "mileage",
		parameters = { 
				@SportsQLParameter(name = "evaluation", parameterClass = SportsQLEvaluationParameter.class, mandatory = false),
				@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
				@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) 				}
		)
public class MileagePlayerSportsQLParser implements ISportsQLParser {
	
	private static final int HEARTBEAT = 5000;
	
	private static final String ATTRIBUTE_MILEAGE = "milage";


	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL) throws NumberFormatException, MissingDDCEntryException {

		// We need this list to initialize all operators
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		ILogicalOperator soccerGameStreamAO = OperatorBuildHelper.createGameSource(session);
		//allOperators.add(soccerGameStreamAO);

		// ----------------------------------------------------
		// First part of the query (Select for questioned time)
		// ----------------------------------------------------

		// 1. Correct selection for the time
		SportsQLTimeParameter timeParam = SportsQLParameterHelper.getTimeParameter(sportsQL);

		SelectAO timeSelect = OperatorBuildHelper.createTimeMapAndSelect(timeParam,
				soccerGameStreamAO);
		allOperators.add(timeSelect);
		
		// Selection for space
		SportsQLSpaceParameter spaceParam = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);
		SelectAO spaceSelectAO = OperatorBuildHelper.createSpaceSelect(
				spaceParam, true, timeSelect);
		allOperators.add(spaceSelectAO);

		// -------------------------------------------------------
		// Second part of the query (Select for questioned entity)
		// -------------------------------------------------------

		SelectAO teamSelect = OperatorBuildHelper.createBothTeamSelectAO(spaceSelectAO);

		// -----------------------
		// Third part of the query
		// -----------------------

		// 2. StateMap
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		// Hint: "__last_n" is part of the StateMap to access historical data.
		// See StateMap documentation.
		SDFExpressionParameter param = OperatorBuildHelper
				.createExpressionParameter(
						"(sqrt(("+OperatorBuildHelper.ATTRIBUTE_X_METER+"-__last_1."+OperatorBuildHelper.ATTRIBUTE_X_METER+")^2 + ("+OperatorBuildHelper.ATTRIBUTE_Y_METER+"-__last_1."+OperatorBuildHelper.ATTRIBUTE_Y_METER+")^2))/1000",
						ATTRIBUTE_MILEAGE, teamSelect);
		SDFExpressionParameter param2 = OperatorBuildHelper
				.createExpressionParameter(IntermediateSchemaAttributes.ENTITY_ID, teamSelect);
		expressions.add(param);
		expressions.add(param2);
		
		StateMapAO statemapAO = OperatorBuildHelper.createStateMapAO(
				expressions, IntermediateSchemaAttributes.ENTITY_ID, teamSelect);
		allOperators.add(statemapAO);

		// 3. Aggregate
		AggregateAO sumAggregateAO = OperatorBuildHelper.createAggregateAO(
				"SUM", IntermediateSchemaAttributes.ENTITY_ID, ATTRIBUTE_MILEAGE, ATTRIBUTE_MILEAGE, null, statemapAO);
		allOperators.add(sumAggregateAO);
		
		List<SDFAttribute> attr = OperatorBuildHelper.createAttributeList(ATTRIBUTE_MILEAGE, sumAggregateAO);
		List<SDFAttribute> groupBy = OperatorBuildHelper.createAttributeList(IntermediateSchemaAttributes.ENTITY_ID, sumAggregateAO);
		ChangeDetectAO checkDifference = OperatorBuildHelper.createChangeDetectAO(attr, 0.1, false, groupBy, sumAggregateAO);
		
		// 4. Clear Endtimestamp
		TimestampAO timestampAO = OperatorBuildHelper.clearEndTimestamp(checkDifference);
		allOperators.add(timestampAO);

		// 5. Assure heatbeat every x seconds
		AssureHeartbeatAO assureHeartbeatAO = OperatorBuildHelper.createHeartbeat(HEARTBEAT, timestampAO);
		allOperators.add(assureHeartbeatAO);

		// 6. Result Aggregate
		List<String> resultAggregateFunctions = new ArrayList<String>();
		resultAggregateFunctions.add("MAX");

		List<String> resultAggregateInputAttributeNames = new ArrayList<String>();
		resultAggregateInputAttributeNames.add(ATTRIBUTE_MILEAGE);

		List<String> resultAggregateOutputAttributeNames = new ArrayList<String>();
		resultAggregateOutputAttributeNames.add(ATTRIBUTE_MILEAGE);

		List<String> resultAggregateGroupBys = new ArrayList<String>();
		resultAggregateGroupBys.add(IntermediateSchemaAttributes.ENTITY_ID);
		
				
		AggregateAO resultAggregate = OperatorBuildHelper
				.createAggregateAO(resultAggregateFunctions,
						resultAggregateGroupBys,
						resultAggregateInputAttributeNames,
						resultAggregateOutputAttributeNames, null,
						assureHeartbeatAO, 1);
		
		return OperatorBuildHelper.finishQuery(resultAggregate, allOperators,	sportsQL.getDisplayName(),sportsQL);
	}
}
