package de.uniol.inf.is.odysseus.sports.sportsql.parser;

import java.io.Serializable;
import java.util.Map;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;


/**
 * This Class represents a simple SportsQL query.
 * 
 * Attributes {@link SportsQLQuery#entityId} and {@link SportsQLQuery#parameters} are optional. 
 * 
 * @author Thore Stratmann
 *
 */
public class SportsQLQuery implements Serializable {

	private static final long serialVersionUID = 3381915483410973297L;
	
	private String name;
	private StatisticType statisticType;
	private GameType gameType;
	private long entityId;
	private Map<String, ISportsQLParameter> parameters;
	
	
	

	

	public Map<String, ISportsQLParameter> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, ISportsQLParameter> parameters) {
		this.parameters = parameters;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StatisticType getStatisticType() {
		return statisticType;
	}

	public void setStatisticType(StatisticType statisticType) {
		this.statisticType = statisticType;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
	
	
	
}
