package de.uniol.inf.is.odysseus.sports.sportsql.registry;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

public class SportsQLParserRegistry {

	private static Map<StatisticType, Map<GameType, Map<String, ISportsQLParser>>> sportsQLParserMap = Maps.newHashMap();

	public static void registerSportsQLParser(ISportsQLParser parser) {
		SportsQL sportsQLAnnotation = parser.getClass().getAnnotation(SportsQL.class);
		Preconditions.checkNotNull(sportsQLAnnotation,"No SportsQL annotation was set in "+ parser.getClass().getSimpleName());
		
		GameType[] games = sportsQLAnnotation.gameTypes();
		Preconditions.checkArgument(games.length>0, "SportsQL annotation of " + parser.getClass().getSimpleName()+" must contain at least one game");
		
		StatisticType[] types = sportsQLAnnotation.statisticTypes();
		Preconditions.checkArgument(types.length>0, "SportsQL annotation of " + parser.getClass().getSimpleName()+" must contain at least one statistic type");
		
		String name = sportsQLAnnotation.name().trim().toUpperCase();
		Preconditions.checkArgument(name.length()>0, "SportsQL annotation of  "  + parser.getClass().getSimpleName()+" must contain a statictic name");

		for (StatisticType statisticType : types) {
			Map<GameType, Map<String, ISportsQLParser>> statisticTypeMap = sportsQLParserMap.get(statisticType);
			if (statisticTypeMap == null) {
				statisticTypeMap = Maps.newHashMap();
				sportsQLParserMap.put(statisticType, statisticTypeMap);
			}
			for (GameType game : games) {
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
		GameType[] games = sportsQLAnnotation.gameTypes();
		StatisticType[] types = sportsQLAnnotation.statisticTypes();
		String name = sportsQLAnnotation.name().toUpperCase();
		for (GameType game : games) {
			for (StatisticType statisticType : types) {
				sportsQLParserMap.get(game).get(statisticType).remove(name);
			}
		}
	}

	public static ISportsQLParser getSportsQLParser(StatisticType type, GameType game, String name) {
		ISportsQLParser parser = sportsQLParserMap.get(type).get(game).get(name.trim().toUpperCase());
		Preconditions.checkNotNull(parser, "No SportsQLParser was found for statistic name " + name);		
		return parser;
	}
	
	public static ISportsQLParser getSportsQLParser(String type, String game, String name) {
		StatisticType statisticType;
		GameType gameType;
		try {
			statisticType = StatisticType.valueOf(type.trim().toUpperCase());
		}catch (IllegalArgumentException ex) {  
			throw new RuntimeException("No SportsQLParser was found for statistic type "+type);
		}
		try {
			gameType = GameType.valueOf(game.trim().toUpperCase());
		}catch (IllegalArgumentException ex) {  
			throw new RuntimeException("No SportsQLParser was found for game type "+type);
		}		
		return getSportsQLParser(statisticType, gameType, name);
	}
	
	public static ISportsQLParser getSportsQLParser(SportsQLQuery sportsQL) {
		return getSportsQLParser(sportsQL.getStatisticType(), sportsQL.getGameType(), sportsQL.getName());
	}
}
