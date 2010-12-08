package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;

public class UseRewritePreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		List<IQueryBuildSetting<?>> config = executor.getQueryBuildConfiguration((String)
						variables.get("TRANSCFG"));
		Iterator<IQueryBuildSetting<?>> iter = config.iterator();
		if (iter != null){
			while (iter.hasNext()) {
				IQueryBuildSetting<?> sett = iter.next();
				if (sett instanceof ParameterDoRewrite) {
					iter.remove();
					break;
				}
			}
			if ("TRUE".equals(parameter.toUpperCase())){
				config.add(ParameterDoRewrite.TRUE);
			}else{
				config.add(ParameterDoRewrite.FALSE);
			}
		}
		return null;
	}

}
