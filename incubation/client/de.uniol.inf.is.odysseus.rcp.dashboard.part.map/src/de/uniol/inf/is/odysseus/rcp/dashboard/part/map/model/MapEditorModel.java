package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.OwnProperties;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard.MapDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerTypeRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.NullConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.VectorLayerConfiguration;

//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.TracemapLayerConfiguration;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap.Heatmap;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.tracemap.TraceLayer;

public class MapEditorModel extends ModelObject {

	private static final Logger LOG = LoggerFactory.getLogger(MapEditorModel.class);

	public static final String MAP = "map";

	private String layerSettings = "";

	private int layercount;
	private int srid;
	private int startIndex;
	private int endIndex;

	private LinkedList<ILayer> layers = new LinkedList<ILayer>();
	private TreeMap<String, LayerUpdater> connections = new TreeMap<String, LayerUpdater>();

	private ScreenManager screenManager = null;
	private OwnProperties ownProperties;

	public void init(MapDashboardPart mapDashboardPart) {
		this.screenManager = mapDashboardPart.getScreenManager();
		ownProperties = new OwnProperties();
		for (LayerUpdater layerUpdater : connections.values()) {
			screenManager.addPropertyChangeListener(layerUpdater);
			screenManager.addConnection(layerUpdater);
		}

		// Cause if we load it from a file, the
		// screenManger was null
		for (ILayer layer : layers) {
			if (layer != null) {
				layer.init(screenManager, null, null);
			}
		}
		screenManager.redraw();

	}

	// TODO Andere Layertypen adden
	public String save() {
		this.layerSettings = "";
		for (ILayer layer : layers) {
			LayerConfiguration configuration = layer.getConfiguration();
			boolean checked = layer.isActive();
			if (configuration instanceof NullConfiguration) {
				this.layerSettings += addToSettingString((NullConfiguration) configuration, layer.getName(), checked)
						+ "\\";
			} else if (configuration instanceof RasterLayerConfiguration) {
				this.layerSettings += addToSettingString((RasterLayerConfiguration) configuration, checked) + "\\";
				// }else if(configuration instanceof HeatmapLayerConfiguration){
				// this.layerSettings +=
				// addToSettingString((HeatmapLayerConfiguration)configuration);
			}
		}
		return layerSettings;
	}

	private String addToSettingString(NullConfiguration configuration, String name, boolean checked) {
		return "BasicLayer;" + name + ";" + checked;
	}

	private String addToSettingString(RasterLayerConfiguration configuration, boolean checked) {
		String layerSetting = "RasterLayerConfiguration;";
		layerSetting += configuration.getName() + ";";
		layerSetting += configuration.getUrl() + ";";
		layerSetting += configuration.getFormat() + ";";
		layerSetting += configuration.getMinZoom() + ";";
		layerSetting += configuration.getMaxZoom() + ";";
		layerSetting += configuration.getTileSizeX() + ";";
		layerSetting += configuration.getTileSizeY() + ";";
		layerSetting += configuration.getSrid() + ";";

		if (configuration.isCoverageProjected()) {
			// add "null" for coverageGeographic
			layerSetting += "null;";

			Envelope coverage = configuration.getCoverage();
			layerSetting += coverage.getMinX() + ";" + coverage.getMaxX() + ";" + coverage.getMinY() + ";"
					+ coverage.getMaxY() + ";";
		} else {
			Envelope coverage = configuration.getCoverage();
			layerSetting += coverage.getMinX() + ";" + coverage.getMaxX() + ";" + coverage.getMinY() + ";"
					+ coverage.getMaxY() + ";";
			// add "null" for coverageProjected
			layerSetting += "null;";
		}
		layerSetting += checked;

		return layerSetting;
	}

