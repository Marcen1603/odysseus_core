package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views.ActivityInterpreterShowView;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServer;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class DeleteActivityInterpreterCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<ActivityInterpreterShowView> optView = ActivityInterpreterShowView
				.getInstance();
		if (optView.isPresent()) {
			ActivityInterpreterShowView view = optView.get();

			List<ActivityInterpreter> selectedLogicRules = view.getSelectedActivityInterpreters();
			if (!selectedLogicRules.isEmpty()) {
				MessageDialog msgDialog = new MessageDialog(Display
						.getCurrent().getActiveShell(), null, null,
						"Delete selected activity interpreter(s)?",
						MessageDialog.WARNING, new String[] { "Ok", "Cancel" },
						1);
				msgDialog.open();

				if (msgDialog.getReturnCode() == 0) {
					int okCount = 0;
					for (ActivityInterpreter rule : selectedLogicRules) {
						if (removeActivityInterpreter(rule)) {
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

	private boolean removeActivityInterpreter(ActivityInterpreter interpreter) {
		System.out.println("removeActivityInterpreter ActivityName:"+interpreter.getActivityName()+" Sensor:"+interpreter.getSensor().getName());
		
		if(SmartDeviceServer.isLocalPeer(interpreter.getSensor().getSmartDevice().getPeerID())){
			return interpreter.getSensor().removeActivityInterpreter(interpreter);
		}else{
			try {
				interpreter.getSensor().removeActivityInterpreter(interpreter);
				interpreter.getSensor().save();
				return true;
			} catch (PeerCommunicationException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
