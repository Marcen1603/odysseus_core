package de.uniol.inf.is.odysseus.peer.rcp.commands;

import java.util.Collection;

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

import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class ImportAllCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ImportAllCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IP2PDictionary dictionary = RCPP2PNewPlugIn.getP2PDictionary();

		Collection<SourceAdvertisement> sources = dictionary.getSources();
		int okCount = 0;
		int badCount = 0;
		for (SourceAdvertisement source : sources) {

			if (!dictionary.isImported(source) && !dictionary.isExported(source.getName())) {

				String sourceName = determineSourceNameToUse(dictionary, source);

				try {
					dictionary.importSource(source, sourceName);
					okCount++;
				} catch (PeerException e) {
					LOG.error("Could not import source with its name {}", sourceName, e);
					badCount++;
				} catch (InvalidP2PSource e) {
					LOG.error("Could not import source {}", sourceName, e);
					badCount++;
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Source is invalid", "The source " + sourceName + " cannot be imported\nsince is not valid anymore (e.g. peer not reachable).");
				} 
			}
		}
		
		StatusBarManager.getInstance().setMessage("Imported : " + okCount + ", Failed : " + badCount);

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
