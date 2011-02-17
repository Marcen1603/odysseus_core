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
package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import net.jxta.peergroup.PeerGroup;


/**
 * Hilfsklasse für die PeerGroup, da diese nicht serialisierbar ist, aber für die Transformation benötigt wird.
 * 
 * @author Mart Köhler
 *
 */
public final class PeerGroupTool {
	private static PeerGroup peerGroup = null;

	public static PeerGroup getPeerGroup() {
		return peerGroup;
	}

	public static void setPeerGroup(PeerGroup peerGroup) {
		PeerGroupTool.peerGroup = peerGroup;
	}
	
	
}
