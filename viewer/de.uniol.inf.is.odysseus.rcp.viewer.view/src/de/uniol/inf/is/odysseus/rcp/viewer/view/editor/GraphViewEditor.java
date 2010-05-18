package de.uniol.inf.is.odysseus.rcp.viewer.view.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.position.SugiyamaPositioner;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.render.SWTRenderManager;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolElementFactory;

public class GraphViewEditor extends EditorPart {

	public static final String EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.viewer.view.GraphEditor";
	
	private static final ISymbolElementFactory<IPhysicalOperator> SYMBOL_FACTORY = new SWTSymbolElementFactory<IPhysicalOperator>();
	
	private GraphViewEditorInput input;
	private SWTRenderManager<IPhysicalOperator> renderManager;
	
	public GraphViewEditor() {}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		this.input = ((GraphViewEditorInput)input);
		setPartName(this.input.getModelGraph().getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
//		GridLayout layout = new GridLayout();
//		layout.numColumns = 2;
//		parent.setLayout(layout);
//		Label label1 = new Label(parent, SWT.BORDER);
//		label1.setText("Graph: ");
		Composite canvasComposite = new Composite(parent, SWT.BORDER);
		canvasComposite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		canvasComposite.setLayout(new FillLayout());

		renderManager = new SWTRenderManager<IPhysicalOperator>(canvasComposite, new SugiyamaPositioner(SYMBOL_FACTORY));
		renderManager.setDisplayedGraph(this.input);
		renderManager.resetPositions();
	}

	@Override
	public void setFocus() {
		if( renderManager != null ) 
			renderManager.getCanvas().setFocus();
	}

}
