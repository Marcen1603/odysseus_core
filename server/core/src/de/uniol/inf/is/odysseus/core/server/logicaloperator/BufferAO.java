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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "BUFFER", minInputPorts = 1, maxInputPorts = 1, doc="Typically, Odysseus provides a buffer placement strategy to place buffers in the query plan. This operator allows adding buffers by hand. Buffers receives data stream elements and stores them in an internal elementbuffer. The scheduler stops the execution here for now. Later, the scheduler resumes to execution (e.g. with an another thread).", category={LogicalOperatorCategory.PROCESSING})
public class BufferAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 9204364375031967542L;

	/**
	 * Different types of buffers can be represented with this string
	 */
	private String type;
	
	/**
	 * This name can be used to control query sharing. Only buffer with the same
	 * sources AND the same name should be shared
	 */
	private String buffername="";

	/**
	 * Allow a buffer to schedule it self
	 */
	private boolean threaded = false;
	
	/**
	 * Limit the number of elements for a buffer.
	 */
	private long maxBufferSize = -1;

	private boolean drainAtClose = false;
	
	public BufferAO(BufferAO ao) {
		super(ao);
		this.type = ao.type;
		this.threaded = ao.threaded;
		this.maxBufferSize = ao.maxBufferSize;
		this.drainAtClose = ao.drainAtClose;
	}

	public BufferAO() {
	}

	@Override
	public BufferAO clone() {
		return new BufferAO(this);
	}

	public String getType() {
		return type;
	}

	@Parameter(type = StringParameter.class, optional = true)
	public void setType(String type) {
		this.type = type;
	}
	
	@Parameter(type = StringParameter.class, optional = true)
	public void setBuffername(String buffername) {
		this.buffername = buffername;
        this.addParameterInfo("BUFFERNAME", buffername);
	}
	
	public String getBuffername() {
		return buffername;
	}
	
	@Parameter(type = LongParameter.class, optional = true)
	public void setMaxBufferSize(long maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
        this.addParameterInfo("MaxBufferSize", maxBufferSize);
	}
	
	public long getMaxBufferSize() {
		return maxBufferSize;
	}
	
	@Parameter(type = BooleanParameter.class, optional= true, doc ="If set to true, this buffer will not be scheduled by the scheduler, but uses an own thread. Handle with care!")
	public void setThreaded(boolean threaded) {
		this.threaded = threaded;
        this.addParameterInfo("Threaded", threaded);
	}

	public boolean isThreaded() {
		return threaded;
	}
		
	@Parameter(type = BooleanParameter.class, optional= true, doc ="If set to true (default is false), this buffer be emptied when calling close. Remark: Could lead to longer termination time!")
	public void setDrainAtClose(boolean drainAtClose) {
		this.drainAtClose = drainAtClose;
        this.addParameterInfo("DrainAtClose", drainAtClose);		
	}

	public boolean isDrainAtClose() {
		return drainAtClose;
	}
	

	
}
