package de.uniol.inf.is.odysseus.net.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.rcp.views.NodeViewPart;

public class CleanNodeViewCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<NodeViewPart> optView = NodeViewPart.getInstance();
		if( optView.isPresent() ) {
			NodeViewPart view = optView.get();
			
			view.clean();
		}
		
		return null;
	}

}
