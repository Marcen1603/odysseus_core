package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SoccerGameAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLBooleanParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLLongParameter;

/**
 * Parser for SportsQL: Query: game time.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
 * { "statisticType": "global", "gameType": "soccer", "name": "gameTime",
 * "parameters": { "startts": 10753295594424116, "endts": 14879639146403495 } };
 * 
 * @author Thore Stratmann
 *
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.GLOBAL }, name = "gameTime", parameters = {
		@SportsQLParameter(name = "startts", parameterClass = SportsQLLongParameter.class, mandatory = false),
		@SportsQLParameter(name = "endts", parameterClass = SportsQLLongParameter.class, mandatory = false),
		@SportsQLParameter(name = "selectDataBetweenTimestamps", parameterClass = SportsQLBooleanParameter.class, mandatory = false),})
public class GameTimeSportsQLParser implements ISportsQLParser {

	private static final String ATTRIBUTE_NEW_TS = "newts";
	private static final String ATTRIBUTE_MILLISECOND = "milliseconds";

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {

		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO();

		// Default starttimestamp and endtimestamp are used if no parameters
		// were set
		String startts = getStartTimestamp(sportsQL);
		String endts = getEndTimestamp(sportsQL);


		// 1. Only data stream elements between starttimestamp and endtimestamp
		ILogicalOperator gameTimeSelect = null;
		
		if (isSelectDataBetweenTimestampsEnabled(sportsQL)) {
			List<String> gameTimeSelectPredicates = new ArrayList<String>();
			gameTimeSelectPredicates.add(SoccerGameAttributes.TS + ">= " + startts);
			gameTimeSelectPredicates.add(SoccerGameAttributes.TS + "<= " + endts);

			gameTimeSelect = OperatorBuildHelper.createSelectAO(gameTimeSelectPredicates, soccerGameStreamAO);
			allOperators.add(gameTimeSelect);
		} else {
			gameTimeSelect = soccerGameStreamAO;
		}


		// 2. Convert timestamp to decimal minutes
		ArrayList<SDFExpressionParameter> newtsMapExpressions = new ArrayList<SDFExpressionParameter>();
		newtsMapExpressions.add(OperatorBuildHelper.createExpressionParameter("(" + SoccerGameAttributes.TS + " - " + startts+ ") / 60000000000000.0", ATTRIBUTE_NEW_TS,gameTimeSelect));

		MapAO newtsMap = OperatorBuildHelper.createMapAO(newtsMapExpressions,
				gameTimeSelect, 0, 0);
		allOperators.add(newtsMap);

		// 3. Convert decimal minutes to minute, second and millisecond
		ArrayList<SDFExpressionParameter> gameTimeMapExpressions = new ArrayList<SDFExpressionParameter>();
		gameTimeMapExpressions.add(OperatorBuildHelper.createExpressionParameter("ToLong(" + ATTRIBUTE_NEW_TS + ")","minute", newtsMap));
		gameTimeMapExpressions.add(OperatorBuildHelper
				.createExpressionParameter("ToLong((" + ATTRIBUTE_NEW_TS
						+ " - ToLong(" + ATTRIBUTE_NEW_TS + ")) * 60)",
						"second", newtsMap));
		gameTimeMapExpressions.add(OperatorBuildHelper
				.createExpressionParameter("ToLong((((" + ATTRIBUTE_NEW_TS
						+ " - ToLong(" + ATTRIBUTE_NEW_TS
						+ ")) * 60) - ToLong((" + ATTRIBUTE_NEW_TS
						+ " - ToLong(" + ATTRIBUTE_NEW_TS + ")) * 60))*1000)",
						ATTRIBUTE_MILLISECOND, newtsMap));

		MapAO gameTimeMap = OperatorBuildHelper.createMapAO(
				gameTimeMapExpressions, newtsMap, 0, 0);
		allOperators.add(gameTimeMap);

		// 4. Remove duplicate data stream elements
		ArrayList<String> gameTimeChangeDetectAttributes = new ArrayList<String>();
		gameTimeChangeDetectAttributes.add(ATTRIBUTE_MILLISECOND);

		ChangeDetectAO gameTimeChangeDetect = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						gameTimeChangeDetectAttributes, gameTimeMap), 0,
						gameTimeMap);
		allOperators.add(gameTimeChangeDetect);

		// 5. Finish
		return OperatorBuildHelper.finishQuery(gameTimeChangeDetect,
				allOperators, sportsQL.getName());
	}

	private String getStartTimestamp(SportsQLQuery sportsQL) {
		ISportsQLParameter starttsParameter = sportsQL.getParameters().get(
				"startts");
		if (starttsParameter != null) {
			return ((SportsQLLongParameter) starttsParameter).getValue() + ".0";
		} else {
			return OperatorBuildHelper.TS_GAME_START;
		}
	}

	private String getEndTimestamp(SportsQLQuery sportsQL) {
		ISportsQLParameter starttsParameter = sportsQL.getParameters().get(
				"endts");
		if (starttsParameter != null) {
			return ((SportsQLLongParameter) starttsParameter).getValue() + ".0";
		} else {
			return OperatorBuildHelper.TS_GAME_END;
		}
	}
	
	private boolean isSelectDataBetweenTimestampsEnabled(SportsQLQuery sportsQL) {
		ISportsQLParameter selectDataBetweenTimestamps = sportsQL.getParameters().get("selectDataBetweenTimestamps");	
		if (selectDataBetweenTimestamps != null) {
			return ((SportsQLBooleanParameter) selectDataBetweenTimestamps).getValue();
		} else {
			return false;
		}
	}

}
