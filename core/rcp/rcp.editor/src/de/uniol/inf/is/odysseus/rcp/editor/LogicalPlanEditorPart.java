package de.uniol.inf.is.odysseus.rcp.editor;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.LayerManager;
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

import de.uniol.inf.is.odysseus.rcp.editor.model.IOperatorPlanExporter;
import de.uniol.inf.is.odysseus.rcp.editor.model.IOperatorPlanImporter;
import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorFactory;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlanExporter;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlanImporter;
import de.uniol.inf.is.odysseus.rcp.editor.parts.MyEditPartFactory;

public class LogicalPlanEditorPart extends GraphicalEditorWithFlyoutPalette implements IEditorPart, IAdaptable {

	private OperatorPlan plan;
	private static PaletteRoot paletteModel = null;

	public LogicalPlanEditorPart() {
		super();
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		FileEditorInput fi = (FileEditorInput)getEditorInput();
		IOperatorPlanExporter exporter = new OperatorPlanExporter(fi.getFile());
		exporter.save(plan);
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		
		FileEditorInput fi = (FileEditorInput)getEditorInput();
		IOperatorPlanImporter importer = new OperatorPlanImporter(fi.getFile());
		plan = importer.load();
		
		// bauen
		for( Operator op : plan.getOperators() ) {
			if( op.getConnectionsAsTarget().size() == 0 ) // Quelle?
				op.build();
		}
		
	}

	@Override
	public void doSaveAs() {
		// TODO: Implement
	}
	
	public OperatorPlan getOperatorPlan() {
		return plan;
	}
	
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		final GraphicalViewer graphicalViewer = getGraphicalViewer();
		graphicalViewer.setRootEditPart(new ScalableRootEditPart());
		graphicalViewer.setEditPartFactory(new MyEditPartFactory());
		graphicalViewer.setKeyHandler(new GraphicalViewerKeyHandler(graphicalViewer));

		RootEditPart root = graphicalViewer.getRootEditPart();

		if (root instanceof LayerManager) {
			((ConnectionLayer) ((LayerManager) root).getLayer(LayerConstants.CONNECTION_LAYER)).setConnectionRouter(new BendpointConnectionRouter());
		}

		createActions();
		ContextMenuProvider cmProvider = new MyContextMenuProvider(graphicalViewer, getActionRegistry());
		graphicalViewer.setContextMenu(cmProvider);
//		getSite().registerContextMenu(cmProvider, graphicalViewer);
	}

	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			@Override
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}

	private TransferDropTargetListener createTransferDropTargetListener() {
		return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
			@Override
			protected CreationFactory getFactory(Object template) {
				if (template instanceof String)
					return new OperatorFactory((String) template);
				else
					return null;
			}
		};
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		graphicalViewer.setContents(plan);
		graphicalViewer.addDropTargetListener(createTransferDropTargetListener());
	}

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

	@Override
	protected PaletteRoot getPaletteRoot() {
		if (paletteModel == null)
			paletteModel = PaletteFactory.createPalette();
		return paletteModel;
	}

}
