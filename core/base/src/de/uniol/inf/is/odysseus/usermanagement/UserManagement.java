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
package de.uniol.inf.is.odysseus.usermanagement;



public class UserManagement {

	static private IUserManagement usrMgmt = null;
	static private ISessionManagement sessMgmt = null;
	
	static public IUserManagement getUsermanagement() {
		return usrMgmt;
	}
	
	static public ISessionManagement getSessionmanagement() {
		return sessMgmt;
	}
	
	protected void bindUserManagement(IUserManagement usermanagement) {
		if (usrMgmt == null){
			usrMgmt = usermanagement;
		}else{
			throw new RuntimeException("UserManagement already bound!");
		}
	}

	// TODO: Will man das?
	protected void unbindUserManagement(IUserManagement usermanagement) {
		usermanagement = null;
	}

	protected void bindSessionManagement(ISessionManagement sessionmanagement) {
		if (sessMgmt == null){
			sessMgmt = sessionmanagement;
		}else{
			throw new RuntimeException("SessionManagement already bound!");
		}
	}

	protected void unbindSessionManagement(ISessionManagement sessionmanagement) {
		sessionmanagement = null;
	}
	
	
}
