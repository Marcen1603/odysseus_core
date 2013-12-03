package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.cfg.DashboardConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;

public class ConfigureDashboardCommand extends AbstractHandler implements IHandler {

	private List<IFile> dashboardFiles;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IFile dashboardFile = dashboardFiles.get(0);
		
		Optional<Dashboard> optDashboard = Optional.absent();
		Optional<DashboardEditor> optDashboardEditor = Optional.absent();
		
		Optional<IFile> optDashboardFileInEditor = determineDashboardFromEditor();
		if( optDashboardFileInEditor.isPresent() ) {
			IFile dashboardFileInEditor = optDashboardFileInEditor.get();
			if( dashboardFileInEditor.equals(dashboardFile)) {
				optDashboardEditor = Optional.of((DashboardEditor) EditorUtil.determineActiveEditor());
				optDashboard = Optional.of(optDashboardEditor.get().getDashboard());
			}
		}
		
		if( optDashboard.isPresent() ) {
			DashboardConfigurer dashboardConfigurer = new DashboardConfigurer(optDashboard.get());
			dashboardConfigurer.startConfigure(dashboardFile);
			
			if( optDashboardEditor.isPresent() ) {
				optDashboardEditor.get().getDashboard().update();
				optDashboardEditor.get().setDirty(false);
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

	private static Optional<IFile> determineDashboardFromEditor() {
		if (EditorUtil.isActiveEditorDashboardEditor()) {
			DashboardEditor dashboardEditor = (DashboardEditor) EditorUtil.determineActiveEditor();
			return Optional.of(dashboardEditor.getDashboardFile());
		}
		return Optional.absent();
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
}
