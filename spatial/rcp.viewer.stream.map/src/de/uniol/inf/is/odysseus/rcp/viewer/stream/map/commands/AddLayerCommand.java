package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.LayerOrderTrayDialog;

public class AddLayerCommand extends AbstractHandler implements IHandler {

	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
		
		System.out.println("Add Layer");
		
		LayerOrderTrayDialog orderDialog = new LayerOrderTrayDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		orderDialog.create();
		
		return null;
    }

}
