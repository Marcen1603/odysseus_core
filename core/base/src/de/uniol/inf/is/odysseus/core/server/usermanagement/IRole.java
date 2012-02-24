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
package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.IAbstractEntity;
import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IRole extends IAbstractEntity, Serializable {
	
	/**
	 * 
	 * @return The name of the role
	 */
	String getName();

	public void setName(String name);
	
	/**
	 * @return The privileges of the role
	 */
	List<? extends IPrivilege> getPrivileges();
	public void addPrivilege(IPrivilege privilege);
	
}
