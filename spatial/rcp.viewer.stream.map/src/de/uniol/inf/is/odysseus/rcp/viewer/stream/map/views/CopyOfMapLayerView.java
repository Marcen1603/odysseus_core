package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import java.util.LinkedList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator.OdysseusMapPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline.StreamMapEditorOutlineLabelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline.StreamMapEditorOutlineTreeContentProvider;

public class CopyOfMapLayerView extends ViewPart {

	private static final Logger LOG = LoggerFactory.getLogger(CopyOfMapLayerView.class);
	private StreamMapEditorPart editor;
	private MapEditorModel model;
	private TableViewer viewer;
	private TreeViewer treeViewer;
	private Composite container;
	

	public CopyOfMapLayerView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		
		this.addPartListener();
		
		 	 treeViewer = new TreeViewer(container);
			 treeViewer.setContentProvider(new StreamMapEditorOutlineTreeContentProvider());
			 treeViewer.setLabelProvider(new StreamMapEditorOutlineLabelProvider());
			 //getSite().setSelectionProvider(ArrayContentProvider.getInstance());
			
			 if (getSite().getPage().getActiveEditor() instanceof StreamMapEditorPart)
			 {
				 StreamMapEditorPart editor = (StreamMapEditorPart)
				 getSite().getPage().getActiveEditor();
				 MapEditorModel mapModel = editor.getMapEditorModel();
				 updateTree(mapModel);
				
//			 mapModel.addPropertyChangeListener(MapEditorModel.MAP, new
//			 PropertyChangeListener() {
//			 @Override
//			 public void propertyChange(PropertyChangeEvent evt) {
//			 if (getSite().getPage().getActiveEditor() instanceof StreamMapEditorPart)
//			 {
//			 StreamMapEditorPart editor = (StreamMapEditorPart)
//			 getSite().getPage().getActiveEditor();
//			 MapEditorModel mapModel = editor.getMapEditorModel();
//			 updateTree(mapModel);
//			 }
//			 }
//			 });
			 }
		
//		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
//		this.createColumns(parent);
//
//		final Table table = viewer.getTable();
//		table.setHeaderVisible(true);
//		table.setLinesVisible(true);
//
//		viewer.setContentProvider(ArrayContentProvider.getInstance());
//		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
//			@Override
//			public void selectionChanged(SelectionChangedEvent arg0) {
//				viewer.refresh();
//			}
//		});
//
//		viewer.setInput(getViewSite());
//
//		getSite().setSelectionProvider(viewer);

	}

//	private void createColumns(Composite parent) {
//		TableViewerColumn col = new TableViewerColumn(viewer, SWT.NONE);
//		col.getColumn().setWidth(200);
//		col.getColumn().setText("Layer");
//		col.setLabelProvider(new ColumnLabelProvider() {
//			@Override
//			public String getText(Object element) {
//				return "Test";
//			}
//		});
//
//	}

	@Override
	public void setFocus() {

	}
	
	public void updateTree(MapEditorModel model) {
		LinkedList<ILayer> layers = model.getLayers();

		boolean allActive = true;
		for (ILayer tmp : layers) {
			if (!tmp.isActive()) {
				allActive = false;
			}
		}
		if (allActive) {
			treeViewer = new TreeViewer(container);
			treeViewer.setContentProvider(new StreamMapEditorOutlineTreeContentProvider());
			treeViewer.setLabelProvider(new StreamMapEditorOutlineLabelProvider());
			 
			treeViewer.setInput(layers.toArray());
			treeViewer.expandAll();
			treeViewer.refresh();
		}
	}
	
	private void addPartListener(){
		IWorkbenchPage page = getSite().getPage();
		IPartListener2 pl = new IPartListener2() {
			
			@Override
			public void partActivated(IWorkbenchPartReference reference) {
				
				
				System.out.println(reference);
				
				if(!(reference instanceof IViewReference)){
					Composite parent = container.getParent();
					container = new Composite(parent, SWT.NONE);
					new Label(container,SWT.None).setText("No Map Selected");
					parent.layout(true);
					treeViewer = null;
				}
				if(reference instanceof IEditorReference){
					if(reference.getId().equals(OdysseusMapPlugIn.ODYSSEUS_MAP_PLUGIN_ID)){
						System.out.println("Active: " + reference.getTitle() );
						model = (MapEditorModel) getSite().getPage().getActiveEditor().getAdapter(CopyOfMapLayerView.class);
						updateTree(model);
					}
				}
			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference reference) {

			}

			@Override
			public void partClosed(IWorkbenchPartReference reference) {

			}

			@Override
			public void partDeactivated(IWorkbenchPartReference reference) {

			}

			@Override
			public void partHidden(IWorkbenchPartReference reference) {

			}

			@Override
			public void partInputChanged(IWorkbenchPartReference reference) {

			}

			@Override
			public void partOpened(IWorkbenchPartReference reference) {

			}

			@Override
			public void partVisible(IWorkbenchPartReference reference) {

			}

		};
		page.addPartListener(pl);
	}
	
	
	

//	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
//		getSite().getAdapter(MapLayerView.class);
//		
//		System.out.println("Test View Part");
//	}

	// @Override
	// public void createPartControl(Composite parent) {
	// treeViewer = new TreeViewer(parent);
	// treeViewer.setContentProvider(new
	// StreamMapEditorOutlineTreeContentProvider());
	// treeViewer.setLabelProvider(new StreamMapEditorOutlineLabelProvider());
	// //getSite().setSelectionProvider(ArrayContentProvider.getInstance());
	//
	// if (getSite().getPage().getActiveEditor() instanceof StreamMapEditorPart)
	// {
	// StreamMapEditorPart editor = (StreamMapEditorPart)
	// getSite().getPage().getActiveEditor();
	// MapEditorModel mapModel = editor.getMapEditorModel();
	// updateTree(mapModel);
	//
	// mapModel.addPropertyChangeListener(MapEditorModel.MAP, new
	// PropertyChangeListener() {
	// @Override
	// public void propertyChange(PropertyChangeEvent evt) {
	// if (getSite().getPage().getActiveEditor() instanceof StreamMapEditorPart)
	// {
	// StreamMapEditorPart editor = (StreamMapEditorPart)
	// getSite().getPage().getActiveEditor();
	// MapEditorModel mapModel = editor.getMapEditorModel();
	// updateTree(mapModel);
	// }
	// }
	// });
	// }
	//
	// }
	//

}
