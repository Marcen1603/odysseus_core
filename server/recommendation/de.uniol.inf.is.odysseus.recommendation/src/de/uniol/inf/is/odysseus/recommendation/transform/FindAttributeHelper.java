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
package de.uniol.inf.is.odysseus.recommendation.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;

/**
 * @author Cornelius Ludmann
 *
 */
public class FindAttributeHelper {
	private FindAttributeHelper() {
	}

	public static int findPortWithAttribute(
			final AbstractLogicalOperator operator, final SDFAttribute attribute) {
		for (int i = 0; i < operator.getNumberOfInputs(); ++i) {
			if (operator.getInputSchema(i).indexOf(attribute) >= 0) {
				return i;
			}
		}
		return -1;
	}

	public static int findAttributeIndex(
			final AbstractLogicalOperator operator,
			final SDFAttribute attribute, final int port) {
		return operator.getInputSchema(port).indexOf(attribute);
	}

	public static SDFAttribute findAttributeByName(
			final AbstractLogicalOperator operator, final String attributeName) {
		for (int i = 0; i < operator.getNumberOfInputs(); ++i) {
			final SDFAttribute attribute = operator.getInputSchema(i)
					.findAttribute(attributeName);
			if (attribute != null) {
				return attribute;
			}
		}
		return null;
	}
}
