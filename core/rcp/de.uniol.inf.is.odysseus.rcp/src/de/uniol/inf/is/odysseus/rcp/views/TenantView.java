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
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.usermanagement.ITenantManagementListener;
import de.uniol.inf.is.odysseus.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.usermanagement.IUser;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;


public class TenantView extends ViewPart implements IUserManagementListener,
		ITenantManagementListener {
	
	private TreeViewer viewer;

	public void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				List<Object> l = new ArrayList<Object>();
				l.add(new TenantsContentNode(TenantManagement.getInstance()
						.getTenants()));
				try {
					List<? extends IUser> users = UserManagement.getUsermanagement()
					.getUsers(
							GlobalState.getActiveSession(OdysseusRCPPlugIn.RCP_USER_TOKEN));
					l.add(new UserContentNode(users));
				} catch (PermissionException e) {
					// If user has no rights to view all users, only the
					// current user is shown
					l.add(GlobalState.getActiveSession(OdysseusRCPPlugIn.RCP_USER_TOKEN));
				}
				viewer.setInput(l);
			}

		});
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new TenantViewContentProvider());
		viewer.setLabelProvider(new TenantViewLabelProvider());
		refresh();

		//UserManagement.getInstance().addUserManagementListener(this);
		UserManagement.getUsermanagement().addUserManagementListener(this);
		TenantManagement.getInstance().addTenantManagementListener(this);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void tenantsChangedEvent() {
		refresh();
	}

	@Override
	public void usersChangedEvent() {
		refresh();
	}

	@Override
	public void roleChangedEvent() {
		refresh();
	}
}
