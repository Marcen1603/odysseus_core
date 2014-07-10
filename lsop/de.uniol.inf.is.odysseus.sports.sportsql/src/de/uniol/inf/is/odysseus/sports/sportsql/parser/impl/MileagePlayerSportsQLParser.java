package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
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

		// 2. Correct timewindow
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

		// 2. Statemap
		List<NamedExpressionItem> expressions = new ArrayList<NamedExpressionItem>();
		SDFExpression stateMapExpression = new SDFExpression(
				"(sqrt(((x-__last_1.x)^2 + (y-__last_1.y)^2))/1000)",
				MEP.getInstance());
		NamedExpressionItem expression = new NamedExpressionItem("mileage",
				stateMapExpression);
		OperatorBuildHelper.getStateMapAO(expressions, enrichAO);
		
		 // 4. Aggregate

		return null;
	}

	private List<NamedExpressionItem> getExpressionsForFirstMap() {
		List<NamedExpressionItem> expressions = new ArrayList<NamedExpressionItem>();

		SDFExpression mepEx1 = new SDFExpression("sid", MEP.getInstance());
		NamedExpressionItem ex1 = new NamedExpressionItem("sid", mepEx1);

		// TODO: These expressions probably wont work
		SDFExpression mepEx2 = new SDFExpression(
				"minutes(toDate(${gameStart_ts}/${ts_to_ms_factor}), toDate(ts/${ts_to_ms_factor}))",
				MEP.getInstance());
		NamedExpressionItem ex2 = new NamedExpressionItem("minute", mepEx2);

		SDFExpression mepEx3 = new SDFExpression(
				"seconds(toDate(${gameStart_ts}/${ts_to_ms_factor}), toDate(ts/${ts_to_ms_factor}))",
				MEP.getInstance());
		NamedExpressionItem ex3 = new NamedExpressionItem("second", mepEx3);

		SDFExpression mepEx4 = new SDFExpression("x", MEP.getInstance());
		NamedExpressionItem ex4 = new NamedExpressionItem("x", mepEx4);

		SDFExpression mepEx5 = new SDFExpression("y", MEP.getInstance());
		NamedExpressionItem ex5 = new NamedExpressionItem("y", mepEx5);

		SDFExpression mepEx6 = new SDFExpression("z", MEP.getInstance());
		NamedExpressionItem ex6 = new NamedExpressionItem("z", mepEx6);

		SDFExpression mepEx7 = new SDFExpression("v", MEP.getInstance());
		NamedExpressionItem ex7 = new NamedExpressionItem("v", mepEx7);

		SDFExpression mepEx8 = new SDFExpression("a", MEP.getInstance());
		NamedExpressionItem ex8 = new NamedExpressionItem("a", mepEx8);

		SDFExpression mepEx9 = new SDFExpression("vx", MEP.getInstance());
		NamedExpressionItem ex9 = new NamedExpressionItem("vx", mepEx9);

		SDFExpression mepEx10 = new SDFExpression("vy", MEP.getInstance());
		NamedExpressionItem ex10 = new NamedExpressionItem("vy", mepEx10);

		SDFExpression mepEx11 = new SDFExpression("vz", MEP.getInstance());
		NamedExpressionItem ex11 = new NamedExpressionItem("vz", mepEx11);

		SDFExpression mepEx12 = new SDFExpression("ax", MEP.getInstance());
		NamedExpressionItem ex12 = new NamedExpressionItem("ax", mepEx12);

		SDFExpression mepEx13 = new SDFExpression("ay", MEP.getInstance());
		NamedExpressionItem ex13 = new NamedExpressionItem("ay", mepEx13);

		SDFExpression mepEx14 = new SDFExpression("ts", MEP.getInstance());
		NamedExpressionItem ex14 = new NamedExpressionItem("ts", mepEx14);

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
