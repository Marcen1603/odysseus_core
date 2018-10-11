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
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
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
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.ControllerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;

public class DashboardEditor extends EditorPart implements IDashboardListener, IDashboardPartListener {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardEditor.class);

	private static final IDashboardHandler DASHBOARD_HANDLER = new XMLDashboardHandler();
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();

	private Dashboard dashboard;
	private FileEditorInput input;
	private boolean dirty;
		
	private DashboardOutlineContentPage outlinePage;

	private Map<IDashboardPart, DashboardPartController> controllers;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());
		final ToolBar toolBar = new ToolBar(parent, SWT.WRAP | SWT.RIGHT);

		initToolBar(toolBar);		
		dashboard.createPartControl(parent, toolBar, getSite());
		
		try {
			startDashboard();
		} catch (final Exception ex) {
			throw new RuntimeException("Could not start dashboard", ex);
		}
	}

	@Override
	public void dashboardChanged(Dashboard sender) {
		setDirty(true);
	}

	@Override
	public void dashboardPartAdded(Dashboard sender, IDashboardPart addedPart) {
		if (!controllers.containsKey(addedPart)) {
			addedPart.addListener(this);
			final DashboardPartController ctrl = new DashboardPartController(addedPart);
			try {
				ctrl.start();
				controllers.put(addedPart, ctrl);
			} catch (final ControllerException e) {
				LOG.error("Could not start dashboard part", e);
			}

			setDirty(true);
		}
	}

	@Override
	public void dashboardPartRemoved(Dashboard sender, IDashboardPart removedPart) {
		removedPart.removeListener(this);
		
		final DashboardPartController ctrl = controllers.get(removedPart);
		if (ctrl != null) {
			try {
				ctrl.stop();
				controllers.remove(removedPart);
			} catch (final Exception e) {
				LOG.error("Could not stop dashboard part", e);
			}
		}
		setDirty(true);
	}

	@Override
	public void dispose() {
		stopDashboard();

		dashboard.dispose();
		getSite().setSelectionProvider(null);

		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		try {
			DASHBOARD_HANDLER.save(dashboard, this.input.getFile());
			setDirty(false);
		} catch (DashboardHandlerException ex) {
			LOG.error("Could not save Dashboard!", ex);
			throw new RuntimeException("Could not save Dashboard!", ex);
		} 
	}

	@Override
	public void doSaveAs() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			if (outlinePage == null) {
				outlinePage = new DashboardOutlineContentPage(dashboard);
			}
			return outlinePage;
		}		
		Object dbAdapter = dashboard.getAdapter(adapter);
		if(dbAdapter!=null){
			return dbAdapter;
		}		
		return super.getAdapter(adapter);
	}


	public Dashboard getDashboard() {
		return dashboard;
	}

	public boolean hasDashboard() {
		return dashboard != null;
	}
	
	public IFile getDashboardFile() {
		return input.getFile();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// Preconditions.checkArgument(input instanceof FileEditorInput, "Input must be of type %s!", FileEditorInput.class);
		this.input = (FileEditorInput) input;

		setSite(site);
		setInput(input);
		setPartName(this.input.getFile().getName());

		try {
			dashboard = DASHBOARD_HANDLER.load(this.input.getFile(), DASHBOARD_PART_HANDLER, this);
			dashboard.addListener(this);
			controllers = createDashboardPartControllers(dashboard.getDashboardPartPlacements());
		} catch (DashboardHandlerException ex) {
			LOG.error("Could not load Dashboard!", ex);
			throw new PartInitException("Could not load Dashboard!", ex);
		} catch (FileNotFoundException ex) {
			LOG.error("Could not load query file!", ex);
			throw new PartInitException("Could not load query file!", ex);
		} 
	}
	
	public final DashboardPartController getDashboardPartController(IDashboardPart dashboardPart) {
		// Preconditions.checkNotNull(dashboardPart, "DashboardPart to get dashboard part controller must not be null!");
		
		return controllers.get(dashboardPart);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
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

	@Override
	public void setFocus() {
		if (dashboard != null) {
			dashboard.setFocus();
		}
	}

	protected final void startDashboard() {
		for (final DashboardPartController controller : controllers.values()) {
			tryStartDashboardPart(controller);
		}
	}

	protected final void stopDashboard() {
		for (final DashboardPartController controller : controllers.values()) {
			controller.stop();
		}
	}

	private DashboardPartController getControllerFromSelection(IStructuredSelection selection) {
		final DashboardPartPlacement selectedPlacement = (DashboardPartPlacement) selection.getFirstElement();
		final IDashboardPart dashboardPart = selectedPlacement.getDashboardPart();
		final DashboardPartController controller = controllers.get(dashboardPart);
		return controller;
	}

	private void initToolBar(ToolBar toolBar) {
		final ToolItem startButton = createToolBarButton(toolBar, DashboardPlugIn.getImageManager().get("startAll"));
		startButton.setToolTipText("Start dashboard part(s)");
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dashboard.hasSelection()) {
					final DashboardPartController controller = getControllerFromSelection(dashboard.getSelection());
					if( controller.isPaused() ) {
						controller.unpause();
					} else if (!controller.isStarted()) {
						tryStartDashboardPart(controller);
					}
				} else {
					for (final DashboardPartController controller : controllers.values()) {
						if( controller.isPaused() ) {
							controller.unpause();
						} else if (!controller.isStarted()) {
							tryStartDashboardPart(controller);
						}
					}
				}
			}
		});

		final ToolItem stopButton = createToolBarButton(toolBar, DashboardPlugIn.getImageManager().get("stopAll"));
		stopButton.setToolTipText("Stop dashboard part(s)");
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dashboard.hasSelection()) {
					final DashboardPartController controller = getControllerFromSelection(dashboard.getSelection());
					if( controller.isPaused() ) {
						controller.unpause();
					}
					
					if (controller.isStarted()) {
						controller.stop();
					}
				} else {
					for (final DashboardPartController controller : controllers.values()) {
						if( controller.isPaused() ) {
							controller.unpause();
						}

						if (controller.isStarted()) {
							controller.stop();
						}
					}
				}
			}
		});
		
		final ToolItem pauseButton = createToolBarButton(toolBar, DashboardPlugIn.getImageManager().get("pauseAll"));
		pauseButton.setToolTipText("Pause dashboard part(s)");
		pauseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dashboard.hasSelection()) {
					final DashboardPartController controller = getControllerFromSelection(dashboard.getSelection());
					if (controller.isStarted() && !controller.isPaused()) {
						controller.pause();
					}
				} else {
					for (final DashboardPartController controller : controllers.values()) {
						if (controller.isStarted() && !controller.isPaused()) {
							controller.pause();
						}
					}
				}
			}
		});
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		final ToolItem layoutButton = createToolBarButton(toolBar, DashboardPlugIn.getImageManager().get("layout"));
		layoutButton.setToolTipText("Layout");
		layoutButton.setEnabled(!dashboard.getSettings().isLocked());
		layoutButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Collection<DashboardPartPlacement> placements = dashboard.getDashboardPartPlacements();
				if (!placements.isEmpty()) {
					IDashboardLayouter gridLayouter = new GridDashboardLayouter();
					gridLayouter.layout(dashboard.getDashboardPartPlacements(), dashboard.getControl().getSize().x, dashboard.getControl().getSize().y);
					dashboard.update();
				}
			}
		});

		final ToolItem removeButton = createToolBarButton(toolBar, DashboardPlugIn.getImageManager().get("removeAll"));
		removeButton.setToolTipText("Remove all part(s)");
		removeButton.setEnabled(!dashboard.getSettings().isLocked());
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dashboard.hasSelection()) {
					final IStructuredSelection structSelection = dashboard.getSelection();
					final DashboardPartPlacement selectedPlacement = (DashboardPartPlacement) structSelection.getFirstElement();
					dashboard.remove(selectedPlacement);
				} else {
					for (DashboardPartPlacement placement : dashboard.getDashboardPartPlacements()) {
						dashboard.remove(placement);
					}
				}
			}
		});

		new ToolItem(toolBar, SWT.SEPARATOR);

	}

	private Map<IDashboardPart, DashboardPartController> createDashboardPartControllers(ImmutableList<DashboardPartPlacement> dashboardPartPlacements) {
		final Map<IDashboardPart, DashboardPartController> controllers = Maps.newHashMap();

		for (final DashboardPartPlacement place : dashboardPartPlacements) {
			final IDashboardPart part = place.getDashboardPart();
			final DashboardPartController controller = new DashboardPartController(part);
			controllers.put(part, controller);
		}

		return controllers;
	}

	private static final ToolItem createToolBarButton(ToolBar tb, Image img) {
		final ToolItem item = new ToolItem(tb, SWT.PUSH);
		item.setImage(img);
		return item;
	}

	private static void tryStartDashboardPart(final DashboardPartController controller) {
		try {
			controller.start();
		} catch (final ControllerException e1) {
			LOG.error("Could not start dashboardpart", e1);
		}
	}

	@Override
	public void dashboardPartChanged(IDashboardPart changedPart) {
		setDirty(true);
	}
}
