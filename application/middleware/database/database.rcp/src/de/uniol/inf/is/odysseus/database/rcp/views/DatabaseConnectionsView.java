/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.database.rcp.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnectionDictionaryListener;

/**
 * 
 * @author Dennis Geesen Created at: 08.11.2011
 */
public class DatabaseConnectionsView extends ViewPart implements IDatabaseConnectionDictionaryListener{

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.uniol.inf.is.odysseus.database.rcp.views.DatabaseConnectionsView";

	private TreeViewer viewer;
	
	/**
	 * The constructor.
	 */
	public DatabaseConnectionsView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
    public void createPartControl(Composite parent) {
		DatabaseConnectionDictionary.getInstance().addListener(this);
		viewer = new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		viewer.setContentProvider(new DatabaseConnectionsViewContentProvider());
		viewer.setLabelProvider(new DatabaseConnectionsViewLabelProvider());		
		viewer.setInput(getViewSite());
	}	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
    public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void databaseConnectionDictionaryChanged() {
		refresh();
		
	}
	
	@Override
	public void dispose() {
		DatabaseConnectionDictionary.getInstance().removeListener(this);
		super.dispose();
	}

	private void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					viewer.refresh(); 
				} catch (Exception e) {
					viewer.setInput("Refresh failed");					
				}
			}

		});
		
	}
}