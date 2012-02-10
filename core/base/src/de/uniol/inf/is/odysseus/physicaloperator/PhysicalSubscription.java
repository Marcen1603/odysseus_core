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
/**
 * @author Marco Grawunder
 */
package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.Subscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

public class PhysicalSubscription<K> extends Subscription<K> {

	private static final long serialVersionUID = -6266008340674321020L;
	private boolean done;
	private int openCalls = 0;
	
	public PhysicalSubscription(K target, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		super(target, sinkInPort, sourceOutPort, schema);
		done = false;
	}
	
	public void setDone(boolean done) {
		this.done = done;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public synchronized void incOpenCalls(){
		openCalls++;
	}

	public synchronized void decOpenCalls(){
		openCalls--;
	}
	
	public synchronized int getOpenCalls(){
		return openCalls;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	
}