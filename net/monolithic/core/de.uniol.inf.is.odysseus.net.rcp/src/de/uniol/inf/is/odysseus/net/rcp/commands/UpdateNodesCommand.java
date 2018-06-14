package de.uniol.inf.is.odysseus.net.rcp.commands;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.rcp.views.NodeViewPart;
import de.uniol.inf.is.odysseus.net.update.OdysseusNodeUpdater;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class UpdateNodesCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<NodeViewPart> optView = NodeViewPart.getInstance();
		if( optView.isPresent() ) {
			NodeViewPart view = optView.get();
			Collection<IOdysseusNode> selectedNodes = view.getSelectedNodes();
			
			if( !selectedNodes.isEmpty() ) {
				if( MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Update remote nodes", "Are you sure to update selected remote nodes?") ) {
					OdysseusNodeUpdater.sendUpdateMessageToRemoteNodes(selectedNodes);
					StatusBarManager.getInstance().setMessage("Send update signal to " + selectedNodes.size() + " nodes");
				}
			}
		}
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		Optional<NodeViewPart> optView = NodeViewPart.getInstance();
		if( optView.isPresent() ) {
			NodeViewPart view = optView.get();
			Collection<IOdysseusNode> selectedNodes = view.getSelectedNodes();
			return !selectedNodes.isEmpty();
		}
		
		return false;
	}
}
