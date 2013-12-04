package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
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

			int okCount = 0;
			for (SourceAdvertisement selectedAdvertisement : selectedAdvertisements) {
				if (dictionary.isImported(selectedAdvertisement)) {
					throw new ExecutionException("Could not import an already imported source");
				}
				// TODO: Alternativ-Advertisements auswählen lassen vom
				// Nutzer

				// TODO: Auswahl des Namens, falls vergeben
				try {
					dictionary.importSource(selectedAdvertisement, selectedAdvertisement.getName());
					
					okCount++;
				} catch (PeerException e) {
					LOG.error("Could not import source {}", selectedAdvertisement.getName(), e);
				} catch (InvalidP2PSource e) {
					LOG.error("Could not import source {}", selectedAdvertisement.getName(), e);
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Invalid source", "Could not import source " + selectedAdvertisement.getName() + " since\nit is invalid (e.g. peer is not reachable).");
				}
			}
			StatusBarManager.getInstance().setMessage(okCount + " source(s) successfully imported");
		}
		return null;
	}
}
