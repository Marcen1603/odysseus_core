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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

/**
 * @author Cornelius Ludmann
 *
 */
@LogicalOperator(minInputPorts = 0, maxInputPorts = Integer.MAX_VALUE, name = "Watermark", category = {
		LogicalOperatorCategory.PROCESSING }, doc = "Sends a watermark (heartbeat) with a certain delay. The watermark then lags behind a certain timespan.")
public class WatermarkAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -4282057482280455210L;

	private long timespan;

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
		this.timespan = other.timespan;
	}

	/**
	 * @return the timespan
	 */
	public long getTimespan() {
		return timespan;
	}

	/**
	 * @param timespan
	 *            the timespan to set
	 */
	@Parameter(type = LongParameter.class, name = "Timespan", optional = false, doc = "Timespan to lag behind. Use same time unit as in the incoming elements.")
	public void setTimespan(final long timespan) {
		this.timespan = timespan;
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

}
