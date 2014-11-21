package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.parser.keyword;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class SmartDeviceKeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		
		keywords.put(SmartDeviceSensorNamePreParserKeyword.KEYWORD, SmartDeviceSensorNamePreParserKeyword.class);
		
		keywords.put(SmartDeviceAddSensorNamePreParserKeyword.KEYWORD, SmartDeviceAddSensorNamePreParserKeyword.class);
		keywords.put(SmartDeviceSensorRawSourceNamePreParserKeyword.KEYWORD, SmartDeviceSensorRawSourceNamePreParserKeyword.class);

		keywords.put(SmartDeviceActivityInterpreterSourceNamePreParserKeyword.KEYWORD, SmartDeviceActivityInterpreterSourceNamePreParserKeyword.class);
		keywords.put(SmartDeviceAddActivityInterpreterNamePreParserKeyword.KEYWORD, SmartDeviceAddActivityInterpreterNamePreParserKeyword.class);
		
		keywords.put(SmartDeviceLogicRuleNamePreParserKeyword.KEYWORD, SmartDeviceLogicRuleNamePreParserKeyword.class);
		
		keywords.put(SmartDeviceActorNamePreParserKeyword.KEYWORD, SmartDeviceActorNamePreParserKeyword.class);
		
		
		
		return keywords;
	}

}
