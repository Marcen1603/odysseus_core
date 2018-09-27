package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;

public class AddDashboardPartCommand extends AbstractHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AddDashboardPartCommand.class);
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();

	private List<IFile> dashboardPartFiles;
	private DashboardEditor dashboardEditor;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Map<IFile, IDashboardPart> dashboardParts = loadDashboardParts(dashboardPartFiles, dashboardEditor);
		if (dashboardParts.isEmpty()) {
			return null;
		}

		final Dashboard dashboard = dashboardEditor.getDashboard();

		for (final IFile dashboardPartFile : dashboardParts.keySet()) {
			final DashboardPartPlacement place = new DashboardPartPlacement(dashboardParts.get(dashboardPartFile), dashboardPartFile.getFullPath().toString(), 0, 0, 200, 200);
			dashboard.add(place);
		}

		dashboardEditor.setDirty(true);

		return null;
	}

	@Override
	public boolean isEnabled() {

		if( EditorUtil.isActiveEditorDashboardEditor() ) {
			dashboardEditor = EditorUtil.determineActiveEditor();
			
			final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
			final List<Object> selectedObjects = getSelectedObjects(selection);
			dashboardPartFiles = getDashboardPartFiles(selectedObjects);
			
			return !dashboardPartFiles.isEmpty();
		} 
		
		return false;
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

	private static boolean isDashboardPartFile(Object obj) {
		return obj instanceof IFile && ((IFile) obj).getFileExtension().equals(DashboardPlugIn.DASHBOARD_PART_EXTENSION);
	}

	private static Map<IFile, IDashboardPart> loadDashboardParts(List<IFile> dashboardPartFiles, IWorkbenchPart partToShow) {
		final Map<IFile, IDashboardPart> parts = Maps.newHashMap();

		for (final IFile dashboardPartFile : dashboardPartFiles) {
			try {
				final IDashboardPart part = DASHBOARD_PART_HANDLER.load(dashboardPartFile, partToShow);
				parts.put(dashboardPartFile, part);
			} catch (FileNotFoundException | DashboardHandlerException ex) {
				LOG.error("Could not load dashboardPart from file {}!", dashboardPartFile, ex);
			}
		}

		return parts;
	}
}
