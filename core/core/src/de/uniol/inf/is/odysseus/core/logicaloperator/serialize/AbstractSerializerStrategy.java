/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.core.logicaloperator.serialize;

/**
 * 
 * @author Dennis Geesen Created at: 17.01.2012
 */
public abstract class AbstractSerializerStrategy<T> implements
		ISerializerStrategy<T> {

	@Override
	public T serialize(ISerializable serializable) {
		SerializeNode node = serializable.serialize();
		return performSerialize(node);
	}

	@Override
	public ISerializable deserialize(T value) {
		SerializeNode node = performDeserialize(value);
		try {
			if (ISerializable.class.isAssignableFrom(node
					.getRepresentingClass())) {
				ISerializable serializableRoot = (ISerializable) node
						.getRepresentingClass().newInstance();
				serializableRoot.deserialize(node);
			} else {
				throw new Exception(
						"The root element has to be an object of ISerializable");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected abstract T performSerialize(SerializeNode node);

	protected abstract SerializeNode performDeserialize(T value);

}
