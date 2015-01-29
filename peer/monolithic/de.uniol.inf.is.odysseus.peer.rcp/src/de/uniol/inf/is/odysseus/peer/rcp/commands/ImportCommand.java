package de.uniol.inf.is.odysseus.peer.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.peer.rcp.views.P2PSourcesViewPart;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class ImportCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ImportCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<P2PSourcesViewPart> optPart = P2PSourcesViewPart.getInstance();
		if (optPart.isPresent()) {
			P2PSourcesViewPart part = optPart.get();
			IP2PDictionary dictionary = RCPP2PNewPlugIn.getP2PDictionary();

			List<SourceAdvertisement> selectedAdvertisements = part.getSelectedSourceAdvertisements();

			int okCount = 0;
			for (SourceAdvertisement selectedAdvertisement : selectedAdvertisements) {
				if (dictionary.isImported(selectedAdvertisement)) {
					throw new ExecutionException("Could not import an already imported source");
				}
				
				String sourceName = determineSourceNameToUse(dictionary, selectedAdvertisement);
				
				try {
					dictionary.importSource(selectedAdvertisement, sourceName);
					
					okCount++;
				} catch (PeerException e) {
					LOG.error("Could not import source {}", sourceName, e);
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Invalid source", "Could not import source " + sourceName + " since\nit is invalid (e.g. peer is not reachable or files are not available).");
				} catch (InvalidP2PSource e) {
					LOG.error("Could not import source {}", sourceName, e);
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Invalid source", "Could not import source " + sourceName + " since\nit is invalid (e.g. peer is not reachable or files are not available).");
				}
			}
			StatusBarManager.getInstance().setMessage(okCount + " source(s) successfully imported");
		}
		return null;
	}

	private static String determineSourceNameToUse(IP2PDictionary dictionary, SourceAdvertisement selectedAdvertisement) {
		String sourceName = selectedAdvertisement.getName();
		while( dictionary.isSourceNameAlreadyInUse(sourceName)) {
			InputDialog inputDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Import P2P source", "Please select another name for the imported P2P source", sourceName + "_2", null );
			if( inputDialog.open() == Window.CANCEL) {
				return null;
			} 
			sourceName = inputDialog.getValue();
		}
		return sourceName;
	}
}
