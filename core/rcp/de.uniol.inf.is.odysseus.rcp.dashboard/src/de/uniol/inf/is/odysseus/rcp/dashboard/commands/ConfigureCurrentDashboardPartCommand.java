package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.cfg.DashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.CommandUtil;

public class ConfigureCurrentDashboardPartCommand extends AbstractHandler implements IHandler {

	private DashboardPartPlacement selectedPart;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		DashboardPartConfigurer configurer = new DashboardPartConfigurer(selectedPart.getDashboardPart());
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
