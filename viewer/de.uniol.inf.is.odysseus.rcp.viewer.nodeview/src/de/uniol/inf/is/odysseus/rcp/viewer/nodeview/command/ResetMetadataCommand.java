package de.uniol.inf.is.odysseus.rcp.viewer.nodeview.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;

public class ResetMetadataCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Object obj = Helper.getSelection(event);
		if (obj instanceof IMonitoringData<?>) {
			IMonitoringData<?> monData = (IMonitoringData<?>) obj;
			monData.reset();
			System.out.println("Metadata " + monData.getType() + " resetted");
		}

		Helper.getTreeViewer(event).refresh();

		return null;
	}

}
