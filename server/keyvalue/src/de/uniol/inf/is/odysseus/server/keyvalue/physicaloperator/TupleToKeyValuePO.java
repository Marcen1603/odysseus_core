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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator.ToKeyValueAO;

/**
 * This operator transforms a Tuple to a KeyValueObject
 *
 * @author Jan Soeren Schwarz
 *
 * @param <M>
 */

public class TupleToKeyValuePO<M extends IMetaAttribute> extends AbstractPipe<Tuple<M>, KeyValueObject<M>> {

	private String template;

	public TupleToKeyValuePO() {
		super();
		this.template = "";
	}

	public TupleToKeyValuePO(ToKeyValueAO ao) {
		super();
		this.template = ao.getTemplate();
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

		/*
		 * We need to use the input schema, not the output schema. As we as a
		 * keyValue do not have any output schema (except meta schema), we would
		 * not be able to use the input attributes.
		 */
		KeyValueObject<M> output = convertToKeyValue(input, getInputSchema(port));
		if (output != null) {
			output.setMetadata((M) input.getMetadata().clone());
			transfer(output);
		}
	}

	@SuppressWarnings("unchecked")
	private KeyValueObject<M> convertToKeyValue(Tuple<M> input, SDFSchema sdfSchema) {

		KeyValueObject<M> output = null;

		if (template == null || template.isEmpty()) {
			output = (KeyValueObject<M>) KeyValueObject.fromTuple((Tuple<IMetaAttribute>) input, sdfSchema);
		} else {
			// We have a template to fill
			output = (KeyValueObject<M>) KeyValueObject.fromTupleWithTemplate((Tuple<IMetaAttribute>) input, sdfSchema,
					template);
		}
		return output;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
