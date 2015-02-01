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
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = GameType.ALL, statisticTypes = { StatisticType.GLOBAL }, name = "heatmap", parameters = {})
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
		SportsHeatMapAO heatmap = OperatorBuildHelper.createHetMapAO(projectAO);
		allOperators.add(heatmap);

		return OperatorBuildHelper.finishQuery(heatmap, allOperators, sportsQL.getDisplayName());
	}

}
