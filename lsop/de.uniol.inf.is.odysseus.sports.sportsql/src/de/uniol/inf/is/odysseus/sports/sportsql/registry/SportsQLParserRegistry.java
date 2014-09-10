package de.uniol.inf.is.odysseus.sports.sportsql.registry;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;

/**
 * This class represents a registry for @link ISportsQLParser. SportsQL parsers
 * are registered and unregistered by OSGI service binding mechanism.
 * 
 * @author Thore Stratmann
 * 
 */
public class SportsQLParserRegistry {

	private static Map<StatisticType, Map<GameType, Map<String, ISportsQLParser>>> sportsQLParserMap = Maps
			.newHashMap();

	public static void registerSportsQLParser(ISportsQLParser parser) {
		SportsQL sportsQLAnnotation = parser.getClass().getAnnotation(
				SportsQL.class);
		Preconditions.checkNotNull(sportsQLAnnotation,
				"No SportsQL annotation was set in "
						+ parser.getClass().getSimpleName());

		GameType[] games = sportsQLAnnotation.gameTypes();
		Preconditions.checkArgument(games.length > 0, "SportsQL annotation of "
				+ parser.getClass().getSimpleName()
				+ " must contain at least one game");

		StatisticType[] types = sportsQLAnnotation.statisticTypes();
		Preconditions.checkArgument(types.length > 0, "SportsQL annotation of "
				+ parser.getClass().getSimpleName()
				+ " must contain at least one statistic type");

		String name = sportsQLAnnotation.name().trim().toUpperCase();
		Preconditions.checkArgument(name.length() > 0,
				"SportsQL annotation of  " + parser.getClass().getSimpleName()
						+ " must contain a statictic name");

		for (StatisticType statisticType : types) {
			Map<GameType, Map<String, ISportsQLParser>> statisticTypeMap = sportsQLParserMap
					.get(statisticType);
			if (statisticTypeMap == null) {
				statisticTypeMap = Maps.newHashMap();
				sportsQLParserMap.put(statisticType, statisticTypeMap);
			}
			for (GameType game : games) {
				Map<String, ISportsQLParser> gameMap = statisticTypeMap
						.get(game);
				if (gameMap == null) {
					gameMap = Maps.newHashMap();
					statisticTypeMap.put(game, gameMap);
				}
				if (gameMap.containsKey(name)) {
					throw new RuntimeException(
							"Duplicate statistic name was found in "
									+ parser.getClass().getSimpleName()
									+ " and "
									+ statisticTypeMap.get(name).getClass()
											.getSimpleName());
				}
				gameMap.put(name, parser);
			}
		}
	}

	public static void unregisterSportsQLParser(ISportsQLParser parser) {
		SportsQL sportsQLAnnotation = parser.getClass().getAnnotation(
				SportsQL.class);
		GameType[] games = sportsQLAnnotation.gameTypes();
		StatisticType[] types = sportsQLAnnotation.statisticTypes();
		String name = sportsQLAnnotation.name().trim().toUpperCase();
		for (StatisticType statisticType : types) {
			for (GameType game : games) {
				sportsQLParserMap.get(statisticType).get(game).remove(name);
			}
		}
	}

	public static ISportsQLParser getSportsQLParser(StatisticType type,
			GameType game, String name) {
		ISportsQLParser parser = sportsQLParserMap.get(type).get(game)
				.get(name.trim().toUpperCase());
		Preconditions.checkNotNull(parser,
				"No SportsQLParser was found for statistic name " + name);
		return parser;
	}

	public static ISportsQLParser getSportsQLParser(String type, String game,
			String name) {
		StatisticType statisticType;
		GameType gameType;
		try {
			statisticType = StatisticType.valueOf(type.trim().toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(
					"No SportsQLParser was found for statistic type " + type);
		}
		try {
			gameType = GameType.valueOf(game.trim().toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(
					"No SportsQLParser was found for game type " + type);
		}
		return getSportsQLParser(statisticType, gameType, name);
	}

	public static ISportsQLParser getSportsQLParser(SportsQLQuery sportsQL) {
		return getSportsQLParser(sportsQL.getStatisticType(),
				sportsQL.getGameType(), sportsQL.getName());
	}

	public static SportsQLQuery createSportsQLQuery(String sportsQL) {		
		JSONObject jsonParameters = null;
		String type = null;
		String game = null;
		String name = null;
		Long entityId = null;
		Map<String, ISportsQLParameter> parametersMap = new HashMap<String, ISportsQLParameter>();

		try {
			JSONObject obj = new JSONObject(sportsQL);
			type = obj.getString("statisticType");		
		} catch (JSONException e) {
			throw new RuntimeException("Statistic type is missing in sportsql query");
		}
		
		try {
			JSONObject obj = new JSONObject(sportsQL);
			game = obj.getString("gameType");		
		} catch (JSONException e) {
			throw new RuntimeException("Game type is missing in sportsql query");
		}
		
		try {
			JSONObject obj = new JSONObject(sportsQL);
			name = obj.getString("name");		
		} catch (JSONException e) {
			throw new RuntimeException("Name of statstic is missing in sportsql query");
		}
		
		try {
			JSONObject obj = new JSONObject(sportsQL);
			entityId = obj.getLong("entityId");
		} catch (JSONException e) {
			// optional
		}
		
		try {
			JSONObject obj = new JSONObject(sportsQL);
			jsonParameters = obj.getJSONObject("parameters");			
		} catch (JSONException e) {
			// optional
		}
		
		ISportsQLParser parser = getSportsQLParser(type, game, name);
		SportsQL sportsQLAnnotation = parser.getClass().getAnnotation(	SportsQL.class);
		SportsQLParameter[] sportsQLParameters = sportsQLAnnotation.parameters();

		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < sportsQLParameters.length; i++) {
			String parameterName = sportsQLParameters[i].name();
			boolean mandatory = sportsQLParameters[i].mandatory();
			Object valueObj = null;
			if (jsonParameters != null) {
				try {
					valueObj = jsonParameters.get(parameterName);
				} catch (JSONException e) {					
					// value not found, is ok if the parameter is optional
				}
			}
			if (mandatory && valueObj == null) {
				throw new RuntimeException("Parameter " + parameterName	+ " is missing");
			} else if (valueObj == null) {
				continue;
			}
			Class<? extends ISportsQLParameter> parameterClass = sportsQLParameters[i].parameterClass();
			String value = null;
			if (valueObj instanceof JSONObject) {
				value = valueObj.toString();
			} else if(valueObj instanceof String){
				value = "{ \"value\" : \"" + valueObj.toString() + "\" }";
			} else {
				value = "{ \"value\" : " + valueObj.toString() + " }";
			}
			ISportsQLParameter parameter = null;
			try {
				parameter = mapper.readValue(value, parameterClass);
			} catch (Exception e) {
				throw new RuntimeException("Value of parameter "+ parameterName + " is not valid");
			}
			parametersMap.put(parameterName, parameter);
		}

		
		SportsQLQuery query = new SportsQLQuery();
		query.setStatisticType(StatisticType.valueOf(type.trim().toUpperCase()));
		query.setGameType(GameType.valueOf(game.trim().toUpperCase()));
		query.setName(name.trim().toUpperCase());
		if (entityId != null) {
			query.setEntityId(entityId);
		}
		query.setParameters(parametersMap);
		return query;
	}
}