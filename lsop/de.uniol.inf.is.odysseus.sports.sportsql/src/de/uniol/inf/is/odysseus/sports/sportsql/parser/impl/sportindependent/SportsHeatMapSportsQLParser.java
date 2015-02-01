package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.sportindependent;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.logicaloperator.SportsHeatMapAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLIntegerParameter;

@SportsQL(gameTypes = GameType.ALL, statisticTypes = { StatisticType.GLOBAL }, name = "heatmap", parameters = {
		@SportsQLParameter(name = "reduceLoadFactor", parameterClass = SportsQLIntegerParameter.class, mandatory = false),
		@SportsQLParameter(name = "numTilesHorizontal", parameterClass = SportsQLIntegerParameter.class, mandatory = false),
		@SportsQLParameter(name = "numTilesVertical", parameterClass = SportsQLIntegerParameter.class, mandatory = false) })
public class SportsHeatMapSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL) throws SportsQLParseException,
			NumberFormatException, MissingDDCEntryException {

		// We need this list to initialize all operators
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		// ---------------------------
		// Access to necessary streams
		// ---------------------------

		// 1. Game-Stream
		StreamAO soccerGameAccessAO = OperatorBuildHelper.createGameStreamAO(session);
		allOperators.add(soccerGameAccessAO);

		// 2. Project
		List<String> attributes = new ArrayList<String>();
		attributes.add("entity_id");
		attributes.add("x");
		attributes.add("y");

		ProjectAO projectAO = OperatorBuildHelper.createProjectAO(attributes, soccerGameAccessAO);
		allOperators.add(projectAO);

		// 3. HeatMap
		SportsQLIntegerParameter reduceLoadFactor = SportsQLParameterHelper.getIntegerParameter(sportsQL,
				"reduceLoadFactor");
		int reduceFactor = SportsHeatMapAO.REDUCE_LOAD_STANDARD_FACTOR;
		if (reduceLoadFactor != null)
			reduceFactor = reduceLoadFactor.getValue();

		SportsQLIntegerParameter numTilesHorizontal = SportsQLParameterHelper.getIntegerParameter(sportsQL,
				"numTilesHorizontal");
		int numTilesX = SportsHeatMapAO.NUM_TIMES_HORIZONTAL_STANDARD;
		if (numTilesHorizontal != null)
			numTilesX = numTilesHorizontal.getValue();

		SportsQLIntegerParameter numTilesVertical = SportsQLParameterHelper.getIntegerParameter(sportsQL,
				"numTilesVertical");
		int numTilesY = SportsHeatMapAO.NUM_TIMES_VERTICAL_STANDARD;
		if (numTilesVertical != null)
			numTilesY = numTilesVertical.getValue();

		SportsHeatMapAO heatmap = OperatorBuildHelper.createHeatMapAO(projectAO, reduceFactor, numTilesX, numTilesY);
		allOperators.add(heatmap);

		return OperatorBuildHelper.finishQuery(heatmap, allOperators, sportsQL.getDisplayName());
	}

}
