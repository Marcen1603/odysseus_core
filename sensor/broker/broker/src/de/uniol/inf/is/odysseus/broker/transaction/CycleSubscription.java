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
package de.uniol.inf.is.odysseus.broker.transaction;

/**
 * This class represents a cyclic subscription.
 * The outgoing port represents the start of the cyclic subplan and
 * the incoming port represents the end of the cyclic subplan.
 * 
 * @author Dennis Geesen
 */
public class CycleSubscription extends AbstractPortTuple{
	
	/**
	 * Instantiates a new cyclic subscription.
	 *
	 * @param outgoingPort the outgoing port
	 * @param incomingPort the incoming port
	 */
	public CycleSubscription(int outgoingPort, int incomingPort){
		super(2);
		super.setPort(0, outgoingPort);
		super.setPort(1, incomingPort);
	}
	
	/**
	 * Gets the outgoing port.
	 *
	 * @return the outgoing port
	 */
	public int getOutgoingPort() {
		return super.getPort(0);
	}
	
	/**
	 * Gets the incoming port.
	 *
	 * @return the incoming port
	 */
	public int getIncomingPort() {
		return super.getPort(1);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return "Cycle from port "+this.getOutgoingPort()+" to port "+this.getIncomingPort();
	}
	
	
}
