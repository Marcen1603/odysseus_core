package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.RCPP2PNewPlugIn;

public class RemoveExportAllCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ImmutableList<SourceAdvertisement> exportedSources = RCPP2PNewPlugIn.getP2PDictionary().getExportedSources();
		for( SourceAdvertisement exportedSource : exportedSources ) {
			RCPP2PNewPlugIn.getP2PDictionary().removeSourceExport(exportedSource.getName());
		}
		StatusBarManager.getInstance().setMessage("Removed " + exportedSources.size() + " exports");
		
		return null;
	}

}
