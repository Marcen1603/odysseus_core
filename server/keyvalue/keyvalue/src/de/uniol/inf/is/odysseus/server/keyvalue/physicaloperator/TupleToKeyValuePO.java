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

import java.util.List;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
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

		KeyValueObject<M> output = convertToKeyValue(input, getOutputSchema());
		if (output != null) {
			output.setMetadata((M) input.getMetadata().clone());
			transfer(output);
		}
	}

	@SuppressWarnings("unchecked")
	private KeyValueObject<M> convertToKeyValue(Tuple<M> input, SDFSchema sdfSchema) {
		KeyValueObject<M> output = null;
		if (getOutputSchema().getType() == KeyValueObject.class) {
			output = new KeyValueObject<M>();
		}
		for (int pos =0;pos<sdfSchema.size(); pos++) {
			SDFAttribute attr = sdfSchema.get(pos);

			if (attr.getDatatype().isListValue()) {
				List<?> o = input.getAttribute(pos);
				for (int i=0;i<o.size();i++) {
					String name = attr.getQualName();
					// Could be a tuple or a base object
					if (attr.getDatatype().getSubType().isTuple()){
						KeyValueObject<M> subObj = convertToKeyValue((Tuple<M>)o.get(i), attr.getDatatype().getSchema());
						output.addAttributeValue(name, subObj);
					}else{
						output.addAttributeValue(name, o.get(i));
					}

					// Lists from Lists?
				}
			}
			if (attr.getDatatype().isTuple()) {
				KeyValueObject<M> subObj = convertToKeyValue((Tuple<M>)input.getAttribute(pos), attr.getDatatype().getSchema());
				output.add(attr.getQualName(),subObj);
			} else {
				output.setAttribute(attr.getQualName(), input.getAttribute(pos));
			}
		}
		return output;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