	public void load(String layerSettings) {
		layers.clear();

		if (!layerSettings.isEmpty()) {
			this.layerSettings = layerSettings;
			ArrayList<String> configurationList = new ArrayList<String>();
			int startIndex = 0;

			// Split the big String into each layerConfiguration String
			// separated by "\\"
			while (startIndex < layerSettings.length()) {
				int endIndex = layerSettings.indexOf("\\", startIndex);
				String configurationString = layerSettings.substring(startIndex, endIndex);
				configurationList.add(configurationString);
				startIndex = endIndex + 1;
			}

			for (String configuration : configurationList) {
				int endIndex = configuration.indexOf(";");
				String layerConfigurationType = configuration.substring(0, endIndex);
				String configurationSettings = configuration.substring(endIndex + 1);

				if (layerConfigurationType.equals("BasicLayer")) {
					loadBasicConfiguration(configurationSettings);
				} else if (layerConfigurationType.equals("RasterLayerConfiguration")) {
					loadRasterLayerConfiguration(configurationSettings);
				}

			}
		}
	}

	private void loadBasicConfiguration(String configuration) {
		startIndex = 0;
		endIndex = configuration.indexOf(";");
		BasicLayer basic = new BasicLayer();

		// Get the name of the BasicLayer
		String subString = configuration.substring(startIndex, endIndex);
		basic.setName(subString);

		// Gets activity
		startIndex = endIndex + 1;
		subString = configuration.substring(startIndex);
		basic.setActive(Boolean.valueOf(subString));

		layers.add(basic);
	}

	// TODO Check
	private void loadRasterLayerConfiguration(String configuration) {
		startIndex = 0;
		endIndex = 0;
		endIndex = configuration.indexOf(";");
		double minX, maxX, minY, maxY;

		// Gets the name
		String substring = configuration.substring(startIndex, endIndex);
		RasterLayerConfiguration rlc = new RasterLayerConfiguration(substring);

		// Sets the setting one by one
		rlc.setUrl(getSubString(configuration, ";"));
		rlc.setFormat(getSubString(configuration, ";"));
		rlc.setMinZoom(Integer.valueOf(getSubString(configuration, ";")));
		rlc.setMaxZoom(Integer.valueOf(getSubString(configuration, ";")));
		rlc.setTileSize(Integer.valueOf(getSubString(configuration, ";")),
				Integer.valueOf(getSubString(configuration, ";")));
		rlc.setSrid(Integer.valueOf(getSubString(configuration, ";")));

		substring = getSubString(configuration, ";");

		if (!substring.equals("null")) {
			minX = Double.valueOf(substring);
			maxX = Double.valueOf(getSubString(configuration, ";"));
			minY = Double.valueOf(getSubString(configuration, ";"));
			maxY = Double.valueOf(getSubString(configuration, ";"));

			rlc.setCoverageProjected(minX, maxX, minY, maxY);
		} else {
			minX = Double.valueOf(getSubString(configuration, ";"));
			maxX = Double.valueOf(getSubString(configuration, ";"));
			minY = Double.valueOf(getSubString(configuration, ";"));
			maxY = Double.valueOf(getSubString(configuration, ";"));

			rlc.setCoverageGeographic(minX, maxX, minY, maxY);
			startIndex = endIndex + 1;
			endIndex = configuration.indexOf(";", startIndex);
		}

		RasterLayer rasterLayer = new RasterLayer(rlc);
		rasterLayer.setActive(Boolean.valueOf(getSubString(configuration, ";")));
		layers.add(rasterLayer);
	}

	private String getSubString(String string, String separator) {
		startIndex = endIndex + 1;
		endIndex = string.indexOf(separator, startIndex);
		String subString = string.substring(startIndex, endIndex);
		return subString;
	}

	/**
	 * Checks the type of the layerConfiguration and calls the right method to
	 * add
	 * 
	 * @param layerConfiguration
	 */
	public void addLayer(LayerConfiguration layerConfiguration) {
		layercount++;
		ILayer layer = null;

		if (layerConfiguration instanceof RasterLayerConfiguration) {
			layer = addLayer((RasterLayerConfiguration) layerConfiguration);
			// else if (layerConfiguration instanceof VectorLayerConfiguration)
			// layer = addLayer((VectorLayerConfiguration) layerConfiguration);
			// else if (layerConfiguration instanceof
			// TracemapLayerConfiguration)
			// layer = addLayer((TracemapLayerConfiguration)
			// layerConfiguration);
			// else if (layerConfiguration instanceof HeatmapLayerConfiguration)
			// layer = addLayer((HeatmapLayerConfiguration) layerConfiguration);
		} else {
			layer = addLayer();
		}

		firePropertyChange(MAP, null, this);
		layers.addLast(layer);
	}

