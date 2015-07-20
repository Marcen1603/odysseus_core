package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value;

import java.util.HashMap;
import java.util.Map;

public class ParallelIntraOperatorSettingValue {
	private int globalDegree = 0;

	// maps unique operator id to individual degree
	private Map<String, Integer> individualDegrees = new HashMap<String, Integer>();

	public ParallelIntraOperatorSettingValue() {
	}

	public ParallelIntraOperatorSettingValue(int globalDegree) {
		this.setGlobalDegree(globalDegree);
	}

	public int getGlobalDegree() {
		return globalDegree;
	}

	public void setGlobalDegree(int globalDegree) {
		this.globalDegree = globalDegree;
	}

	public boolean hasIndividualDegrees() {
		return !individualDegrees.isEmpty();
	}

	public boolean hasIndividualDegreeForOperator(String operatorId) {
		if (operatorId == null){
			return false;
		}
		return individualDegrees.containsKey(operatorId);
	}

	public int getIndividualDegree(String operatorId) {
		if (operatorId == null){
			return -1;
		}
		return individualDegrees.get(operatorId);
	}

	public void addIndividualDegree(String operatorId, int degree) {
		if (individualDegrees.containsKey(operatorId)) {
			throw new IllegalArgumentException(
					"Duplicate definition for operator with id " + operatorId);
		}
		individualDegrees.put(operatorId, degree);
	}

}
