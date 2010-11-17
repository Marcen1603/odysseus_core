package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.rcp.viewer.query.QueryBuildConfigurationRegistry;

public class BufferPlacementPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(Map<String, String> variables, String parameter)
			throws QueryTextParseException {
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		IBufferPlacementStrategy s = executor
				.getBufferPlacementStrategy(parameter);
		if (s == null) {
			throw new QueryTextParseException("No Buffer Placement Strategy "
					+ parameter + " loaded");
		}
	}

	@Override
	public void execute(Map<String, String> variables, String parameter)
			throws QueryTextParseException {
		List<IQueryBuildSetting<?>> config = QueryBuildConfigurationRegistry
				.getInstance().getQueryBuildConfiguration(
						variables.get("TRANSCFG"));
		Iterator<IQueryBuildSetting<?>> iter = config.iterator();
		IQueryBuildSetting<?> sett;
		while ((sett = iter.next()) != null) {
			if (sett instanceof ParameterBufferPlacementStrategy) {
				iter.remove();
				break;
			}
		}
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		IBufferPlacementStrategy s = executor
				.getBufferPlacementStrategy(parameter);
		config.add(new ParameterBufferPlacementStrategy(s));
	}

}
