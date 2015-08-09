package de.uniol.inf.is.odysseus.rcp.dashboard.part.map;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
//import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.outline.StreamMapEditorOutlinePage;
//import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.buffer.TimeSliderComposite;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.views.AbstractStreamMapEditorViewPart;

public class StreamMapEditorPart extends EditorPart implements IStreamMapEditor {

	//public static final String ID = "de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMap";

	private static final Logger LOG = LoggerFactory.getLogger(StreamMapEditorPart.class);

	protected ScreenTransformation transformation;
	protected ScreenManager screenManager;
	private MapEditorModel mapModel;
	private boolean dirt = false;
	//private TimeSliderComposite timeSliderComposite;
	
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
		mapModel = MapEditorModel.open(file,this);
		
		if(mapModel.getSRID() == 0) {
			// If a new file was created or the map has
			// no srid because of other reasons
			// set a standard srid, here 3785
			mapModel.setSrid(3857);
			this.dirt = true;
		}
		
		screenManager = new ScreenManager(transformation, this);
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
		parent.setLayout(new GridLayout(1, false));

		screenManager.setCanvasViewer(screenManager.createCanvas(parent));
//		setTimeSlider(createTimeSliderComposite(parent));

		BasicLayer basic = new BasicLayer();
		mapModel.getLayers().addFirst(basic);
		mapModel.init(this);
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@Override
	public void update() {
		if (screenManager.hasCanvasViewer()) {
			screenManager.redraw();
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
	
	//TODO SHOULD BE CALLED BY EDIT - TEXTFIELD CHANGELISTENER
	public void renameLayer(ILayer layer, String name) {
		   mapModel.rename(layer, name);
		   dirt = true;
		   firePropertyChange(PROP_DIRTY);
		   update();
	}

	public void setLayerChanged() {
		dirt = true;
		firePropertyChange(PROP_DIRTY);
		update();
	}

//	public TimeSliderComposite createTimeSliderComposite(Composite parent) {
//		timeSliderComposite = new TimeSliderComposite(parent, SWT.BORDER);
//		timeSliderComposite.setScreenmanager(this.screenManager);
//		return timeSliderComposite;
//	}
//	
//	@Override
//	public final TimeSliderComposite getTimeSliderComposite() {
//		return timeSliderComposite;
//	}
//	
//	public void setTimeSlider(TimeSliderComposite timeSlider) {
//		if (timeSlider != null) {
//			this.timeSliderComposite = timeSlider;
//		} else {
//			LOG.error("TimeSlider is null.");
//		}
//	}

	public void setActive(ILayer iLayer, boolean checked) {
		iLayer.setActive(checked);
		this.screenManager.redraw();
	}
}
