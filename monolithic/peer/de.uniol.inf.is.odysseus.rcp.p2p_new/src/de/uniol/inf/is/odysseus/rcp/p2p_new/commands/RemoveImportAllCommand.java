package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.RCPP2PNewPlugIn;

public class RemoveImportAllCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		List<SourceAdvertisement> importedSources = RCPP2PNewPlugIn.getP2PDictionary().getImportedSources();
		int okCount = 0;
		for( SourceAdvertisement importedSource : importedSources ) {
			if( RCPP2PNewPlugIn.getP2PDictionary().removeSourceImport(importedSource) ) {
				okCount++;
			}
		}
		
		if( !importedSources.isEmpty() ) {
			StatusBarManager.getInstance().setMessage("Removed " + okCount + " imports");
		} else {
			StatusBarManager.getInstance().setMessage("No imports to remove");
		}
		
		return null;
	}

}
