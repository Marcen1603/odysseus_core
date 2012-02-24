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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.event;


import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.event.AbstractEvent;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */

public class POEvent extends AbstractEvent<IPhysicalOperator, String>{

	private static final long serialVersionUID = 6854987972249370174L;
	
	public POEvent(IPhysicalOperator source, POEventType type) {
		super(source,type,"");
	}
	
	public IPhysicalOperator getSource(){
		return getSender();
	}

	public POEventType getPOEventType(){
		return (POEventType) getEventType();
	}
}