package de.uniol.inf.is.odysseus.sports.sportsql.registry;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.Game;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

public class SportsQLParserRegistry {

	private static Map<StatisticType, Map<Game, Map<String, ISportsQLParser>>> sportsQLParserMap = Maps.newHashMap();

	public static void registerSportsQLParser(ISportsQLParser parser) {
		SportsQL sportsQLAnnotation = parser.getClass().getAnnotation(SportsQL.class);
		Preconditions.checkNotNull(sportsQLAnnotation,"No SportsQL annotation was set in "+ parser.getClass().getSimpleName());
		
		Game[] games = sportsQLAnnotation.games();
		Preconditions.checkArgument(games.length>0, "SportsQL annotation of " + parser.getClass().getSimpleName()+" must contain at least one game");
		
		StatisticType[] types = sportsQLAnnotation.types();
		Preconditions.checkArgument(types.length>0, "SportsQL annotation of " + parser.getClass().getSimpleName()+" must contain at least one statistic type");
		
		String name = sportsQLAnnotation.name().toLowerCase();
		Preconditions.checkArgument(name.trim().length()>0, "SportsQL annotation of  "  + parser.getClass().getSimpleName()+" must contain a statictic name");

		for (StatisticType statisticType : types) {
			Map<Game, Map<String, ISportsQLParser>> statisticTypeMap = sportsQLParserMap.get(statisticType);
			if (statisticTypeMap == null) {
				statisticTypeMap = Maps.newHashMap();
				sportsQLParserMap.put(statisticType, statisticTypeMap);
			}
			for (Game game : games) {
				Map<String, ISportsQLParser> gameMap = statisticTypeMap.get(game);
				if (gameMap == null) {
					gameMap = Maps.newHashMap();
					statisticTypeMap.put(game, gameMap);
				}
				if (gameMap.containsKey(name)) {
					throw new RuntimeException( "Duplicate statistic name was found in "+parser.getClass().getSimpleName() +" and "+statisticTypeMap.get(name).getClass().getSimpleName());
				}
				gameMap.put(name, parser);
			}
		}
	}

	public static void unregisterSportsQLParser(ISportsQLParser parser) {
		SportsQL sportsQLAnnotation = parser.getClass().getAnnotation(SportsQL.class);
		Game[] games = sportsQLAnnotation.games();
		StatisticType[] types = sportsQLAnnotation.types();
		String name = sportsQLAnnotation.name().toLowerCase();
		for (Game game : games) {
			for (StatisticType statisticType : types) {
				sportsQLParserMap.get(game).get(statisticType).remove(name);
			}
		}
	}

	public static ISportsQLParser getSportsQLParser(StatisticType type, Game game, String name) {
		ISportsQLParser parser = sportsQLParserMap.get(type).get(game).get(name.toLowerCase());
		Preconditions.checkNotNull(parser, "No SportsQLParser was found for statistic name " + name);		
		return parser;
	}
	
	public static ISportsQLParser getSportsQLParser(String type, String game, String name) {
		StatisticType statisticType;
		Game gameType;
		try {
			statisticType = StatisticType.valueOf(type.toLowerCase());
		}catch (IllegalArgumentException ex) {  
			throw new RuntimeException("No SportsQLParser was found for statistic type "+type);
		}
		try {
			gameType = Game.valueOf(game.toLowerCase());
		}catch (IllegalArgumentException ex) {  
			throw new RuntimeException("No SportsQLParser was found for game type "+type);
		}		
		return getSportsQLParser(statisticType, gameType, name);
	}
	
	public static ISportsQLParser getSportsQLParser(String sportsQL) {	
		return null;
	}
	
}
