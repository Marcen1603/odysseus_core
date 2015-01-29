package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.soccer;

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
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;
/**
 * Parser for SportsQL:
 * Query: Ball contacts of players.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
 * {
	    "displayName":"Ball_Contact_Player",
	    "statisticType":"PLAYER",
	    "gameType":"SOCCER",
	    "name":"ball_contact"
 * }
 * @author Thomas Pr�nie, Marc Wilken
 *
 */
@SportsQL(
		gameTypes = GameType.SOCCER, 
		statisticTypes = { StatisticType.PLAYER },
		name = "ball_contact",
		parameters = { 
				@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false)			
				}
		)
public class BallContactPlayerSportsQLParser implements ISportsQLParser {
	
	private final int dumpAtValueCount = 1;

	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException, MissingDDCEntryException {
		
	ArrayList<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		
		//Get all ball contacts of every player by using the global parser
		BallContactGlobalOutput ballcontactsGlobal = new BallContactGlobalOutput();	
		ILogicalOperator globalOutput = ballcontactsGlobal.getOutputOperator(OperatorBuildHelper.createGameStreamAO(session), sportsQL, allOperators);
		
		List<String> groupCount = new ArrayList<String>();
		groupCount.add("entity_id");
		groupCount.add("team_id");
		
		ILogicalOperator countOutput = OperatorBuildHelper.createAggregateAO("count", groupCount, "entity_id", "ballContactCount", "Integer", globalOutput, dumpAtValueCount);
		allOperators.add(countOutput);		
		
		return OperatorBuildHelper.finishQuery(countOutput, allOperators, sportsQL.getDisplayName());
	}

}
