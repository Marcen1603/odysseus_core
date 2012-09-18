/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.google.common.base.Preconditions;

public class DashboardOutlineContentPage extends ContentOutlinePage implements IDashboardListener, ISelectionListener {

	private final Dashboard dashboard;
	
	public DashboardOutlineContentPage(Dashboard dashboard) {
		this.dashboard = Preconditions.checkNotNull(dashboard, "Dashboard for Outline-content must not be null!");
		this.dashboard.addListener(this);
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new DashboardOutlineContentProvider());
		viewer.setLabelProvider(new DashboardOutlineLabelProvider());
		viewer.setInput(dashboard);
		
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}
	
	public void refresh() {
		getTreeViewer().refresh();
	}
	
	@Override
	public void dispose() {
		this.dashboard.removeListener(this);
		super.dispose();
	}

	@Override
	public void dashboardChanged(Dashboard sender) {
		refresh();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {	
		if (part instanceof DashboardEditor && selection instanceof IStructuredSelection && !selection.isEmpty()) {
			setSelection(selection);
		}
	}
}
