package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.rcp.dashboard.cfg.DashboardConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;

public class ConfigureCurrentDashboardCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DashboardEditor dashboardEditor = (DashboardEditor) EditorUtil.determineActiveEditor();
		
		DashboardConfigurer configurer = new DashboardConfigurer(dashboardEditor.getDashboard());
		configurer.startConfigure(dashboardEditor.getDashboardFile());
		
		dashboardEditor.getDashboard().update();
		dashboardEditor.setDirty(false);
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		return EditorUtil.isActiveEditorDashboardEditor();
	}

}
