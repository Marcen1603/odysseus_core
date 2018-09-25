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
package de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class KeyValueRenamePO<T extends KeyValueObject<M>, M extends IMetaAttribute> extends AbstractPipe<T, T> {
	private Map<String, String> renameMap;
	boolean keepInputObject;

	public KeyValueRenamePO() {

	}

	public KeyValueRenamePO(RenameAO renameAO) {
		this.renameMap = renameAO.getAliasesAsMap();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T input, int port) {
		// TODO: find a better way ...
		Map<String, Object> attributes = input.getAsKeyValueMap();
		for(Entry<String, String> renamePair : this.renameMap.entrySet()) {
			attributes.put(renamePair.getValue(), attributes.remove(renamePair.getKey()));
		}
		T output = (T) KeyValueObject.createInstance(attributes);
		if (input.getMetadata() != null) {
			output.setMetadata((M) input.getMetadata().clone());
		}
		transfer(output);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

}
