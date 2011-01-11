package de.uniol.inf.is.odysseus.rcp.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.rcp.editor.model.IOperatorPlanExporter;
import de.uniol.inf.is.odysseus.rcp.editor.model.IOperatorPlanImporter;
import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorFactory;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlanExporter;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlanImporter;
import de.uniol.inf.is.odysseus.rcp.editor.parts.MyEditPartFactory;

public class LogicalPlanEditorPart extends GraphicalEditorWithFlyoutPalette implements IEditorPart, IAdaptable, IDataDictionaryListener, PropertyChangeListener {

	private OperatorPlan plan;
	private static PaletteRoot paletteModel = null;
	private boolean isDirty = false;

	public LogicalPlanEditorPart() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		DataDictionary.getInstance().addListener(this);
	}

	@Override
	public void dispose() {
		super.dispose();
		DataDictionary.getInstance().removeListener(this);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		Job job = new Job("Save " + getPartName() ) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				FileEditorInput fi = (FileEditorInput) getEditorInput();
				IOperatorPlanExporter exporter = new OperatorPlanExporter(fi.getFile());
				exporter.save(plan);
				setDirty(false);
				return Status.OK_STATUS;
			}
			
		};
		
		job.setUser(true);
		job.schedule();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);

		FileEditorInput fi = (FileEditorInput) getEditorInput();
		IOperatorPlanImporter importer = new OperatorPlanImporter(fi.getFile());
		plan = importer.load();

		fullBuild();
		
		setPartName(fi.getFile().getName());
		setDirty(false);
		plan.addPropertyChangeListener(this);
	}

	private void fullBuild() {
		// bauen
		for (Operator op : plan.getOperators()) {
			if (op.getConnectionsAsTarget().size() == 0) // Quelle?
				op.build();
		}
	}

	@Override
	public void doSaveAs() {
		// TODO: Implement
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public boolean isSaveOnCloseNeeded() {
		return true;
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
	
	@Override
	public boolean isDirty() {
		return isDirty;
	}
	
	protected void setDirty( boolean dirty ) {
		if( dirty != isDirty ) {
			isDirty = dirty;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					firePropertyChange(IEditorPart.PROP_DIRTY);
				}
				
			});
		}
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

	@Override
	public void addedViewDefinition(DataDictionary sender, String name, ILogicalOperator op) {
		fullBuild();
	}

	@Override
	public void removedViewDefinition(DataDictionary sender, String name, ILogicalOperator op) {
		fullBuild();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if( OperatorPlan.PROPERTY_OPERATOR_ADD.equals(event.getPropertyName())) {
			((Operator)event.getNewValue()).addPropertyChangeListener(this);
			setDirty(true);
			
		} else if( OperatorPlan.PROPERTY_OPERATOR_REMOVE.equals(event.getPropertyName())) {
			((Operator)event.getNewValue()).removePropertyChangeListener(this);
			setDirty(true);
			
		} else if( OperatorPlan.PROPERTY_OPERATOR_CHANGE.equals(event.getPropertyName()) ) {
			PropertyChangeEvent evt = (PropertyChangeEvent)event.getNewValue();
			
			// Bauen ändert nicht das Model
			if( !evt.getPropertyName().equals(Operator.PROPERTY_BUILD))
				setDirty(true);
		}
		
	}
}
