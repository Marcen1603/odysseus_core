/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.p2p.thinpeer;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;

public interface IThinPeer {

	public void publishQuerySpezification(String query,
			String language, ISession user);

	public void sendQuerySpezificationToAdminPeer(String query,
			String language, String adminPeer, ISession user);

	public void registerListener(IThinPeerListener thinPeerListener);
	public void removeListener(IThinPeerListener thinPeerListener);

	public void addToDD(ISourceAdvertisement adv, IDataDictionary dd, ISession caller);
	

}
