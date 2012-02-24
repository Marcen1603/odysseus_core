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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

abstract public class P2PAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 2451774121232984603L;
	private String adv;
	
	public P2PAO(String adv) {
		this.adv = adv;
	}

	public P2PAO(P2PAO p2pAO) {
		super(p2pAO);
		this.adv = p2pAO.adv;
	}

	public String getAdv() {
		return adv;
	}

	public void setAdv(String adv) {
		this.adv = adv;
	}

}
