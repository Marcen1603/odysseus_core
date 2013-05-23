package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import java.util.List;

import net.jxta.id.ID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.sources.ViewAdvertisement;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.views.StreamsViewsPart;

public class ImportCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<StreamsViewsPart> optPart = StreamsViewsPart.getInstance();
		if( optPart.isPresent() ){
			StreamsViewsPart part = optPart.get();
			IP2PDictionary dictionary = P2PDictionaryService.get();
			
			Optional<ID> optSelectedID = part.getSelectedStreamOrViewID();
			if( optSelectedID.isPresent() ) {
				ID selectedID = optSelectedID.get();
				
				if( !dictionary.existsView(selectedID)) {
					throw new ExecutionException("Could not import since the selected id is not known");
				}
				
				List<ViewAdvertisement> views = dictionary.getViews(selectedID);
				
				// TODO: choose view name
				ViewAdvertisement viewAdv = views.get(0);
				
				try {
					dictionary.importView(optSelectedID.get(), viewAdv.getViewName());
					StatusBarManager.getInstance().setMessage("View/Stream successfully imported");
				} catch (PeerException e) {
					throw new ExecutionException("Could not import", e);
				}
			}
		}
		return null;
	}

}
