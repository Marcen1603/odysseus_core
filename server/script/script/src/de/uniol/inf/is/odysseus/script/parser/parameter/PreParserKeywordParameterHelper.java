/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.script.parser.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class provides functionality for parsing and creating paramter strings used in PreParserKeywords (Odysseus Script)
 *
 * @author ChrisToenjesDeye
 *
 */
public class PreParserKeywordParameterHelper<T extends Enum<?> & IKeywordParameter> {

	private static final String DEFAULT_PATTERN_PARAMETER_VALUE = "((([a-zA-Z0-9_]+))[,])"
			+ "*(([a-zA-Z0-9_]+))";
	
	private static final String BLANK = " ";

	private Map<Integer, IKeywordParameter> positionMap;
	private Map<String, IKeywordParameter> nameMap;
	private String regexString;
	private int numberOfParameters;
	private int numberOfOptionalParameters;

	/**
	 * Construction of this helper is only possible via static methods
	 * 
	 * @param positionMap
	 * @param nameMap
	 * @param regexString
	 * @param numberOfParameters
	 * @param numberOfOptionalParameters
	 */
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

	/**
	 * Creates a new instance of this helper with custom regex
	 * 
	 * @param parameterClazz - the type of the parameters
	 * @param ParameterValueRegexString - a custom regular expression
	 * @return new instance of helper
	 */
	public static <T extends Enum<?> & IKeywordParameter> PreParserKeywordParameterHelper<T> newInstance(
			Class<T> parameterClazz, String parameterValueRegexString) {
		Map<Integer, IKeywordParameter> positionMap = new HashMap<Integer, IKeywordParameter>();
		Map<String, IKeywordParameter> nameMap = new HashMap<String, IKeywordParameter>();
		int numberOfParameters = 0;
		int numberOfOptionalParameters = 0;

		List<IKeywordParameter> keywordParameters = getParametersAsArray(parameterClazz);

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
				//Empty names are not allowed
				throw new IllegalArgumentException("Empty name is not allowed");
			}
			if (!nameMap.containsKey(keywordParameter.getName().toLowerCase())) {
				nameMap.put(keywordParameter.getName().toLowerCase(),
						keywordParameter);
			} else {
				// names need to be unique
				throw new IllegalArgumentException("Name needs to be unique.");
			}

