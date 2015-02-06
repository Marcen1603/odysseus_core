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

public class MDAStoreInitPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "MDASTORE_INIT";

	private static final int MIN_ATTRIBUTE_COUNT = 3;

	private static final String PATTERN = "Name ClassName ValuesOfFirstDimension ... ValuesOfNthDimension";

	private static final String COLON_PATTERN = "SmallestValue:GreatestValue:NumValues";

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
			if (splitted[i].contains(":")) {
				values.add(getValuesSplittedByColon(splitted[i], clz));
			} else {
				values.add(getValuesSplittedByComma(splitted[i], clz));
			}
		}

		store.initialize(values);
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List getValuesSplittedByComma(String strValues, Class<?> clz)
			throws OdysseusScriptException {
		String[] splitted_intern = strValues.trim().split(",");
		List values = Lists.newArrayList();
		for (int i = 0; i < splitted_intern.length; i++) {
			try {
				if (Number.class.isAssignableFrom(clz)) {
					values.add(clz.cast(Double.valueOf(splitted_intern[i])
							.doubleValue()));
				} else {
					values.add(clz.cast(splitted_intern[i]));
				}
			} catch (Throwable e) {
				throw new OdysseusScriptException("Could not cast "
						+ splitted_intern[i] + " to " + clz.getCanonicalName());
			}
		}
		return values;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List getValuesSplittedByColon(String strValues, Class<?> clz)
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

		List values = Lists.newArrayList();
		double firstValue = Double.valueOf(strFirst);
		double lastValue = Double.valueOf(strLast);
		int count = Integer.valueOf(strCount);
		if((firstValue == lastValue && count != 1) || (firstValue != lastValue && count <= 1)) {
			throw new OdysseusScriptException("Illegal number of coordinates for given start and end!");
		}
		
		double increasingValue = (lastValue-firstValue) / (count-1);	// first and last value included
		if(firstValue == lastValue) {
			try {
				values.add(clz.cast(Double.valueOf(firstValue)));
			} catch (Throwable e) {
				throw new OdysseusScriptException("Could not cast "
						+ firstValue + " to " + clz.getCanonicalName());
			}
		} else if(firstValue < lastValue) {
			for(double value = firstValue; value <= lastValue; value += increasingValue) {
				try {
					values.add(clz.cast(Double.valueOf(value)));
				} catch (Throwable e) {
					throw new OdysseusScriptException("Could not cast "
							+ value + " to " + clz.getCanonicalName());
				}
			}
		} else {
			for(double value = firstValue; value >= lastValue; value += increasingValue) {
				try {
					values.add(clz.cast(Double.valueOf(value)));
				} catch (Throwable e) {
					throw new OdysseusScriptException("Could not cast "
							+ value + " to " + clz.getCanonicalName());
				}
			}
		}
		return values;
	}

}