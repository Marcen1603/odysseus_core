package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
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
	public ILogicalQuery parse(ISession session,SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {

		// We need this list to initialize all operators
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		// ---------------------------
		// Access to necessary streams
		// ---------------------------

		// 1. Game-Stream
		StreamAO soccerGameAccessAO = OperatorBuildHelper.createGameStreamAO(session);
		allOperators.add(soccerGameAccessAO);

		// Time parameter
		SelectAO timeSelect = OperatorBuildHelper.createTimeMapAndSelect(
				SportsQLParameterHelper.getTimeParameter(sportsQL),
				soccerGameAccessAO);

		// Space parameter
		SelectAO spaceSelect = OperatorBuildHelper.createSpaceSelect(
				SportsQLParameterHelper.getSpaceParameter(sportsQL), false,
				timeSelect);

		List<String> attributes = new ArrayList<String>();
		attributes.add("x");
		attributes.add("y");
		attributes.add("entity_id");
		attributes.add("team_id");

		ProjectAO projectAO = OperatorBuildHelper.createProjectAO(attributes,
				spaceSelect);
		allOperators.add(projectAO);
		
	
		return OperatorBuildHelper.finishQuery(projectAO, allOperators,
				sportsQL.getDisplayName());
	}

}
