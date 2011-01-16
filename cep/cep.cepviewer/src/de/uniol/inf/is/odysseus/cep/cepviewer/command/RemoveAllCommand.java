package de.uniol.inf.is.odysseus.cep.cepviewer.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;

public class RemoveAllCommand extends AbstractHandler implements IHandler {

	@SuppressWarnings("rawtypes")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		for (IViewReference a : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
			if (a.getId().equals(CEPListView.ID)) {
				CEPListView view = (CEPListView) a.getView(false);
				for (CepOperator operator : view.getOperators()) {
					operator.getCEPEventAgent().removeCEPEventListener(
							view.getListener());
				}
				view.getNormalList().removeAll();
				view.getQueryList().removeAll();
				view.getStatusList().removeAll();
				view.setInfoData();
				// TODO: clear all views
			}
		}
		return null;
	}

}
