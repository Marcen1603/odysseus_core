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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.usermanagement.IRole;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.IUser;

public class UserViewLabelProvider extends LabelProvider {

	@Override
	public String getText(Object obj) {

		if (obj instanceof ISession) {
			return ((ISession) obj).getUser().getName() + " "
					+ ((ISession) obj).getId();
		}
		if (obj instanceof IUser){
			return ((IUser) obj).getName();
		}
		if (obj instanceof IRole) {
			IRole role = (IRole) obj;
			return role.getName();
		}
		if (obj instanceof IPrivilege) {
			return "Privilege: " + ((IPrivilege) obj).getObjectURI();
		}
		if (obj instanceof IPermission){
			return "Permission:"+obj;
		}
		return obj.toString();
	}

	@Override
	public Image getImage(Object obj) {
		if (obj instanceof ISession) {
			return ImageManager.getInstance().get("loggedinuser");
		}
		if (obj instanceof IUser) {
			return ImageManager.getInstance().get("user");
		}
		if (obj instanceof IRole) {
			return ImageManager.getInstance().get("role");
		}
		String imageKey = ISharedImages.IMG_OBJ_FILE;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}
