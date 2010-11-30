package de.uniol.inf.is.odysseus.rcp.tenants.view.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.tenants.view.Activator;
import de.uniol.inf.is.odysseus.usermanagement.HasNoPermissionException;
import de.uniol.inf.is.odysseus.usermanagement.IPercentileConstraint;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.ITenantManagementListener;
import de.uniol.inf.is.odysseus.usermanagement.ITimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.usermanagement.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.Role;
import de.uniol.inf.is.odysseus.usermanagement.Tenant;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.ActiveUser;

public class TenantView extends ViewPart implements IUserManagementListener,
		ITenantManagementListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.uniol.inf.is.odysseus.rcp.tenants.view.views.TenantView";

	private TreeViewer viewer;

	class ViewContentProvider implements IStructuredContentProvider,
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
			if (parentElement instanceof UserWrapper) {
				ArrayList<Object> list = new ArrayList<Object>();
				list.addAll(((UserWrapper) parentElement).users);
				return list.toArray();
			}
			if (parentElement instanceof TenantWrapper) {
				return ((TenantWrapper) parentElement).tenants.toArray();
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
				return ((IServiceLevelAgreement) parentElement)
						.getPercentilConstraints().toArray();
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof UserWrapper) {
				return true;
			}
			if (element instanceof TenantWrapper) {
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

	class ViewLabelProvider extends LabelProvider {

		@Override
		public String getText(Object obj) {

			if (obj instanceof TenantWrapper) {
				return "Tenants";
			}
			if (obj instanceof UserWrapper) {
				return "User";
			}
			if (obj instanceof User) {
				return ((User) obj).getUsername()+ " "+((User) obj).getSession();
			}
			if (obj instanceof Role) {
				Role role = (Role) obj;
				return (role.isGroup()?"Group ":"Role: ") +  role.getRolename();
			}
			if (obj instanceof Privilege) {
				return "Privilege: " + ((Privilege) obj).getPrivname();
			}
			if (obj instanceof Tenant) {
				return ((Tenant) obj).getName();
			}
			if (obj instanceof IServiceLevelAgreement) {
				StringBuffer ret = new StringBuffer("SLA ").append(((IServiceLevelAgreement) obj).getName()).append(" initialized ").append(((IServiceLevelAgreement) obj).isInitialized());
				if (obj instanceof ITimeBasedServiceLevelAgreement){
					ret.append("Time: ");
					ret.append(((ITimeBasedServiceLevelAgreement)obj).getTimeperiod());
				}
				return ret.toString();
			}
			return obj.toString();
		}

		@Override
		public Image getImage(Object obj) {
			if (obj instanceof UserWrapper) {
				return Activator.getDefault().getImageRegistry().get("users");
			}
			if (obj instanceof TenantWrapper) {
				return Activator.getDefault().getImageRegistry().get("users");
			}
			if (obj instanceof User) {
				if (UserManagement.getInstance().isLoggedIn(
						((User) obj).getUsername())) {
					return Activator.getDefault().getImageRegistry()
							.get("loggedinuser");
				} else {
					return Activator.getDefault().getImageRegistry()
							.get("user");
				}
			}
			if (obj instanceof Tenant) {
				return Activator.getDefault().getImageRegistry().get("tenant");
			}
			if (obj instanceof Role) {
				return Activator.getDefault().getImageRegistry().get("role");
			}
			if (obj instanceof IServiceLevelAgreement) {
				return Activator.getDefault().getImageRegistry().get("sla");
			}
			if (obj instanceof IPercentileConstraint) {
				return Activator.getDefault().getImageRegistry()
						.get("percentile");
			}

			String imageKey = ISharedImages.IMG_OBJ_FILE;
			if (obj instanceof Tenant)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(imageKey);
		}
	}

	public void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				List<Object> l = new ArrayList<Object>();
				l.add(new TenantWrapper(TenantManagement.getInstance()
						.getTenants()));
				try {
					l.add(new UserWrapper(
							UserManagement.getInstance()
									.getUsers(
											ActiveUser.getActiveUser())));
				} catch (HasNoPermissionException e) {
					// If user has no rights to view all users, only the
					// current user is shown
					l.add(ActiveUser.getActiveUser());
				}
				viewer.setInput(l);
			}

		});
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
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

	/**
	 * Passing the focus request to the viewer's control.
	 */
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

class UserWrapper {
	public Collection<User> users;

	public UserWrapper(Collection<User> users) {
		this.users = users;
	}
}

class TenantWrapper {
	public Collection<Tenant> tenants;

	public TenantWrapper(Collection<Tenant> tenants) {
		this.tenants = tenants;
	}
}