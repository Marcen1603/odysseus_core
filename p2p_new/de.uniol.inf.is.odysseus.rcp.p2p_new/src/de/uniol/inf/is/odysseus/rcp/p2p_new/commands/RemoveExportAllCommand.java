package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisement;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;

public class RemoveExportAllCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ImmutableList<SourceAdvertisement> exportedSources = P2PDictionaryService.get().getExportedSources();
		for( SourceAdvertisement exportedSource : exportedSources ) {
			P2PDictionaryService.get().removeSourceExport(exportedSource.getName());
		}
		StatusBarManager.getInstance().setMessage("Removed " + exportedSources.size() + " exports");
		
		return null;
	}

}
