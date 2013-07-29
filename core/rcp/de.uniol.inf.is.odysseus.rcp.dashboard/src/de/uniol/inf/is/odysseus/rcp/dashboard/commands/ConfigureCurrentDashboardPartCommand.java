package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.windows.DashboardPartConfigWindow;

public class ConfigureCurrentDashboardPartCommand extends AbstractHandler implements IHandler {

	private DashboardPartPlacement selectedPart;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Configuration config = selectedPart.getDashboardPart().getConfiguration();
		if( !config.getSettings().isEmpty() ) {
			DashboardPartConfigWindow window = new DashboardPartConfigWindow(determineCurrentShell(), config);
			window.open();

			if( applyNewSettings(config, window.getSelectedSettings()) ) {
				DashboardEditor dashboardEditor = (DashboardEditor) EditorUtil.determineActiveEditor();
				dashboardEditor.setDirty(true);
			}
		} else {
			MessageDialog.openInformation(determineCurrentShell(),  "DashboardPart configuration", "This dashboard part has no settings to configure!");
		}
		
		return null;
	}

	private boolean applyNewSettings(Configuration config, Map<String, String> settings) {
		boolean changed = false;
		for (final String key : settings.keySet()) {
			String newValue = settings.get(key);
			String oldValue = (config.get(key) != null) ? config.get(key).toString() : "";
			
			if( !oldValue.equals(newValue)) {
				config.setAsString(key, settings.get(key));
				changed = true;
			}
		}
		return changed;
	}
	
	private static Shell determineCurrentShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
	@Override
	public boolean isEnabled() {
		Optional<DashboardPartPlacement> optDashboardPart = determineSelectedDashboardPart();
		if( optDashboardPart.isPresent() ) {
			selectedPart = optDashboardPart.get();
			return true;
		}
		return false;
	}

	private Optional<DashboardPartPlacement> determineSelectedDashboardPart() {
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
		if( selection instanceof IStructuredSelection ) {
			IStructuredSelection strucSelection = (IStructuredSelection)selection;
			Object selectedObject = strucSelection.getFirstElement();
			if( selectedObject instanceof DashboardPartPlacement ) {
				return Optional.of((DashboardPartPlacement)selectedObject); 
			}
		}
		return Optional.absent();
	}

}
