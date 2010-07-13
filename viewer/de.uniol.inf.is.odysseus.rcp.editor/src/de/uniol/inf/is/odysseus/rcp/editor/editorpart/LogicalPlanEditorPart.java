package de.uniol.inf.is.odysseus.rcp.editor.editorpart;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorFactory;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;

public class LogicalPlanEditorPart extends GraphicalEditorWithFlyoutPalette implements IEditorPart, IAdaptable {

	private OperatorPlan plan;
	
	public LogicalPlanEditorPart() {
		super();
		setEditDomain(new DefaultEditDomain(this));
	}

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
		setPartName(((FileEditorInput)input).getName());
		
		plan = new OperatorPlan();
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
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		
		final GraphicalViewer graphicalViewer = getGraphicalViewer();
		graphicalViewer.setRootEditPart(new ScalableRootEditPart());
		graphicalViewer.setEditPartFactory(new MyEditPartFactory());
		graphicalViewer.setKeyHandler(new GraphicalViewerKeyHandler(graphicalViewer));
	}
	
	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}
	
	private TransferDropTargetListener createTransferDropTargetListener() {
		return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
			protected CreationFactory getFactory(Object template) {
				if( template instanceof IOperatorExtensionDescriptor)
					return new OperatorFactory((IOperatorExtensionDescriptor)template);
				else 
					return null;
			}
		};
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();	
		
		plan = new OperatorPlan();
	    GraphicalViewer graphicalViewer = getGraphicalViewer();
	    graphicalViewer.setContents(plan);
	    graphicalViewer.addDropTargetListener(createTransferDropTargetListener());
	    
//		int operations = DND.DROP_COPY;
//		Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
//		DropTarget target = new DropTarget(getGraphicalViewer().getControl(), operations);
//		target.setTransfer(transferTypes);
//		target.addDropListener(new DropTargetListener() {
//
//			@Override
//			public void dragEnter(DropTargetEvent event) {
//			}
//
//			@Override
//			public void dragLeave(DropTargetEvent event) {
//			}
//
//			@Override
//			public void dragOperationChanged(DropTargetEvent event) {
//			}
//
//			@Override
//			public void dragOver(DropTargetEvent event) {
//				event.detail = DND.DROP_COPY;
//			}
//
//			@Override
//			public void drop(DropTargetEvent event) {
//				// IOperatorDescriptor holen
//				String operatorID = (String)event.data;
//				IOperatorExtensionDescriptor desc = OperatorExtensionRegistry.getInstance().getExtension(operatorID);
//				if( desc == null ) {
//					System.out.println("No IOperatorDescriptionExtension gefunden");
//					return;
//				}
//				
//				Operator operator = new Operator(desc);
//				Point p = getGraphicalViewer().getControl().toControl(event.x, event.y);
//				operator.setX(p.x);
//				operator.setY(p.y);
//				plan.addOperator(operator);
//			}
//
//			@Override
//			public void dropAccept(DropTargetEvent event) {
//			}
//			
//		});	
	}

//	@Override
//	public void createPartControl(Composite parent) {
//		super.createPartControl(parent);
//		
//		Composite composite = new Composite(parent, SWT.NONE);
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 2;
//		composite.setLayout(gridLayout);
//		
//		FlyoutPaletteComposite flyout = new FlyoutPaletteComposite(composite, SWT.BORDER, getSite().getPage(), 
//				new PaletteViewerProvider(getEditDomain()), new MyFlyoutPreferences());
//		flyout.setLayoutData(new GridData(GridData.FILL_VERTICAL));
//	}

	@Override
	public void setFocus() {
	}
	
//	public EditDomain getEditDomain() {
//		if (editDomain == null) {
//			editDomain = new DefaultEditDomain(this);
//		}
//		return editDomain;
//	}
//
//	public GraphicalViewer getGraphicalViewer() {
//		if (graphicalViewer == null) {
//			graphicalViewer = new ScrollingGraphicalViewer();
//		}
//		return graphicalViewer;
//	}
//
//	public RootEditPart getRootEditPart() {
//		if (rootEditPart == null) {
//			rootEditPart = new ScalableRootEditPart();
//		}
//		return rootEditPart;
//	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == EditPartViewer.class) {
			return getGraphicalViewer();
		} else if (adapter == EditDomain.class) {
			return getEditDomain();
		} else if (adapter == IWorkbenchPage.class) {
			return getSite().getPage();
		} else if (adapter == ISelectionProvider.class) {
			return this.getSite().getSelectionProvider();
		} else if (adapter == LogicalPlanEditorPart.class) {
			return this;
		}
		return super.getAdapter(adapter);
	}

	private static PaletteRoot PALETTE_MODEL = null;
	@Override
	protected PaletteRoot getPaletteRoot() {
		if (PALETTE_MODEL == null)
			PALETTE_MODEL = PaletteFactory.createPalette();
		return PALETTE_MODEL;
	}

}
