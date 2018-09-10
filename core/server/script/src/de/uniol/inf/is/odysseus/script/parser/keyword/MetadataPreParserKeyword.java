package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class MetadataPreParserKeyword extends AbstractPreParserKeyword {

	public static final String METADATA = "METADATA";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		if( !MetadataRegistry.getNames().contains(parameter) ) {
			throw new OdysseusScriptException("Metadata '" + parameter + "' is not known.");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {

		IMetaAttribute m = MetadataRegistry.tryCreateMetadataInstance(parameter);
		
		List<Class<? extends IMetaAttribute>> classes = new ArrayList(Arrays.asList(m.getClasses()));

		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		Optional<ParameterTransformationConfiguration> optParam = getParameterTransformationConfiguration(settings);
		if (optParam.isPresent()) {
			optParam.get().getValue().addTypes(MetadataRegistry.toClassNames(classes));
		} else {
			ParameterTransformationConfiguration param = new ParameterTransformationConfiguration(new TransformationConfiguration(classes.toArray(new Class[0])));
			settings.add(param);
		}

		return null;
	}


	private static Optional<ParameterTransformationConfiguration> getParameterTransformationConfiguration(List<IQueryBuildSetting<?>> settings) {
		for (IQueryBuildSetting<?> s : settings) {
			if (s instanceof ParameterTransformationConfiguration) {
				return Optional.of((ParameterTransformationConfiguration) s);
			}
		}

		return Optional.absent();
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return MetadataRegistry.getNames();
	}

}
