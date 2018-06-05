package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateKVStoreCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class CreateKVStorePreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "CREATE_KV_STORE";
	private static final int MIN_ATTRIBUTE_COUNT = 2;
	private static final String PATTERN = "NAME TYPE (PARAM_1_KEY=PARAM_1_VALUE) ... (PARAM_N_KEY=PARAM_N_VALUE)";

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
		String type = splitted[1];
		OptionMap options = new OptionMap();

		for (int i = 2; i < splitted.length; i++) {
			StringTokenizer token = new StringTokenizer(splitted[i], "(=)");
			if (token.hasMoreTokens()) {
				String param1 = token.nextToken();
				if (token.hasMoreTokens()) {
					String param2 = token.nextToken();
					options.setOption(param1, param2);
				}else{
					throw new OdysseusScriptException("Could not parse parameter "+splitted[i]+". Must be (KEY=VALUE)");
				}
			}else{
				throw new OdysseusScriptException("Could not parse parameter "+splitted[i]+". Must be (KEY=VALUE)");
			}
		}

		List<IExecutorCommand> cmds = new LinkedList<>();
		cmds.add(new CreateKVStoreCommand(name, type, options, caller));
		return cmds;
	}


}
