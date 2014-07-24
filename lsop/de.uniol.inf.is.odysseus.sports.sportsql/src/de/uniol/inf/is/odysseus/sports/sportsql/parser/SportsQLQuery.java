package de.uniol.inf.is.odysseus.sports.sportsql.parser;

import java.io.Serializable;
import java.util.Map;

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
	private String statisticType;
	private String gameType;
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

	public String getStatisticType() {
		return statisticType;
	}

	public void setStatisticType(String statisticType) {
		this.statisticType = statisticType;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	
	
	
}
