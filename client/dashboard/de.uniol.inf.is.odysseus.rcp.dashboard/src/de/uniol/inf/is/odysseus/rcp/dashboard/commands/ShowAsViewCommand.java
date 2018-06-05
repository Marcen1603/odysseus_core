package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.views.DashboardPartView;

public class ShowAsViewCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ShowAsViewCommand.class);

	private List<IFile> dashboardPartFiles;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		for( IFile dashboardPartFile : dashboardPartFiles ) {
			tryShowView(event, DashboardPlugIn.DASHBOARD_PART_VIEW_ID, dashboardPartFile);
		}
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		final List<Object> selectedObjects = getSelectedObjects(selection);
		dashboardPartFiles = getDashboardPartFiles(selectedObjects);

		return !dashboardPartFiles.isEmpty();
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

	private static List<IFile> getDashboardPartFiles(List<Object> selectedObjects) {
		LOG.debug("" + selectedObjects);
		final List<IFile> foundFiles = Lists.newArrayList();
		for (final Object obj : selectedObjects) {
			if (isDashboardPartFile(obj)) {
				foundFiles.add((IFile) obj);
			}
		}
		LOG.debug("" + foundFiles);
		return foundFiles;
	}
	
	private static boolean isDashboardPartFile(Object obj) {
		return obj instanceof IFile && ((IFile) obj).getFileExtension().equals(DashboardPlugIn.DASHBOARD_PART_EXTENSION);
	}
	
	private static void tryShowView(ExecutionEvent event, String viewID, IFile dashboardPartFile) {
		try {
			String name = dashboardPartFile.getFullPath().toString() + dashboardPartFile.getName();
			DashboardPartView part = (DashboardPartView)HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(viewID, viewID + name, IWorkbenchPage.VIEW_VISIBLE);
			part.showDashboardPart( dashboardPartFile );
		} catch (PartInitException e) {
			LOG.error("Could not show view");
		}
	}
}
