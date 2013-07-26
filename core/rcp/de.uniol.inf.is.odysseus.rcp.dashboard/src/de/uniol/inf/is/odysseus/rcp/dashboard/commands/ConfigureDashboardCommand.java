package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import java.io.FileNotFoundException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.windows.DashboardConfigWindow;

public class ConfigureDashboardCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigureDashboardCommand.class);
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();
	private static final IDashboardHandler DASHBOARD_LOADER = new XMLDashboardHandler();
	
	private List<IFile> dashboardFiles;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IFile dashboardFile = dashboardFiles.get(0);
		
		Optional<Dashboard> optDashboard = tryLoadDashboard(dashboardFile);
		if( optDashboard.isPresent() ) {
			Dashboard dashboard = optDashboard.get();
			
			DashboardConfigWindow cfgWindow = new DashboardConfigWindow(null, dashboard);
			if( cfgWindow.open() == Window.OK ) {
				applySettingsToDashboard(dashboard, cfgWindow);
				
				trySaveDashboard(dashboard, dashboardFile);
			} 
		}
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		final List<Object> selectedObjects = getSelectedObjects(selection);
		dashboardFiles = getDashboardFiles(selectedObjects);

		return dashboardFiles.size() == 1;
	}

	private static void applySettingsToDashboard(Dashboard dashboard, DashboardConfigWindow cfgWindow) {
		IFile selectedImageFilename = cfgWindow.getBackgroundImageFile();
		dashboard.setBackgroundImageFilename(selectedImageFilename);
		dashboard.setLock(cfgWindow.isDasboardLocked());
	}
	
	@SuppressWarnings("unchecked")
	private static List<Object> getSelectedObjects(ISelection selection) {
		if (selection == null) {
			return Lists.newArrayList();
		}

		if (selection instanceof StructuredSelection) {
			final StructuredSelection structuredSelection = (StructuredSelection) selection;
			return ImmutableList.<Object> copyOf(structuredSelection.toList());
		}

		return Lists.newArrayList();
	}
	

	private static void trySaveDashboard(Dashboard dashboard, IFile dashboardFile) {
		try {
			FileUtil.write(DASHBOARD_LOADER.save(dashboard), dashboardFile);
		} catch (DashboardHandlerException | CoreException e) {
			LOG.error("Could not save dashboard", e);
		} 
	}
	
	private static List<IFile> getDashboardFiles(List<Object> selectedObjects) {
		final List<IFile> foundFiles = Lists.newArrayList();
		for (final Object obj : selectedObjects) {
			if (isDashboardFile(obj)) {
				foundFiles.add((IFile) obj);
			}
		}
		return foundFiles;
	}

	private static boolean isDashboardFile(Object obj) {
		return obj instanceof IFile && ((IFile) obj).getFileExtension().equals(DashboardPlugIn.DASHBOARD_EXTENSION);
	}

	private static Optional<Dashboard> tryLoadDashboard(IFile dashboardFile) {
		try {
			Dashboard dashboard = DASHBOARD_LOADER.load(FileUtil.read(dashboardFile), DASHBOARD_PART_HANDLER);
			return Optional.of(dashboard);

		} catch (FileNotFoundException | DashboardHandlerException | CoreException e) {
			LOG.error("Could not load dashboard from file {}", dashboardFile.getName(), e);
			MessageDialog.openError(null, "Dashboard configuration", "Could not load dashboard");
		}
		return Optional.absent();
	}
}
