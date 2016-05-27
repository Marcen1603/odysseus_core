package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard;

import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractMultiSourceDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.IMapDashboardAdapter;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Buffer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;

public class MapDashboardPart extends AbstractMultiSourceDashboardPart implements IMapDashboardAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(MapDashboardPart.class);

	private String attributeList = "*";
	private String layerSettings = "";

	private int srid = 0;
	private int maxData = 1000;
	private int updateInterval = 5;

	protected ScreenTransformation transformation;
	protected ScreenManager screenManager;
	private MapEditorModel mapModel;
	private Buffer puffer;
	private Collection<IPhysicalOperator> operators;

	/**
	 * If the MapEditorModel is initiated and the LayerUpdater is connected
	 */
	private boolean initiated = false;

	/**
	 * Needed to check if the user is still in the wizard to create the
	 * mapDashboardpart
	 */
	private boolean wizard = true;

	@Override
	public void createPartControl(final Composite parent, ToolBar toolbar) {
		parent.setLayout(new GridLayout(1, false));

		transformation = new ScreenTransformation();
		screenManager = new ScreenManager(transformation, this);
		screenManager.setCanvasViewer(screenManager.createCanvas(parent));

		if (mapModel.getLayers().isEmpty() && wizard == true) {
			BasicLayer basic = new BasicLayer();
			basic.setActive(true);
			mapModel.getLayers().addFirst(basic);
		}
		puffer = mapModel.addConnection(this);
		puffer.setMaxPufferSize(maxData);

		if (!wizard && layerSettings != null) {
			mapModel.load(layerSettings);
		}

		mapModel.init(this);

	}

	@Override
	public Map<String, String> onSave() {
		if (mapModel.getLayers().isEmpty() && wizard) {
			BasicLayer basic = new BasicLayer();
			basic.setActive(true);
			mapModel.getLayers().addFirst(basic);
		}

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

		puffer.getPuffer().insert(tuple);
		if (screenManager.getInterval().getEnd().isInfinite()
				|| (screenManager.getInterval().getStart().beforeOrEquals(timestamp)
						&& this.screenManager.getInterval().getEnd().afterOrEquals(timestamp))) {
			// Add tuple to current list if the new timestamp is in the interval

			puffer.addTuple(tuple);

		}

		// Prevent an overflow in the puffer
		puffer.checkForBufferSize();

		if (updateInterval == 0)
			screenManager.redraw();
	}

	public void init() {

		if (!initiated) {
			mapModel = new MapEditorModel();

			if (mapModel.getSRID() == 0 && srid == 0) {
				// If a new file was created or the map has
				// no srid because of other reasons
				// set a standard srid, here 3857
				mapModel.setSrid(3857);
			} else {
				mapModel.setSrid(srid);
			}

			puffer = mapModel.addConnection(this);
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

	/**
	 * 
	 * @return The number of layers currently in the list
	 */
	public int getNumberOfLayers() {
		return mapModel.getLayers().size();
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
		if (maxData == 0)
			this.maxData = Integer.MAX_VALUE;
		else {
			this.maxData = maxData;
			puffer.setMaxPufferSize(maxData);
		}
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
		puffer.setTimeRange(updateInterval);
	}

	public Buffer getPuffer() {
		return this.puffer;
	}

	public Collection<IPhysicalOperator> getOperators() {
		return this.operators;
	}

	public void setOperators(Collection<IPhysicalOperator> operators) {
		this.operators = operators;
	}

	public boolean getWizardBoolean() {
		return wizard;
	}
}