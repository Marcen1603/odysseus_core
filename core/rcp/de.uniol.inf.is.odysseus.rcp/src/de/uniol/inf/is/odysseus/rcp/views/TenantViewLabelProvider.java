package de.uniol.inf.is.odysseus.rcp.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.usermanagement.IPercentileConstraint;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.ITimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.Role;
import de.uniol.inf.is.odysseus.usermanagement.Tenant;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class TenantViewLabelProvider extends LabelProvider {

	@Override
	public String getText(Object obj) {

		if (obj instanceof TenantsContentNode) {
			return "Tenants";
		}
		if (obj instanceof UserContentNode) {
			return "User";
		}
		if (obj instanceof User) {
			return ((User) obj).getUsername() + " " + ((User) obj).getSession();
		}
		if (obj instanceof Role) {
			Role role = (Role) obj;
			return (role.isGroup() ? "Group " : "Role: ") + role.getRolename();
		}
		if (obj instanceof Privilege) {
			return "Privilege: " + ((Privilege) obj).getPrivname();
		}
		if (obj instanceof Tenant) {
			return ((Tenant) obj).getName();
		}
		if (obj instanceof IServiceLevelAgreement) {
			StringBuffer ret = new StringBuffer("SLA ").append(((IServiceLevelAgreement) obj).getName()).append(" initialized ").append(((IServiceLevelAgreement) obj).isInitialized());
			if (obj instanceof ITimeBasedServiceLevelAgreement) {
				ret.append("Time: ");
				ret.append(((ITimeBasedServiceLevelAgreement) obj).getTimeperiod());
			}
			return ret.toString();
		}
		return obj.toString();
	}

	@Override
	public Image getImage(Object obj) {
		if (obj instanceof UserContentNode) {
			return ImageManager.getInstance().get("users");
		}
		if (obj instanceof TenantsContentNode) {
			return ImageManager.getInstance().get("users");
		}
		if (obj instanceof User) {
			if (UserManagement.getInstance().isLoggedIn(((User) obj).getUsername())) {
				return ImageManager.getInstance().get("loggedinuser");
			} else {
				return ImageManager.getInstance().get("user");
			}
		}
		if (obj instanceof Tenant) {
			return ImageManager.getInstance().get("tenant");
		}
		if (obj instanceof Role) {
			return ImageManager.getInstance().get("role");
		}
		if (obj instanceof IServiceLevelAgreement) {
			return ImageManager.getInstance().get("sla");
		}
		if (obj instanceof IPercentileConstraint) {
			return ImageManager.getInstance().get("percentile");
		}

		String imageKey = ISharedImages.IMG_OBJ_FILE;
		if (obj instanceof Tenant)
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}
