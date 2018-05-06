/**
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
package de.uniol.inf.is.odysseus.aggregation.functions.factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Cornelius Ludmann
 *
 */
public class AggregationFunctionParseOptionsHelper {

	public static final String FUNCTION_NAME = "FUNCTION";
	public static final String OUTPUT_ATTRIBUTES = "OUTPUT_ATTRIBUTES";
	public static final String INPUT_ATTRIBUTES = "INPUT_ATTRIBUTES";

	private AggregationFunctionParseOptionsHelper() {
	}

	/**
	 * Returns the function name or {@code null}.
	 * 
	 * @param parameters
	 * @return
	 */
	public static String getFunctionName(final Map<String, Object> parameters) {
		return getFunctionParameterAsString(parameters, FUNCTION_NAME);
	}

	/**
	 * Returns the indices of the input inputAttributeIndices that should be
	 * used by this function. This function returns {@code null}, iff all
	 * incoming inputAttributeIndices should be used as input and uses input
	 * port 0.
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter can not be parsed.
	 * 
	 * @param parameters
	 *            The parameters map.
	 * @param attributeResolver
	 *            The attribute resolver.
	 * @param inputPort
	 *            The input port.
	 * @return The indices of the input inputAttributeIndices.
	 */
	public static int[] getInputAttributeIndices(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver) {
		return getInputAttributeIndices(parameters, attributeResolver, 0, true);
	}

	/**
	 * Returns the indices of the input inputAttributeIndices that should be
	 * used by this function.
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter can not be parsed.
	 * 
	 * @param parameters
	 *            The parameters map.
	 * @param attributeResolver
	 *            The attribute resolver.
	 * @param inputPort
	 *            The input port.
	 * @param returnNullIfAllAttributesInOrder
	 *            If set to true, this method returns {@code null} if all
	 *            inputAttributeIndices should be used as input in the original
	 *            order. If set to false, this method returns the indices of all
	 *            inputAttributeIndices if all inputAttributeIndices should be
	 *            used as input.
	 * @return The indices of the input inputAttributeIndices.
	 */
	public static int[] getInputAttributeIndices(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver, final int inputPort,
			final boolean returnNullIfAllAttributesInOrder) {
		final Object inputAttr = getFunctionParameter(parameters, INPUT_ATTRIBUTES);

		final SDFSchema inputSchema = attributeResolver.getSchema().get(inputPort);

		if (inputAttr == null || (inputAttr instanceof String && inputAttr.equals("*"))) {
			// all inputAttributeIndices
			if (returnNullIfAllAttributesInOrder) {
				return null;
			} else {
				final int[] attrIdx = new int[inputSchema.size()];
				for (int i = 0; i < attrIdx.length; ++i) {
					attrIdx[i] = i;
				}
				return attrIdx;
			}
		}

		if (inputAttr instanceof String) {
			if (returnNullIfAllAttributesInOrder && inputSchema.size() == 1
					&& inputSchema.findAttributeIndex((String) inputAttr) == 0) {
				return null;
			}
			final int[] attrIdx = new int[1];
			attrIdx[0] = inputSchema.findAttributeIndex((String) inputAttr);
			if (attrIdx[0] == -1) {
				throw new IllegalArgumentException(
						"Input attribute '" + inputAttr + "' not found in schema: " + inputSchema.toString());
			}
			return attrIdx;
		}

		if (inputAttr instanceof Collection) {
			final int[] attrIdx = new int[((Collection<?>) inputAttr).size()];
			int i = 0;
			for (final Iterator<?> iter = ((Collection<?>) inputAttr).iterator(); iter.hasNext(); ++i) {
				final Object n = iter.next();
				if (!(n instanceof String)) {
					throw new IllegalArgumentException("Could not parse parameter as attribute: " + n);
				} else {
					attrIdx[i] = inputSchema.findAttributeIndex((String) n);
					if (attrIdx[i] == -1) {
						throw new IllegalArgumentException(
								"Input attribute '" + n + "' not found in schema: " + inputSchema.toString());
					}
				}
			}
			if (returnNullIfAllAttributesInOrder && attrIdx.length == inputSchema.size() && isOrdered(attrIdx)) {
				return null;
			}
			return attrIdx;
		}

		throw new IllegalArgumentException("Could not parse attribute " + INPUT_ATTRIBUTES + ".");
	}

