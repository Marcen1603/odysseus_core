package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptConfigSetting;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class ConfigPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "CONFIG";

	private String key;
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		String[] params = getSimpleParameters(parameter);
		
		if( params.length != 2 ) {
			throw new OdysseusScriptException("Config must be a key-value-pair");
		}
		
		key = params[0].trim();
		String value = params[1].trim();
		
		Optional<IOdysseusScriptConfigSetting> optSetting = OdysseusScriptConfigRegistry.getInstance().getConfigSetting(key);
		if( !optSetting.isPresent() ) {
			throw new OdysseusScriptException("Config setting '" + key + "' not known");
		}
		
		IOdysseusScriptConfigSetting setting = optSetting.get();
		List<IQueryBuildSetting<?>> transformationSettings = getAdditionalTransformationSettings(variables);
		if( !setting.isValidValue(value, variables, transformationSettings, caller)) {
			throw new OdysseusScriptException("Config setting '" + key + "' is not valid for setting '" + value + "'");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		String[] params = parameter.split(" |\t");
		
		key = params[0].trim();
		String value = params[1].trim();
		List<IQueryBuildSetting<?>> transformationSettings = getAdditionalTransformationSettings(variables);
		
		OdysseusScriptConfigRegistry.getInstance().getConfigSetting(key).get().set(value, variables, transformationSettings, caller);
		
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return OdysseusScriptConfigRegistry.getInstance().getConfigSettingNames();
	}
}
