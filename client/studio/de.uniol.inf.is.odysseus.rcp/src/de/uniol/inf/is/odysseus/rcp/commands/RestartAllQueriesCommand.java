package de.uniol.inf.is.odysseus.rcp.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

public class RestartAllQueriesCommand extends AbstractRestartQueryCommand{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
		if (executor != null) {			
			restart(executor.getLogicalQueryIds(OdysseusRCPPlugIn.getActiveSession()));
		} else {
			logger.error(OdysseusNLS.NoExecutorFound);
			MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
			box.setMessage(OdysseusNLS.NoExecutorFound);
			box.setText("Error");
			box.open();

			return null;
		}
		return null;
	}

}
