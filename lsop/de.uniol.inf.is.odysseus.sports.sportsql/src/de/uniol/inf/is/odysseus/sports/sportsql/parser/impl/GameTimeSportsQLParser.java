package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SoccerGameAttributes;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.helper.TimeUnitHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLBooleanParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLLongParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter.TimeUnit;

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
		@SportsQLParameter(name = "isSelectDataBetweenTimestamps", parameterClass = SportsQLBooleanParameter.class, mandatory = false),
		@SportsQLParameter(name = "addMilliseconds", parameterClass = SportsQLBooleanParameter.class, mandatory = false),})
public class GameTimeSportsQLParser implements ISportsQLParser {

//	private static final String ATTRIBUTE_NEW_TS = "newts";
//	private static final String ATTRIBUTE_MILLISECOND = "milliseconds";

	@Override
	public ILogicalQuery parse(ISession session,SportsQLQuery sportsQL)
			throws SportsQLParseException, MissingDDCEntryException {

		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO(session);

		// 1. Only data stream elements between starttimestamp and endtimestamp
		ILogicalOperator gameTimeSelect = null;
		
		if (isSelectDataBetweenTimestampsEnabled(sportsQL)) {
			String startts = getStartTimestamp(sportsQL);
			String endts = getEndTimestamp(sportsQL);
			
			List<String> gameTimeSelectPredicates = new ArrayList<String>();
			gameTimeSelectPredicates.add(SoccerGameAttributes.TS + ">= " + startts);
			gameTimeSelectPredicates.add(SoccerGameAttributes.TS + "<= " + endts);

			gameTimeSelect = OperatorBuildHelper.createSelectAO(gameTimeSelectPredicates, soccerGameStreamAO);
			allOperators.add(gameTimeSelect);
		} else {
			gameTimeSelect = soccerGameStreamAO;
		}

		//long test3 = TimeUnitHelper.getBTUtoMinutesFactor(TimeUnit.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase()));
		//double test4 = TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase()));
		//System.out.println(test3 + " " + test4 + " " + AbstractSportsDDCAccess.getBasetimeunit().toLowerCase() + " " + AbstractSportsDDCAccess.getBasetimeunit().toLowerCase());
		// 3. Convert decimal minutes to minute, second and millisecond
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		expressions.add(OperatorBuildHelper
				.createExpressionParameter("DoubleToInteger(ts/"+TimeUnitHelper.getBTUtoMinutesFactor(TimeUnit.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase()))+")", OperatorBuildHelper.ATTRIBUTE_MINUTE, gameTimeSelect));
		expressions.add(OperatorBuildHelper
				.createExpressionParameter("DoubleToInteger((ts/"+TimeUnitHelper.getBTUtoSecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase()))+") % 60)", OperatorBuildHelper.ATTRIBUTE_SECOND, gameTimeSelect));
		if(addMilliseconds(sportsQL)) {
			expressions.add(OperatorBuildHelper.
					createExpressionParameter("DoubleToInteger((ts/"+TimeUnitHelper.getBTUtoMillisecondsFactor(TimeUnit.valueOf(AbstractSportsDDCAccess.getBasetimeunit().toLowerCase()))+") % 1000)", OperatorBuildHelper.ATTRIBUTE_MILLISECOND, gameTimeSelect));
		}
			MapAO timeMap = OperatorBuildHelper.createMapAO(expressions, gameTimeSelect,
				0, 0, false);
		timeMap.initialize();
		allOperators.add(timeMap);

		// 4. Remove duplicate data stream elements
		ArrayList<String> gameTimeChangeDetectAttributes = new ArrayList<String>();
		if(addMilliseconds(sportsQL)) {
			gameTimeChangeDetectAttributes.add(OperatorBuildHelper.ATTRIBUTE_MILLISECOND);
		} else {
			gameTimeChangeDetectAttributes.add(OperatorBuildHelper.ATTRIBUTE_SECOND);
		}
		
		ChangeDetectAO gameTimeChangeDetect = OperatorBuildHelper
				.createChangeDetectAO(OperatorBuildHelper.createAttributeList(
						gameTimeChangeDetectAttributes, timeMap), 0,
						timeMap);
		allOperators.add(gameTimeChangeDetect);

		// 5. Finish
		return OperatorBuildHelper.finishQuery(gameTimeChangeDetect,
				allOperators, sportsQL.getName());
	}

	private String getStartTimestamp(SportsQLQuery sportsQL) {
		ISportsQLParameter starttsParameter = sportsQL.getParameters().get(
				"startts");
		return ((SportsQLLongParameter) starttsParameter).getValue() + ".0";
	}

	private String getEndTimestamp(SportsQLQuery sportsQL) {
		ISportsQLParameter starttsParameter = sportsQL.getParameters().get(
				"endts");
		return ((SportsQLLongParameter) starttsParameter).getValue() + ".0";
	}
	
	private boolean isSelectDataBetweenTimestampsEnabled(SportsQLQuery sportsQL) {
		ISportsQLParameter selectDataBetweenTimestamps = sportsQL.getParameters().get("selectDataBetweenTimestamps");	
		if (selectDataBetweenTimestamps != null) {
			return ((SportsQLBooleanParameter) selectDataBetweenTimestamps).getValue();
		} else {
			return false;
		}
	}
	
	private boolean addMilliseconds(SportsQLQuery sportsQL) {
		ISportsQLParameter addMilliseconds = sportsQL.getParameters().get("addMilliseconds");	
		if (addMilliseconds != null) {
			return ((SportsQLBooleanParameter) addMilliseconds).getValue();
		} else {
			return false;
		}
	}

}
