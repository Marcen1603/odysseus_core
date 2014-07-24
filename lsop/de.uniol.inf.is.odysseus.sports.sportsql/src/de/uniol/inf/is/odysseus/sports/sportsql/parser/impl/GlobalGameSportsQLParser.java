package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * This query transfers the positions of all entities.
 * 
 * @author Tobias Brandt
 * @since 24.07.2014
 *
 */
@SportsQL(gameTypes = GameType.SOCCER, statisticTypes = { StatisticType.GLOBAL }, name = "game", parameters = {
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class GlobalGameSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {

		// We need this list to initialize all operators
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		// ---------------------------
		// Access to necessary streams
		// ---------------------------

		// 1. Game-Stream
		String soccerGameSourceName = OperatorBuildHelper.MAIN_STREAM_NAME;
		AccessAO soccerGameAccessAO = OperatorBuildHelper
				.createAccessAO(soccerGameSourceName);
		allOperators.add(soccerGameAccessAO);

		// 2. Metadata-Stream
		String metadataSourceName = OperatorBuildHelper.METADATA_STREAM_NAME;
		AccessAO metadataAccessAO = OperatorBuildHelper
				.createAccessAO(metadataSourceName);
		allOperators.add(metadataAccessAO);
		
		// -----------------------
		// First part (TimeWindow)
		// -----------------------

		// 1. TimeWindow
		TimeWindowAO timeWindow = OperatorBuildHelper.createTimeWindowAO(1,
				"MILLISECONDS", soccerGameAccessAO);
		allOperators.add(timeWindow);

		// --------------------------------
		// Second part (Enrich and Project)
		// --------------------------------

		// 1. Enrich
		EnrichAO enrichAO = OperatorBuildHelper.createEnrichAO(
				"sensorid = sid", timeWindow, metadataAccessAO);
		allOperators.add(enrichAO);
		
		// 2. Project
		List<String> attributes = new ArrayList<String>();
		attributes.add("x");
		attributes.add("y");
		attributes.add("entity_id");
		attributes.add("entity");
		attributes.add("remark");
		attributes.add("team_id");
		
		ProjectAO projectAO = OperatorBuildHelper.createProjectAO(attributes, enrichAO); 
		allOperators.add(projectAO);
		
		return OperatorBuildHelper.finishQuery(projectAO, allOperators, sportsQL.getName());
	}

}
