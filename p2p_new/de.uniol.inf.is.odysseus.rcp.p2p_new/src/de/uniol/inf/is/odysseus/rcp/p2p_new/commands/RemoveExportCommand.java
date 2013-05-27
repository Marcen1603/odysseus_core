package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisement;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.views.P2PSourcesViewPart;

public class RemoveExportCommand extends AbstractHandler implements IHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<P2PSourcesViewPart> optView = P2PSourcesViewPart.getInstance();
		if( optView.isPresent() ) {
			P2PSourcesViewPart view = optView.get();
			
			List<SourceAdvertisement> selectedAdvertisements = view.getSelectedSourceAdvertisements();
			if( !selectedAdvertisements.isEmpty() ) {
				
				int okCount = 0;
				for( SourceAdvertisement selectedAdvertisement : selectedAdvertisements ) {
					if( P2PDictionaryService.get().removeSourceExport(selectedAdvertisement.getName()) ) {
						okCount++;
					}
				}
				
				StatusBarManager.getInstance().setMessage("Removed " + okCount + " exports");
			}
		}
		return null;
	}
}
