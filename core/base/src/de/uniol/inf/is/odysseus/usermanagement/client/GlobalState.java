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
package de.uniol.inf.is.odysseus.usermanagement.client;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class GlobalState {
	
	private static List<IActiveUserListener> activeUserListener = new CopyOnWriteArrayList<IActiveUserListener>();
	
	private static User activeUser; 
	private static IDataDictionary activeDatadictionary;
	
	public static IDataDictionary getActiveDatadictionary() {
		return activeDatadictionary;
	}

	public static void setActiveDatadictionary(IDataDictionary activeDatadictionary) {
		GlobalState.activeDatadictionary = activeDatadictionary;
	}

	private GlobalState() {
	}
	
	public synchronized static void setActiveUser( User user ) {
		activeUser = user;
		fire();
	}
	
	public synchronized static User getActiveUser() { 
		return activeUser;
	}
	
	public static void addActiveUserListner(IActiveUserListener listener){
		activeUserListener.add(listener);
	}
	
	public static void removeActiveUserListner(IActiveUserListener listener){
		activeUserListener.remove(listener);
	}
	
	public static void fire(){
		for (IActiveUserListener l: activeUserListener){
			l.activeUserChanged(activeUser);
		}
	}
	
}