	public static int[] getAttributeIndices(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver, final String key) {
		final Object inputAttr = getFunctionParameter(parameters, key);
		final SDFSchema inputSchema = attributeResolver.getSchema().get(0);

		if (inputAttr instanceof String) {
			final int[] attrIdx = new int[1];
			attrIdx[0] = inputSchema.findAttributeIndex((String) inputAttr);
			if (attrIdx[0] == -1) {
				throw new IllegalArgumentException(
						"Input attribute '" + inputAttr + "' not found in schema: " + inputSchema.toString());
			}
			return attrIdx;
		}

		if (inputAttr instanceof Collection) {
			final int[] attrIdx = new int[((Collection<?>) inputAttr).size()];
			int i = 0;
			for (final Iterator<?> iter = ((Collection<?>) inputAttr).iterator(); iter.hasNext(); ++i) {
				final Object n = iter.next();
				if (!(n instanceof String)) {
					throw new IllegalArgumentException("Could not parse parameter as attribute: " + n);
				} else {
					attrIdx[i] = inputSchema.findAttributeIndex((String) n);
					if (attrIdx[i] == -1) {
						throw new IllegalArgumentException(
								"Input attribute '" + n + "' not found in schema: " + inputSchema.toString());
					}
				}
			}
			return attrIdx;
		}

		throw new IllegalArgumentException("Could not parse attribute " + key + ".");
	}

