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
package de.uniol.inf.is.odysseus.recommendation.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * @author Cornelius Ludmann
 *
 */
@LogicalOperator(name = "TIME_BUFFER", minInputPorts = 1, maxInputPorts = 1, category = { LogicalOperatorCategory.BASE }, doc = "This operator add a time buffer to the time interval.")
public class TimeBufferAO extends UnaryLogicalOp {
	private static final long serialVersionUID = -3068460319482501166L;

	private TimeValueItem bufferSize = null;

	/**
	 * Default constructor.
	 */
	public TimeBufferAO() {
		super();
	}

	public TimeBufferAO(final TimeBufferAO op) {
		super(op);
		this.bufferSize = op.bufferSize;
	}

	/**
	 * @return the bufferSize
	 */
	public TimeValueItem getBufferSize() {
		return this.bufferSize;
	}

	/**
	 * @param bufferSize
	 *            the bufferSize to set
	 */
	@Parameter(name = "buffer_size", type = TimeParameter.class, doc = "Buffer size. Default: no buffer", optional = true)
	public void setBufferSize(final TimeValueItem bufferSize) {
		this.bufferSize = bufferSize;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new TimeBufferAO(this);
	}

}
