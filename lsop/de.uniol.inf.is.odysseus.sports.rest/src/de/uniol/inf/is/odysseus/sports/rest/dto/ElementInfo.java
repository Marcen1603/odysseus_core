package de.uniol.inf.is.odysseus.sports.rest.dto;

import java.io.Serializable;

/**
 * This class represents a data transfer object for different kind of elements on the gamefield
 * @author Thomas
 *
 */
public class ElementInfo implements Serializable {
	
	private static final long serialVersionUID = -5349759487022901177L;
	private Integer sensor_id;
	private Integer entity_id;
	private String entity;
	private String remark;
	private Integer team_id;
	
	public ElementInfo(Integer sensor_id, Integer entity_id, String entity, String remark, Integer team_id) {
		
		this.sensor_id = sensor_id;
		this.entity_id = entity_id;
		this.entity = entity;
		this.remark = remark;
		this.team_id = team_id;
		
	}

	public Integer getSensor_id() {
		return sensor_id;
	}

	public void setSensor_id(int sensor_id) {
		this.sensor_id = sensor_id;
	}

	public Integer getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(int entity_id) {
		this.entity_id = entity_id;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Integer team_id) {
		this.team_id = team_id;
	}
}
