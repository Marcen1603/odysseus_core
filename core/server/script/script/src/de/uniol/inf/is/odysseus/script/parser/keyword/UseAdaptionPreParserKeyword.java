/**
 * 
 */
package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoPlanAdaption;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoPlanGeneration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * @author Merlin Wasmann
 *
 */
public class UseAdaptionPreParserKeyword extends AbstractPreParserKeyword {

	public static final String DOADAPT = "DOADAPT";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		if("TRUE".equals(parameter.toUpperCase())) {
			addSettings.add(ParameterDoPlanAdaption.TRUE);
			addSettings.add(ParameterDoPlanGeneration.TRUE);
		} else {
			addSettings.add(ParameterDoPlanAdaption.FALSE);
		}
		return null;
	}
	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return Arrays.asList("true", "false");
	}

}
