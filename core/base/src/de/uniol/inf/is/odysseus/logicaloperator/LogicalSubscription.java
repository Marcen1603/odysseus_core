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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.Subscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

public class LogicalSubscription extends Subscription<ILogicalOperator> implements Serializable{

	private static final long serialVersionUID = 678442733825703258L;
		
	public LogicalSubscription(ILogicalOperator target, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		super(target, sinkInPort, sourceOutPort, schema);
	}
		
	//ACHTUNG. NICHT ÄNDERN!
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
		
}
