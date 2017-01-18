package de.uniol.inf.is.odysseus.admission.status.mep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class LoadSheddingPreParserKeyword extends AbstractPreParserKeyword {

	public static String LOADSHEDDING = "LOADSHEDDING";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		variables.put("LOADSHEDDINGENABLED", true);
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws OdysseusScriptException {
		LoggerFactory.getLogger(this.getClass()).info("Transform AdmissionTransformationHandler executed");
		
		variables.put("LOADSHEDDINGENABLED", true);
		
		String name = "AdmissionTransformationHandler";
		Pair<String, String> pair = new Pair<>("", "");
		List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
		list.add(pair);
		
		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		// Determine if ParameterTransformationConfiguration is already set
		for (IQueryBuildSetting<?> s : addSettings) {
			if (s instanceof PreTransformationHandlerParameter) {
				((PreTransformationHandlerParameter) s)
						.add(name, list);
				return null;
			}
		}
		
		PreTransformationHandlerParameter p = new PreTransformationHandlerParameter();
		addSettings.add(p);
		p.add(name, list);
		return null;
	}

}
