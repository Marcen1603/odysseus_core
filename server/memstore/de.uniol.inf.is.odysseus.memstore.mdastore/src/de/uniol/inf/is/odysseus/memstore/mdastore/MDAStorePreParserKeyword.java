package de.uniol.inf.is.odysseus.memstore.mdastore;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class MDAStorePreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "MDASTORE_INIT";

	private static final int MIN_ATTRIBUTE_COUNT = 3;

	private static final String PATTERN = "Name ClassName ValuesOfFirstDimension ... ValuesOfNthDimension";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("MDAStore name is missing!");
		}

		String[] splitted = parameter.trim().split(" ");
		if (splitted.length < MIN_ATTRIBUTE_COUNT) {
			throw new OdysseusScriptException(KEYWORD + " needs at least "
					+ MIN_ATTRIBUTE_COUNT + " attributes: " + PATTERN + "!");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {
		String[] splitted = parameter.trim().split(" ");
		String name = splitted[0];
		MDAStore<?> store = MDAStoreManager.create(name);

		String className = splitted[1];
		Class<?> clz = null;
		try {
			clz = Class.forName(className);
		} catch (Throwable e) {
			throw new OdysseusScriptException(className
					+ " is not full qualified name of a comparable class!");
		}

		List values = Lists.newArrayList();
		for (int i = 2; i < splitted.length; i++) {
			String[] splitted_intern = splitted[i].trim().split(",");
			List values_intern = Lists.newArrayList();
			for (int j = 0; j < splitted_intern.length; j++) {
				try {
					if (Number.class.isAssignableFrom(clz)) {
						values_intern.add(clz.cast(Double.valueOf(
								splitted_intern[j]).doubleValue()));
					} else {
						values_intern.add(clz.cast(splitted_intern[j]));
					}
				} catch (Throwable e) {
					throw new OdysseusScriptException("Could not cast "
							+ splitted_intern[j] + " to " + className);
				}
			}
			values.add(values_intern);
		}

		store.initialize(values);
		return null;
	}

}