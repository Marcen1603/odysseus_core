package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard;

import java.util.Map;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;

public class MapDashboardPart extends AbstractDashboardPart implements IMapDashboardAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(MapDashboardPart.class);

	

	private String attributeList = "*";
	private String layerSettings = "";

	private int srid = 0;
	private int maxData = 100;
	private int updateInterval = 5;

	protected ScreenTransformation transformation;
	protected ScreenManager screenManager;
	private MapEditorModel mapModel;
	private LayerUpdater layerUpdater;
	private IPhysicalOperator operator;
	private Thread updateThread;

	private boolean initiated = false;
	/**
	 * Needed to check if the user is still in the wizard to create the mapDashboardpart
	 */
	private boolean wizard = true;

	@Override
	public void createPartControl(final Composite parent, ToolBar toolbar) {
		parent.setLayout(new GridLayout(1, false));
		
		transformation = new ScreenTransformation();
		screenManager = new ScreenManager(transformation, this);
		screenManager.setCanvasViewer(screenManager.createCanvas(parent));
		
		if(mapModel.getLayers().isEmpty()){
			BasicLayer basic = new BasicLayer();
			basic.setActive(true);
			mapModel.getLayers().addFirst(basic);
		}
		layerUpdater = mapModel.addConnection(this);		
		mapModel.init(this);

		updateThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!parent.isDisposed()) {
					final Display disp = PlatformUI.getWorkbench().getDisplay();
					if (!disp.isDisposed()) {
						disp.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (!screenManager.getCanvas().isDisposed()) {
									screenManager.redraw();
								}
							}
						});
					}
					waiting(updateInterval);
				}
			}

		});

		updateThread.setName("StreamList Updater");
		updateThread.start();
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
		init();

		attributeList = saved.get("Attributes");
		maxData = Integer.valueOf(saved.get("MaxData"));
		updateInterval = Integer.valueOf(saved.get("UpdateInterval"));
		srid = Integer.valueOf(saved.get("SRID"));
		layerSettings = saved.get("LayerSettings");
		if (layerSettings != null) {
			mapModel.load(layerSettings);
		}
		
		wizard = false;
	};

	@Override
	public void streamElementReceived(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: Map-DashboardPart is only for spatial relational tuple!");
			return;
		}

		// TODO IST DAS IN ABHÄNGIGKEIT VOM TIMESLIDER?
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
			//System.out.println("Tupel:" + tuple);
		}

		// Prevent an overflow in the puffer
		layerUpdater.checkForPufferSize();

		//TODO SOLLTE DAS HIER SEIN?
		// Should we redraw here or just if we added the tupel to the current
		// list?
		screenManager.redraw();

	}

	public void init() {
		
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
			
			layerUpdater = mapModel.addConnection(this);
			layerUpdater.setMaxPufferSize(maxData);
			initiated = true;
		}

	}
	
	private static void waiting(long length) {
		try {
			Thread.sleep(length);
		} catch (final InterruptedException e) {
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
		layerUpdater.setMaxPufferSize(maxData);
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
		layerUpdater.setTimeRange(updateInterval);
	}

	
	public LayerUpdater getLayerUpdater (){
		return this.layerUpdater;
	}
	
	public IPhysicalOperator getOperator(){
		return this.operator;
	}
	
	public void setOperator(IPhysicalOperator operator){
		this.operator = operator;
	}
	
	public boolean getWizardBoolean(){
		return wizard;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}