/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.logicaloperator.intervalapproach;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "TimestampToPayload", minInputPorts = 1, maxInputPorts = 1, doc="This operator is needed before data is send to another system (e.g. via a socket sink) to keep the time meta information (i.e. start and end time stamp). The input object gets two new fields with start and end timestamp. If this output is read again by (another) Odysseus instance, the following needs to be attached to the schema: ['start', 'StartTimestamp'], ['end', 'EndTimestamp']",category = {LogicalOperatorCategory.TRANSFORM})
public class TimestampToPayloadAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 7506659021418301530L;

	public TimestampToPayloadAO() {
	}

	public TimestampToPayloadAO(TimestampToPayloadAO timestampToPayloadAO) {
		super(timestampToPayloadAO);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		SDFAttribute starttimeStamp = new SDFAttribute(null,
				"meta_valid_start", SDFDatatype.TIMESTAMP);
		SDFAttribute endtimeStamp = new SDFAttribute(null, "meta_valid_end",
				SDFDatatype.TIMESTAMP);
		
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		String name = "";
		@SuppressWarnings("rawtypes")
		Class<? extends IStreamObject> type = null;
		
		if (getInputSchema(0) != null) {
			outputAttributes.addAll(getInputSchema(0).getAttributes());
			name = getInputSchema(0).getURI();
			type = getInputSchema(0).getType();
		}
		outputAttributes.add(starttimeStamp);
		outputAttributes.add(endtimeStamp);
		
		setOutputSchema(new SDFSchema(name, type, outputAttributes));

		return getOutputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimestampToPayloadAO(this);
	}

}
