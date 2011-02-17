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
package de.uniol.inf.is.odysseus.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.Role;
import de.uniol.inf.is.odysseus.usermanagement.Tenant;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class TenantViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

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
		if (parentElement instanceof UserContentNode) {
			ArrayList<Object> list = new ArrayList<Object>();
			list.addAll(((UserContentNode) parentElement).users);
			return list.toArray();
		}
		if (parentElement instanceof TenantsContentNode) {
			return ((TenantsContentNode) parentElement).tenants.toArray();
		}

		if (parentElement instanceof List) {
			return ((List<?>) parentElement).toArray();
		}
		if (parentElement instanceof Collection) {
			return ((Collection<?>) parentElement).toArray();
		}
		if (parentElement instanceof Tenant) {
			ArrayList<Object> list = new ArrayList<Object>();
			Tenant t = (Tenant) parentElement;
			list.add(t.getServiceLevelAgreement());
			list.addAll(t.getUsers());
			return list.toArray();
		}
		if (parentElement instanceof User) {
			ArrayList<Object> list = new ArrayList<Object>();
			User u = (User) parentElement;
			list.addAll(u.getRoles());
			list.addAll(u.getPrivileges());
			return list.toArray();
		}
		// new
		if (parentElement instanceof Role) {
			ArrayList<Object> list = new ArrayList<Object>();
			Role r = (Role) parentElement;
			list.addAll(r.getPrivileges());
			return list.toArray();
		}
		if (parentElement instanceof Privilege) {
			ArrayList<Object> list = new ArrayList<Object>();
			Privilege p = (Privilege) parentElement;
			list.addAll(p.getOperations());
			return list.toArray();
		}

		if (parentElement instanceof IServiceLevelAgreement) {
			return ((IServiceLevelAgreement) parentElement).getPercentilConstraints().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof UserContentNode) {
			return true;
		}
		if (element instanceof TenantsContentNode) {
			return true;
		}
		if (element instanceof Collection)
			return true;
		if (element instanceof List)
			return true;
		if (element instanceof IServiceLevelAgreement)
			return true;
		if (element instanceof Tenant)
			return true;
		if (element instanceof User)
			return true;
		// new
		if (element instanceof Role)
			return true;
		if (element instanceof Privilege)
			return true;
		return false;
	}

}
