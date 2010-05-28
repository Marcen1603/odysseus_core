package de.uniol.inf.is.odysseus.rcp.viewer.view.views;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.Model;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManager;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManagerListener;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.commands.CallGraphEditor;

public class ModelManagerViewPart extends ViewPart implements IModelManagerListener<IPhysicalOperator> {

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.rcp.viewer.view.ModelManagerView";
	
	private TreeViewer treeViewer;
	private Display display;
	
	public ModelManagerViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		display = parent.getDisplay();
		
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new FillLayout());
		
		treeViewer = new TreeViewer(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
		treeViewer.setContentProvider(new ModelContentProvider());
		treeViewer.setLabelProvider(new ModelLabelProvider());
		treeViewer.setInput(Model.getInstance().getModelManager());
		
		getSite().setSelectionProvider(treeViewer);
		
		Model.getInstance().getModelManager().addListener(this);
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
				
				try {
					handlerService.executeCommand(CallGraphEditor.COMMAND_ID, null);
				} catch( Exception ex ) {
					ex.printStackTrace();
				}
			}			
		});
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	@Override
	public void modelAdded(IModelManager<IPhysicalOperator> sender, IGraphModel<IPhysicalOperator> model) {
		if( treeViewer != null ) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					treeViewer.refresh();
				}			
			});
		}
	}

	@Override
	public void modelRemoved(IModelManager<IPhysicalOperator> sender, IGraphModel<IPhysicalOperator> model) {
		if( treeViewer != null ) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					treeViewer.refresh();
				}				
			});
		}
	}
	
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	@Override
	public void activeModelChanged(IModelManager<IPhysicalOperator> sender,
			IGraphModel<IPhysicalOperator> activeModel) {
		System.out.println("Active MODEL CHANGED " + activeModel.getName());
		if( treeViewer != null ) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					treeViewer.refresh();
				}			
			});
		}		
	}

}
