package de.uniol.inf.is.odysseus.query.transformation.viewer.commands;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.query.transformation.viewer.gui.ExportQueryDialog;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.commands.AbstractQueryCommand;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

public class ExportQueryCommand extends AbstractQueryCommand {

	private static final Logger LOG = LoggerFactory.getLogger(ExportQueryCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Object> selections = SelectionProvider.getSelection(event);
		
		for (Object selection : selections) {
			IExecutor executor = OdysseusRCPPlugIn.getExecutor();
			if (executor != null) {
				int queryId = (Integer)selection;
		
				ExportQueryDialog transformationDialog = new ExportQueryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), queryId);
				transformationDialog.open();
						
			} else {
				LOG.error("Executor is not set");
			}						
		}
		return null;
	}
	
}
