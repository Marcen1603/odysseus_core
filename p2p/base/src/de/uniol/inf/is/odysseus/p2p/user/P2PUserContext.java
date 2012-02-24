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
package de.uniol.inf.is.odysseus.p2p.user;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class P2PUserContext {
		
	private static Map<String, ISession> activeSession = new HashMap<String, ISession>(); 
	
	private P2PUserContext() {
	}
	
	public synchronized static void setActiveSession(String token, ISession user) {
		activeSession.put(token, user);
	}
	
	public synchronized static ISession getActiveSession(String token) { 
		return activeSession.get(token);
	}
	
	public synchronized static void removeActiveUser(String token){
		activeSession.remove(token);
	}
	

	
}
