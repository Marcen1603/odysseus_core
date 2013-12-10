package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class MetadataPreParserKeyword extends AbstractPreParserKeyword {

	public static final String METADATA = "METADATA";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {

		IMetaAttribute m = null;
		try {
			m = MetadataRegistry
					.getMetadataTypeByName(parameter).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Class<? extends IMetaAttribute>> classes = new ArrayList(Arrays.asList(m.getClasses()));
		Set<String> classNames = new TreeSet<String>();
		for (Class<?> c:classes){
			classNames.add(c.getName());
		}
		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		// Determine if ParameterTransformationConfiguration is already set
		for (IQueryBuildSetting<?> s : addSettings) {
			if (s instanceof ParameterTransformationConfiguration) {
				((ParameterTransformationConfiguration) s)
						.getValue().addTypes(classNames);
				return null;
			}
		}

		// Not found, create new
		@SuppressWarnings("unchecked")
		ParameterTransformationConfiguration p = new ParameterTransformationConfiguration(
				new TransformationConfiguration(classes.toArray(new Class[1])));
		addSettings.add(p);

		return null;
	}

}
