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

import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.builder.AggregationFunctionRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;

/**
 * A factory that creates an instance of an aggregation function. This function
 * should be registered at the {@link AggregationFunctionRegistry}.
 * 
 * @see IAggregationFunction
 * @see AggregationFunctionRegistry
 * 
 * @author Cornelius Ludmann
 *
 */
public interface IAggregationFunctionFactory {

	/**
	 * This method checks if this factory is applicable to the parameters. This
	 * method returns true, if all required parameters are present, false
	 * otherwise.
	 * 
	 * @param parameters
	 *            A key-value map that holds the parameters.
	 * @param attributeResolver
	 *            An attribute resolver for the input schema.
	 * @return True, iff this factory has all parameters to create the function.
	 */
	boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver);

	/**
	 * Creates an instance of the function.
	 * 
	 * @param parameters
	 *            The parameters.
	 * @param attributeResolver
	 *            An attribute resolver for the input schema.
	 * @return An instance of the function.
	 */
	IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver);

	/**
	 * The name of the function.
	 * 
	 * @return Name of the function.
	 */
	String getFunctionName();
}
