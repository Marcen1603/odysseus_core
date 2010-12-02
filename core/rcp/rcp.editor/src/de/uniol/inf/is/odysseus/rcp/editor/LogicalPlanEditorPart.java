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

import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorFactory;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.editor.parts.MyEditPartFactory;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.ActiveUser;

public class LogicalPlanEditorPart extends GraphicalEditorWithFlyoutPalette implements IEditorPart, IAdaptable {

	private OperatorPlan plan;
	private User user;
	private static PaletteRoot paletteModel = null;

	public LogicalPlanEditorPart() {
		super();
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
//		try {
//			IEditorInput input = getEditorInput();
//			FileEditorInput fei = (FileEditorInput)input;
//			IFile file = fei.getFile();
//			
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ObjectOutputStream objOut = new ObjectOutputStream(baos);
//			objOut.writeObject(plan);
//			objOut.close();
//
//			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//			if (!file.exists())
//				file.create(bais, true, null); // neue Datei
//			else
//				file.setContents(bais, IResource.FORCE | IResource.KEEP_HISTORY, null);
//		} catch( IOException exception ) {
//			exception.printStackTrace();
//		} catch( CoreException exception ) {
//			exception.printStackTrace();
//		}
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
//		
//		try {
//			FileEditorInput fei = (FileEditorInput)input;
//			IFile file = fei.getFile();
//			if( file.exists() ) {
//				ObjectInputStream objIn = new ObjectInputStream(file.getContents());
//				plan = (OperatorPlan)objIn.readObject();
//				objIn.close();
//			}
//		} catch( IOException exception ) {
//			exception.printStackTrace();
//		} catch( ClassNotFoundException exception ) {
//			exception.printStackTrace();
//		} catch( CoreException exception) {
//			exception.printStackTrace();
//		}
	}

	@Override
	public void doSaveAs() {

	}
	
	public OperatorPlan getOperatorPlan() {
		return plan;
	}
	
	public User getUser(){
		return user;
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
		user = ActiveUser.getActiveUser();
		plan = new OperatorPlan();
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
