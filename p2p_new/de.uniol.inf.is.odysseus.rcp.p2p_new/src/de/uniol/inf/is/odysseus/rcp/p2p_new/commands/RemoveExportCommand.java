package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.views.P2PSourcesViewPart;

public class RemoveExportCommand extends AbstractHandler implements IHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Object> selections = SelectionHelper.getSelection();
		if( !selections.isEmpty() ) {
			
			int removedOk = 0;
			for (Object selectedObject : selections) {
				if( selectedObject instanceof Entry ) {
					// From sourcesView
					Entry<?, ?> selectedEntry = (Entry<?,?>) selectedObject;
					String sourceName = (String) selectedEntry.getKey();
					if( P2PDictionaryService.get().removeSourceExport(sourceName) ) {
						removedOk++;
					}
				} else if( selectedObject instanceof P2PSourcesViewPart.TableEntry ) {
					// From p2pSourcesView
					P2PSourcesViewPart.TableEntry selectedTableEntry = (P2PSourcesViewPart.TableEntry)selectedObject;
					SourceAdvertisement adv = selectedTableEntry.advertisement;
					if( P2PDictionaryService.get().removeSourceExport(adv.getName()) ) {
						removedOk++;
					}
				}
			}
			StatusBarManager.getInstance().setMessage("Removed " + removedOk + " exports");
		}
		return null;
	}
}
