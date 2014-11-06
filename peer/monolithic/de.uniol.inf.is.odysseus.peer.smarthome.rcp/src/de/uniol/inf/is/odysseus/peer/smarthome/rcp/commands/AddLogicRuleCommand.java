package de.uniol.inf.is.odysseus.peer.smarthome.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.smarthome.rcp.views.SmartDeviceLogicView;


public class AddLogicRuleCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<SmartDeviceLogicView> optView = SmartDeviceLogicView.getInstance();
		if( optView.isPresent() ) {
			System.out.println("AddLogicRuleCommand");
			
			
		}
		
		return null;
	}

}
