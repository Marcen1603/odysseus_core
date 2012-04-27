/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.context.store.types;

import java.util.List;

import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 22.03.2012
 */
public class ContextStoreFactory {

	public static <T extends Tuple<? extends ITimeInterval>> IContextStore<T> createStore(String name, ContextStoreType type, SDFSchema schema) {

		switch (type) {
		case MULTI_ELEMENT_STORE:
			return new MultiElementStore<T>(name, schema);
		case MULTI_HISTORY_STORE:
			break;
		case SINGLE_ELEMENT_STORE:
			return new SingleElementStore<T>(name, schema);
		case SINGLE_HISTORY_STORE:
			break;
		default:
			break;
		}
		return null;
	}
}
