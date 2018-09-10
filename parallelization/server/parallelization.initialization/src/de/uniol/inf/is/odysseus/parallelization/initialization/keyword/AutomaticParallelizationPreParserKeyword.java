/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.keyword;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * @author Dennis Nowak
 *
 */
public class AutomaticParallelizationPreParserKeyword extends AbstractPreParserKeyword {

	private static final Logger LOG = LoggerFactory.getLogger(AutomaticParallelizationPreParserKeyword.class);

	public static final String KEYWORD = "PARALLELIZATION_AUTOMATIC";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws OdysseusScriptException {
		// TODO Auto-generated method stub
		LOG.info("Keyword " + KEYWORD + " parsed.");
		return null;
	}

}
