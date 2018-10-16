package de.uniol.inf.is.odysseus.script.keyword.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class PreTransformationHandlerPreParserKeyword extends
		AbstractPreParserKeyword {

	public static final String KEYWORD = "PRETRANSFORM";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {

		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException(
					"PreTransformationHandler's name is missing");
		}

		String[] splitted = parameter.trim().split(" ");
		String preTransformationHandlerName = splitted[0].trim();

		if (!executor.hasPreTransformationHandler(
				preTransformationHandlerName)) {
			throw new OdysseusScriptException(
					"Query distribution preprocessor name '"
							+ preTransformationHandlerName
							+ "' is not registered!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {

		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);

		String[] splitted = parameter.trim().split(" ");
		List<Pair<String, String>> parameters = generateParameterList(splitted);

		Optional<PreTransformationHandlerParameter> optParameter = getQueryBuildSettingOfType(
				settings, PreTransformationHandlerParameter.class);
		if (optParameter.isPresent()) {
			optParameter.get().add(splitted[0], parameters);
		} else {
			PreTransformationHandlerParameter param = new PreTransformationHandlerParameter();
			param.add(splitted[0], parameters);
			settings.add(param);
		}

		return null;
	}

	private static List<Pair<String, String>> generateParameterList(
			String[] splitted) {
		if (splitted.length == 1) {
			return Lists.newArrayList();
		}

		List<Pair<String, String>> parameters = Lists.newArrayList();
		for (int i = 1; i < splitted.length; i++) {
			String[] splittedParameter = splitted[i].trim().split(":");
			if (splittedParameter.length >= 1) {
				String key = "";
				if (splittedParameter.length > 1) {
					key = splittedParameter[0];
				}
				String value = splittedParameter[1];

				Pair<String, String> newPair = new Pair<String, String>();
				newPair.setE1(key);
				newPair.setE2(value);

				parameters.add(newPair);

			}
		}

		return parameters;
	}

	@SuppressWarnings("unchecked")
	public static <T extends IQueryBuildSetting<?>> Optional<T> getQueryBuildSettingOfType(
			List<IQueryBuildSetting<?>> settings, Class<T> parameterClass) {
		// Preconditions.checkNotNull(settings,
			//	"List of settings must not be null!");
		// Preconditions.checkNotNull(parameterClass,
//				"Class of parameter to find must not be null!");

		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(parameterClass)) {
				return (Optional<T>) Optional.of(setting);
			}
		}
		return Optional.absent();
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		// TODO
//		try {
//			return getServerExecutor().getPreTransformationHandlerNames();
//		} catch (OdysseusScriptException e) {
//			return Lists.newArrayList();
//		}
		return Lists.newArrayList();
	}

}
