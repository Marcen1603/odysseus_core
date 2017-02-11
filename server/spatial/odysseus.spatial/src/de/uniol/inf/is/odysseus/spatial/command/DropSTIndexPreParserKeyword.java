package de.uniol.inf.is.odysseus.spatial.command;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class DropSTIndexPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "DROP_ST_INDEX";
	private static final int MIN_ATTRIBUTE_COUNT = 1;
	private static final String PATTERN = "NAME";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException(
					KEYWORD + " needs at least " + MIN_ATTRIBUTE_COUNT + " attributes: " + PATTERN + "!");
		}

		String[] splitted = parameter.trim().split(" ");
		if (splitted.length < MIN_ATTRIBUTE_COUNT) {
			throw new OdysseusScriptException(
					KEYWORD + " needs at least " + MIN_ATTRIBUTE_COUNT + " attributes: " + PATTERN + "!");
		}

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws OdysseusScriptException {
		String[] splitted = parameter.trim().split(" ");
		String name = splitted[0];

		List<IExecutorCommand> cmds = new LinkedList<>();
		cmds.add(new DropSTIndexCommand(name, caller));
		return cmds;
	}

}
