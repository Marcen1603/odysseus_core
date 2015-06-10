package de.uniol.inf.is.odysseus.multithreaded.parameter;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class MultithreadedOperatorParameter extends Setting<List<String>> implements IQueryBuildSetting<List<String>>{

	private List<String> operatorIds = new ArrayList<String>();
	
	public MultithreadedOperatorParameter(List<String> operatorIds) {
		super(null);
		if(operatorIds != null){
			this.operatorIds.addAll(operatorIds);
		}
	}
	
	public void addOperatorId(String operatorId){
		this.operatorIds.add(operatorId);
	}
	
	public void addOperatorId(List<String> operatorIds){
		this.operatorIds.addAll(operatorIds);
	}
	
	public List<String> getOperatorIds(){
		return operatorIds;
	}

}
