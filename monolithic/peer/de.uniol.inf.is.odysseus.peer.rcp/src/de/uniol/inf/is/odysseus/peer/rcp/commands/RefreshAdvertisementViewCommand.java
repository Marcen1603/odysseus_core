package de.uniol.inf.is.odysseus.peer.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.rcp.views.AdvertisementsView;

public class RefreshAdvertisementViewCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<AdvertisementsView> optInstance = AdvertisementsView.getInstance();
		if(optInstance.isPresent()) {
			optInstance.get().refreshTable();
		}
		return null;
	}

}
