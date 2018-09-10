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

import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractParameter;

/**
 * @author Cornelius Ludmann
 *
 */
public class AggregationItemParameter extends AbstractParameter<IAggregationFunction> {

	private static final long serialVersionUID = 7100488040304394350L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.
	 * AbstractParameter#internalAssignment()
	 */
	@Override
	protected void internalAssignment() {
		if (inputValue instanceof IAggregationFunction) {
			setValue((IAggregationFunction) inputValue);
			return;
		}

		if (inputValue instanceof Map) {
			@SuppressWarnings("unchecked")
			final Map<String, Object> value = (Map<String, Object>) inputValue;
			final IAggregationFunction function = AggregationFunctionRegistry.createFunction(value,
					getAttributeResolver());
			setValue(function);
			return;
		}
		throw new IllegalArgumentException("inputValue is not a map: " + inputValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.
	 * AbstractParameter#getPQLStringInternal()
	 */
	@Override
	protected String getPQLStringInternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
