/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.io.FileNotFoundException;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;

public class DashboardEditor extends EditorPart implements IDashboardListener {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardEditor.class);

	private static final IDashboardHandler DASHBOARD_HANDLER = new XMLDashboardHandler();
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();

	private Dashboard dashboard;
	private FileEditorInput input;
	private boolean dirty;
	
	private DashboardOutlineContentPage outlinePage;

	private Map<IDashboardPart, DashboardPartController> controllers;

	@Override
	public void doSave(IProgressMonitor monitor) {

		try {
			FileUtil.write(DASHBOARD_HANDLER.save(dashboard), this.input.getFile());
			setDirty(false);
		} catch (DashboardHandlerException ex) {
			LOG.error("Could not save Dashboard!", ex);
			throw new RuntimeException("Could not save Dashboard!", ex);
		} catch (CoreException ex) {
			LOG.error("Could not save Dashboard!", ex);
			throw new RuntimeException("Could not save Dashboard!", ex);
		}
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		Preconditions.checkArgument(input instanceof FileEditorInput, "Input must be of type %s!", FileEditorInput.class);
		this.input = (FileEditorInput) input;

		setSite(site);
		setInput(input);
		setPartName(this.input.getFile().getName());

		try {
			dashboard = DASHBOARD_HANDLER.load(FileUtil.read(this.input.getFile()), DASHBOARD_PART_HANDLER);
			dashboard.addListener(this);
			controllers = createControllers(dashboard.getDashboardPartPlacements());
		} catch (DashboardHandlerException ex) {
			LOG.error("Could not load Dashboard!", ex);
			throw new PartInitException("Could not load Dashboard!", ex);
		} catch (FileNotFoundException ex) {
			LOG.error("Could not load query file!", ex);
			throw new PartInitException("Could not load query file!", ex);
		} catch (CoreException ex) {
			LOG.error("Could not load Dashboard!", ex);
			throw new PartInitException("Could not load Dashboard!", ex);
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void dispose() {
		stopDashboard();
		
		dashboard.dispose();
		getSite().setSelectionProvider(null);
		
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());
		ToolBar toolBar = new ToolBar(parent, SWT.WRAP | SWT.RIGHT);
		
		dashboard.createPartControl(parent, toolBar);
		getSite().setSelectionProvider(dashboard);
		
		try {
			startDashboard();
		} catch (Exception ex) {
			throw new RuntimeException("Could not start dashboard", ex);
		}
	}

	@Override
	public void setFocus() {
		if( dashboard != null ) {
			dashboard.setFocus();
		}
	}
	
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			if (outlinePage == null) {
				outlinePage = new DashboardOutlineContentPage(dashboard);
			}
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}
	
	public void setDirty(boolean dirty) {
		if (dirty != this.dirty) {
			this.dirty = dirty;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					firePropertyChange(IEditorPart.PROP_DIRTY);
				}
			});
		}
	}
	
	public Dashboard getDashboard() {
		return dashboard;
	}
	
	public boolean hasDashboard() {
		return dashboard != null;
	}

	private void startDashboard() throws Exception {
		for (DashboardPartController controller : controllers.values()) {
			controller.start();
		}
	}

	private void stopDashboard() {
		for (DashboardPartController controller : controllers.values()) {
			controller.stop();
		}
	}

	private static Map<IDashboardPart, DashboardPartController> createControllers(ImmutableList<DashboardPartPlacement> dashboardPartPlacements) {
		Map<IDashboardPart, DashboardPartController> controllers = Maps.newHashMap();

		for (DashboardPartPlacement place : dashboardPartPlacements) {
			IDashboardPart part = place.getDashboardPart();
			DashboardPartController controller = new DashboardPartController(part);
			controllers.put(part, controller);
		}

		return controllers;
	}

	@Override
	public void dashboardChanged(Dashboard sender) {
		setDirty(true);
	}
}
