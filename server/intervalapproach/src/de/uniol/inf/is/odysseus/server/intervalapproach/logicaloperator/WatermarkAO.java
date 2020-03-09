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
package de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * @author Cornelius Ludmann Tobias Brandt
 *
 */
@LogicalOperator(minInputPorts = 0, maxInputPorts = Integer.MAX_VALUE, name = "Watermark", category = {
		LogicalOperatorCategory.PROCESSING }, doc = "Sends a watermark (heartbeat) with a certain delay. The watermark then lags behind a certain timespan.")
public class WatermarkAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -4282057482280455210L;

	private TimeValueItem timeParameter = null;

	private boolean removeOutdated = true;
	
	/**
	 * Default constructor.
	 */
	public WatermarkAO() {
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public WatermarkAO(WatermarkAO other) {
		this.timeParameter = other.getTimeParameter();
	}

	@Parameter(type = TimeParameter.class, name = "TimespanValue", optional = false, doc = "How long the watermark lacks behind the data stream timestamps.")
	public void setWindowSize(TimeValueItem timespan) {
		this.timeParameter = timespan;
	}

	public TimeValueItem getTimeParameter() {
		return timeParameter;
	}

	@Parameter(type = BooleanParameter.class, name = "removeOutdated", optional = true, doc = "By default, all elements older than the last send watermark are remove. Use this flag to send all element.")
	public void setRemoveOutdated(boolean removeOutdated) {
		this.removeOutdated = removeOutdated;
	}
	
	public boolean isRemoveOutdated() {
		return removeOutdated;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.
	 * AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new WatermarkAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		setOutputSchema(SDFSchemaFactory.createNewWithOutOfOrder(false, getInputSchema()));
		return getOutputSchema();
	}

}
