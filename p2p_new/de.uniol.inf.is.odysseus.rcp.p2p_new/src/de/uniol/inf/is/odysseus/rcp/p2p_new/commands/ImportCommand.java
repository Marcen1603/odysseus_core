package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisement;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.views.P2PSourcesViewPart;

public class ImportCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ImportCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<P2PSourcesViewPart> optPart = P2PSourcesViewPart.getInstance();
		if (optPart.isPresent()) {
			P2PSourcesViewPart part = optPart.get();
			IP2PDictionary dictionary = P2PDictionaryService.get();

			List<SourceAdvertisement> selectedAdvertisements = part.getSelectedSourceAdvertisements();

			for (SourceAdvertisement selectedAdvertisement : selectedAdvertisements) {
				try {
					if (!dictionary.existsSource(selectedAdvertisement)) {
						throw new ExecutionException("Could not import since the selected source advertisement is not known");
					}
					// TODO: Alternativ-Advertisements auswählen lassen vom
					// Nutzer

					// TODO: Auswahl des Namens, falls vergeben
					dictionary.importSource(selectedAdvertisement, selectedAdvertisement.getName());
					StatusBarManager.getInstance().setMessage("Source successfully imported as " + selectedAdvertisement.getName());
					
				} catch (Throwable t) {
					LOG.error("Could not import", t);
				}
			}
		}
		return null;
	}
}
