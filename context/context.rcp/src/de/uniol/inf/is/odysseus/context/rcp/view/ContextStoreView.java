/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.context.rcp.view;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.context.IContextManagementListener;
import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.04.2012
 */
public class ContextStoreView extends ViewPart implements IContextManagementListener {

	private static final Logger LOG = LoggerFactory.getLogger(ContextStoreView.class);
	
	private TreeViewer viewer;
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		viewer = new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		viewer.setContentProvider(new ContextViewContentProvider());
		viewer.setLabelProvider(new ContextViewLabelProvider());
		refresh();
		ContextStoreManager.addListener(this);
		// UserManagement.getInstance().addUserManagementListener(this);
		getSite().setSelectionProvider(viewer);

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(viewer.getControl());
		// Set the MenuManager
		viewer.getControl().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, viewer);

	}

	
	@Override
	public void dispose() {	
		super.dispose();
		ContextStoreManager.removeListener(this);
	}
	private void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					viewer.setInput(ContextStoreManager.getStores());
				} catch (Exception e) {
					LOG.error("Exception during setting input for treeViewer in sourcesView", e);
				}
			}

		});
		
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();

	}

	@Override
	public void contextManagementChanged() {
		refresh();		
	}

}
