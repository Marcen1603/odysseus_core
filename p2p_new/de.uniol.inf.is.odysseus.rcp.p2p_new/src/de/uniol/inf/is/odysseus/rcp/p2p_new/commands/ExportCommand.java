package de.uniol.inf.is.odysseus.rcp.p2p_new.commands;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;

public class ExportCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExportCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Entry<?, ?>> selections = SelectionHelper.getSelection();

		int okCount = 0;
		for (Entry<?, ?> selectedObject : selections) {

			// TODO: Transformationscfg wählen lassen
			String sourceName = (String) selectedObject.getKey();
			try {
				if (!P2PDictionaryService.get().isExported(sourceName) && !P2PDictionaryService.get().isImported(sourceName)) {
					P2PDictionaryService.get().exportSource(sourceName, "Standard");
					okCount++;
				}
			} catch (PeerException e) {
				LOG.error("Could not export {}", sourceName, e);
			}

			StatusBarManager.getInstance().setMessage("Exported " + okCount + " sources");
		}
		return null;
	}
}
