package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class CopyNodeSchemaCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<IOdysseusNodeView> optNodeView = getSelectedNodeView(event);
		if( optNodeView.isPresent() ) {
			IOdysseusNodeView nodeView = optNodeView.get();

			if(nodeView.getModelNode() != null && nodeView.getModelNode().getContent() != null){
				SDFSchema outputSchema = nodeView.getModelNode().getContent().getOutputSchema();
				StringBuffer output = new StringBuffer();
				output.append("schema = [\n");
				for (SDFAttribute a:outputSchema){
					output.append("['").append(a.getURI()).append("','").append(a.getDatatype()).append("'],\n");
				}
				output = new StringBuffer(output.subSequence(0, output.length()-2));
				output.append("\n]\n");
				transferToClipboard(output.toString(),event);
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


	private void transferToClipboard(String text, ExecutionEvent event){
		Display display = HandlerUtil.getActiveWorkbenchWindow(event).getShell().getDisplay();
		Clipboard clipboard = new Clipboard(display);
		TextTransfer textTransfer = TextTransfer.getInstance();
		Transfer[] transfers = new Transfer[]{textTransfer};
		Object[] data = new Object[]{text};
		clipboard.setContents(data, transfers);
		clipboard.dispose();
	}

}
