package de.uniol.inf.is.odysseus.sports.rest.dto;

import com.google.common.base.Optional;

/**
 * This class represents a data transfer object for different kind of elements on the gamefield
 * @author Thomas
 *
 */
public class ElementInfo {
	
	private int sensor_id;
	private int entity_id;
	private String entity;
	private Optional<String> remark;
	private Optional<Integer> team_id;
	
	public ElementInfo(int sensor_id, int entity_id, String entity, Optional<String> remark, Optional<Integer> team_id) {
		
		this.sensor_id = sensor_id;
		this.entity_id = entity_id;
		this.entity = entity;
		this.remark = remark;
		this.team_id = team_id;
		
	}

	public int getSensor_id() {
		return sensor_id;
	}

	public void setSensor_id(int sensor_id) {
		this.sensor_id = sensor_id;
	}

	public int getEntity_id() {
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

	public Optional<String> getRemark() {
		return remark;
	}

	public void setRemark(Optional<String> remark) {
		this.remark = remark;
	}

	public Optional<Integer> getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Optional<Integer> team_id) {
		this.team_id = team_id;
	}
}
