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
package de.uniol.inf.is.odysseus.p2p.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;

public class P2PSinkAO extends P2PAO{

	private static final long serialVersionUID = -4079953031372502548L;
		
	public P2PSinkAO(String adv) {
		super(adv);
	}

	public P2PSinkAO(P2PSinkAO p2pSinkAO) {
		super(p2pSinkAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new P2PSinkAO(this);
	}

}
