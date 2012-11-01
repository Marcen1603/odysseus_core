package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline.StreamMapEditorOutlinePage;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.AbstractStreamMapEditorViewPart;

public class StreamMapEditorPart extends EditorPart implements IStreamMapEditor {

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMap";

	private static final Logger LOG = LoggerFactory.getLogger(StreamMapEditorPart.class);

	protected ScreenTransformation transformation;
	protected ScreenManager screenManager;
	private MapEditorModel mapModel;
	private boolean dirt = false;

	private Action selectAllAction, addItemAction, deleteItemAction;
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			FileEditorInput in = ((FileEditorInput) this.getEditorInput());
			IFile file = in.getFile();
			mapModel.save(file);
			this.dirt = false;
			firePropertyChange(PROP_DIRTY);
		} finally {
			monitor.done();
		}

	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		FileEditorInput in = ((FileEditorInput) input);
		IFile file = in.getFile();
		transformation = new ScreenTransformation();
		screenManager = new ScreenManager(transformation, this);
		mapModel = MapEditorModel.open(file,this);
	}

	@Override
	public boolean isDirty() {
		return dirt;
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		screenManager.setCanvasViewer(screenManager.createCanvas(parent));
		
//		//Always add a Basic Layer. 
//		if(mapModel.getLayers().isEmpty()){
			BasicLayer basic = new BasicLayer(new LayerConfiguration("Basic"));
			mapModel.getLayers().addFirst(basic);
//		}
			createActions();
			 createToolbar();
			 createMenu();
			 
		mapModel.init(this);
	}
	
	
	private static IMenuManager barMgr = null;
	private static IToolBarManager toolMgr = null;
	private static MenuManager subMenu = null;
	
	protected void createMenu() {
		if(barMgr == null){
			barMgr = getEditorSite().getActionBars().getMenuManager();
			subMenu = new MenuManager("Spatial", null);
			barMgr.add(subMenu);
			subMenu.add(selectAllAction);
		}
		else{
			subMenu.removeAll();
			subMenu.add(selectAllAction);
		}
	}

	protected void createToolbar() {
		if(toolMgr == null){
			toolMgr = getEditorSite().getActionBars().getToolBarManager();
			toolMgr.add(addItemAction);
			toolMgr.add(deleteItemAction);
		}
		else {
			toolMgr.removeAll();
			toolMgr.add(addItemAction);
			toolMgr.add(deleteItemAction);
		}
	}

	protected void createActions() {
		
		addItemAction = new Action("Add new Layer") {
			public void run() {
				MapEditorModel model = (MapEditorModel)getSite().getPage().getActiveEditor().getAdapter(MapEditorModel.class);	
				LOG.debug("Delete.." +  model.getLayers().getLast().toString());
			}
		};
		//addItemAction.setImageDescriptor(OdysseusMapPlugIn.getDefault().imageDescriptorFromPlugin(OdysseusMapPlugIn.ODYSSEUS_MAP_PLUGIN_ID, "layer_plus_16"));
//		addItemAction.setImageDescriptor(new ImageDescriptor() {
//			
//			@Override
//			public ImageData getImageData() {
//				if(OdysseusMapPlugIn.getDefault().getImageRegistry().get("layers_plus_16") != null){
//					return null;
//				}
//				else
//				return OdysseusMapPlugIn.getDefault().getImageRegistry().get("layers_plus_16").getImageData();
//			}
//			
//		});
		
		deleteItemAction = new Action("Selection") {
			public void run() {
				MapEditorModel model = (MapEditorModel)getSite().getPage().getActiveEditor().getAdapter(MapEditorModel.class);	
				LOG.debug("Delete.." +  model.getLayers().getLast().toString());
			}
		};


		selectAllAction = new Action("Add new Layer") {
			public void run() {
				MapEditorModel model = (MapEditorModel)getSite().getPage().getActiveEditor().getAdapter(MapEditorModel.class);	
				LOG.debug("Delete.." +  model.getLayers().getLast().toString());
			}
		};
		

	}

	
	@Override
	public void setFocus() {
		if (screenManager.hasCanvasViewer()){
			screenManager.getCanvas().setFocus();
		}
	}
	
	@Override
	public ScreenManager getScreenManager() {
		return this.screenManager;
	}

	public void addLayer(LayerConfiguration layer){
		if(mapModel != null){
			mapModel.addLayer(layer);
			dirt = true;
			firePropertyChange(PROP_DIRTY);
			update();
		}
		else{
			LOG.error("Map Model is not initialized.");
		}
	}
	
	public void removeLayer(ILayer layer){
		if(mapModel != null){
			mapModel.removeLayer(layer);
			dirt = true;
			firePropertyChange(PROP_DIRTY);
			update();
		}
		else{
			LOG.error("Map Model is not initialized.");
		}
	}
	
	public void addConnection(IPhysicalQuery query) {
		List<IPhysicalOperator>ops = query.getRoots();
		mapModel.addConnection(new DefaultStreamConnection(ops), query, this);
		
		dirt = true;
		firePropertyChange(PROP_DIRTY);
		update();
	}

	public void removeConnection(LayerUpdater connection){
		if(mapModel != null){
			mapModel.removeConnection(connection);
			dirt = true;
			firePropertyChange(PROP_DIRTY);
			update();
		}
		else{
			LOG.error("Map Model is not initialized.");
		}
	}
	
	public int getMaxTuplesCount() {
		return 1000;
	}

	public void update() {
		if (screenManager.hasCanvasViewer()) {
			screenManager.getCanvas().redraw();
		}
		//Update all related ViewParts 
		for(IViewReference ref :getSite().getPage().getViewReferences()){
			if(ref.getPart(false) instanceof AbstractStreamMapEditorViewPart){
				((AbstractStreamMapEditorViewPart) ref.getPart(false)).updateView();
			}
		}
	}

	@Override
	public boolean isRectangleZoom() {
		return false;
	}

	@Override
	public MapEditorModel getMapEditorModel() {
		return mapModel;
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (MapEditorModel.class.equals(adapter)){
			return mapModel;
		}
		
		if(StreamMapEditorPart.class.equals(adapter)){
			return this;
		}
		
		if (IContentOutlinePage.class.equals(adapter)){
			return new StreamMapEditorOutlinePage(this);
		}
		
		return super.getAdapter(adapter);
	}

	public void addFile(IFile newFile) {
	    mapModel.addFile(newFile);
		dirt = true;
		firePropertyChange(PROP_DIRTY);
		update();
    }

	public void removeFile(IFile file) {
	    mapModel.removeFile(file);
		dirt = true;
		firePropertyChange(PROP_DIRTY);
		update();
    }

	public void layerUp(ILayer layer) {
	   mapModel.layerUp(layer);
	   dirt = true;
	   firePropertyChange(PROP_DIRTY);
	   update();
    }

	public void layerDown(ILayer layer) {
		   mapModel.layerDown(layer);
		   dirt = true;
		   firePropertyChange(PROP_DIRTY);
		   update();
    }


}
