package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.PropertyTitleDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;


public class AddLayerCommand extends AbstractHandler implements IHandler {

	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
		
		StreamMapEditorPart editor = (StreamMapEditorPart) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getActiveEditor();
		
		Shell shell = editor.getScreenManager().getDisplay().getActiveShell(); 
		
		MapEditorModel mapModel = editor.getMapEditorModel();
	
		PropertyTitleDialog dialog = new PropertyTitleDialog(shell, mapModel.getLayers(), mapModel.getConnectionCollection());
		dialog.create();
		dialog.open();
		
		editor.addLayer(dialog.getLayerConfiguration());
		
		return null;
    }

}
