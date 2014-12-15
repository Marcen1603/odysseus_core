package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MergeAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.TimeUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter.TimeUnit;


/**
 * Parser for SportsQL:
 * Query: Team passes.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
{
    "statisticType": "team",
    "gameType": "soccer",
    "name": "passes",
}
 * 
 * @author Pascal Schmedt
 *
 */
@SportsQL(
		gameTypes = { GameType.SOCCER }, 
		statisticTypes = { StatisticType.TEAM }, 
		name = "passes",
		parameters = { 
				@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
				@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false)
			}
		)
public class PassesTeamSportsQLParser implements ISportsQLParser {
	

		private static String ATTRIBUTE_P1_TEAM_ID = PassesSportsQLParser.ATTRIBUTE_PLAYER_TEAM_ID+"_1";	
		private static String ATTRIBUTE_P2_TEAM_ID = PassesSportsQLParser.ATTRIBUTE_PLAYER_TEAM_ID+"_2";
		private static String ATTRIBUTE_TEAM_ENTITY_ID = "team_id";
				
		private static String ATTRIBUTE_PASS_SUCCESS = "passes_successful";
		private static String ATTRIBUTE_PASS_FAIL = "passes_misplaced";
		private static String ATTRIBUTE_PASS_RECEIVED = "passes_received";
		private static String ATTRIBUTE_PASS_INTERCEPTED = "passes_intercepted";
		private static String ATTRIBUTE_PASS_SUCC_SHORT = "short_passes_successful";
		private static String ATTRIBUTE_PASS_FAIL_SHORT = "short_passes_misplaced";
		private static String ATTRIBUTE_PASS_SUCC_LONG = "long_passes_successful";
		private static String ATTRIBUTE_PASS_FAIL_LONG = "long_passes_misplaced";
		private static String ATTRIBUTE_PASS_SUCC_FORWARD = "forward_passes_successful";
		private static String ATTRIBUTE_PASS_FAIL_FORWARD = "forward_passes_misplaced";
		private static String ATTRIBUTE_PASS_SUCC_CROSS = "cross_passes_successful";
		private static String ATTRIBUTE_PASS_FAIL_CROSS = "cross_passes_misplaced";
		private static String ATTRIBUTE_PASS_SUCC_BACK = "back_passes_successful";
		private static String ATTRIBUTE_PASS_FAIL_BACK = "back_passes_misplaced";
		private static String ATTRIBUTE_PASS_SUCC_DIRECT = "direct_passes_successful";
		private static String ATTRIBUTE_PASS_FAIL_DIRECT = "direct_passes_misplaced";
		private static String ATTRIBUTE_PASS_SUCC_DOUBLE = "double_passes_successful";
		private static String ATTRIBUTE_PASS_FAIL_DOUBLE = "double_passes_misplaced";
		
		
	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException,
			MissingDDCEntryException {

		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		PassesSportsQLParser globalPasses = new PassesSportsQLParser();	
		ILogicalOperator globalPassesLast = globalPasses.getPasses(session, sportsQL, allOperators);
		
		// 23. Statemap1
		List<SDFExpressionParameter> statemapExpressions2 = new ArrayList<SDFExpressionParameter>();

		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_P1_TEAM_ID, ATTRIBUTE_TEAM_ENTITY_ID, globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ ", 1, 0)", ATTRIBUTE_PASS_SUCCESS,
						globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ ", 0, 1)", ATTRIBUTE_PASS_FAIL, globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_RECEIVED, globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_INTERCEPTED, globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_LENGTH + " = '" + PassesSportsQLParser.SHORT_PASS
						+ "', 1, 0)", ATTRIBUTE_PASS_SUCC_SHORT,
						globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_LENGTH + " = '" + PassesSportsQLParser.SHORT_PASS
						+ "', 1, 0)", ATTRIBUTE_PASS_FAIL_SHORT,
						globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_LENGTH + " = '" + PassesSportsQLParser.LONG_PASS
						+ "', 1, 0)", ATTRIBUTE_PASS_SUCC_LONG,
						globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_LENGTH + " = '" + PassesSportsQLParser.LONG_PASS
						+ "', 1, 0)", ATTRIBUTE_PASS_FAIL_LONG,
						globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_DIRECTION + " = '"
						+ PassesSportsQLParser.FORWARDS_PASS + "', 1, 0)",
				ATTRIBUTE_PASS_SUCC_FORWARD, globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_DIRECTION + " = '"
						+ PassesSportsQLParser.FORWARDS_PASS + "', 1, 0)",
				ATTRIBUTE_PASS_FAIL_FORWARD, globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_DIRECTION + " = '"
						+ PassesSportsQLParser.CROSS_PASS + "', 1, 0)", ATTRIBUTE_PASS_SUCC_CROSS,
						globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_DIRECTION + " = '"
						+ PassesSportsQLParser.CROSS_PASS + "', 1, 0)", ATTRIBUTE_PASS_FAIL_CROSS,
						globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_DIRECTION + " = '"
						+ PassesSportsQLParser.BACK_PASS + "', 1, 0)", ATTRIBUTE_PASS_SUCC_BACK,
						globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_PASS_DIRECTION + " = '"
						+ PassesSportsQLParser.BACK_PASS + "', 1, 0)", ATTRIBUTE_PASS_FAIL_BACK,
						globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_DIRECT_PASS + " = true, 1, 0)",
				ATTRIBUTE_PASS_SUCC_DIRECT, globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_DIRECT_PASS + " = false, 1, 0)",
				ATTRIBUTE_PASS_FAIL_DIRECT, globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_DOUBLE_PASS + " = true, 1, 0)",
				ATTRIBUTE_PASS_SUCC_DOUBLE, globalPassesLast));
		statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
						+ " AND " + PassesSportsQLParser.ATTRIBUTE_DOUBLE_PASS + " = false, 1, 0)",
				ATTRIBUTE_PASS_FAIL_DOUBLE, globalPassesLast));

		StateMapAO lastStateMapAO = OperatorBuildHelper.createStateMapAO(
				statemapExpressions2, "", globalPassesLast);
		allOperators.add(lastStateMapAO);

		// 24. Statemap2
		List<SDFExpressionParameter> statemapExpressions3 = new ArrayList<SDFExpressionParameter>();

		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				ATTRIBUTE_P2_TEAM_ID, ATTRIBUTE_TEAM_ENTITY_ID, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_SUCCESS, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_FAIL, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ ", 1, 0)", ATTRIBUTE_PASS_RECEIVED, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
						+ ", 0, 1)", ATTRIBUTE_PASS_INTERCEPTED, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_SUCC_SHORT, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_FAIL_SHORT, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_SUCC_LONG, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_FAIL_LONG, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_SUCC_FORWARD, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_FAIL_FORWARD, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_SUCC_CROSS, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_FAIL_CROSS, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_SUCC_BACK, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_FAIL_BACK, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_SUCC_DIRECT, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_FAIL_DIRECT, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_SUCC_DOUBLE, globalPassesLast));
		statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
				"0", ATTRIBUTE_PASS_FAIL_DOUBLE, globalPassesLast));

		StateMapAO forRealLastStateMapAO = OperatorBuildHelper
				.createStateMapAO(statemapExpressions3, "", globalPassesLast);
		allOperators.add(forRealLastStateMapAO);

		// 25. Join
		MergeAO lastStateMapsJoin = OperatorBuildHelper.createMergeAO(lastStateMapAO,forRealLastStateMapAO);
		allOperators.add(lastStateMapsJoin);
		
		// 26. Window
		TimeWindowAO timeWindow = OperatorBuildHelper.createTimeWindowAO(
				(long)(120 * TimeUnitHelper.getBTUtoMillisecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase()))), "MINUTES", lastStateMapsJoin);
		allOperators.add(timeWindow);
		
		// 27. Aggregate 
		List<String> functions2 = new ArrayList<String>();
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		functions2.add("SUM");
		
		List<String> inputAttributeNames2 = new ArrayList<String>();
		inputAttributeNames2.add(ATTRIBUTE_PASS_SUCCESS);
		inputAttributeNames2.add(ATTRIBUTE_PASS_FAIL);
		inputAttributeNames2.add(ATTRIBUTE_PASS_RECEIVED);
		inputAttributeNames2.add(ATTRIBUTE_PASS_INTERCEPTED);
		inputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_SHORT);
		inputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_SHORT);
		inputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_LONG);
		inputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_LONG);
		inputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_FORWARD);
		inputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_FORWARD);
		inputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_CROSS);
		inputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_CROSS);
		inputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_BACK);
		inputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_BACK);
		inputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_DIRECT);
		inputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_DIRECT);
		inputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_DOUBLE);
		inputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_DOUBLE);

		List<String> outputAttributeNames2 = new ArrayList<String>();
		outputAttributeNames2.add(ATTRIBUTE_PASS_SUCCESS);
		outputAttributeNames2.add(ATTRIBUTE_PASS_FAIL);
		outputAttributeNames2.add(ATTRIBUTE_PASS_RECEIVED);
		outputAttributeNames2.add(ATTRIBUTE_PASS_INTERCEPTED);
		outputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_SHORT);
		outputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_SHORT);
		outputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_LONG);
		outputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_LONG);
		outputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_FORWARD);
		outputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_FORWARD);
		outputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_CROSS);
		outputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_CROSS);
		outputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_BACK);
		outputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_BACK);
		outputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_DIRECT);
		outputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_DIRECT);
		outputAttributeNames2.add(ATTRIBUTE_PASS_SUCC_DOUBLE);
		outputAttributeNames2.add(ATTRIBUTE_PASS_FAIL_DOUBLE);

		List<String> groupBy = new ArrayList<String>();
		groupBy.add(ATTRIBUTE_TEAM_ENTITY_ID);

		AggregateAO aggregate2 = OperatorBuildHelper.createAggregateAO(
				functions2, groupBy, inputAttributeNames2, outputAttributeNames2,
				null, timeWindow, 2);

		allOperators.add(aggregate2);
		
		List<String> changeDetectAttributes = new ArrayList<String>();
		changeDetectAttributes.add(ATTRIBUTE_PASS_SUCCESS);
		changeDetectAttributes.add(ATTRIBUTE_PASS_FAIL);
		changeDetectAttributes.add(ATTRIBUTE_PASS_RECEIVED);
		changeDetectAttributes.add(ATTRIBUTE_PASS_INTERCEPTED);
		changeDetectAttributes.add(ATTRIBUTE_PASS_SUCC_SHORT);
		changeDetectAttributes.add(ATTRIBUTE_PASS_FAIL_SHORT);
		changeDetectAttributes.add(ATTRIBUTE_PASS_SUCC_LONG);
		changeDetectAttributes.add(ATTRIBUTE_PASS_FAIL_LONG);
		changeDetectAttributes.add(ATTRIBUTE_PASS_SUCC_FORWARD);
		changeDetectAttributes.add(ATTRIBUTE_PASS_FAIL_FORWARD);
		changeDetectAttributes.add(ATTRIBUTE_PASS_SUCC_CROSS);
		changeDetectAttributes.add(ATTRIBUTE_PASS_FAIL_CROSS);
		changeDetectAttributes.add(ATTRIBUTE_PASS_SUCC_BACK);
		changeDetectAttributes.add(ATTRIBUTE_PASS_FAIL_BACK);
		changeDetectAttributes.add(ATTRIBUTE_PASS_SUCC_DIRECT);
		changeDetectAttributes.add(ATTRIBUTE_PASS_FAIL_DIRECT);
		changeDetectAttributes.add(ATTRIBUTE_PASS_SUCC_DOUBLE);
		changeDetectAttributes.add(ATTRIBUTE_PASS_FAIL_DOUBLE);
		List<String> changeDetectGroupBy = new ArrayList<String>();
		changeDetectGroupBy.add(ATTRIBUTE_TEAM_ENTITY_ID);
		
		ChangeDetectAO changeDetect = OperatorBuildHelper.createChangeDetectAO(OperatorBuildHelper.createAttributeList(changeDetectAttributes,aggregate2), 0.1, true, OperatorBuildHelper.createAttributeList(changeDetectGroupBy,aggregate2), aggregate2);
		allOperators.add(changeDetect);

		// 28. Finish		
		return OperatorBuildHelper.finishQuery(changeDetect, allOperators, sportsQL.getDisplayName());		
	}

}
