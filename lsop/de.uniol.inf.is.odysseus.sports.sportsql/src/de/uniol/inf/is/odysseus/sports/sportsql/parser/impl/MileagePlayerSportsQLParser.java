package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
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
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

@SportsQL(
		gameTypes = { GameType.SOCCER },
		statisticTypes = { StatisticType.PLAYER },
		name = "mileage",
		parameters = { 
				@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false)				}
		)
public class MileagePlayerSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) {

		// We need this list to initialize all operators
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		String soccerGameSourceName = OperatorBuildHelper.MAIN_STREAM_NAME;
		AccessAO soccerGameAccessAO = OperatorBuildHelper
				.createAccessAO(soccerGameSourceName);
		allOperators.add(soccerGameAccessAO);

		// ----------------------------------------------------
		// First part of the query (Select for questioned time)
		// ----------------------------------------------------

		// 1. MAP
		MapAO firstMap = OperatorBuildHelper.createMapAO(
				getExpressionsForFirstMap(soccerGameAccessAO),
				soccerGameAccessAO, 0, 0);
		allOperators.add(firstMap);

		// 2. Correct selection for the time
		SportsQLTimeParameter timeParam = SportsQLParameterHelper.getTimeParameter(sportsQL);

		// TODO SelectAO does not have an output schema - why?
		SelectAO timeSelect = OperatorBuildHelper.createTimeSelect(timeParam,
				firstMap);
		// timeSelect.updateSchemaInfos();
		allOperators.add(timeSelect);

		// -------------------------------------------------------
		// Second part of the query (Select for questioned entity)
		// -------------------------------------------------------
		String metadataSourceName = OperatorBuildHelper.METADATA_STREAM_NAME;
		AccessAO metaDataAccessAO = OperatorBuildHelper
				.createAccessAO(metadataSourceName);

		SelectAO entitySelect = OperatorBuildHelper.createEntitySelect(
				sportsQL.getEntityId(), metaDataAccessAO);
		allOperators.add(entitySelect);

		// -----------------------
		// Third part of the query
		// -----------------------

		// 1. Enrich
		EnrichAO enrichAO = OperatorBuildHelper.createEnrichAO(
				"sensorid = sid", entitySelect, timeSelect);
		allOperators.add(enrichAO);

		// 2. StateMap
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		// Hint: "__last_n" is part of the StateMap to access historical data.
		// See StateMap documentation.
		SDFExpressionParameter param = OperatorBuildHelper
				.createExpressionParameter(
						"(sqrt(((x-__last_1.x)^2 + (y-__last_1.y)^2))/1000)",
						"mileage", enrichAO);
		SDFExpressionParameter param2 = OperatorBuildHelper
				.createExpressionParameter("sensorid", enrichAO);
		expressions.add(param);
		expressions.add(param2);
		StateMapAO statemapAO = OperatorBuildHelper.createStateMapAO(
				expressions, "sensorid", enrichAO);
		allOperators.add(statemapAO);

		// 3. Aggregate
		AggregateAO sumAggregateAO = OperatorBuildHelper.createAggregateAO(
				"SUM", "sensorid", "mileage", "mileage", null, statemapAO);
		allOperators.add(sumAggregateAO);

		// 4. Aggregate
		AggregateAO maxAggregateAO = OperatorBuildHelper.createAggregateAO(
				"MAX", "mileage", "mileage", sumAggregateAO);
		allOperators.add(maxAggregateAO);

		return OperatorBuildHelper.finishQuery(maxAggregateAO, allOperators,
				sportsQL.getName());
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
