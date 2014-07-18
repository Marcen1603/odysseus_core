package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = GameType.SOCCER, statisticTypes = { StatisticType.PLAYER }, name = "ball_contact")
public class BallContactPlayerSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {
		
		/* 
		 * Perhaps set the select at the bottom of the query plan for performance issues.
		 * In this case we select the specific player from the query plan of the BallContactGlobalSportsQlParser
		 */
		
		
		ArrayList<String> predicates = new ArrayList<String>();
		ArrayList<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		
		//Get all ball contacts of every player by using the global parser
		BallContactGlobalSportsQLParser ballcontacts_global = new BallContactGlobalSportsQLParser();	
		SportsQLQuery sql = new SportsQLQuery();
		
		sql.setGameType(sportsQL.getGameType());
		sql.setName(sportsQL.getName());
		sql.setStatisticType("global"); //to use global stats
			
		ILogicalQuery global_query = ballcontacts_global.parse(sql);
		
		//new source 
		ILogicalOperator source = global_query.getLogicalPlan();
		
		//Select player
		predicates.add("entity_id = "+ sportsQL.getEntityId());
		
		SelectAO single_player = OperatorBuildHelper.createSelectAO(predicates, source);	
		allOperators.add(single_player);
		
		return OperatorBuildHelper.finishQuery(single_player, allOperators, sportsQL.getName());
	}

}
