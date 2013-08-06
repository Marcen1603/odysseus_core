package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.cfg.DashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.CommandUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;

public class ConfigureCurrentDashboardPartCommand extends AbstractHandler implements IHandler {

	private DashboardPartPlacement selectedPart;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IDashboardPart selectedDashboardPart = selectedPart.getDashboardPart();
		DashboardEditor dashboardEditor = (DashboardEditor)EditorUtil.determineActiveEditor();
		DashboardPartController dashboardPartController = dashboardEditor.getDashboardPartController(selectedDashboardPart);
		
		DashboardPartConfigurer configurer = new DashboardPartConfigurer(selectedDashboardPart, dashboardPartController);
		configurer.startConfigure();
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		Optional<DashboardPartPlacement> optDashboardPart = CommandUtil.determineSelectedDashboardPart();
		if( optDashboardPart.isPresent() ) {
			selectedPart = optDashboardPart.get();
			return true;
		}
		return false;
	}
}
