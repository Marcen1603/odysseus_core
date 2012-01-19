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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tenant implements Serializable{

	private static final long serialVersionUID = -4491105336941528822L;

	private List<IUser> users = new ArrayList<IUser>();
	private String name;
	private IServiceLevelAgreement sla;
	
	Tenant(String name, IServiceLevelAgreement sla){
		this.name = name;
		this.sla = sla;
	}
	
	void addUser(IUser user) throws TooManyUsersException {
		if (sla.getMaxUsers() == -1 || users.size() < sla.getMaxUsers()){
			users.add(user);
		}else{
			throw new TooManyUsersException("Current limit: "+sla.getMaxUsers());
		}
	}
	
	void removeUser(IUser user){
		users.remove(user);
	}
	
	public List<IUser> getUsers(){
		return Collections.unmodifiableList(users);
	}
	
	public String getName() {
		return name;
	}
	
	public IServiceLevelAgreement getServiceLevelAgreement() {
		return sla;
	}

	@Override
	public String toString() {
		return name+" "+sla+" "+users;
	}
	
}
