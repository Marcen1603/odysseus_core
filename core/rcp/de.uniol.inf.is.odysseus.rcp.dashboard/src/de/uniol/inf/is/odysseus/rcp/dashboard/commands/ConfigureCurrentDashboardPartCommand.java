package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.cfg.DashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.CommandUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;

public class ConfigureCurrentDashboardPartCommand extends AbstractHandler implements IHandler {

	private DashboardPartPlacement selectedPart;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Configuration config = selectedPart.getDashboardPart().getConfiguration();
		if( !config.getSettings().isEmpty() ) {
			DashboardPartConfigurer configurer = new DashboardPartConfigurer(selectedPart.getDashboardPart());
			configurer.startConfigure();
			
		} else {
			MessageDialog.openInformation(EditorUtil.determineCurrentShell(),  "DashboardPart configuration", "This dashboard part has no settings to configure!");
		}
		
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
