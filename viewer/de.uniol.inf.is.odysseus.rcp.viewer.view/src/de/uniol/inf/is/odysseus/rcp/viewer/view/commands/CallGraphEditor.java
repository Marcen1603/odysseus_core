package de.uniol.inf.is.odysseus.rcp.viewer.view.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.GraphViewEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.GraphViewEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.GraphViewEditorInputFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.resource.IResourceConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.resource.impl.XMLResourceConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.impl.XMLSymbolConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.view.views.ModelManagerViewPart;

public class CallGraphEditor extends AbstractHandler implements IHandler {

	public static final String COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.viewer.view.commands.callGraphEditor";
	private static GraphViewEditorInputFactory GRAPH_VIEW_FACTORY;
	
	private static boolean resLoaded = false;
	private static IResourceConfiguration RESOURCE_CONFIGURATION = null;
	private static ISymbolConfiguration SYMBOL_CONFIGURATION = null;
	private static ISymbolElementFactory<IPhysicalOperator> SYMBOL_FACTORY = null;
	
	public CallGraphEditor() {
		try {
			RESOURCE_CONFIGURATION = new XMLResourceConfiguration(Activator.getContext().getBundle().getEntry("viewer_cfg/resources.xml"), Activator.getContext().getBundle().getEntry("viewer_cfg/resourcesSchema.xsd"), Activator.getContext().getBundle());
			SYMBOL_CONFIGURATION = new XMLSymbolConfiguration(Activator.getContext().getBundle().getEntry("viewer_cfg/symbol.xml"), Activator.getContext().getBundle().getEntry("viewer_cfg/symbolSchema.xsd"));
			SYMBOL_FACTORY = new SWTSymbolElementFactory<IPhysicalOperator>();
			GRAPH_VIEW_FACTORY = new GraphViewEditorInputFactory();
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		ModelManagerViewPart view = (ModelManagerViewPart) page.findView(ModelManagerViewPart.VIEW_ID);
	
		ISelection selection = view.getSite().getSelectionProvider().getSelection();
		if( selection instanceof ITreeSelection ) {
			ITreeSelection treeSelection = (ITreeSelection)selection;
			Object obj = treeSelection.getFirstElement();
			
			if( !resLoaded ) {
				SWTResourceManager.getInstance().load(window.getShell().getDisplay(), RESOURCE_CONFIGURATION);
				resLoaded = true;
			}
			
			// Auswahl holen
			IGraphModel<IPhysicalOperator> graph;
			if( obj instanceof IGraphModel<?>)
				graph = (IGraphModel<IPhysicalOperator>)obj;
			else if( obj instanceof IOdysseusNodeModel) {
				Object o = ((ITreeContentProvider)view.getTreeViewer().getContentProvider()).getParent(obj);
				graph = (IGraphModel<IPhysicalOperator>)o;
			} else {
				return null;
			}
			
			// schauen, ob Editor f�r den Graphen schon offen ist
			for( IEditorReference editorRef : page.getEditorReferences() ) {
				try {
					IEditorInput i = editorRef.getEditorInput();
					if( i instanceof GraphViewEditorInput) {
						GraphViewEditorInput gInput = (GraphViewEditorInput)i;
						if( gInput.getModelGraph() == graph ) 
							return null; // Graph wird schon angezeigt
					}
				} catch( PartInitException ex ) {
					ex.printStackTrace();
				}
			}
			
			
			// ViewModell erzeugen
			GraphViewEditorInput input = (GraphViewEditorInput)GRAPH_VIEW_FACTORY.createGraphView(graph, SYMBOL_CONFIGURATION, SYMBOL_FACTORY);
			
			try {
				page.openEditor(input, GraphViewEditor.EDITOR_ID);
				
			} catch( PartInitException ex ) {
				System.out.println(ex.getStackTrace());
			}
		}
		
		return null;
	}

}
