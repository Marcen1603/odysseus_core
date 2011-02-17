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

import java.util.Collection;

import de.uniol.inf.is.odysseus.store.MemoryStore;

public class MemoryUserStore extends MemoryStore<String, User> implements
		IUserStore {

	@Override
	public User getUserByName(String username) {
		return get(username);
	}

	@Override
	public void storeUser(User user) {
		put(user.getUsername(), user);
	}

	@Override
	public Collection<User> getUsers() {
		return values();
	}

	@Override
	public User removeByName(String username) {
		return remove(username);
	}

}
