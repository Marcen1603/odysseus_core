package de.uniol.inf.is.odysseus.peer.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.peer.rcp.views.P2PSourcesViewPart;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class RemoveImportCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Optional<P2PSourcesViewPart> optView = P2PSourcesViewPart.getInstance();
		if( optView.isPresent() ) {
			P2PSourcesViewPart view = optView.get();
			
			List<SourceAdvertisement> selectedAdvertisements = view.getSelectedSourceAdvertisements();
			if( !selectedAdvertisements.isEmpty() ) {
				int okCount = 0;
				for( SourceAdvertisement selectedAdvertisement : selectedAdvertisements ) {
					if( RCPP2PNewPlugIn.getP2PDictionary().removeSourceImport(selectedAdvertisement) ) {
						okCount++;
					}
				}
				StatusBarManager.getInstance().setMessage("Removed " + okCount + " imports");
			}
		}
		
		return null;
	}
}
