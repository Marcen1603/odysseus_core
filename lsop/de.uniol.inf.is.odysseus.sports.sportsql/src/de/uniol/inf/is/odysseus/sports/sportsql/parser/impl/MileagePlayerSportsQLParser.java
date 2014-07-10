package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.TEAM }, name = "mileageplayer")
public class MileagePlayerSportsQLParser implements ISportsQLParser {

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) {
		// Gson gson = new GsonBuilder().create();
		// SportsQLQuery query = gson.fromJson(sportsQL, SportsQLQuery.class);

		// Here I expect to have a nice SportsQL Object
		// for now, work with what's there until we know how to parse, ...

		// TODO: Get Source from DataDictionary -> Create an AccessPO

		// First Query
		// -----------

		// 1. MAP
		// Question: How to get the initial input schema?
		MapAO firstMap = OperatorBuildHelper.getMapAO(
				getExpressionsForFirstMap(), null);

		// 2. Correct timewindow
		// TODO: Use correct times
		// How can a Map be an ISource?
		SelectPO timeSelect = OperatorBuildHelper.getTimeSelect(10, 50, null);
		
		LogicalQuery query = new LogicalQuery();
		List<IPhysicalOperator> plan = new ArrayList<IPhysicalOperator>();
		
		//PhysicalQuery physicalQuery = new PhysicalQuery()

		// 3. Time Window
//		WindowAO firstWindow = new WindowAO();
//		firstWindow.setWindowType(WindowType.TIME);
//		TimeValueItem windowSize = new TimeValueItem(10, TimeUnit.MINUTES);
//		firstWindow.setWindowSize(windowSize);
//		firstWindow.subscribeToSource(timeSelect, 0, 0,
//				timeSelect.getOutputSchema());
		
		
		// Second Query
		// ------------

		// // 1. Select
		// SelectAO secondSelect = new SelectAO();
		// // TODO: Subscribe to correct source
		//
		// // Predicate
		// // TODO: Correct predicate
		// String secondSelectPredicateString = "sensorid = ${entity_id}";
		// SDFExpression secondSelectPredicateExpression = new SDFExpression(
		// secondSelectPredicateString, MEP.getInstance());
		// RelationalPredicate secondSelectPredicate = new RelationalPredicate(
		// secondSelectPredicateExpression);
		// // EqualsPredicate<Long> equalsPredicate = new
		// EqualsPredicate<Long>();
		// // EqualsPredicate<Long> equalsPredicate2 =
		// EqualsPredicate.getInstance();
		// // secondSelect.setPredicate(secondSelectPredicate);
		//
		// // Third Query
		// // -----------
		//
		// // 1. Enrich
		// // EnrichAO enrichAO = new EnrichAO();
		//
		// // Predicate
		// // TODO: Correct predicate
		// String thirdSelectPredicateString = "sensorid = sid";
		// SDFExpression thirdSelectPredicateExpression = new SDFExpression(
		// thirdSelectPredicateString, MEP.getInstance());
		// RelationalPredicate thirdSelectPredicate = new RelationalPredicate(
		// thirdSelectPredicateExpression);
		//
		//
		// //EnrichPO<IStreamObject<IMetaAttribute>, IMetaAttribute> po = new
		// EnrichPO<IStreamObject<IMetaAttribute>,
		// IMetaAttribute>(thirdSelectPredicate);
		//
		// // 2. Statemap
		// // How to use this? Where is the physical operator?
		// StateMapAO stateMap = new StateMapAO();
		//
		// // 3. Window
		// ElementWindowAO elemWindowAO = new ElementWindowAO();
		// // How to set number of tuples?
		// SlidingElementWindowTIPO<IStreamObject<ITimeInterval>> elemWindow =
		// new SlidingElementWindowTIPO<IStreamObject<ITimeInterval>>(
		// elemWindowAO);
		//
		// // 4. Aggregate
		//
		// // Set the sources
		// // EnrichPO<IStreamObject<M>, IMetaAttribute> enrichPO = new
		// // EnrichPO<IStreamObject<M>, IMetaAttribute>(predicate);

		return null;
	}

	private List<NamedExpressionItem> getExpressionsForFirstMap() {
		List<NamedExpressionItem> expressions = new ArrayList<NamedExpressionItem>();

		SDFExpression mepEx1 = new SDFExpression("sid", MEP.getInstance());
		NamedExpressionItem ex1 = new NamedExpressionItem("sid", mepEx1);

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
