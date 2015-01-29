package de.uniol.inf.is.odysseus.peer.rcp.commands;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class ExportAllCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExportAllCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Set<Entry<Resource, ILogicalOperator>> streamsAndViews = RCPP2PNewPlugIn.getServerExecutor().getDataDictionary(RCPP2PNewPlugIn.getActiveSession().getTenant()).getStreamsAndViews(RCPP2PNewPlugIn.getActiveSession());
		
		List<String> sourceNames = Lists.newArrayList();
		for( Entry<Resource, ILogicalOperator> streamOrView : streamsAndViews ) {
			// FIXME: Use Resources
			//Resource sourceName = streamOrView.getKey();
			String sourceName = streamOrView.getKey().toString();
			if( !RCPP2PNewPlugIn.getP2PDictionary().isExported(sourceName) && !RCPP2PNewPlugIn.getP2PDictionary().isImported(sourceName)) {
				sourceNames.add(sourceName);
			}
		}
			
		// TODO: Transcfg wählen lassen
		try {
			RCPP2PNewPlugIn.getP2PDictionary().exportSources(sourceNames);
		} catch (PeerException e) {
			LOG.error("Could not export source {}", sourceNames, e);
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Invalid source", "Could not export source " + sourceNames + ":\n" + e.getMessage());
		}
		
		if( !sourceNames.isEmpty() ) {
			StatusBarManager.getInstance().setMessage("Exported " + sourceNames.size() + " sources");
		}
		return null;
	}

}
