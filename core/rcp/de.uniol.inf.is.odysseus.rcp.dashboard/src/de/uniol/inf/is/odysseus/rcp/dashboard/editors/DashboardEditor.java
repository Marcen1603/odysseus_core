package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.io.FileNotFoundException;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
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

public class DashboardEditor extends EditorPart {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardEditor.class);

	private static final IDashboardHandler DASHBOARD_HANDLER = new XMLDashboardHandler();
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();

	private Dashboard dashboard;
	private FileEditorInput input;

	private Map<IDashboardPart, DashboardPartController> controllers;

	@Override
	public void doSave(IProgressMonitor monitor) {

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
			dashboard = DASHBOARD_HANDLER.load(this.input.getFile(), DASHBOARD_PART_HANDLER);
			controllers = createControllers(dashboard.getDashboardPartPlacements());
		} catch (DashboardHandlerException ex) {
			LOG.error("Could not load Dashboard!", ex);
			throw new PartInitException("Could not load Dashboard!", ex);
		} catch (FileNotFoundException ex) {
			LOG.error("Could not load query file!", ex);
			throw new PartInitException("Could not load query file!", ex);
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
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void dispose() {
		stopDashboard();
		
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());
		ToolBar toolBar = new ToolBar(parent, SWT.WRAP | SWT.RIGHT);
		
		dashboard.createPartControl(parent, toolBar);
		
		try {
			startDashboard();
		} catch (Exception ex) {
			throw new RuntimeException("Could not start dashboard", ex);
		}
	}

	@Override
	public void setFocus() {

	}

	private void startDashboard() throws Exception {
		for( DashboardPartController controller : controllers.values() ) {
			controller.start();
		}
	}
	
	private void stopDashboard() {
		for( DashboardPartController controller : controllers.values() ) {
			controller.stop();
		}
	}
}
