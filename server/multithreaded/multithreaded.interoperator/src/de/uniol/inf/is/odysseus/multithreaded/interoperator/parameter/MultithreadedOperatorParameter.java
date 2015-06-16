package de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class MultithreadedOperatorParameter extends Setting<Map<String, MultithreadedOperatorSettings>> implements IQueryBuildSetting<Map<String, MultithreadedOperatorSettings>>{

	protected MultithreadedOperatorParameter(
			Map<String, MultithreadedOperatorSettings> value) {
		super(value);
	}

	public MultithreadedOperatorParameter() {
		super(new HashMap<String, MultithreadedOperatorSettings>());
	}

	public MultithreadedOperatorSettings getSettingsForOperator(String operatorId){
		if (super.getValue().containsKey(operatorId.toLowerCase())){
			return super.getValue().get(operatorId.toLowerCase());
		}
		return null;
	}
	
	public boolean settingsForOperatorExists(String operatorId) {
		
		return super.getValue().containsKey(operatorId.toLowerCase());
	}

	public void addSettingsForOperator(String operatorId, MultithreadedOperatorSettings settings){
		if (!settingsForOperatorExists(operatorId)){
			super.getValue().put(operatorId, settings);			
		} else {
			throw new IllegalArgumentException("Multiple operator settings for id: "+operatorId);
		}
	}
	
	public void addSettingsForOperators(List<String> operatorIds, MultithreadedOperatorSettings settings){
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
