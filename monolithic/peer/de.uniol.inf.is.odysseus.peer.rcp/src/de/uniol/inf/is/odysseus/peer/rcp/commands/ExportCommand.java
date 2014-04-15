package de.uniol.inf.is.odysseus.peer.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class ExportCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExportCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<ViewInformation> selections = SelectionHelper.getSelection();

		List<String> sourceNames = Lists.newArrayList();
		for( ViewInformation streamOrView : selections ) {
			// FIXME: Use Resources
			//Resource sourceName = streamOrView.getKey();
			String sourceName = streamOrView.getName().getResourceName().toString();
			if( !RCPP2PNewPlugIn.getP2PDictionary().isExported(sourceName) && !RCPP2PNewPlugIn.getP2PDictionary().isImported(sourceName)) {
				sourceNames.add(sourceName);
			}
		}
			
		// TODO: Transcfg wählen lassen
		try {
			RCPP2PNewPlugIn.getP2PDictionary().exportSources(sourceNames);
		} catch (PeerException e) {
			LOG.error("Could not export source {}", sourceNames, e);
		}
		
		if( !sourceNames.isEmpty() ) {
			StatusBarManager.getInstance().setMessage("Exported " + sourceNames.size() + " sources");
		}
		return null;
	}
}
