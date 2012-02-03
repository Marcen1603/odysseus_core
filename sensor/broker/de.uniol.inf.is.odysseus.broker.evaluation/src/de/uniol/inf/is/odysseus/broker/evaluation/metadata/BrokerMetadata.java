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
package de.uniol.inf.is.odysseus.broker.evaluation.metadata;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

/**
 * The BrokerMetadata implements a default {@link IBrokerInterval}.
 * 
 * @author Dennis Geesen
 */
public class BrokerMetadata extends TimeInterval implements IBrokerInterval {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9106037522259783069L;

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.intervalapproach.TimeInterval#clone()
	 */
	public BrokerMetadata clone(){
		BrokerMetadata clone = new BrokerMetadata();		
		return clone;
	}
	
}
