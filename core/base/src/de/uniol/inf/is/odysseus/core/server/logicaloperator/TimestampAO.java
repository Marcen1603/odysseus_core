/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "Timestamp")
public class TimestampAO extends UnaryLogicalOp {
	private static final long serialVersionUID = -467482177921504749L;

	private SDFAttribute startTimestamp;
	private SDFAttribute endTimestamp;
	private boolean isUsingSystemTime;
	private boolean isUsingNoTime;

	private boolean clearEnd;

	public TimestampAO(TimestampAO ao) {
		super(ao);
		this.startTimestamp = ao.startTimestamp;
		this.endTimestamp = ao.endTimestamp;
		this.isUsingSystemTime = ao.isUsingSystemTime;
		this.isUsingNoTime = ao.isUsingNoTime;
		this.clearEnd = ao.clearEnd;
	}

	public TimestampAO() {
		startTimestamp = null;
		endTimestamp = null;
		isUsingSystemTime = true;
		isUsingNoTime = false;
		clearEnd = false;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimestampAO(this);
	}

	public SDFAttribute getStartTimestamp() {
		return startTimestamp;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "START", isList = false, optional = true)
	public void setStartTimestamp(SDFAttribute startTimestamp) {
		this.startTimestamp = startTimestamp;
		if (this.startTimestamp == null) {
			this.isUsingSystemTime = true;
		} else {
			this.isUsingSystemTime = false;
		}
	}

	public SDFAttribute getEndTimestamp() {
		return endTimestamp;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "END", isList = false, optional = true)
	public void setEndTimestamp(SDFAttribute endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	
	@Parameter(type = BooleanParameter.class, name = "clearEnd", isList = false, optional = true)
	public void setClearEnd(boolean clearEnd) {
		this.clearEnd = clearEnd;
	}
	
	public boolean isClearEnd() {
		return clearEnd;
	}
	
	public boolean hasStartTimestamp() {
		return this.startTimestamp != null;
	}
	
	public boolean hasEndTimestamp() {
		return this.endTimestamp != null;
	}
	
	public boolean isUsingSystemTime() {
		return this.isUsingSystemTime;
	}
	
	public void setIsUsingSystemTime(boolean value) {
		this.isUsingSystemTime = value;
	}
	
	public void setUsingNoTime(boolean b){
		this.isUsingNoTime = b;
	}
	
	public boolean isUsingNoTime(){
		return this.isUsingNoTime;
	}

	@Override
	public String toString() {
		return  super.toString()+"s :"+startTimestamp+" e:"+endTimestamp+" "+isUsingSystemTime+" "+isUsingNoTime+" "+clearEnd;
	}
	
}
