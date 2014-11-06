package de.uniol.inf.is.odysseus.peer.smarthome.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.rcp.views.SmartDeviceLogicView;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;


public class DeleteLogicRuleCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<SmartDeviceLogicView> optView = SmartDeviceLogicView.getInstance();
		if( optView.isPresent() ) {
			System.out.println("DeleteLogicRuleCommand");
			
			SmartDeviceLogicView view = optView.get();
			
			List<LogicRule> selectedLogicRules = view.getSelectedLogicRules();
			if( !selectedLogicRules.isEmpty() ) {
				int okCount = 0;
				for( LogicRule rule : selectedLogicRules ) {
					if( removeLogicRule(rule) ) {
						okCount++;
					}
				}
				StatusBarManager.getInstance().setMessage("Removed " + okCount + " logic rules");
				view.refresh();
			}
		}
		
		return null;
	}

	private boolean removeLogicRule(LogicRule rule) {
		//System.out.println("DeleteLogicRule ActivityName:"+rule.getActivityName()+" Actor:"+rule.getActor().getName());
		
		return rule.getActor().deleteLogicRule(rule);
	}

}
