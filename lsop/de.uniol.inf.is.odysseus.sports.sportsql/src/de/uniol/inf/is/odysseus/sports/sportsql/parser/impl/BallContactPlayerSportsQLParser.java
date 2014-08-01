package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
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
 * Query: Ball contacts of specific player.
 * 
 * SportsQL:
 * 
 * Example Query:
 * 
 * {
 * "statisticType": "player",
 *   "gameType": "soccer", 
 *   "name": "ball_contact",
 *   "entity_id": "4"
 *   "parameters": {
 *       "time": {
 *           "start": 10753295594424116,
 *           "end" : 9999999999999999,   
 *       }
 *    "space": {
 *      	"startx":-50,
 *          "starty":-33960
 *      	"endx":52489
 *     		"endy":33965
 *   }
 *  }
 *}
 * 
 * @author Thomas Prünie
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

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {
		
		/* 
		 * Perhaps set the select at the bottom of the query plan for performance issues.
		 * In this case we select the specific player from the query plan of the BallContactGlobalSportsQlParser
		 */
		
		ArrayList<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		
		//Get all ball contacts of every player by using the global parser
		BallContactGlobalSportsQLParser ballcontactsGlobal = new BallContactGlobalSportsQLParser();	
		ILogicalQuery globalQuery = ballcontactsGlobal.parse(sportsQL);
		
		//new source 
		ILogicalOperator source = globalQuery.getLogicalPlan();
		
		//Select player
		SelectAO single_player = OperatorBuildHelper.createEntityIDSelect(sportsQL.getEntityId(), source);
		allOperators.add(single_player);
		
		return OperatorBuildHelper.finishQuery(single_player, allOperators, sportsQL.getName());
	}

}
