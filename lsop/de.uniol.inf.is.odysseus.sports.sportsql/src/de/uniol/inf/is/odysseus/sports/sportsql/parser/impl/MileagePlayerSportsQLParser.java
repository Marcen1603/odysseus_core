package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
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

		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO();
		allOperators.add(soccerGameStreamAO);

		// ----------------------------------------------------
		// First part of the query (Select for questioned time)
		// ----------------------------------------------------

		// 1. Correct selection for the time
		SportsQLTimeParameter timeParam = SportsQLParameterHelper.getTimeParameter(sportsQL);

		SelectAO timeSelect = OperatorBuildHelper.createTimeMapAndSelect(timeParam,
				soccerGameStreamAO);
		// timeSelect.updateSchemaInfos();
		allOperators.add(timeSelect);

		// -------------------------------------------------------
		// Second part of the query (Select for questioned entity)
		// -------------------------------------------------------
		StreamAO metaDataStreamAO = OperatorBuildHelper.createMetadataStreamAO();

		SelectAO entitySelect = OperatorBuildHelper.createEntityIDSelect(
				sportsQL.getEntityId(), metaDataStreamAO);
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
}
