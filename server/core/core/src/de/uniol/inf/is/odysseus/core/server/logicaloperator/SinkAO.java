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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts=Integer.MAX_VALUE, minInputPorts=1, name="Sink", doc="Represents a view for s sink.", category={LogicalOperatorCategory.SINK})
public class SinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -5432522510944805110L;
	private String sinkname;
	
	public SinkAO(AbstractLogicalOperator op) {
		super(op);
	}

	public SinkAO() {
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SinkAO(this);
	}
	
	@Parameter(name = "sink", type = StringParameter.class, optional = false, possibleValues = "__DD_SINKS", possibleValuesAreDynamic=true)
	public void setSink(String sinkname) {		
		setName(sinkname);
		this.sinkname = sinkname;
	}
	
	public String getSink() {
		return this.sinkname;
	}
	
	public String getSinkname(){
		return this.sinkname;
	}

	@Override
	public boolean isSourceOperator() {
		return false;
	}
}
