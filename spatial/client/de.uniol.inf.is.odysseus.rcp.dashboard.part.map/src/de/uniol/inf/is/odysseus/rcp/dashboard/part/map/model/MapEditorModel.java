package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.OwnProperties;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Buffer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard.MapDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.NullConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.TracemapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.heatmap.Heatmap;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.tracemap.TraceLayer;

/**
 * Model for all the map layers. Holds the map layers, can return a string to
 * save and load them.
 * 
 * @author Tobias Brandt
 *
 */
public class MapEditorModel {

	// private static final Logger LOG =
	// LoggerFactory.getLogger(MapEditorModel.class);

	public static final String MAP = "map";

	// Spatial reference identifier to identify the coordinate system
	private int srid;

	private LinkedList<ILayer> layers = new LinkedList<ILayer>();

	private ScreenManager screenManager = null;
	private OwnProperties ownProperties;
	private Buffer buffer;

	/**
	 * Initiates all available layers and draws the layers.
	 * 
	 * @param mapDashboardPart
	 */
	public void init(MapDashboardPart mapDashboardPart) {
		for (ILayer layer : layers) {
			if (layer != null) {
				layer.init(screenManager, null, null);
			}
		}
		screenManager.redraw();
	}

	/**
	 * Creates a base64 serialization string from the list of all layers. This
	 * string can be saved and loaded, to persist the map.
	 * 
	 * @return String that contains all layer setting
	 */
	public String save() {
		// Save only the configuration, not the whole layer
		List<LayerConfiguration> layerConfigurations = new ArrayList<>();
		for (ILayer layer : layers) {
			LayerConfiguration configuration = layer.getConfiguration();
			layerConfigurations.add(configuration);
		}

		String layerSettings = getSerializedString(layerConfigurations);
		return layerSettings;
	}

	/**
	 * Serializes an object with a base64 encoder.
	 * 
	 * @param object
	 *            The object you want to serialize
	 * @return A base64 serialization string from the object
	 */
	private String getSerializedString(Object object) {
		byte[] data = SerializationUtils.serialize((Serializable) object);
		String encoded = Base64.getEncoder().encodeToString(data);
		return encoded;
	}

	/**
	 * Reads a base64 string that encodes a list of layerConfigurations.
	 * Converts the string to a list.
	 * 
	 * @param dataString
	 *            The string with the base64 encoded string.
	 * @return A list of layerConfiguration
	 */
	private List<LayerConfiguration> getLayerConfigurationsFromSerializedString(String dataString) {
		try {
			byte[] data = Base64.getDecoder().decode(dataString);
			Object listObject = convertFromBytes(data);
			@SuppressWarnings("unchecked")
			List<LayerConfiguration> layers = (List<LayerConfiguration>) listObject;
			return layers;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Reads a byte array and converts it into an object
	 * 
	 * @param bytes
	 *            The bytes that should be converted into an object
	 * @return An object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
			return in.readObject();
		}
	}

	/**
	 * Decodes the base64 encoded string version of the list of
	 * layerConfiguration and creates the layers based on the saved
	 * configurations.
	 * 
	 * @param layerSettings
	 *            String which contains all layerConfigurations (base64 encoded)
	 */
	public void load(String layerSettings) {
		layers.clear();

		List<LayerConfiguration> loadedLayerConfigs = getLayerConfigurationsFromSerializedString(layerSettings);

		for (LayerConfiguration layerConf : loadedLayerConfigs) {
			addLayer(layerConf);
		}
	}