	private ILayer addLayer() {
		ILayer layer = new BasicLayer();
		if (screenManager != null) {
			layer.init(screenManager, null, null);
		}
		return layer;
	}

	private ILayer addLayer(RasterLayerConfiguration layerConfiguration) {
		ILayer layer = new RasterLayer(layerConfiguration);

		if (screenManager != null) {
			layer.init(screenManager, null, null);
		}
		return layer;
	}

	// private ILayer addLayer(VectorLayerConfiguration layerConfiguration) {
	// ILayer layer = null;
	// SDFAttribute attribute = null;
	// SDFSchema schema = null;
	//
	// // Find the Query
	// for (LayerUpdater connection : connections.values()) {
	// if
	// (connection.getQuery().getQueryText().equals(layerConfiguration.getQuery()))
	// {
	//
	// // Connect the Stream to the iStreamListner
	// schema = connection.getConnection().getOutputSchema();
	// for (SDFAttribute tmpAttribute : schema) {
	// if
	// (tmpAttribute.getAttributeName().equals(layerConfiguration.getAttribute()))
	// {
	// attribute = tmpAttribute;
	// layer = LayerTypeRegistry.getLayer(attribute.getDatatype());
	// layer.setConfiguration(layerConfiguration);
	//
	// // PreInit
	// layer.init(null, schema, attribute);
	// connection.add(layer);
	// }
	// }
	// }
	// }
	// if (screenManager != null && layer != null) {
	// layer.init(screenManager, schema, attribute);
	// }
	// return layer;
	// }

	// /**
	// * Adds an HeatmapLayer to the map
	// *
	// * @param layerConfiguration
	// * @return The layer
	// */
	// private ILayer addLayer(HeatmapLayerConfiguration layerConfiguration) {
	// ILayer layer = new Heatmap(layerConfiguration);
	// if (screenManager != null) {
	// layer.init(screenManager, null, null);
	// }
	//
	// // Add to the selected connection (LayerUpdater)
	// for (LayerUpdater connection : connections.values()) {
	// if (connection.getQuery().getQueryText()
	// .equals(layerConfiguration.getQuery())) {
	// connection.add(layer);
	// }
	// }
	//
	// // We don't want to set it active manually, too much clicks ...
	// layer.setActive(true);
	//
	// return layer;
	// }

	// /**
	// * Adds a TracemapLayer to the map
	// *
	// * @param layerConfiguration
	// * @return The layer
	// */
	// private ILayer addLayer(TracemapLayerConfiguration layerConfiguration) {
	// ILayer layer = new TraceLayer(layerConfiguration);
	// if (screenManager != null) {
	// layer.init(screenManager, null, null);
	// }
	//
	// // Add to the selected connection (LayerUpdater)
	// for (LayerUpdater connection : connections.values()) {
	// if (connection.getQuery().getQueryText()
	// .equals(layerConfiguration.getQuery())) {
	// connection.add(layer);
	// }
	// }
	//
	// // We don't want to set it active manually, too much clicks ...
	// layer.setActive(true);
	//
	// return layer;
	// }

	public void editLayer(ILayer layer, LayerConfiguration layerConfiguration) {
		if (layerConfiguration instanceof RasterLayerConfiguration)
			layer = editLayer((RasterLayerConfiguration) layerConfiguration);
		// else if (layerConfiguration instanceof VectorLayerConfiguration)
		// layer = editLayer((VectorLayerConfiguration) layerConfiguration);
		// else if (layerConfiguration instanceof TracemapLayerConfiguration)
		// layer = editLayer((TracemapLayerConfiguration) layerConfiguration);
		// else if (layerConfiguration instanceof HeatmapLayerConfiguration)
		// layer = editLayer((HeatmapLayerConfiguration) layerConfiguration);

		firePropertyChange(MAP, null, this);
	}

	private ILayer editLayer(RasterLayerConfiguration layerConfiguration) {
		ILayer layer = new RasterLayer(layerConfiguration);
		return layer;
	}

