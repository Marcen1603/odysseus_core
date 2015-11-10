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
package de.uniol.inf.is.odysseus.keyvalue.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator transforms a Tuple to a KeyValueObject
 * 
 * @author Jan S�ren Schwarz
 * 
 * @param <M>
 */

public class TupleToKeyValuePO<M extends IMetaAttribute> extends AbstractPipe<Tuple<M>, KeyValueObject<M>> {

	public TupleToKeyValuePO() {
		super();
	}

	public TupleToKeyValuePO(TupleToKeyValuePO<M> tupleToKeyValuePO) {
		super(tupleToKeyValuePO);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<M> input, int port) {
		KeyValueObject<M> output = null;
		if (getOutputSchema().getType() == KeyValueObject.class)
			output = new KeyValueObject<M>();
		else if (getOutputSchema().getType() == NestedKeyValueObject.class)
			output = new NestedKeyValueObject<M>();

		if (output != null) {
			int pos = 0;
			for (SDFAttribute attr : getOutputSchema()) {
				if (attr.getDatatype() == SDFDatatype.LIST_TUPLE) {
					List<?> o = input.getAttribute(pos++);
					for (Object oo : o) {
						Tuple<?> t = (Tuple<?>) oo;
						for (int i = 0; i < t.size(); ++i) {
							output.addAttributeValue(attr.getQualName() + "_" + i, t.getAttribute(i));
						}
					}
				} else
					output.setAttribute(attr.getQualName(), input.getAttribute(pos++));
			}
			output.setMetadata((M) input.getMetadata().clone());
			transfer(output);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
