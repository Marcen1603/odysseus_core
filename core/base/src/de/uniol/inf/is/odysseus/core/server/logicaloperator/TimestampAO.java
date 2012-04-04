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

public class TimestampAO extends UnaryLogicalOp {
	private static final long serialVersionUID = -467482177921504749L;

	private SDFAttribute startTimestamp;
	private SDFAttribute endTimestamp;
	private boolean isUsingSystemTime;
	private boolean isUsingNoTime;

	public TimestampAO(TimestampAO ao) {
		super(ao);
		this.startTimestamp = ao.startTimestamp;
		this.endTimestamp = ao.endTimestamp;
		this.isUsingSystemTime = ao.isUsingSystemTime;
		this.isUsingNoTime = ao.isUsingNoTime;
	}

	public TimestampAO() {
		startTimestamp = null;
		endTimestamp = null;
		isUsingSystemTime = true;
		isUsingNoTime = false;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimestampAO(this);
	}

	public SDFAttribute getStartTimestamp() {
		return startTimestamp;
	}

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

	public void setEndTimestamp(SDFAttribute endTimestamp) {
		this.endTimestamp = endTimestamp;
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

}
