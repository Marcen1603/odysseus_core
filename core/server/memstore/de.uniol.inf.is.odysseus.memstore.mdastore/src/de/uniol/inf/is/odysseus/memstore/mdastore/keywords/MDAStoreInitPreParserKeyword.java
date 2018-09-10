package de.uniol.inf.is.odysseus.memstore.mdastore.keywords;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.memstore.mdastore.commands.CreateMDAStoreCommand;
import de.uniol.inf.is.odysseus.memstore.mdastore.functions.MDADimFunction;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class MDAStoreInitPreParserKeyword extends AbstractPreParserKeyword {
	
	public static final String KEYWORD = "MDASTORE_INIT";

	private static final int MIN_ATTRIBUTE_COUNT = 2;

	private static final String PATTERN = "Name ValuesOfFirstDimension ... ValuesOfNthDimension";

	private static final String COLON_PATTERN = "SmallestValue:GreatestValue:NumValues";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("MDAStore name is missing!");
		}
		
		String[] splitted = parameter.trim().split(" ");
		if (splitted.length < MIN_ATTRIBUTE_COUNT) {
			throw new OdysseusScriptException(KEYWORD + " needs at least "
					+ MIN_ATTRIBUTE_COUNT + " attributes: " + PATTERN + "!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		String[] splitted = parameter.trim().split(" ");
		String name = splitted[0];

		List<List<Double>> values = Lists.newArrayList();
		for (int i = 1; i < splitted.length; i++) {
			values.add(getValuesSplittedByColon(splitted[i]));
		}
		List<IExecutorCommand> cmds = new LinkedList<>();
		CreateMDAStoreCommand cmd = new CreateMDAStoreCommand(caller,name,values);
		cmds.add(cmd);
		return cmds;
	}

	private static List<Double> getValuesSplittedByColon(String strValues)
			throws OdysseusScriptException {
		String[] strValues_intern = strValues.trim().split(":");
		if (strValues_intern.length != 3) {
			throw new OdysseusScriptException(strValues
					+ " is not a valid input for a MDA dimension: "
					+ COLON_PATTERN);
		}

		String strFirst = strValues_intern[0];
		String strLast = strValues_intern[1];
		String strCount = strValues_intern[2];

		double firstValue = Double.valueOf(strFirst);
		double lastValue = Double.valueOf(strLast);
		int count = Integer.valueOf(strCount);
		if((firstValue == lastValue && count != 1) || (firstValue != lastValue && count <= 1)) {
			throw new OdysseusScriptException("Illegal number of coordinates for given start and end!");
		}
		
		return MDADimFunction.createDimension(firstValue, lastValue, count);
	}

}