package de.uniol.inf.is.odysseus.condition.messages;

public class WarningMessage {

	private int machineId;
	private int sensorId;
	private WarningLevel level;
	private String description;
	
	public WarningMessage(int machineId, int sensorId, WarningLevel level, String description) {
		super();
		this.machineId = machineId;
		this.sensorId = sensorId;
		this.level = level;
		this.description = description;
	}

	public int getMachineId() {
		return machineId;
	}

	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}

	public int getSensorId() {
		return sensorId;
	}

	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	public WarningLevel getLevel() {
		return level;
	}

	public void setLevel(WarningLevel level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
