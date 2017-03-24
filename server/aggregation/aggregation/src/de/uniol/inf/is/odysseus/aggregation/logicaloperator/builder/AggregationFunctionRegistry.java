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
package de.uniol.inf.is.odysseus.aggregation.logicaloperator.builder;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

/**
 * @author Cornelius Ludmann
 *
 */
public class AggregationFunctionRegistry {

	// Removed to allow declarative services
//	private static AggregationFunctionRegistry INSTANCE = new AggregationFunctionRegistry();
//
//	private static AggregationFunctionRegistry getInstance() {
//		return INSTANCE;
//	}

	static private final Multimap<String, IAggregationFunctionFactory> functionFactories = ArrayListMultimap.create();

	// Must be public for service
	public AggregationFunctionRegistry() {
		;
	}

	public void addFunction(final IAggregationFunctionFactory function) {
		functionFactories.put(function.getFunctionName().toUpperCase(), function);
	}
	
	public void removeFunction(final IAggregationFunctionFactory function){
		functionFactories.remove(function.getFunctionName().toUpperCase(), function);
	}

//	public void addFunction(final String name, final IAggregationFunctionFactory function) {
//		functionFactories.put(name, function);
//	}

	/**
	 * @param value
	 * @param iAttributeResolver
	 * @return
	 */
	static public IAggregationFunction createFunction(final Map<String, Object> value,
			final IAttributeResolver attributeResolver) {

		final String functionName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(value,
				AggregationFunctionParseOptionsHelper.FUNCTION_NAME);

		if (functionName == null) {
			throw new QueryParseException(
					"Could not find parameter '" + AggregationFunctionParseOptionsHelper.FUNCTION_NAME + "'.");
		}

		final Collection<IAggregationFunctionFactory> matchingFunctions = functionFactories.get(functionName.toUpperCase());

		for (final IAggregationFunctionFactory candidate : matchingFunctions) {
			if (candidate.checkParameters(value, attributeResolver)) {
				return candidate.createInstance(value, attributeResolver);
			}
		}

		throw new QueryParseException("Could not find function " + functionName+ " with paramterset "+value);
	}

}