	/**
	 * @param restr
	 * @return
	 */
	private static boolean isOrdered(final int[] restr) {
		final int[] copy = Arrays.copyOf(restr, restr.length);
		Arrays.sort(copy);
		for (int i = 0; i < restr.length; ++i) {
			if (restr[i] != copy[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param parameters
	 * @param attributeResolver
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String[] getOutputAttributeNames(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver) {
		final Object outAttr = getFunctionParameter(parameters, OUTPUT_ATTRIBUTES);
		if (outAttr != null) {
			if (outAttr instanceof String) {
				return new String[] { (String) outAttr };
			} else if (outAttr instanceof Collection) {
				return ((Collection<String>) outAttr).toArray(new String[0]);
			}
			throw new IllegalArgumentException("Could not parse attribute " + OUTPUT_ATTRIBUTES + ".");
		} else {
			final int[] inputAttr = getInputAttributeIndices(parameters, attributeResolver, 0, false);
			final SDFSchema inputSchema = attributeResolver.getSchema().get(0);

			final String[] outNames = new String[inputAttr.length];
			for (int i = 0; i < inputAttr.length; ++i) {
				outNames[i] = getFunctionName(parameters) + "_"
						+ inputSchema.getAttribute(inputAttr[i]).getAttributeName();
			}
			return outNames;
		}
	}

	/**
	 * Returns the parameter that is associated with the key as {@code String}
	 * or {@code null}, if the parameter is missing}.
	 * 
	 * @throws IllegalArgumentException
	 *             when value is not of type {@code String}.
	 * 
	 * @param parameters
	 * @param key
	 * @return
	 */
	public static String getFunctionParameterAsString(final Map<String, Object> parameters, final String key) {
		if (key == null) {
			return null;
		}
		final Object value = getFunctionParameter(parameters, key);
		if (value == null || value instanceof String) {
			return (String) value;
		} else {
			throw new IllegalArgumentException("Could not parse parameter " + key
					+ ". Value should be of type String but is: " + value.getClass());
		}
	}

	/**
	 * 
	 * The value should have the following structure:
	 * 
	 * <pre>
	 * [[name1, value1],[name2, value2]]
	 * </pre>
	 * 
	 * the return map is
	 * 
	 * <pre>
	 * name1=>value1 name2=>value2
	 * </pre>
	 * 
	 * key and value are string.
	 * 
	 * @throws IllegalArgumentException
	 *             when value is not of type {@code String}.
	 * 
	 * @param parameters
	 *            the map as string-representation
	 * @param key
	 *            key of the parameter in FUNCTION-parameter
	 * @return
	 */
	public static Map<String, String> getFunctionParameterAsMap(final Map<String, Object> parameters,
			final String key) {

		if (key == null) {
			return null;
		}

		final Object value = getFunctionParameter(parameters, key);
		if (value instanceof String) {
			String[] mapValues = ((String) value).split("],");

			Map<String, String> functionParameterMap = new HashMap<>();

			for (int indexMapValue = 0; indexMapValue < mapValues.length; indexMapValue++) {
				String mapValue = mapValues[indexMapValue];

				mapValue = mapValue.replace("[", "");
				mapValue = mapValue.replace("]", "");
				mapValue = mapValue.replace("\"", "");

				String[] keyValue = mapValue.split(",");
				functionParameterMap.put(keyValue[0], keyValue[1]);

			}

			return functionParameterMap;

		}

		throw new IllegalArgumentException("Could not parse parameter " + key
				+ ". Value should be of type String in structure [[],[]] but is: " + value.getClass());
	}

	/**
	 * Returns the parameter that is associated with the key as {@code String}
	 * or {@code defaultValue}, if the parameter is missing.
	 * 
	 * @throws IllegalArgumentException
	 *             when value is not of type {@code Boolean}.
	 * 
	 * @param parameters
	 * @param key
	 * @param defaultValue
	 *            The default value when key is {@code null} or value is
	 *            {@code null}.
	 * @return
	 */
	public static boolean getFunctionParameterAsBoolean(final Map<String, Object> parameters, final String key,
			final boolean defaultValue) {
		if (key == null) {
			return defaultValue;
		}
		final Object value = getFunctionParameter(parameters, key);
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}

		throw new IllegalArgumentException(
				"Could not parse parameter " + key + ". Value should be of type Boolean but is: " + value.getClass());
	}

	/**
	 * Returns the parameter that is associated with the key or {@code null}.
	 * 
	 * @param parameters
	 * @param key
	 * @return
	 */
	public static Object getFunctionParameter(final Map<String, Object> parameters, final String key) {
		if (key == null) {
			return null;
		}
		Object value = parameters.get(key);
		if (value == null) {
			value = parameters.get(key.toUpperCase());
		}
		if (value == null) {
			value = parameters.get(key.toLowerCase());
		}
		return value;
	}

	/**
	 * Return true, iff the number of input parameters are equal to the number
	 * of output parameters.
	 * 
	 * @param paramters
	 *            The parameters map.
	 * @param attributeResolver
	 *            The attribute resolver.
	 * @return True, iff the number of input parameters are equal to the number
	 *         of output parameters.
	 */
	public static boolean checkInputAttributesLengthEqualsOutputAttributesLength(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver) {
		return getInputAttributeIndices(parameters, attributeResolver, 0,
				false).length == getOutputAttributeNames(parameters, attributeResolver).length;
	}

	/**
	 * Returns true, iff all input attributes are numeric.
	 * 
	 * @param paramters
	 *            The parameters map.
	 * @param attributeResolver
	 *            The attribute resolver.
	 * @return True, iff all input attributes are numeric.
	 */
	public static boolean checkNumericInput(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver) {
		final int[] inputAttrs = getInputAttributeIndices(parameters, attributeResolver, 0, false);
		final SDFSchema inputSchema = attributeResolver.getSchema().get(0);
		for (final int idx : inputAttrs) {
			if (!inputSchema.getAttribute(idx).getDatatype().isNumeric()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param parameters
	 * @param string
	 * @param i
	 * @return
	 */
	public static int getFunctionParameterAsInt(final Map<String, Object> parameters, final String key,
			final int defaultValue) {
		if (key == null) {
			return defaultValue;
		}
		final Object value = getFunctionParameter(parameters, key);
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof String) {
			return Integer.parseInt((String) value);
		}

		throw new IllegalArgumentException(
				"Could not parse parameter " + key + ". Value should be of type Integer but is: " + value.getClass());
	}

}
