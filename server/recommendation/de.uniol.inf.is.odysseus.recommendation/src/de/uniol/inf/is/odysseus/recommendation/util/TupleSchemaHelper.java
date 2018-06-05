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
package de.uniol.inf.is.odysseus.recommendation.util;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Cornelius Ludmann
 *
 */
public class TupleSchemaHelper<M extends IMetaAttribute, A extends Enum<?>> {

	private final Map<A, Integer> attributeMap;

	/**
	 *
	 */
	public TupleSchemaHelper(final Map<A, Integer> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public <V> V getAttributeValue(final Tuple<M> tuple, final A attribute) {
		return tuple.getAttribute(getPos(attribute));
	}

	/**
	 * @param attribute
	 * @return
	 */
	public int getPos(final A attribute) {
		return this.attributeMap.get(attribute);
	}
}
