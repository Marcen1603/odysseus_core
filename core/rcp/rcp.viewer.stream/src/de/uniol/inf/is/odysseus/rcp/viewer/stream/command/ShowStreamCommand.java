package de.uniol.inf.is.odysseus.rcp.viewer.stream.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.IStreamConstants;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.editor.StreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.StreamEditorRegistry;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.StreamExtensionDefinition;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;

public class ShowStreamCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String editorTypeID = event.getParameter(IStreamConstants.STREAM_EDITOR_TYPE_PARAMETER_ID);
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		
		ISelection selection = window.getActivePage().getSelection();
		if (selection == null)
			return null;

		IPhysicalOperator opForStream = null;
		if (selection instanceof IStructuredSelection) {

			IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object selectedObject = structSelection.getFirstElement();
			
			if( selectedObject instanceof IQuery ) {
				IQuery query = (IQuery)selectedObject;
				if( query.getRoots().size() > 0 )
					opForStream = query.getRoots().get(0);
				else 
					opForStream = query.getPhysicalChilds().get(0);
				
			}
			
			if (selectedObject instanceof IOdysseusNodeView) {
				IOdysseusNodeView nodeView = (IOdysseusNodeView) selectedObject;

				// Auswahl holen
				opForStream = nodeView.getModelNode().getContent();
			}
		}
		
		if( opForStream != null ) {
				
				// schauen, ob Editor für den Graphen schon offen ist
				for (IEditorReference editorRef : page.getEditorReferences()) {
					try {
						IEditorInput i = editorRef.getEditorInput();
						if (i instanceof StreamEditorInput) {
							StreamEditorInput gInput = (StreamEditorInput) i;
							if (gInput.getPhysicalOperator() == opForStream && gInput.getEditorTypeID().equals(editorTypeID) ) {
								page.activate(editorRef.getPart(false));
								return null; // Stream wird schon angezeigt
							}
						}
					} catch (PartInitException ex) {
						ex.printStackTrace();
						return null;
					}
				}
				
				// Ediortyp holen und anzeigen
				for( StreamExtensionDefinition def : StreamEditorRegistry.getInstance().getStreamExtensionDefinitions()) {
					if(def.getID().equals(editorTypeID)) {
						try {
							final Object editorType = def.getConfigElement().createExecutableExtension("class");
							if( editorType instanceof IStreamEditorType ) {
								IStreamEditorType editor = (IStreamEditorType)editorType;
								
								// ViewModell erzeugen
								StreamEditorInput input = new StreamEditorInput(opForStream, editor, editorTypeID, def.getLabel());
	
								try {
									page.openEditor(input, IStreamConstants.STREAM_EDITOR_ID);
									return null;
								} catch (PartInitException ex) {
									System.out.println(ex.getStackTrace());
								}						
							}
						} catch( CoreException ex ) {
							ex.printStackTrace();
						}
					}
				}
		}
		return null;
	}
}
