package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views.LogicRuleShowViewPart;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class DeleteLogicRuleCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<LogicRuleShowViewPart> optView = LogicRuleShowViewPart
				.getInstance();
		if (optView.isPresent()) {
			LogicRuleShowViewPart view = optView.get();

			List<LogicRule> selectedLogicRules = view.getSelectedLogicRules();
			if (!selectedLogicRules.isEmpty()) {
				MessageDialog msgDialog = new MessageDialog(Display
						.getCurrent().getActiveShell(), null, null,
						"Delete selected logic rule(s)?",
						MessageDialog.WARNING, new String[] { "Ok", "Cancel" },
						1);
				msgDialog.open();

				if (msgDialog.getReturnCode() == 0) {
					int okCount = 0;
					for (LogicRule rule : selectedLogicRules) {
						if (removeLogicRule(rule)) {
							okCount++;
						}
					}
					StatusBarManager.getInstance().setMessage(
							"Removed " + okCount + " logic rules");
					view.refresh();
				}
			}
		}
		return null;
	}

	private boolean removeLogicRule(LogicRule rule) {
		// System.out.println("DeleteLogicRule ActivityName:"+rule.getActivityName()+" Actor:"+rule.getActor().getName());
		
		if(SmartDevicePublisher.isLocalPeer(rule.getActor().getSmartDevice().getPeerID())){
			return rule.getActor().deleteLogicRule(rule);
		}else{
			rule.getActor().deleteLogicRule(rule);
			try {
				rule.getActor().save();
				return true;
			} catch (PeerCommunicationException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