	/**
	 * Checks the type of the layerConfiguration and calls the right method to
	 * add
	 * 
	 * @param layerConfiguration
	 */
	public void addLayer(LayerConfiguration layerConfiguration) {
		ILayer layer = null;

		if (layerConfiguration instanceof HeatmapLayerConfiguration) {
			layer = addLayer((HeatmapLayerConfiguration) layerConfiguration);
		} else if (layerConfiguration instanceof TracemapLayerConfiguration) {
			layer = addLayer((TracemapLayerConfiguration) layerConfiguration);
		} else if (layerConfiguration instanceof RasterLayerConfiguration) {
			layer = addLayer((RasterLayerConfiguration) layerConfiguration);
		} else if (layerConfiguration instanceof NullConfiguration) {
			layer = new BasicLayer((NullConfiguration) layerConfiguration);
		} else {
			layer = addLayer();
		}
		// firePropertyChange(MAP, null, this);
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

	/**
	 * Adds an HeatmapLayer to the map
	 *
	 * @param layerConfiguration
	 * @return The layer
	 */
	private ILayer addLayer(HeatmapLayerConfiguration layerConfiguration) {
		ILayer layer = new Heatmap(layerConfiguration);
		if (screenManager != null) {
			layer.init(screenManager, null, null);
		}

		// We don't want to set it active manually, too much clicks ...
		layer.setActive(true);
		layer.setBuffer(buffer);
		return layer;
	}

	/**
	 * Adds a TracemapLayer to the map
	 *
	 * @param layerConfiguration
	 * @return The layer
	 */
	private ILayer addLayer(TracemapLayerConfiguration layerConfiguration) {
		ILayer layer = new TraceLayer(layerConfiguration);
		if (screenManager != null) {
			layer.init(screenManager, null, null);
		}

		// We don't want to set it active manually, too much clicks ...
		layer.setActive(true);
		layer.setBuffer(buffer);
		return layer;
	}

	/**
	 * Edit a layer by removing the old one and place in the "edit" one.
	 * 
	 * @param layer
	 * @param layerConfiguration
	 */
	public void editLayer(ILayer layer, LayerConfiguration layerConfiguration) {
		LinkedList<ILayer> group = layers;
		if (group.contains(layer)) {
			int positionOrign = group.lastIndexOf(layer);
			int positionLast = group.size();
			addLayer(layerConfiguration);

			ILayer tmp = group.get(positionOrign);
			ILayer tmp1 = group.get(positionLast);

			group.set(positionLast, tmp);
			group.set(positionOrign, tmp1);

			removeLayer(group.getLast());
		}
	}

	/**
	 * Removes a layer from the list
	 * 
	 * @param layer
	 */
	public void removeLayer(ILayer layer) {
		layers.remove(layer);
	}

	/**
	 * Rearranges the layers and puts the given layer one up (it's more visible)
	 * 
	 * @param layer
	 *            The layer that should be played one up
	 */
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

	/**
	 * Puts the given layer on the top (above all others, it's most visible)
	 * 
	 * @param layer
	 *            The layer to be placed on top
	 */
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

	/**
	 * Puts the given layer one down. It's less visible.
	 * 
	 * @param layer
	 *            The layer to be put one down
	 */
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

	/**
	 * Puts the given layer at the bottom. It's least visible.
	 * 
	 * @param layer
	 *            The layer to be put on the bottom
	 */
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

	/**
	 * Renames the given layer.
	 * 
	 * @param layer
	 *            The layer to be renamed
	 * @param name
	 *            The new name of the layer
	 */
	public void rename(ILayer layer, String name) {
		layer.setName(name);
	}

	/**
	 * Generates a puffer connection to the ScreenManager
	 * 
	 * @param mapDashboardPart
	 */
	public Buffer addConnection(MapDashboardPart mapDashboardPart) {
		this.screenManager = mapDashboardPart.getScreenManager();
		buffer = new Buffer(mapDashboardPart);
		if (screenManager != null) {
			// If this Model is not opened by a file
			screenManager.addPropertyChangeListener(buffer);
			screenManager.addConnection(buffer);
		}
		return buffer;
	}

	/**
	 * 
	 * @return An array with the names of all layers
	 */
	public String[] getLayerNameList() {
		ArrayList<String> names = new ArrayList<String>();
		for (ILayer layer : layers) {
			names.add(layer.getComplexName());
		}
		return names.toArray(new String[names.size()]);
	}

	/**
	 * Generates a name for the next layer in the form "Layer" and the next free
	 * number based on the length of the list.
	 * 
	 * @return
	 */
	public String getNextLayerName() {
		return "Layer" + this.layers.size();
	}

	/**
	 * 
	 * @return The SRID (Spatial Reference Identifier) of the map
	 */
	public int getSRID() {
		return this.srid;
	}

	/**
	 * 
	 * @param srid The SRID (Spatial Reference Identifier) of the map
	 */
	public void setSrid(int srid) {
		this.srid = srid;
	}

	/**
	 * 
	 * @return A list of all layers
	 */
	public LinkedList<ILayer> getLayers() {
		return layers;
	}

	public OwnProperties getOwnProperties() {
		return ownProperties;
	}
}
