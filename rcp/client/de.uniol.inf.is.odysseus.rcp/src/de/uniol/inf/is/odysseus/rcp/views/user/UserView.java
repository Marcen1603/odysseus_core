/**********************************************************************************
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.views.user;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class UserView extends ViewPart implements IUpdateEventListener {

	static final InfoService INFO = InfoServiceFactory.getInfoService(UserView.class);

	private TreeViewer viewer;

	public void refresh() {
		if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					List<Object> l = new ArrayList<>();
					try {
						try {
							List<? extends IUser> users = OdysseusRCPPlugIn.getExecutor()
									.getUsers(OdysseusRCPPlugIn.getActiveSession());
							if (users != null) {
								l.addAll(users);
							} else {
								INFO.warning("Problems with the user management! Did not find any user");
							}
						} catch (UnsupportedOperationException e) {
							INFO.warning("UserView view is not available on clients. Please close this view!");
						}
					} catch (PermissionException e) {
						// If user has no rights to view all users, only the
						// current user is shown
						l.add(OdysseusRCPPlugIn.getActiveSession());
					}
					viewer.setInput(l);
				}

			});
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new UserViewContentProvider());
		viewer.setLabelProvider(new UserViewLabelProvider());
		refresh();
		OdysseusRCPPlugIn.getExecutor().addUpdateEventListener(this, IUpdateEventListener.SESSION, null);
		OdysseusRCPPlugIn.getExecutor().addUpdateEventListener(this, IUpdateEventListener.USER, null);

	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void eventOccured(String type) {
		refresh();
	}

}
