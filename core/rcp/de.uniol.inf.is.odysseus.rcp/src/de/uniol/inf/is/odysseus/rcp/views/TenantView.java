package de.uniol.inf.is.odysseus.rcp.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.usermanagement.HasNoPermissionException;
import de.uniol.inf.is.odysseus.usermanagement.ITenantManagementListener;
import de.uniol.inf.is.odysseus.usermanagement.IUserManagementListener;
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
					l.add(new UserContentNode(
							UserManagement.getInstance()
									.getUsers(
											GlobalState.getActiveUser())));
				} catch (HasNoPermissionException e) {
					// If user has no rights to view all users, only the
					// current user is shown
					l.add(GlobalState.getActiveUser());
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

		UserManagement.getInstance().addUserManagementListener(this);
		TenantManagement.getInstance().addTenantManagementListener(this);

		// Create the help context id for the viewer's control
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(viewer.getControl(),
						"de.uniol.inf.is.odysseus.rcp.tenants.view.viewer");

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
