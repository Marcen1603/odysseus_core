package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
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
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class MileageTeamSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) {

		// We need this list to initialize all operators
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		String soccerGameSourceName = OperatorBuildHelper.MAIN_STREAM_NAME;
		AccessAO soccerGameAccessAO = OperatorBuildHelper
				.createAccessAO(soccerGameSourceName);

		String metadataSourceName = OperatorBuildHelper.METADATA_STREAM_NAME;
		AccessAO metaDataAccessAO = OperatorBuildHelper
				.createAccessAO(metadataSourceName);

		// ---------------------------------------
		// First part (Select for questioned time)
		// ---------------------------------------

		// TODO Copied code: here and in MileagePlayer ...
		// 1. MAP
		MapAO firstMap = OperatorBuildHelper.createMapAO(
				getExpressionsForFirstMap(soccerGameAccessAO),
				soccerGameAccessAO, 0, 0);
		allOperators.add(firstMap);

		// 2. Correct selection for the time
		SportsQLTimeParameter timeParam = SportsQLParameterHelper
				.getTimeParameter(sportsQL);

		// TODO SelectAO does not have an output schema - why?
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
				spaceParam, true, soccerGameAccessAO);
		allOperators.add(spaceSelectAO);

		// -----------------------------------------
		// Third part (Select for questioned team)
		// -----------------------------------------

		// 1. Select for team
		SelectAO teamSelect = OperatorBuildHelper.createEntitySelect(
				sportsQL.getEntityId(), metaDataAccessAO);
		allOperators.add(teamSelect);

		// -----------------------------------------
		// Fourth part (Calculate distance)
		// -----------------------------------------

		// 1. Join
		// TODO add card to join
		List<String> predicates = new ArrayList<String>();
		predicates.add("sid = sensor_id");
		JoinAO sensorJoinAO = OperatorBuildHelper.createJoinAO(predicates,
				timeSelect, spaceSelectAO);
		allOperators.add(sensorJoinAO);

		// 2. Enrich MainStream with MetaData
		EnrichAO sensorEnrich = OperatorBuildHelper.createEnrichAO(
				"sensorid = sid", teamSelect, sensorJoinAO);
		allOperators.add(sensorEnrich);

		// 3. StateMapAO for mileage calculation
		// TODO group by list
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter param1 = OperatorBuildHelper
				.createExpressionParameter("sensorid", sensorEnrich);
		SDFExpressionParameter param2 = OperatorBuildHelper
				.createExpressionParameter("entity_id", sensorEnrich);
		SDFExpressionParameter param3 = OperatorBuildHelper
				.createExpressionParameter(
						"sqrt(((x_meter-__last_1.x_meter)^2 + (y_meter-__last_1.y_meter)^2))",
						"mileage", sensorEnrich);

		expressions.add(param1);
		expressions.add(param2);
		expressions.add(param3);
		
		StateMapAO mileageStateMap = OperatorBuildHelper.createStateMapAO(
				expressions, "", sensorEnrich);
		allOperators.add(mileageStateMap);
		
		// 3. Aggregate for the sum
		// TODO list for group
		// TODO is null possible there?
		AggregateAO sumAggregate = OperatorBuildHelper.createAggregateAO("SUM", "", "mileage", "mileage", null, mileageStateMap);
		allOperators.add(sumAggregate);
		
		// 4. Aggregate for max
		// TODO list for group
		AggregateAO maxAggregate = OperatorBuildHelper.createAggregateAO("MAX", "", "mileage", "mileage", null, sumAggregate);
		allOperators.add(maxAggregate);
		
		// 5. Aggregate for sum again
		AggregateAO finalSumAggregate = OperatorBuildHelper.createAggregateAO("SUM", "mileage", "mileage", maxAggregate);
		allOperators.add(finalSumAggregate);
		
		return OperatorBuildHelper.finishQuery(finalSumAggregate, allOperators, sportsQL.getName());
	}

	private List<SDFExpressionParameter> getExpressionsForFirstMap(
			ILogicalOperator source) {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();

		SDFExpressionParameter ex1 = OperatorBuildHelper
				.createExpressionParameter("sid", source);

		// TODO Maybe we have to use the MEP-functions manually
		SDFExpressionParameter ex2 = OperatorBuildHelper
				.createExpressionParameter("minutes(toDate("
						+ OperatorBuildHelper.TS_GAME_START + "/"
						+ OperatorBuildHelper.TS_TO_MS_FACTOR + "), toDate(ts/"
						+ OperatorBuildHelper.TS_TO_MS_FACTOR + "))", "minute",
						source);
		SDFExpressionParameter ex3 = OperatorBuildHelper
				.createExpressionParameter("seconds(toDate("
						+ OperatorBuildHelper.TS_GAME_START + "/"
						+ OperatorBuildHelper.TS_TO_MS_FACTOR + "), toDate(ts/"
						+ OperatorBuildHelper.TS_TO_MS_FACTOR + "))", "second",
						source);

		SDFExpressionParameter ex4 = OperatorBuildHelper
				.createExpressionParameter("x", source);
		SDFExpressionParameter ex5 = OperatorBuildHelper
				.createExpressionParameter("y", source);
		SDFExpressionParameter ex6 = OperatorBuildHelper
				.createExpressionParameter("z", source);
		SDFExpressionParameter ex7 = OperatorBuildHelper
				.createExpressionParameter("v", source);
		SDFExpressionParameter ex8 = OperatorBuildHelper
				.createExpressionParameter("a", source);
		SDFExpressionParameter ex9 = OperatorBuildHelper
				.createExpressionParameter("vx", source);
		SDFExpressionParameter ex10 = OperatorBuildHelper
				.createExpressionParameter("vy", source);
		SDFExpressionParameter ex11 = OperatorBuildHelper
				.createExpressionParameter("vz", source);
		SDFExpressionParameter ex12 = OperatorBuildHelper
				.createExpressionParameter("ax", source);
		SDFExpressionParameter ex13 = OperatorBuildHelper
				.createExpressionParameter("ay", source);
		SDFExpressionParameter ex14 = OperatorBuildHelper
				.createExpressionParameter("ts", source);

		expressions.add(ex1);
		expressions.add(ex2);
		expressions.add(ex3);
		expressions.add(ex4);
		expressions.add(ex5);
		expressions.add(ex6);
		expressions.add(ex7);
		expressions.add(ex8);
		expressions.add(ex9);
		expressions.add(ex10);
		expressions.add(ex11);
		expressions.add(ex12);
		expressions.add(ex13);
		expressions.add(ex14);

		return expressions;
	}

}
