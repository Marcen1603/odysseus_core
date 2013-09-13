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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.IConfigurerListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;

public class DashboardPartEditor extends EditorPart implements IDashboardPartListener, IConfigurerListener {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartEditor.class);
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();

	private FileEditorInput input;
	private IDashboardPart dashboardPart;
	private DashboardPartController dashboardPartController;
	private boolean dirty;
	
	private TabFolder tabFolder;

	private DashboardPartEditorToolBar dashboardPartToolBar;
	private IDashboardPartConfigurer<IDashboardPart> configurer;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new GridLayout());

		final Composite presentationTab = createTabComposite(tabFolder, "Presentation");
		final Composite settingsTab = createTabComposite(tabFolder, "Settings");
		createPresentationTabContent(presentationTab);
		createSettingsTabContent(settingsTab);
	}

	@Override
	public void dispose() {
		dashboardPartController.stop();

		dashboardPart.dispose();
		
		configurer.removeListener(this);
		configurer.dispose();

		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			FileUtil.write(DASHBOARD_PART_HANDLER.save(dashboardPart), this.input.getFile());

			setDirty(false);

		} catch (final DashboardHandlerException e) {
			LOG.error("Could not save DashboardPart to file {}.", input.getFile().getName(), e);
		} catch (final CoreException ex) {
			LOG.error("Could not save DashboardPart to file {}.", input.getFile().getName(), ex);
		}
	}

	@Override
	public void doSaveAs() {
		// do nothing
	}

	public DashboardPartController getDashboardPartController() {
		return dashboardPartController;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {	
		Object dbAdapter = dashboardPart.getAdapter(adapter);
		if(dbAdapter!=null){
			return dbAdapter;
		}
		return super.getAdapter(adapter);
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof FileEditorInput)) {
			LOG.error("Could not open editor because input is from type {} instead of {}!", input.getClass(), FileEditorInput.class);
			throw new PartInitException("Input is from type " + input.getClass() + " instead of " + FileEditorInput.class + "!");
		}
		this.input = (FileEditorInput) input;

		setSite(site);
		setInput(input);
		setPartName(this.input.getFile().getName());

		try {
			dashboardPart = DASHBOARD_PART_HANDLER.load(FileUtil.read(this.input.getFile()));
			dashboardPart.setWorkbenchPart(this);
			dashboardPart.addListener(this);
			dashboardPartController = new DashboardPartController(dashboardPart);
		} catch (final DashboardHandlerException e) {
			LOG.error("Could not load DashboardPart for editor from file {}!", this.input.getFile().getName(), e);
			throw new PartInitException("Could not load DashboardPart from file " + this.input.getFile().getName(), e);
		} catch (final FileNotFoundException ex) {
			LOG.error("Could not load corresponding query file!", ex);
			throw new PartInitException("Could not load corresponding query file!", ex);
		} catch (final CoreException ex) {
			LOG.error("Could not load corresponding query file!", ex);
			throw new PartInitException("Could not load corresponding query file!", ex);
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
		if (tabFolder != null) {
			tabFolder.setFocus();
		}
	}

	public void setPartNameSuffix(String partNameSuffix) {
		if (Strings.isNullOrEmpty(partNameSuffix)) {
			super.setPartName(this.input.getFile().getName());
		} else {
			super.setPartName(this.input.getFile().getName() + " [" + partNameSuffix + "]");
		}
	}

	private void createPresentationTabContent(Composite presentationTab) {
		dashboardPartToolBar = new DashboardPartEditorToolBar(presentationTab, this);

		final Composite comp = new Composite(presentationTab, SWT.NONE);
		comp.setLayout(new GridLayout());
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		try {
			dashboardPart.createPartControl(comp, dashboardPartToolBar.getToolBar());
			dashboardPart.setWorkbenchPart(this);
			dashboardPartController.start();
		} catch (final Exception ex) {
			dashboardPartToolBar.setStatusToStopped();
			throw new RuntimeException(ex);
		}
	}	

	@SuppressWarnings("unchecked")
	private void createSettingsTabContent(Composite settingsTab) {
		settingsTab.setLayout(new GridLayout(1, false));
		settingsTab.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Optional<String> optName = DashboardPartRegistry.getRegistrationName(dashboardPart.getClass());
		if( optName.isPresent() ) {
			String name = optName.get();
			
			try {
				configurer = (IDashboardPartConfigurer<IDashboardPart>) DashboardPartRegistry.createDashboardPartConfigurer(name);
				configurer.addListener(this);
				configurer.init(dashboardPart, dashboardPartController.getQueryRoots());
				
				configurer.createPartControl(settingsTab);
				
			} catch (InstantiationException e) {
				LOG.error("Could not create configurer", e);
			}
		}
	}

	private static Composite createTabComposite(TabFolder tabFolder, String title) {
		final TabItem presentationTab = new TabItem(tabFolder, SWT.NULL);
		presentationTab.setText(title);

		final Composite presentationTabComposite = new Composite(tabFolder, SWT.NONE);
		presentationTabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		presentationTabComposite.setLayout(new GridLayout());

		presentationTab.setControl(presentationTabComposite);

		return presentationTabComposite;
	}

	@Override
	public void dashboardPartChanged(IDashboardPart changedPart) {
		setDirty(true);
	}

	@Override
	public void configChanged(IDashboardPartConfigurer<?> sender) {
		setDirty(true);
	}

	
	
}
