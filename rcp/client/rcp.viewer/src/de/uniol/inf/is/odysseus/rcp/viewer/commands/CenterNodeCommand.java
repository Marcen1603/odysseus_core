package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.impl.GraphViewEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class CenterNodeCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		Optional<IOdysseusNodeView> optNodeView = getSelectedNodeView(event);
		if( optNodeView.isPresent() ) {
			IEditorPart activeEditor = page.getActiveEditor();
			if(activeEditor instanceof GraphViewEditor) {
				GraphViewEditor editor = (GraphViewEditor)activeEditor;
				editor.center(optNodeView.get());
			}
		}
		
		return null;
	}

	private static Optional<IOdysseusNodeView> getSelectedNodeView(ExecutionEvent event) {
		List<Object> selectedObjects = SelectionProvider.getSelection(event);
		if( !selectedObjects.isEmpty() ) {
			Object firstSelectedObject = selectedObjects.get(0);
			if( firstSelectedObject instanceof IOdysseusNodeView ) {
				IOdysseusNodeView node = (IOdysseusNodeView)firstSelectedObject;
				return Optional.of(node);
			}
		}
		return Optional.absent();
	}

}
