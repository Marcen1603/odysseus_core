package de.uniol.inf.is.odysseus.peer.rcp.commands;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.peer.rcp.views.P2PSourcesViewPart;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class RemoveExportCommand extends AbstractHandler implements IHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Object> selections = SelectionHelper.getSelection();
		if( !selections.isEmpty() ) {
			
			List<String> srcs = Lists.newArrayList();
			for (Object selectedObject : selections) {
				if( selectedObject instanceof Entry ) {
					// From sourcesView
					Entry<?, ?> selectedEntry = (Entry<?,?>) selectedObject;
					String sourceName = (String) selectedEntry.getKey();
					srcs.add(sourceName);
				} else if( selectedObject instanceof P2PSourcesViewPart.TableEntry ) {
					// From p2pSourcesView
					P2PSourcesViewPart.TableEntry selectedTableEntry = (P2PSourcesViewPart.TableEntry)selectedObject;
					SourceAdvertisement adv = selectedTableEntry.advertisement;
					srcs.add(adv.getName());
				}
			}
			RCPP2PNewPlugIn.getP2PDictionary().removeSourcesExport(srcs);
			StatusBarManager.getInstance().setMessage("Removed " + srcs.size() + " exports");
		}
		return null;
	}
}
