package de.uniol.inf.is.odysseus.rcp.dashboard.cfg;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.window.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardSettings;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.windows.DashboardConfigWindow;

public class DashboardConfigurer {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardConfigurer.class);
	private static final IDashboardHandler DASHBOARD_LOADER = new XMLDashboardHandler();
	
	private final Dashboard dashboard;
	
	public DashboardConfigurer( Dashboard dashboard ) {
		// Preconditions.checkNotNull(dashboard, "Dashboard to configure must not be null!");
		
		this.dashboard = dashboard;
	}
	
	public void startConfigure(IFile fileToSaveDashboard) {
		DashboardConfigWindow cfgWindow = new DashboardConfigWindow(null, dashboard, fileToSaveDashboard.getName());
		if( cfgWindow.open() == Window.OK ) {
			applySettingsToDashboard(dashboard, cfgWindow);
			trySaveDashboard(dashboard, fileToSaveDashboard);
		} 
	}

	private static void applySettingsToDashboard(Dashboard dashboard, DashboardConfigWindow cfgWindow) {
		IFile selectedImageFilename = cfgWindow.getBackgroundImageFile();
		
		DashboardSettings newSettings = new DashboardSettings(
				selectedImageFilename, 
				cfgWindow.isDasboardLocked(), 
				cfgWindow.isBackgroundImageStretched());
		
		dashboard.setSettings(newSettings);
	}

	private static void trySaveDashboard(Dashboard dashboard, IFile dashboardFile) {
		try {
			DASHBOARD_LOADER.save(dashboard, dashboardFile);
		} catch (DashboardHandlerException e) {
			LOG.error("Could not save dashboard", e);
		}
	}
}
