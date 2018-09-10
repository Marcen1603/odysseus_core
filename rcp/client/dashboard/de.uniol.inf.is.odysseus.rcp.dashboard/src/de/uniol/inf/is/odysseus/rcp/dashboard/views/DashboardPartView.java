package de.uniol.inf.is.odysseus.rcp.dashboard.views;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.ControllerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;

public class DashboardPartView extends ViewPart {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartView.class);
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();
	
	private Composite parent;
	private IDashboardPart dashboardPart;
	private DashboardPartController dashboardPartController;
	
	private boolean isShowing;
	private Composite comp;
	private ToolBar toolBar;
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		this.parent = parent;
	}

	@Override
	public void setFocus() {
		
	}
	

	/**
	 * Cleans The View if there is a Dashboardpart showing. 
	 * You have to call this before you can show a new dashboardPartFile in this View.
	 */
	public void unshowDashboardPart() {
		if (isShowing) {
			if( dashboardPartController != null && dashboardPartController.isStarted() ) {
				dashboardPartController.stop();
			}
			comp.dispose();
			toolBar.dispose();
			isShowing = false;
			parent.layout();
		}
	}

	public void showDashboardPart(IFile dashboardPartFile) {
		if( isShowing ) {
			return;
		}
		
		comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setLayout(new GridLayout());
		
		toolBar = new ToolBar(parent, SWT.WRAP | SWT.RIGHT);
		
		try {
			dashboardPart = DASHBOARD_PART_HANDLER.load(dashboardPartFile, this);
			dashboardPartController = new DashboardPartController(dashboardPart);

			dashboardPart.createPartControl(comp, toolBar);
			dashboardPartController.start();
			
			parent.layout();
			
			isShowing = true;
			setPartName(dashboardPartFile.getName());
			
		} catch (FileNotFoundException | DashboardHandlerException | ControllerException e) {
			LOG.error("Could not load and start dashboard part", e);
		}
	}
	
	@Override
	public void dispose() {
		if( dashboardPartController != null && dashboardPartController.isStarted() ) {
			dashboardPartController.stop();
		}
		
		super.dispose();
	}
}
