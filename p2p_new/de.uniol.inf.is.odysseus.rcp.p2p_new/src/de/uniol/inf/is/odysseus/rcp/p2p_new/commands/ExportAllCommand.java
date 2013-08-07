package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.SessionManagementService;

public class ExportAllCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExportAllCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Set<Entry<String, ILogicalOperator>> streamsAndViews = ServerExecutorService.getDataDictionary().getStreamsAndViews(SessionManagementService.getActiveSession());
		int okCount = 0;
		for( Entry<String, ILogicalOperator> streamOrView : streamsAndViews ) {
			String sourceName = streamOrView.getKey();
			
			// TODO: Transcfg wählen lassen
			try {
				if( !P2PDictionaryService.get().isExported(sourceName) && !P2PDictionaryService.get().isImported(sourceName)) {
					P2PDictionaryService.get().exportSource(sourceName, "Standard");
					okCount++;
				}
			} catch (PeerException e) {
				LOG.error("Could not export source {}", sourceName, e);
			}
		}
		
		if( okCount > 0 ) {
			StatusBarManager.getInstance().setMessage("Exported " + okCount + " sources");
		}
		return null;
	}

}
