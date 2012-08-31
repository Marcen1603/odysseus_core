package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.LayerOrderTrayDialog;


public class AddLayerCommand extends AbstractHandler implements IHandler {

	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell shell = ((Event)event.getTrigger()).display.getActiveShell();
		
		LayerOrderTrayDialog orderDialog = new LayerOrderTrayDialog(shell);
		orderDialog.create();
		orderDialog.open();
		
		return null;
    }

}
