package de.uniol.inf.is.odysseus.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.args.marshaller.BooleanMarshaller;
import de.uniol.inf.is.odysseus.args.marshaller.CharacterMarshaller;
import de.uniol.inf.is.odysseus.args.marshaller.IParameterMarshaller;
import de.uniol.inf.is.odysseus.args.marshaller.NumberMarshaller;
import de.uniol.inf.is.odysseus.args.marshaller.StringMarshaller;

/**
 * @author Jonas Jacobi
 */
public class Args {
	public static enum REQUIREMENT {
		MANDATORY, OPTIONAL
	};

	private Map<String, IParameterMarshaller> marshaller;
	private Set<String> mandatoryParameters;
	private Map<String, String> helpTexts;

	public Args() {
		this.marshaller = new HashMap<String, IParameterMarshaller>();
		this.mandatoryParameters = new HashSet<String>();
		this.helpTexts = new TreeMap<String, String>();
	}

	public void addParameter(String name, IParameterMarshaller marshaller,
			REQUIREMENT requirement) {
		addParameter(name, marshaller, requirement, " - no help available");
	}

	public void addParameter(String name, IParameterMarshaller marshaller,
			REQUIREMENT requirement, String helptext) {
		ensureParameterIsNew(name);

		this.marshaller.put(name, marshaller);
		if (requirement == REQUIREMENT.MANDATORY) {
			mandatoryParameters.add(name);
		}

		this.helpTexts.put(name, helptext);
	}

	private void ensureParameterIsNew(String name) {
		if (this.marshaller.containsKey(name)) {
			throw new IllegalArgumentException(
					"multiple definition of the same parameter: " + name);
		}
	}

	public void addBoolean(String name) {
		addParameter(name, new BooleanMarshaller(), REQUIREMENT.OPTIONAL);
	}

	public void addString(String name, REQUIREMENT requirement) {
		addParameter(name, new StringMarshaller(), requirement);
	}

	public void addCharacter(String name, REQUIREMENT requirement) {
		addParameter(name, new CharacterMarshaller(), requirement);
	}

	public void addInteger(String name, REQUIREMENT requirement) {
		addParameter(name, new NumberMarshaller<Integer>(Integer.class),
				requirement);
	}

	public void addFloat(String name, REQUIREMENT requirement) {
		addParameter(name, new NumberMarshaller<Float>(Float.class),
				requirement);
	}

	public void addDouble(String name, REQUIREMENT requirement) {
		addParameter(name, new NumberMarshaller<Double>(Double.class),
				requirement);
	}

	public void addLong(String name, REQUIREMENT requirement) {
		addParameter(name, new NumberMarshaller<Long>(Long.class), requirement);
	}

	public void addBoolean(String name, String help) {
		addParameter(name, new BooleanMarshaller(), REQUIREMENT.OPTIONAL, help);
	}

	public void addString(String name, REQUIREMENT requirement, String help) {
		addParameter(name, new StringMarshaller(), requirement, help);
	}

	public void addCharacter(String name, REQUIREMENT requirement, String help) {
		addParameter(name, new CharacterMarshaller(), requirement, help);
	}

	public void addInteger(String name, REQUIREMENT requirement, String help) {
		addParameter(name, new NumberMarshaller<Integer>(Integer.class),
				requirement, help);
	}

	public void addFloat(String name, REQUIREMENT requirement, String help) {
		addParameter(name, new NumberMarshaller<Float>(Float.class),
				requirement, help);
	}

	public void addDouble(String name, REQUIREMENT requirement, String help) {
		addParameter(name, new NumberMarshaller<Double>(Double.class),
				requirement, help);
	}

	public void addLong(String name, REQUIREMENT requirement, String help) {
		addParameter(name, new NumberMarshaller<Long>(Long.class), requirement,
				help);
	}

	public void parse(String[] args) throws ArgsException {
		List<String> argsList = Arrays.asList(args);
		ListIterator<String> it = argsList.listIterator();
		Set<String> tmpMandatory = new HashSet<String>(this.mandatoryParameters);
		while (it.hasNext()) {
			String curParameter = it.next();
			tmpMandatory.remove(curParameter);
			IParameterMarshaller curMarshaller = marshaller.get(curParameter);
			if (curMarshaller == null) {
				throw new ArgsException("undefined parameter: " + curParameter);
			}
			curMarshaller.parse(it);
		}
		if (!tmpMandatory.isEmpty()) {
			throw new ArgsException(
					"missing one or more mandatory parameters: "
							+ tmpMandatory.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String parameterName) {
		if (!marshaller.containsKey(parameterName)) {
			throw new IllegalArgumentException("no such parameter: "
					+ parameterName);
		}

		IParameterMarshaller cur = marshaller.get(parameterName);
		return (T) cur.getValue();
	}

	public String getString(String parameterName) {
		return get(parameterName);
	}

	public Character getChararacter(String parameterName) {
		return get(parameterName);
	}

	public Integer getInteger(String parameterName) {
		return get(parameterName);
	}

	public Double getDouble(String parameterName) {
		return get(parameterName);
	}

	public String getFloat(String parameterName) {
		return get(parameterName);
	}

	public String getBoolean(String parameterName) {
		return get(parameterName);
	}

	public boolean hasParameter(String parameterName) {
		return this.marshaller.containsKey(parameterName)
				&& this.marshaller.get(parameterName).getValue() != null;
	}

	public Long getLong(String parameterName) {
		return get(parameterName);
	}

	public String getHelpText() {
		StringBuilder mandatoryBuilder = new StringBuilder();
		mandatoryBuilder.append("mandatory parameters:\n");
		StringBuilder optionalBuilder = new StringBuilder();
		optionalBuilder.append("optional parameters:\n");
		for (Map.Entry<String, String> help : helpTexts.entrySet()) {
			StringBuilder builder = optionalBuilder;
			if (mandatoryParameters.contains(help.getKey())) {
				builder = mandatoryBuilder;
			}
			builder.append("  ");
			builder.append(help.getKey());
			builder.append(" ");
			builder.append(help.getValue());
			builder.append('\n');
		}
		if (mandatoryParameters.isEmpty()) {
			return optionalBuilder.toString();
		} else {
			if (isAllParametersMandatory()) {
				return mandatoryBuilder.toString();
			} else {
				mandatoryBuilder.append('\n');
				mandatoryBuilder.append(optionalBuilder);
				return mandatoryBuilder.toString();
			}
		}
	}

	private boolean isAllParametersMandatory() {
		return mandatoryParameters.size() == this.helpTexts.size();
	}
}
