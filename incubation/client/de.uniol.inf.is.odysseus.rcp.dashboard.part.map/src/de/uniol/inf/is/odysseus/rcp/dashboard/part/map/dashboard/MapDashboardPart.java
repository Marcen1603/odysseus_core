package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.IMapDashboardAdapter;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;

public class MapDashboardPart extends AbstractDashboardPart implements IMapDashboardAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(MapDashboardPart.class);

	private IPhysicalOperator operator;

	private String[] attributes;
	private int[] positions;
	private boolean refreshing = false;

	private final List<Tuple<?>> data = Lists.newArrayList();

	private String attributeList = "*";
	private String layerSettings = "";

	private int srid = 0;
	private int maxData = 10000;
	private int updateInterval = 1000;

	protected ScreenTransformation transformation;
	protected ScreenManager screenManager;
	private MapEditorModel mapModel;
	private LayerUpdater layerUpdater;

	private boolean initiated = false;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		parent.setLayout(new GridLayout(1, false));
		initMapModel();
		
		transformation = new ScreenTransformation();
		screenManager = new ScreenManager(transformation, this);
		screenManager.setCanvasViewer(screenManager.createCanvas(parent));
		// setTimeSlider(createTimeSliderComposite(parent))

		layerUpdater = mapModel.addConnection(this);
		mapModel.init(this);
		layerUpdater.setMaxPufferSize(maxData);

		update();
	} 
	
	@Override
	public Map<String, String> onSave() {
		Map<String, String> toSaveMap = Maps.newHashMap();
		layerSettings = mapModel.save();
		toSaveMap.put("Attributes", attributeList);
		toSaveMap.put("MaxData", String.valueOf(maxData));
		toSaveMap.put("UpdateInterval", String.valueOf(updateInterval));
		toSaveMap.put("LayerSettings", layerSettings);
		toSaveMap.put("SRID", String.valueOf(mapModel.getSRID()));
		return toSaveMap;
	};

	@Override
	public void onLoad(Map<String, String> saved) {
		initMapModel();

		attributeList = saved.get("Attributes");
		maxData = Integer.valueOf(saved.get("MaxData"));
		updateInterval = Integer.valueOf(saved.get("UpdateInterval"));
		srid = Integer.valueOf(saved.get("SRID"));
		layerSettings = saved.get("LayerSettings");
		if (layerSettings != null) {
			mapModel.load(layerSettings);
		}
	};

	@Override
	public void streamElementReceived(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: Map-DashboardPart is only for spatial relational tuple!");
			return;
		}

		// TODO IST DAS IN ABHÄNGIGKEIT VOM TIMESLIDER
		// SweepArea definiert Element Fenster bzw. die zu visualisierenden
		// Elemente.
		@SuppressWarnings("unchecked")
		Tuple<? extends ITimeInterval> tuple = (Tuple<? extends ITimeInterval>) element;
		PointInTime timestamp = tuple.getMetadata().getStart().clone();
		if (timestamp.afterOrEquals(screenManager.getMaxIntervalEnd())
				|| screenManager.getMaxIntervalEnd().isInfinite()) {
			// Maybe the stream elements do not come in the right order (e.g.
			// wrong csv-data)
			getScreenManager().setMaxIntervalEnd(timestamp);
		} else if (timestamp.beforeOrEquals(screenManager.getMaxIntervalStart())) {
			screenManager.setMaxIntervalStart(timestamp);
		}

		layerUpdater.getPuffer().insert(tuple);
		if (screenManager.getInterval().getEnd().isInfinite()
				|| (screenManager.getInterval().getStart().beforeOrEquals(timestamp)
						&& this.screenManager.getInterval().getEnd().afterOrEquals(timestamp))) {
			// Add tuple to current list if the new timestamp is in the interval
			layerUpdater.addTuple(tuple);
		}

		// Prevent an overflow in the puffer
		layerUpdater.checkForPufferSize();

		//TODO SOLLTE DAS HIER SEIN?
		// Should we redraw here or just if we added the tupel to the current
		// list?
		screenManager.redraw();

	}

	public void initMapModel() {

		if (!initiated) {
			mapModel = new MapEditorModel();

			if (mapModel.getSRID() == 0 && srid == 0) {
				// If a new file was created or the map has
				// no srid because of other reasons
				// set a standard srid, here 3785
				mapModel.setSrid(3857);
			} else {
				mapModel.setSrid(srid);
			}
			initiated = true;
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

	public void editLayer(ILayer layer, LayerConfiguration layerConf) {
		if (mapModel != null) {
			mapModel.editLayer(layer, layerConf);
			update();
		} else {
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

	//TODO CHECK IF THIS IS USED ANYMORE
	public void renameLayer(ILayer layer, String name) {
		mapModel.rename(layer, name);
		update();
	}

	public void setActive(ILayer iLayer, boolean checked) {
		iLayer.setActive(checked);
		this.screenManager.redraw();
	}
	
	public int getMaxData() {
		return maxData;
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
	
	public LayerUpdater getLayerUpdater (){
		return this.layerUpdater;
	}
	
	public IPhysicalOperator getOperator(){
		return this.operator;
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

	

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}