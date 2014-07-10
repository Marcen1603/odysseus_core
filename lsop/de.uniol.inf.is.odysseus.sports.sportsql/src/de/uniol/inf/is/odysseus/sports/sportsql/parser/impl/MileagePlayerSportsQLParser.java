package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.TEAM }, name = "mileageplayer")
public class MileagePlayerSportsQLParser implements ISportsQLParser {

	@SuppressWarnings({ "unused" })
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) {

		// TODO: Get relevant information from SportsQLQuery object

		// TODO: Get Source from DataDictionary -> Create an AccessPO

		// First Query (Select for questioned time)
		// ----------------------------------------

		// 1. MAP
		// Question: How to get the initial input?
		MapAO firstMap = OperatorBuildHelper.getMapAO(
				getExpressionsForFirstMap(), null);

		// 2. Correct timeWindow
		// TODO: Use correct times
		// How can a Map be an ISource?
		SelectAO timeSelect = OperatorBuildHelper.getTimeSelect(10, 50,
				firstMap);

		// Second Query (Select for questioned entity)
		// -------------------------------------------
		// TODO: Correct entityId
		// TODO: Correct source
		SelectAO entitySelect = OperatorBuildHelper.getEntitySelect(0, null);

		// Third Query
		// -----------

		// 1. Enrich
		// TODO: Where to get the right streams?
		EnrichAO enrichAO = OperatorBuildHelper.getEnrichAO("sensorid = sid",
				null, null);

		// 2. StateMap
		List<NamedExpressionItem> expressions = new ArrayList<NamedExpressionItem>();
		SDFExpression stateMapExpression = new SDFExpression(
				"(sqrt(((x-__last_1.x)^2 + (y-__last_1.y)^2))/1000)",
				MEP.getInstance());
		NamedExpressionItem expression = new NamedExpressionItem("mileage",
				stateMapExpression);
		StateMapAO statemap = OperatorBuildHelper.getStateMapAO(expressions,
				enrichAO);

		// 4. Aggregate
		AggregateAO aggregateAO = OperatorBuildHelper.getAggregateAO("SUM",
				"mileage", "mileage", statemap);

		return null;
	}

	private List<SDFExpressionParameter> getExpressionsForFirstMap() {
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();

		SDFExpressionParameter ex1 = OperatorBuildHelper
				.getExpressionParameter("sid");

		// TODO: These two expressions maybe won't work
		SDFExpressionParameter ex2 = OperatorBuildHelper
				.getExpressionParameter(
						"minutes(toDate(${gameStart_ts}/${ts_to_ms_factor}), toDate(ts/${ts_to_ms_factor}))",
						"minute");
		SDFExpressionParameter ex3 = OperatorBuildHelper
				.getExpressionParameter(
						"seconds(toDate(${gameStart_ts}/${ts_to_ms_factor}), toDate(ts/${ts_to_ms_factor}))",
						"second");

		SDFExpressionParameter ex4 = OperatorBuildHelper
				.getExpressionParameter("x");
		SDFExpressionParameter ex5 = OperatorBuildHelper
				.getExpressionParameter("y");
		SDFExpressionParameter ex6 = OperatorBuildHelper
				.getExpressionParameter("z");
		SDFExpressionParameter ex7 = OperatorBuildHelper
				.getExpressionParameter("v");
		SDFExpressionParameter ex8 = OperatorBuildHelper
				.getExpressionParameter("a");
		SDFExpressionParameter ex9 = OperatorBuildHelper
				.getExpressionParameter("vx");
		SDFExpressionParameter ex10 = OperatorBuildHelper
				.getExpressionParameter("vy");
		SDFExpressionParameter ex11 = OperatorBuildHelper
				.getExpressionParameter("vz");
		SDFExpressionParameter ex12 = OperatorBuildHelper
				.getExpressionParameter("ax");
		SDFExpressionParameter ex13 = OperatorBuildHelper
				.getExpressionParameter("ay");
		SDFExpressionParameter ex14 = OperatorBuildHelper
				.getExpressionParameter("ts");

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
