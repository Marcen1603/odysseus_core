package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.CommandUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;

public class RemoveDashboardPartCommand extends AbstractHandler implements IHandler {

	private Dashboard dashboard;
	private DashboardPartPlacement selectedDashboardPart;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		dashboard.remove(selectedDashboardPart);
		dashboard.update();
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		Optional<DashboardPartPlacement> optSelectedDashboardPart = CommandUtil.determineSelectedDashboardPart();
		if( optSelectedDashboardPart.isPresent() ) {
			if( EditorUtil.isActiveEditorDashboardEditor() ) {
				DashboardEditor editor = EditorUtil.determineActiveEditor();
				dashboard = editor.getDashboard();
				selectedDashboardPart = optSelectedDashboardPart.get();
				return true;
			}
		}
		return false;
	}

}
