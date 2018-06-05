/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.INamedAttributeStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractNamedAttributeMapPO;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 * Implementation of map operator for key value objects.
 *
 * @author Marco Grawunder
 *
 * @param <T>
 */
public class KeyValueMapPO<K extends IMetaAttribute, T extends INamedAttributeStreamObject<K>> extends AbstractNamedAttributeMapPO<K, T> {

	public KeyValueMapPO(MapAO mapAO) {
		super(mapAO);
	}

	@SuppressWarnings({ "unchecked" })
	protected T createInstance() {
		T outputVal = (T) KeyValueObject.createInstance();
		return outputVal;
	}


	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof KeyValueMapPO)) {
			return false;
		}
		return super.process_isSemanticallyEqual(ipo);
	}
}
