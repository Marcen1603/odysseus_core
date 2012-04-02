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
package de.uniol.inf.is.odysseus.rcp.server.views.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class UserViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof List) {
			return ((List<?>) parentElement).toArray();
		}
		if (parentElement instanceof Collection) {
			return ((Collection<?>) parentElement).toArray();
		}

		if (parentElement instanceof IUser){
			ArrayList<Object> list = new ArrayList<Object>();
			IUser u = (IUser) parentElement;
			list.addAll(u.getRoles());
			list.addAll(u.getPrivileges());		
			return list.toArray();
		}
		if (parentElement instanceof ISession) {
			ArrayList<Object> list = new ArrayList<Object>();
			ISession u = (ISession) parentElement;
			list.add(u.getUser());
			return list.toArray();
		}
		// new
		if (parentElement instanceof IRole) {
			ArrayList<Object> list = new ArrayList<Object>();
			IRole r = (IRole) parentElement;
			list.addAll(r.getPrivileges());
			return list.toArray();
		}
		if (parentElement instanceof IPrivilege) {
			ArrayList<Object> list = new ArrayList<Object>();
			IPrivilege p = (IPrivilege) parentElement;
			list.addAll(p.getPermissions());
			return list.toArray();
		}

		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Collection)
			return true;
		if (element instanceof List)
			return true;
		if (element instanceof IUser)
			return true;
		if (element instanceof ISession)
			return true;
		// new
		if (element instanceof IRole)
			return true;
		if (element instanceof IPrivilege)
			return true;
		return false;
	}

}