	// private ILayer editLayer(VectorLayerConfiguration layerConfiguration){
	// ILayer layer = new VectorLayer(layerConfiguration);
	// return layer;
	// }

	public void removeLayer(ILayer layer) {
		layers.remove(layer);
		firePropertyChange(MAP, null, this);
	}

	public void layerUp(ILayer layer) {
		LinkedList<ILayer> group = layers;
		if (group.contains(layer)) {
			int positionOrign = group.lastIndexOf(layer);
			int positionNext = positionOrign + 1;
			if (positionNext < group.size()) {
				ILayer tmp = group.get(positionOrign);
				ILayer tmp1 = group.get(positionNext);

				group.set(positionNext, tmp);
				group.set(positionOrign, tmp1);
			}
		}
	}

	public void layerTop(ILayer layer) {
		LinkedList<ILayer> group = layers;
		if (group.contains(layer)) {
			int positionOrign = group.lastIndexOf(layer);
			int positionTop = group.size() - 1;
			if (positionTop != positionOrign) {
				ILayer tmp = group.get(positionOrign);
				ILayer tmp1 = group.get(positionTop);

				group.set(positionTop, tmp);
				group.set(positionOrign, tmp1);
			}
		}
	}

	public void layerDown(ILayer layer) {
		LinkedList<ILayer> group = layers;
		if (group.contains(layer)) {
			int positionOrign = group.lastIndexOf(layer);
			int positionNext = positionOrign - 1;
			if (positionNext >= 0) {
				ILayer tmp = group.get(positionOrign);
				ILayer tmp1 = group.get(positionNext);

				group.set(positionNext, tmp);
				group.set(positionOrign, tmp1);
			}
		}
	}

	public void layerBottom(ILayer layer) {
		LinkedList<ILayer> group = layers;
		if (group.contains(layer)) {
			int positionOrign = group.lastIndexOf(layer);
			int positionBottom = 0;
			if (positionBottom != positionOrign) {
				ILayer tmp = group.get(positionOrign);
				ILayer tmp1 = group.get(positionBottom);

				group.set(positionBottom, tmp);
				group.set(positionOrign, tmp1);
			}
		}
	}

	// TODO This should be handled by the Edit-Button later
	public void rename(ILayer layer, String name) {
		layer.setName(name);
	}

	// TODO THIS SHOULD BE DONE BY THE DASHBOARDPART
	public void addConnection(IStreamConnection<Object> connection, IPhysicalQuery query,
			MapDashboardPart mapDashboardPart) {
		LOG.debug("Add Connection: " + query.getLogicalQuery().getQueryText());

		if (!connections.containsKey(String.valueOf(query.hashCode()))) {

			LayerUpdater updater = new LayerUpdater(mapDashboardPart, query, connection);
			connections.put(String.valueOf(query.hashCode()), updater);
			if (screenManager != null) {
				// If this Model is not opened by a file
				screenManager.addPropertyChangeListener(updater);
				screenManager.addConnection(updater);
			}

			LOG.debug("Bind Query: " + query.getID());
			LOG.debug("Bind Query: " + query.getLogicalQuery().getQueryText());
		}
		firePropertyChange(MAP, null, this);
	}

	public void removeConnection(LayerUpdater connection) {
		String key = String.valueOf(connection.getQuery().hashCode());
		for (ILayer layer : connections.get(key)) {
			removeLayer(layer);
		}
		connections.remove(key);
		screenManager.removeConnection(connection);
		firePropertyChange(MAP, null, this);
	}

	public String[] getLayerNameList() {
		ArrayList<String> names = new ArrayList<String>();
		for (ILayer layer : layers) {
			names.add(layer.getComplexName());
		}
		return names.toArray(new String[names.size()]);
	}

	public String getNextLayerName() {
		return "Layer" + layercount++;
	}

	public int getSRID() {
		return this.srid;
	}

	public void setSrid(int srid) {
		this.srid = srid;
	}

	public LinkedList<ILayer> getLayers() {
		return layers;
	}

	public Collection<LayerUpdater> getConnectionCollection() {
		return connections.values();
	}

	public OwnProperties getOwnProperties() {
		return ownProperties;
	}
}
