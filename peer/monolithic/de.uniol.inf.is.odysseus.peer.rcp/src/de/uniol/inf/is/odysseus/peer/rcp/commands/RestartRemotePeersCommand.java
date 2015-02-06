package de.uniol.inf.is.odysseus.peer.rcp.commands;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.rcp.views.peer.PeerView;
import de.uniol.inf.is.odysseus.peer.update.PeerUpdatePlugIn;

public class RestartRemotePeersCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<PeerView> optPeerView = PeerView.getInstance();
		if( optPeerView.isPresent() ) {
			PeerView peerView = optPeerView.get();
			Collection<PeerID> selectedPeerIDs = peerView.getSelectedPeerIDs();
			
			if( selectedPeerIDs.isEmpty() ) {
				if( MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Restart remote peers", "Are you sure to restart ALL remote peers?") ) {
					PeerUpdatePlugIn.sendRestartMessageToRemotePeers();
				}
			} else {
				if( MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Restart remote peers", "Are you sure to restart the selected " + selectedPeerIDs.size() + " remote peer(s)?") ) {
					PeerUpdatePlugIn.sendRestartMessageToRemotePeers(selectedPeerIDs);
				}
			}
		}
		
		return null;
	}

}
