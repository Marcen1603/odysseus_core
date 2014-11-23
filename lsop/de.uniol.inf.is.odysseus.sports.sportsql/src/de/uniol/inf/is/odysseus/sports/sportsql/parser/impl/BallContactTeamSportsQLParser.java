package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
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


@SportsQL(gameTypes = GameType.SOCCER, statisticTypes = { StatisticType.TEAM }, name = "ball_contact", parameters = {
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class BallContactTeamSportsQLParser implements ISportsQLParser{
	 
	@Override
	public ILogicalQuery parse(ISession session,SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {
		
		ArrayList<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		
		//Get all ball contacts of every player by using the global parser
		BallContactGlobalOutput ballcontactsGlobal = new BallContactGlobalOutput();	
		ILogicalOperator globalOutput = ballcontactsGlobal.getOutputOperator(OperatorBuildHelper.createGameStreamAO(session), sportsQL, allOperators);
		
		List<String> groupCount = new ArrayList<String>();
		groupCount.add("team_id");
		
		ILogicalOperator countOutput = OperatorBuildHelper.createAggregateAO("count", groupCount, "team_id", "ballContactCount", "Integer", globalOutput, 1);
		allOperators.add(countOutput);
		
		
		return OperatorBuildHelper.finishQuery(countOutput, allOperators, sportsQL.getDisplayName());
	}

}
