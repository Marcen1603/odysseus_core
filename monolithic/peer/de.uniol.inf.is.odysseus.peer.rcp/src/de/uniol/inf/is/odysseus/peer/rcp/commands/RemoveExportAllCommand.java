package de.uniol.inf.is.odysseus.peer.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

public class RemoveExportAllCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ImmutableList<SourceAdvertisement> exportedSources = RCPP2PNewPlugIn.getP2PDictionary().getExportedSources();
		List<String> srcs = Lists.newArrayList();
		for( SourceAdvertisement exportedSource : exportedSources ) {
			srcs.add(exportedSource.getName());
		}
		RCPP2PNewPlugIn.getP2PDictionary().removeSourcesExport(srcs);
		
		StatusBarManager.getInstance().setMessage("Removed " + exportedSources.size() + " exports");
		
		return null;
	}

}
