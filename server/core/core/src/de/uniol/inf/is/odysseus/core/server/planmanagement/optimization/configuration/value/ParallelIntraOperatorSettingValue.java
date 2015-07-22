package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value;

import java.util.HashMap;
import java.util.Map;

public class ParallelIntraOperatorSettingValue {
	private int globalDegree = 0;
	private int globalBuffersize = 0;

	// maps unique operator id to individual degree
	private Map<String, ParallelIntraOperatorSettingValueElement> individualSettings = new HashMap<String, ParallelIntraOperatorSettingValueElement>();

	public ParallelIntraOperatorSettingValue() {
	}

	public ParallelIntraOperatorSettingValue(int globalDegree, int globalBuffersize) {
		this.globalDegree = globalDegree;
		this.globalBuffersize = globalBuffersize;
	}

	public int getGlobalDegree() {
		return globalDegree;
	}

	public void setGlobalDegree(int globalDegree) {
		this.globalDegree = globalDegree;
	}

	public boolean hasIndividualSettings() {
		return !individualSettings.isEmpty();
	}

	public boolean hasIndividualSettingsForOperator(String operatorId) {
		if (operatorId == null){
			return false;
		}
		return individualSettings.containsKey(operatorId);
	}

	public ParallelIntraOperatorSettingValueElement getIndividualSettings(String operatorId) {
		if (operatorId == null){
			return null;
		}
		return individualSettings.get(operatorId);
	}

	public void addIndividualSettings(String operatorId, ParallelIntraOperatorSettingValueElement individualSetting) {
		if (individualSettings.containsKey(operatorId)) {
			throw new IllegalArgumentException(
					"Duplicate definition for operator with id " + operatorId);
		}
		individualSettings.put(operatorId, individualSetting);
	}

	public int getGlobalBuffersize() {
		return globalBuffersize;
	}

	public void setGlobalBuffersize(int globalBuffersize) {
		this.globalBuffersize = globalBuffersize;
	}

}
