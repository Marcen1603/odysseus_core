package de.uniol.inf.is.odysseus.parallelization.interoperator.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParallelOperatorParameter extends Setting<Map<String, ParallelOperatorSettings>> implements IQueryBuildSetting<Map<String, ParallelOperatorSettings>>{

	protected ParallelOperatorParameter(
			Map<String, ParallelOperatorSettings> value) {
		super(value);
	}

	public ParallelOperatorParameter() {
		super(new HashMap<String, ParallelOperatorSettings>());
	}

	public ParallelOperatorSettings getSettingsForOperator(String operatorId){
		if (super.getValue().containsKey(operatorId.toLowerCase())){
			return super.getValue().get(operatorId.toLowerCase());
		}
		return null;
	}
	
	public boolean settingsForOperatorExists(String operatorId) {
		
		return super.getValue().containsKey(operatorId.toLowerCase());
	}

	public void addSettingsForOperator(String operatorId, ParallelOperatorSettings settings){
		if (!settingsForOperatorExists(operatorId)){
			super.getValue().put(operatorId.toLowerCase(), settings);			
		} else {
			throw new IllegalArgumentException("Multiple operator settings for id: "+operatorId);
		}
	}
	
	public void addSettingsForOperators(List<String> operatorIds, ParallelOperatorSettings settings){
		for (String operatorId : operatorIds) {
			addSettingsForOperator(operatorId, settings);
		}
	}

	public List<String> getOperatorIds() {
		List<String> operatorIds = new ArrayList<String>();
		operatorIds.addAll(super.getValue().keySet());
		return operatorIds;
	}
}
