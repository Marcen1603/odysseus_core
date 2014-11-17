package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
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
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLArrayParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLBooleanParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLDistanceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter.TimeUnit;


/**
 * Parser for SportsQL:
 * Query: Player, Team, Global passes.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
{
    "statisticType": "player",
    "gameType": "soccer",
    "name": "passes",
}
 * 
 * @author Pascal Schmedt
 *
 */
@SportsQL(
		gameTypes = { GameType.SOCCER }, 
		statisticTypes = { StatisticType.PLAYER }, 
		name = "passes",
		parameters = { 
				@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
				@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false),
				@SportsQLParameter(name = "entityIdIsPassReceiver", parameterClass = SportsQLBooleanParameter.class, mandatory = false),		
				@SportsQLParameter(name = "distance", parameterClass = SportsQLDistanceParameter.class, mandatory = false),		
				@SportsQLParameter(name = "doublePasses", parameterClass = SportsQLBooleanParameter.class, mandatory = false),		
				@SportsQLParameter(name = "directPasses", parameterClass = SportsQLBooleanParameter.class, mandatory = false),		
				@SportsQLParameter(name = "passDirection", parameterClass = SportsQLArrayParameter.class, mandatory = false),		
		}
		)
public class PassesPlayerSportsQLParser implements ISportsQLParser {

	// labels
		private static final String SHORT_PASS = "short";
		private static final String LONG_PASS = "long";
		private static final String FORWARDS_PASS = "forwards";
		private static final String BACK_PASS = "back";
		private static final String CROSS_PASS = "cross";	
		
		//Attributes
		private static String ATTRIBUTE_P1_TEAM_ID = "p1_team_id";
		private static String ATTRIBUTE_P2_TEAM_ID = "p2_team_id";
		private static String ATTRIBUTE_P1_ENTITY_ID = "p1_entity_id";
		private static String ATTRIBUTE_P2_ENTITY_ID = "p2_entity_id";	
		private static String ATTRIBUTE_PLAYER_SID = "player_sid";
		private static String ATTRIBUTE_PASS_DIRECTION= "pass_direction";
		private static String ATTRIBUTE_PASS_LENGTH= "pass_length";
		private static String ATTRIBUTE_DIRECT_PASS= "direct_pass";
		private static String ATTRIBUTE_DOUBLE_PASS= "double_pass";
			
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
						ATTRIBUTE_P1_ENTITY_ID, ATTRIBUTE_PLAYER_SID, globalPassesLast));
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
								+ " AND " + ATTRIBUTE_PASS_LENGTH + " = '" + SHORT_PASS
								+ "', 1, 0)", ATTRIBUTE_PASS_SUCC_SHORT,
								globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_PASS_LENGTH + " = '" + SHORT_PASS
								+ "', 1, 0)", ATTRIBUTE_PASS_FAIL_SHORT,
								globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_PASS_LENGTH + " = '" + LONG_PASS
								+ "', 1, 0)", ATTRIBUTE_PASS_SUCC_LONG,
								globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_PASS_LENGTH + " = '" + LONG_PASS
								+ "', 1, 0)", ATTRIBUTE_PASS_FAIL_LONG,
								globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_PASS_DIRECTION + " = '"
								+ FORWARDS_PASS + "', 1, 0)",
						ATTRIBUTE_PASS_SUCC_FORWARD, globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_PASS_DIRECTION + " = '"
								+ FORWARDS_PASS + "', 1, 0)",
						ATTRIBUTE_PASS_FAIL_FORWARD, globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_PASS_DIRECTION + " = '"
								+ CROSS_PASS + "', 1, 0)", ATTRIBUTE_PASS_SUCC_CROSS,
								globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_PASS_DIRECTION + " = '"
								+ CROSS_PASS + "', 1, 0)", ATTRIBUTE_PASS_FAIL_CROSS,
								globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_PASS_DIRECTION + " = '"
								+ BACK_PASS + "', 1, 0)", ATTRIBUTE_PASS_SUCC_BACK,
								globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_PASS_DIRECTION + " = '"
								+ BACK_PASS + "', 1, 0)", ATTRIBUTE_PASS_FAIL_BACK,
								globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_DIRECT_PASS + " = true, 1, 0)",
						ATTRIBUTE_PASS_SUCC_DIRECT, globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_DIRECT_PASS + " = false, 1, 0)",
						ATTRIBUTE_PASS_FAIL_DIRECT, globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " = " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_DOUBLE_PASS + " = true, 1, 0)",
						ATTRIBUTE_PASS_SUCC_DOUBLE, globalPassesLast));
				statemapExpressions2.add(OperatorBuildHelper.createExpressionParameter(
						"eif(" + ATTRIBUTE_P1_TEAM_ID + " != " + ATTRIBUTE_P2_TEAM_ID
								+ " AND " + ATTRIBUTE_DOUBLE_PASS + " = false, 1, 0)",
						ATTRIBUTE_PASS_FAIL_DOUBLE, globalPassesLast));

				StateMapAO lastStateMapAO = OperatorBuildHelper.createStateMapAO(
						statemapExpressions2, "", globalPassesLast);
				allOperators.add(lastStateMapAO);

				// 24. Statemap2
				List<SDFExpressionParameter> statemapExpressions3 = new ArrayList<SDFExpressionParameter>();

				statemapExpressions3.add(OperatorBuildHelper.createExpressionParameter(
						ATTRIBUTE_P2_ENTITY_ID, ATTRIBUTE_PLAYER_SID, globalPassesLast));
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
				groupBy.add(ATTRIBUTE_PLAYER_SID);

				AggregateAO aggregate2 = OperatorBuildHelper.createAggregateAO(
						functions2, groupBy, inputAttributeNames2, outputAttributeNames2,
						null, timeWindow, 2);

				allOperators.add(aggregate2);
				
				// 28. Finish		
				return OperatorBuildHelper.finishQuery(aggregate2, allOperators, sportsQL.getName());		
			}
	}
