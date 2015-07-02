package de.uniol.inf.is.odysseus.parallelization.parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PreParserKeywordParameterHelper {

	private static final String PATTERN_PAIRS = "((([a-zA-Z0-9_]+))[,])"
			+ "*(([a-zA-Z0-9_]+))";
	private static final String PATTERN_WITH_ATTRIBUTESNAMES = "([(][a-zA-Z0-9_]+[=]"
			+ PATTERN_PAIRS
			+ "[)])([\\s][(][a-zA-Z0-9_]+[=]"
			+ PATTERN_PAIRS
			+ "[)])*";
	private static final String PATTERN_WITHOUT_ATTRIBUTENAMES = "("
			+ PATTERN_PAIRS + ")([\\s]" + PATTERN_PAIRS + ")*";
	private static final String PATTERN_KEYWORD = PATTERN_WITH_ATTRIBUTESNAMES
			+ "|" + PATTERN_WITHOUT_ATTRIBUTENAMES;

	private Map<Integer, IKeywordParameter> positionMap;
	private Map<String, IKeywordParameter> nameMap;
	private String regexString;
	private int numberOfParameters;
	private int numberOfOptionalParameters;

	private PreParserKeywordParameterHelper(
			Map<Integer, IKeywordParameter> positionMap,
			Map<String, IKeywordParameter> nameMap, String regexString,
			int numberOfParameters, int numberOfOptionalParameters) {
		this.positionMap = positionMap;
		this.nameMap = nameMap;
		this.regexString = regexString;
		this.numberOfParameters = numberOfParameters;
		this.numberOfOptionalParameters = numberOfOptionalParameters;
	}

	public static PreParserKeywordParameterHelper newInstance(
			List<IKeywordParameter> keywordParameters, String regexString) {
		Map<Integer, IKeywordParameter> positionMap = new HashMap<Integer, IKeywordParameter>();
		Map<String, IKeywordParameter> nameMap = new HashMap<String, IKeywordParameter>();
		int numberOfParameters = 0;
		int numberOfOptionalParameters = 0;

		for (IKeywordParameter keywordParameter : keywordParameters) {
			if (!positionMap.containsKey(keywordParameter.getPosition())) {
				positionMap.put(keywordParameter.getPosition(),
						keywordParameter);
			} else {
				throw new IllegalArgumentException(
						"Position needs to be unique.");
			}
			if (keywordParameter.getName() == null
					|| keywordParameter.getName().isEmpty()) {
				throw new IllegalArgumentException("Empty name is not allowed");
			}
			if (!nameMap.containsKey(keywordParameter.getName().toLowerCase())) {
				nameMap.put(keywordParameter.getName().toLowerCase(),
						keywordParameter);
			} else {
				throw new IllegalArgumentException("Name needs to be unique.");
			}

			// count number of attributes and optional parameters
			numberOfParameters++;
			if (keywordParameter.isOptional()) {
				numberOfOptionalParameters++;
			}
		}

		PreParserKeywordParameterHelper instance = new PreParserKeywordParameterHelper(
				positionMap, nameMap, regexString, numberOfParameters,
				numberOfOptionalParameters);
		instance.validateParametersAndRegex();

		return instance;
	}

	public static PreParserKeywordParameterHelper newInstance(
			List<IKeywordParameter> keywordParameters) {
		return newInstance(keywordParameters, PATTERN_KEYWORD);
	}

	private boolean validateParametersAndRegex() {
		// check regex
		Pattern.compile(this.regexString);

		// check if positions from zero to numberOfParamters-1 exists
		boolean firstOptionalParameterFound = false;
		for (int i = 0; i < this.numberOfParameters; i++) {
			if (!positionMap.containsKey(i)) {
				throw new IllegalArgumentException(
						"positions need to be from 0 to numberOfParameters-1");
			} else {
				if (!positionMap.get(i).isOptional()
						&& firstOptionalParameterFound) {
					throw new IllegalArgumentException(
							"No mandatory parameters after optional parameters are allowed");
				}
				if (positionMap.get(i).isOptional()) {
					firstOptionalParameterFound = true;
				}
			}
		}

		return false;
	}

	public String getShortParameterPattern() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.numberOfParameters; i++) {
			if (positionMap.get(i) instanceof IExtendedKeywordParameter) {
				IExtendedKeywordParameter extendedParameter = (IExtendedKeywordParameter) positionMap
						.get(i);
				if (!extendedParameter.getValuePattern().isEmpty()) {
					builder.append(extendedParameter.getValuePattern());
					continue;
				}
			}
			builder.append("<value for ");
			builder.append(positionMap.get(i).getName());
			if (positionMap.get(i).isOptional()) {
				builder.append(" (optional)");
			}
			builder.append("> ");

		}
		return builder.toString();
	}

	public String getLongParameterPattern() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.numberOfParameters; i++) {
			builder.append(positionMap.get(i).getName());
			builder.append("=<value");
			if (positionMap.get(i).isOptional()) {
				builder.append(" (optional)");
			}
			builder.append("> ");
		}
		return builder.toString();
	}

	public Map<IKeywordParameter, String> parse(String parameterString) {
		validateParameterString(parameterString);

		Map<IKeywordParameter, String> result = new HashMap<IKeywordParameter, String>();

		String[] split = parameterString.trim().split(" ");
		if (split[0].contains("=")) {
			doParsingWithParameterNames(split, result);
		} else {
			doParsingWithoutParameterNames(split, result);
		}
		// we need to check if all mandatory parameters are set
		checkIfAllMandatoryParametersAreSet(result);

		return result;
	}

	private void checkIfAllMandatoryParametersAreSet(
			Map<IKeywordParameter, String> result) {
		for (IKeywordParameter keywordParameter : positionMap.values()) {
			if (!keywordParameter.isOptional()) {
				// if parameter is mandatory
				if (!result.containsKey(keywordParameter)) {
					// if this mandatory parameter does not exists
					throw new IllegalArgumentException("Mandatory parameter "
							+ keywordParameter.getName() + " is missing.");
				}
			}
		}
	}

	private void doParsingWithoutParameterNames(String[] splittedParameters,
			Map<IKeywordParameter, String> result) {
		for (int i = 0; i < splittedParameters.length; i++) {
			result.put(positionMap.get(i), splittedParameters[i]);
		}
	}

	private void doParsingWithParameterNames(String[] splittedParameters,
			Map<IKeywordParameter, String> result) {
		for (int i = 0; i < splittedParameters.length; i++) {
			// remove surrounding brackets
			String keyValueString = splittedParameters[i].substring(1,
					splittedParameters[i].length() - 1);

			// split key and value on =
			String[] keyValueArray = keyValueString.trim().split("=");
			if (keyValueArray.length == 2) {
				String key = keyValueArray[0].trim().toLowerCase();
				String value = keyValueArray[1].trim();
				if (!nameMap.containsKey(key)) {
					throw new IllegalArgumentException("Parameter " + key
							+ " does not exists. Pattern: "
							+ getLongParameterPattern());
				} else {
					IKeywordParameter keywordParameter = nameMap.get(key);
					if (result.containsKey(keywordParameter)) {
						throw new IllegalArgumentException(
								"Duplicate definition for parameter " + key);
					}
					result.put(nameMap.get(key), value);
				}
			}
		}
	}

	public void validateParameterString(String parameterString) {
		if (parameterString == null || parameterString.isEmpty()) {
			throw new IllegalArgumentException(
					"Null or empty string is not allowed for parsing");
		}

		if (!parameterString.matches(regexString)) {
			if (parameterString.contains("=")) {
				throw new IllegalArgumentException(
						"String does not match pattern. Pattern: "
								+ getLongParameterPattern());
			} else {
				throw new IllegalArgumentException(
						"String does not match pattern. Pattern: "
								+ getShortParameterPattern());
			}
		}

		String[] split = parameterString.trim().split(" ");

		StringBuilder builder = new StringBuilder();
		boolean numberOfParametersIncorrect = false;
		// too much parameters
		if (split.length > numberOfParameters) {
			builder.append("String contains too much parameters. Remove all not needed blanks. Pattern: ");
			numberOfParametersIncorrect = true;
		}
		// not enough parameters
		if (split.length < numberOfParameters - numberOfOptionalParameters) {
			builder.append("String contains not enough parameters. Pattern: ");
			numberOfParametersIncorrect = true;
		}
		// check which pattern is needed
		if (numberOfParametersIncorrect) {
			if (parameterString.contains("=")) {
				builder.append(getLongParameterPattern());
			} else {
				builder.append(getShortParameterPattern());
			}
			throw new IllegalArgumentException(builder.toString());
		}
	}
}
