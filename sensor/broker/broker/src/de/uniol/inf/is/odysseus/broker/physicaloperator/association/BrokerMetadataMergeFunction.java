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
package de.uniol.inf.is.odysseus.broker.physicaloperator.association;

import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

/**
 * The BrokerMetadataMergeFunction merges two {@link ITimeInterval}. 
 * The new ITimeInterval will be the ITimeInterval of the right input.
 * 
 * @author Dennis Geesen
 */
public class BrokerMetadataMergeFunction implements IMetadataMergeFunction<ITimeInterval>{
			
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction#init()
	 */
	@Override
	public void init() {		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction#mergeMetadata(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ITimeInterval mergeMetadata(ITimeInterval left, ITimeInterval right) {		
			return (ITimeInterval)right.clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public BrokerMetadataMergeFunction clone() {
		return new BrokerMetadataMergeFunction();
	}

}
