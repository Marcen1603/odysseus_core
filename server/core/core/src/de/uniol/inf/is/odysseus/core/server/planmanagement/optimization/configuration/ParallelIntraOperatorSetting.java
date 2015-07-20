package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParallelIntraOperatorSetting extends Setting<Map<String, String>>implements IQueryBuildSetting<Map<String, String>> {

	public enum ParallelIntraOperatorSettingKeys{
		DEGREE
	}
	
	protected ParallelIntraOperatorSetting(Map<String, String> value) {
		super(value);
	}

	public ParallelIntraOperatorSetting() {
		super(new HashMap<String, String>());
	}

	public void addKeyValuePair(ParallelIntraOperatorSettingKeys key, String value){
		super.getValue().put(key.name(), value);
	}
	
	public String getValue(ParallelIntraOperatorSettingKeys key){
		return super.getValue().get(key.name());
	}
}
