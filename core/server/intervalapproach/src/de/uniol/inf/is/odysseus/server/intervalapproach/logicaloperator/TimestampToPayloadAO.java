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
package de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "TimestampToPayload", minInputPorts = 1, maxInputPorts = 1, doc = "Depracated: Use Map and TimeInterval.Start and TimeInterval.End directly. This operator is needed before data is send to another system (e.g. via a socket sink) to keep the time meta information (i.e. start and end time stamp). The input object gets two new fields with start and end timestamp. If this output is read again by (another) Odysseus instance, the following needs to be attached to the schema: ['start', 'StartTimestamp'], ['end', 'EndTimestamp']", category = { LogicalOperatorCategory.TRANSFORM }, deprecation=true)
public class TimestampToPayloadAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 7506659021418301530L;
	private List<String> attributes;

	public TimestampToPayloadAO() {
	}

	public TimestampToPayloadAO(TimestampToPayloadAO timestampToPayloadAO) {
		super(timestampToPayloadAO);
		if (timestampToPayloadAO.attributes != null) {
			this.attributes = new ArrayList<String>(
					timestampToPayloadAO.attributes);
		}
	}

	@Parameter(name = "attributes", type = StringParameter.class, isList = true, optional = true, doc = "Names of the attributes for the start and endtimestamp (default meta_valid_start and meta_valid_end.")
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public List<String> getAttributes() {
		return this.attributes;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {

		final String start;
		final String end;
		if (attributes == null) {
			start = "meta_valid_start";
			end = "meta_valid_end";
		} else {
			start = attributes.get(0);
			end = attributes.get(1);
		}

		// TODO: Is there a chance to determine the unit of the timestamp?
		SDFAttribute starttimeStamp = new SDFAttribute(null, start,
				SDFDatatype.TIMESTAMP, null, null, null);
		SDFAttribute endtimeStamp = new SDFAttribute(null, end,
				SDFDatatype.TIMESTAMP, null, null, null);

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

		if (getInputSchema(0) != null) {
			setOutputSchema(SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(0)));
		} else {
			setOutputSchema(SDFSchemaFactory.createNewSchema(name, type, outputAttributes));
		}
		return getOutputSchema();
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (attributes != null && attributes.size() != 2) {
			addError("Must defined two attribute names!");
			isValid = false;
		}

		return isValid && super.isValid();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimestampToPayloadAO(this);
	}

}
