package de.uniol.inf.is.odysseus.rcp.viewer.view.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.Model;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl.GraphViewEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl.GraphViewEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl.GraphViewEditorInputFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolElementFactory;

public class CallActiveGraphEditor extends AbstractHandler implements IHandler {

	private static GraphViewEditorInputFactory GRAPH_VIEW_FACTORY = new GraphViewEditorInputFactory();
	private static ISymbolElementFactory<IPhysicalOperator> SYMBOL_FACTORY = new SWTSymbolElementFactory<IPhysicalOperator>();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		// Auswahl holen
		IGraphModel<IPhysicalOperator> graph = Model.getInstance().getModelManager().getActiveModel();

		if (graph == null)
			return null;

		// schauen, ob Editor f�r den Graphen schon offen ist
		for (IEditorReference editorRef : page.getEditorReferences()) {
			try {
				IEditorInput i = editorRef.getEditorInput();
				if (i instanceof GraphViewEditorInput) {
					GraphViewEditorInput gInput = (GraphViewEditorInput) i;
					if (gInput.getModelGraph() == graph)
						return null; // Graph wird schon angezeigt
				}
			} catch (PartInitException ex) {
				ex.printStackTrace();
			}
		}

		// ViewModell erzeugen
		GraphViewEditorInput input = (GraphViewEditorInput) GRAPH_VIEW_FACTORY.createGraphView(graph, Activator.SYMBOL_CONFIGURATION, SYMBOL_FACTORY);

		try {
			page.openEditor(input, GraphViewEditor.EDITOR_ID);

		} catch (PartInitException ex) {
			System.out.println(ex.getStackTrace());
		}

		return null;
	}

}
