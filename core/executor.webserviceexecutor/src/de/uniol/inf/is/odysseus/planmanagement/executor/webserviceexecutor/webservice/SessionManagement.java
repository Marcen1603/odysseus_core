/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * 
 * @author Dennis Geesen
 * Created at: 09.08.2011
 */
public class SessionManagement {

	private Map<String, Long> currentSessions = new HashMap<String, Long>();
	private static SessionManagement instance = null;
	
	private SessionManagement(){
		if(GlobalState.getActiveDatadictionary()==null){
			GlobalState.setActiveDatadictionary(DataDictionaryFactory.getDefaultDataDictionary(OdysseusDefaults.get("defaultDataDictionaryName")));
		}
	}
	
	public synchronized static SessionManagement getInstance(){		
		if(instance == null){
			instance = new SessionManagement();
		}
		instance.purge();
		return instance;
	}
	
	
	public void purge(){
//		long currentTime = System.currentTimeMillis();
//		Iterator<Entry<String, Long>> iterator = currentSessions.entrySet().iterator();
//		while(iterator.hasNext()){
//			Entry<String, Long> e = iterator.next();
//			if(e.getValue()<currentTime){
//				GlobalState.removeActiveUser(e.getKey());
//				iterator.remove();
//			}
//		}
	}
	
	public boolean isValidSession(String token){
		return currentSessions.containsKey(token);
	}
	
	public synchronized IDataDictionary getDataDictionary(){		
		return GlobalState.getActiveDatadictionary();
	}
	
	public ISession getUser(String token){
		return GlobalState.getActiveSession(token);
	}
	
	public String createNewSession(ISession user){
		String token = createFreshSessionToken();
		this.currentSessions.put(token, System.currentTimeMillis());
		GlobalState.setActiveSession(token, user);
		return token;
	}
	
	private String createFreshSessionToken(){
		String token = createToken();
		while(GlobalState.getActiveSession(token)!=null){
			token = createToken();
		}
		return token;
	}

	private String createToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
}
