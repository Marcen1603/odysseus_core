package de.uniol.inf.is.odysseus.cep.cepviewer.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

public class RemoveAllCommand extends AbstractHandler implements IHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		for (IViewReference a : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getViewReferences()) {
			if (a.getId().equals(StringConst.LIST_VIEW_ID)) {
				CEPListView view = (CEPListView) a.getView(false);
				view.getNormalList().getTree().getTree().removeAll();
				view.getQueryList().getTree().getTree().removeAll();
				view.getStatusList().removeAll();
				view.setInfoData();
			}
		}
		return null;
	}

}
