package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.RCPP2PNewPlugIn;

public class ExportAllCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExportAllCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Set<Entry<Resource, ILogicalOperator>> streamsAndViews = RCPP2PNewPlugIn.getServerExecutor().getDataDictionary(RCPP2PNewPlugIn.getActiveSession().getTenant()).getStreamsAndViews(RCPP2PNewPlugIn.getActiveSession());
		int okCount = 0;
		for( Entry<Resource, ILogicalOperator> streamOrView : streamsAndViews ) {
			// FIXME: Use Resources
			//Resource sourceName = streamOrView.getKey();
			String sourceName = streamOrView.getKey().toString();
			
			// TODO: Transcfg wählen lassen
			try {
				if( !RCPP2PNewPlugIn.getP2PDictionary().isExported(sourceName) && !RCPP2PNewPlugIn.getP2PDictionary().isImported(sourceName)) {
					RCPP2PNewPlugIn.getP2PDictionary().exportSource(sourceName, "Standard");
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