			// count number of attributes and optional parameters
			numberOfParameters++;
			if (keywordParameter.isOptional()) {
				numberOfOptionalParameters++;
			}
		}

		String regexString = createRegexString(parameterValueRegexString);
		
		PreParserKeywordParameterHelper<T> instance = new PreParserKeywordParameterHelper<T>(
				positionMap, nameMap, regexString, numberOfParameters,
				numberOfOptionalParameters);
		instance.validateParametersAndRegex();

		return instance;
	}

	private static String createRegexString(String parameterValueRegexString) {
		String patternWithAttributeNames = "([(][a-zA-Z0-9_]+[=]"
				+ parameterValueRegexString
				+ "[)])([\\s][(][a-zA-Z0-9_]+[=]"
				+ parameterValueRegexString
				+ "[)])*";
		
		String patternWithoutAttributeNames = "("
				+ parameterValueRegexString + ")([\\s]" + parameterValueRegexString + ")*";
		
		String patternKeyword = patternWithAttributeNames
				+ "|" + patternWithoutAttributeNames;
		
		return patternKeyword;
	}

	/**
	 * Creates a new instance of this helper with custom regex
	 * @param parameterClazz - the type of the parameters
	 * @return new instance of helper
	 */
	public static <T extends Enum<?> & IKeywordParameter> PreParserKeywordParameterHelper<T> newInstance(
			Class<T> parameterClass) {
		return newInstance(parameterClass, DEFAULT_PATTERN_PARAMETER_VALUE);
	}

	/**
	 * Returns the parameters for a given type as an array
	 * 
	 * @param parameterClazz
	 * @return every parameter of this enum
	 */
	private static <T extends Enum<?> & IKeywordParameter> List<IKeywordParameter> getParametersAsArray(
			Class<T> parameterClazz) {
		List<IKeywordParameter> result = new ArrayList<IKeywordParameter>();

		T[] enumConstants = parameterClazz.getEnumConstants();

		if (enumConstants.length > 0) {
			for (int i = 0; i < enumConstants.length; i++) {
				result.add(enumConstants[i]);
			}
		}

		return result;
	}

	/**
	 * Validates if the given parameter class and elements are valid. Also the given regex is validated
	 * 
	 * @return true if valid, else false
	 * 
	 */
	private boolean validateParametersAndRegex() {
		// check regex
		Pattern.compile(this.regexString);

		// check if positions from zero to numberOfParamters-1 exists
		boolean firstOptionalParameterFound = false;
		for (int i = 0; i < this.numberOfParameters; i++) {
			// checks if the positions are correct
			if (!positionMap.containsKey(i)) {
				throw new IllegalArgumentException(
						"positions need to be from 0 to numberOfParameters-1");
			}
			// checks if a mandatory parameter exists after a optional parameter (this is not valid)
			if (!positionMap.get(i).isOptional() && firstOptionalParameterFound) {
				throw new IllegalArgumentException(
						"No mandatory parameters after optional parameters are allowed");
			}
			if (positionMap.get(i).isOptional()) {
				firstOptionalParameterFound = true;
			}
		}

		return false;
	}

	/**
	 * Creates a short version of the pattern for the given parameter (without name of parameter)
	 * Needs the correct order of parameters
	 * @return pattern
	 */
	public String getShortParameterPattern() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.numberOfParameters; i++) {
			if (positionMap.get(i) instanceof ICustomPatternKeywordParameter) {
				ICustomPatternKeywordParameter extendedParameter = (ICustomPatternKeywordParameter) positionMap
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

	/**
	 * creates the long pattern version for the given parameters (with name of parameters)
	 * @return pattern
	 */
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

	/**
	 * Parses the given parameter string and returns a map with parameter and value
	 * @param parameterString
	 * @return map with parameter and value
	 */
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

	/**
	 * creates a parameter string from the given map (name and value)
	 * @param parameters
	 * @return parameter string
	 */
	public String createParameterString(
			Map<T, String> parameters) {
		StringBuilder builder = new StringBuilder();
		for (IKeywordParameter parameter : parameters.keySet()) {
			builder.append("(");
			builder.append(parameter.getName());
			builder.append("=");
			builder.append(parameters.get(parameter));
			builder.append(")");
			builder.append(BLANK);
		}
		builder.append(System.lineSeparator());

		String resultString = builder.toString();
		validateParameterString(resultString);
		return resultString;
	}

	/**
	 * checks if all mandatory parameters are set
	 * @param result the result of parsing
	 */
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

	/**
	 * do the parsing if no parameter names exists
	 * @param splittedParameters
	 * @param result
	 */
	private void doParsingWithoutParameterNames(String[] splittedParameters,
			Map<IKeywordParameter, String> result) {
		for (int i = 0; i < splittedParameters.length; i++) {
			result.put(positionMap.get(i), splittedParameters[i]);
		}
	}

	/**
	 * do the parsing if parameter names exists
	 * @param splittedParameters
	 * @param result
	 */
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
				}
				IKeywordParameter keywordParameter = nameMap.get(key);
				if (result.containsKey(keywordParameter)) {
					throw new IllegalArgumentException(
							"Duplicate definition for parameter " + key);
				}
				result.put(nameMap.get(key), value);

			}
		}
	}

	/**
	 * Validates the given parameter string
	 * 
	 * @param parameterString
	 */
	public void validateParameterString(String parameterString) {
		parameterString = parameterString.trim();
		// empty string is not allowed
		if (parameterString == null || parameterString.isEmpty()) {
			throw new IllegalArgumentException(
					"Null or empty string is not allowed for parsing");
		}

		// need to match the regex
		if (!parameterString.matches(regexString)) {
			if (parameterString.contains("=")) {
				throw new IllegalArgumentException(
						"String does not match pattern. Pattern: "
								+ getLongParameterPattern());
			}
			throw new IllegalArgumentException(
					"String does not match pattern. Pattern: "
							+ getShortParameterPattern());
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
