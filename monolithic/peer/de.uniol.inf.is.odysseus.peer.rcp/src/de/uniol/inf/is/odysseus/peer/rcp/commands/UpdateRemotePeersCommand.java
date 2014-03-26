package de.uniol.inf.is.odysseus.peer.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.peer.update.PeerUpdatePlugIn;

public class UpdateRemotePeersCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if( MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Update remote peers", "Are you sure to update remote peers?") ) {
			PeerUpdatePlugIn.sendUpdateMessageToRemotePeers();
		}
		
		return null;
	}

}
