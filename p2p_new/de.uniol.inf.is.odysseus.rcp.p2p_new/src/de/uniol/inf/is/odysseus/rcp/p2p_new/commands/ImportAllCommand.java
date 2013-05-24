package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisement;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.SessionManagementService;

public class ImportAllCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ImportAllCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IP2PDictionary dictionary = P2PDictionaryService.get();
		IDataDictionary dataDictionary = DataDictionaryService.get();

		ImmutableList<SourceAdvertisement> sources = dictionary.getSources();
		int okCount = 0;
		int badCount = 0;
		for (SourceAdvertisement source : sources) {

			if (!dictionary.isImported(source) && !dictionary.isExported(source.getName())) {

				try {
					if (dataDictionary.containsViewOrStream(source.getName(), SessionManagementService.getActiveSession())) {
						// TODO: choose name is needed
						throw new PeerException("The name " + source.getName() + " is already used locally");
					}
					
					dictionary.importSource(source, source.getName());
					okCount++;
				} catch (PeerException e) {
					LOG.error("Could not import source with its name {}", source.getName(), e);
					badCount++;
				}
			}
		}
		
		StatusBarManager.getInstance().setMessage("Imported : " + okCount + ", Failed : " + badCount);

		return null;
	}

}
