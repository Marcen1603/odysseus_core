package de.uniol.inf.is.odysseus.rcp.viewer.queryview.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;

public class CopyCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object obj = Helper.getSelection(event);
		if (obj instanceof IQuery) {
			IQuery query = ((IQuery) obj);
			
			Clipboard cb = new Clipboard(HandlerUtil.getActiveWorkbenchWindow(event).getShell().getDisplay());
			TextTransfer textTransfer = TextTransfer.getInstance();
			cb.setContents(new Object[] { query.getQueryText() }, new Transfer[] { textTransfer });
			StatusBarManager.getInstance().setMessage("Copy successful");
		}
		
		return null;
	}

}
