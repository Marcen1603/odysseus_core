package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard;

import java.util.List;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.IStreamMapEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;


public class MapDashboardPart extends AbstractDashboardPart implements IStreamMapEditor {

	private static final Logger LOG = LoggerFactory.getLogger(MapDashboardPart.class);

	private IPhysicalOperator operator;

	private String[] attributes;
	private int[] positions;
	private boolean refreshing = false;

	private final List<Tuple<?>> data = Lists.newArrayList();

	private String attributeList = "*";
	private String operatorName = "";

	private int maxData = 10;
	private int updateInterval = 1000;

	protected ScreenTransformation transformation;
	protected ScreenManager screenManager;
	private MapEditorModel mapModel;

	private boolean initiated = false;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		parent.setLayout(new GridLayout(1, false));

		transformation = new ScreenTransformation();
		initMapModel();
		screenManager = new ScreenManager(transformation, this);
		screenManager.setCanvasViewer(screenManager.createCanvas(parent));
		// setTimeSlider(createTimeSliderComposite(parent));

		BasicLayer basic = new BasicLayer();
		mapModel.getLayers().addFirst(basic);
		
		mapModel.init(this);
	}

	public void initMapModel() {

		if (!initiated) {
			// mapModel = MapEditorModel.open(file,this);
			mapModel = new MapEditorModel();

			if (mapModel.getSRID() == 0) {
				// If a new file was created or the map has
				// no srid because of other reasons
				// set a standard srid, here 3785
				mapModel.setSrid(3857);
			}
			initiated = true;
		}

	}

	// TODO Das oder getMaxTuple nutzen
	public int getMaxData() {
		return maxData;
	}

	public int getMaxTuplesCount() {
		return 1000;
	}

	public void setMaxData(int maxData) {
		this.maxData = maxData;
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	public void setFocus() {
		if (screenManager != null) {
			if (screenManager.hasCanvasViewer()) {
				screenManager.getCanvas().setFocus();
			}
		}
	}

	@Override
	public ScreenManager getScreenManager() {
		return this.screenManager;
	}

	public void addLayer(LayerConfiguration layer) {
		if (mapModel != null) {
			mapModel.addLayer(layer);
			update();
		} else {
			LOG.error("Map Model is not initialized.");
		}
	}
	
	public void editLayer(LayerConfiguration layer){
		if(mapModel != null){
			mapModel.editLayer(layer);
			update();
		}else{
			LOG.error("Map Model is not initialized.");
		}
	}

	public void removeLayer(ILayer layer) {
		if (mapModel != null) {
			mapModel.removeLayer(layer);
			update();
		} else {
			LOG.error("Map Model is not initialized.");
		}
	}

	@Override
	public void update() {
		if (screenManager != null) {
			if (screenManager.hasCanvasViewer()) {
				screenManager.redraw();
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

	public void layerUp(ILayer layer) {
		mapModel.layerUp(layer);
		update();
	}

	public void layerTop(ILayer layer) {
		mapModel.layerTop(layer);
		update();
	}

	public void layerDown(ILayer layer) {
		mapModel.layerDown(layer);
		update();
	}

	public void layerBottom(ILayer layer) {
		mapModel.layerBottom(layer);
		update();
	}

	// TODO SHOULD BE CALLED BY EDIT - TEXTFIELD CHANGELISTENER
	public void renameLayer(ILayer layer, String name) {
		mapModel.rename(layer, name);
		update();
	}

	public void setActive(ILayer iLayer, boolean checked) {
		iLayer.setActive(checked);
		this.screenManager.redraw();
	}

	// public TimeSliderComposite createTimeSliderComposite(Composite parent) {
	// timeSliderComposite = new TimeSliderComposite(parent, SWT.BORDER);
	// timeSliderComposite.setScreenmanager(this.screenManager);
	// return timeSliderComposite;
	// }
	//
	// @Override
	// public final TimeSliderComposite getTimeSliderComposite() {
	// return timeSliderComposite;
	// }
	//
	// public void setTimeSlider(TimeSliderComposite timeSlider) {
	// if (timeSlider != null) {
	// this.timeSliderComposite = timeSlider;
	// } else {
	// LOG.error("TimeSlider is null.");
	// }
	// }

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}