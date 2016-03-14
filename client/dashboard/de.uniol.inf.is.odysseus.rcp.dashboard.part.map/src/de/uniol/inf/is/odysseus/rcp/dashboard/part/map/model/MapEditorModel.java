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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.vividsolutions.jts.geom.Envelope;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.OwnProperties;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Puffer;
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

public class MapEditorModel {

	// private static final Logger LOG =
	// LoggerFactory.getLogger(MapEditorModel.class);

	public static final String MAP = "map";

	private int layercount;
	private int srid;
	private int startIndex;
	private int endIndex;

	private LinkedList<ILayer> layers = new LinkedList<ILayer>();

	private ScreenManager screenManager = null;
	private OwnProperties ownProperties;
	private Puffer puffer;

	public void init(MapDashboardPart mapDashboardPart) {

		for (ILayer layer : layers) {
			if (layer != null) {
				layer.init(screenManager, null, null);
			}
		}
		screenManager.redraw();

	}

	/**
	 * Makes a String which contains the settings of all layers. The individual
	 * layer settings are separated by
	 * "\\". Each single attribute for the layer configuration is separated by a "
	 * ;"
	 * 
	 * @return String which contains all layer setting
	 */
	public String save() {
		// this.layerSettings = "";
		// for (ILayer layer : layers) {
		// LayerConfiguration configuration = layer.getConfiguration();
		// boolean checked = layer.isActive();
		// if (configuration instanceof NullConfiguration) {
		// this.layerSettings += addToSettingString((NullConfiguration)
		// configuration, layer.getName(), checked)
		// + "\\";
		// } else if (configuration instanceof HeatmapLayerConfiguration) {
		// this.layerSettings += addToSettingString((HeatmapLayerConfiguration)
		// configuration, checked) + "\\";
		// } else if (configuration instanceof TracemapLayerConfiguration) {
		// // TODO
		// } else if (configuration instanceof RasterLayerConfiguration) {
		// this.layerSettings += addToSettingString((RasterLayerConfiguration)
		// configuration, checked) + "\\";
		// }
		// }

		// TODO Try with serialization

		// Save only the configuration, not the whole layer
		List<LayerConfiguration> layerConfigurations = new ArrayList<>();
		for (ILayer layer : layers) {
			LayerConfiguration configuration = layer.getConfiguration();
			layerConfigurations.add(configuration);
		}

		String layerSettings = getSerializedString(layerConfigurations);
		// System.out.println(test);
		// @SuppressWarnings("unused")
		// List<LayerConfiguration> loadedLayerConfigs =
		// getLayersFromSerializedString(test);

		return layerSettings;
	}

	private String getSerializedString(Object object) {
		byte[] data = SerializationUtils.serialize((Serializable) object);
		String encoded = Base64.getEncoder().encodeToString(data);
		return encoded;
	}

	private List<LayerConfiguration> getLayerConfigurationsFromSerializedString(String dataString) {
		try {
			byte[] data = Base64.getDecoder().decode(dataString);
			Object listObject = convertFromBytes(data);
			@SuppressWarnings("unchecked")
			List<LayerConfiguration> layers = (List<LayerConfiguration>) listObject;
			return layers;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
			return in.readObject();
		}
	}

	private String addToSettingString(NullConfiguration configuration, String name, boolean checked) {
		return "BasicLayer;" + name + ";" + checked;
	}

	private String addToSettingString(RasterLayerConfiguration configuration, boolean checked) {
		String layerSetting = "RasterLayerConfiguration;";
		layerSetting += configuration.getName() + ";";
		layerSetting += configuration.getUrl() + ";";
		layerSetting += configuration.getUrlNumber() + ";";
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

	private String addToSettingString(HeatmapLayerConfiguration configuration, boolean checked) {
		String layerSetting = "HeatmapLayerConfiguration;";
		layerSetting += configuration.getName() + ";";
		layerSetting += configuration.getSrid() + ";";
		layerSetting += configuration.getGeometricAttributePosition() + ";";
		layerSetting += configuration.getLatAttribute() + ";";
		layerSetting += configuration.getLngAttribute() + ";";
		layerSetting += configuration.usePoint() + ";";
		layerSetting += configuration.getValueAttributePosition() + ";";

		// Just get the RGB values. Usually getMinColor would return e.g. "Color
		// {0 ,255, 0}"
		String subString = configuration.getMinColor().toString().trim();

		int startIndex = subString.indexOf("{");
		int endIndex = subString.indexOf("}");
		// Minus 5 to remove the alpha. New Color can only use 0 or 255. We need
		// values between those
		subString = subString.substring(startIndex + 1, endIndex - 5);
		layerSetting += subString + ";";

		// Just get the RGB values. Usually getMinColor would return e.g. "Color
		// {0 ,255, 0}"
		subString = configuration.getMaxColor().toString().trim();
		startIndex = subString.indexOf("{");
		endIndex = subString.indexOf("}");
		// Minus 5 to remove the alpha. New Color can only use 0 or 255.
		subString = subString.substring(startIndex + 1, endIndex - 5);
		layerSetting += subString + ";";

		layerSetting += configuration.getAlpha() + ";";
		layerSetting += configuration.isInterpolation() + ";";
		layerSetting += configuration.isAutoPosition() + ";";
		layerSetting += configuration.isHideWithoutInformation() + ";";
		layerSetting += configuration.getNumTilesWidth() + ";";
		layerSetting += configuration.getNumTilesHeight() + ";";
		layerSetting += configuration.getLatSW() + ";";
		layerSetting += configuration.getLngSW() + ";";
		layerSetting += configuration.getLatNE() + ";";
		layerSetting += configuration.getLngNE() + ";";
		layerSetting += checked + ";";

		return layerSetting;
	}

	// This could be done with an array
	/**
	 * Separates the given String into each layer configuration represented by a
	 * String
	 * 
	 * @param layerSettings
	 *            String which contains all layerConfigurations
	 */
	public void load(String layerSettings) {
		layers.clear();

//		if (!layerSettings.isEmpty()) {
//			ArrayList<String> configurationList = new ArrayList<String>();
//			int startIndex = 0;
//
//			// Split the big String into each layerConfiguration String
//			// separated by "\\"
//			while (startIndex < layerSettings.length()) {
//				int endIndex = layerSettings.indexOf("\\", startIndex);
//				String configurationString = layerSettings.substring(startIndex, endIndex);
//				configurationList.add(configurationString);
//				startIndex = endIndex + 1;
//			}
//
//			for (String configuration : configurationList) {
//				int endIndex = configuration.indexOf(";");
//				String layerConfigurationType = configuration.substring(0, endIndex);
//				String configurationSettings = configuration.substring(endIndex + 1);
//
//				if (layerConfigurationType.equals("BasicLayer")) {
//					loadBasicConfiguration(configurationSettings);
//				} else if (layerConfigurationType.equals("HeatmapLayerConfiguration")) {
//					loadHeatMapLayerConfiguration(configurationSettings);
//				} else if (layerConfigurationType.equals("RasterLayerConfiguration")) {
//					loadRasterLayerConfiguration(configurationSettings);
//				}
//
//			}
//		}

		List<LayerConfiguration> loadedLayerConfigs = getLayerConfigurationsFromSerializedString(layerSettings);

		for (LayerConfiguration layerConf : loadedLayerConfigs) {
			if (layerConf instanceof HeatmapLayerConfiguration) {
				Heatmap layer = new Heatmap((HeatmapLayerConfiguration) layerConf);
				this.layers.add(layer);
			} else if (layerConf instanceof TracemapLayerConfiguration) {
				TraceLayer layer = new TraceLayer((TracemapLayerConfiguration) layerConf);
				this.layers.add(layer);
			} else if (layerConf instanceof RasterLayerConfiguration) {
				RasterLayer layer = new RasterLayer((RasterLayerConfiguration) layerConf);
				this.layers.add(layer);
			} else if (layerConf instanceof NullConfiguration) {
				BasicLayer layer = new BasicLayer((NullConfiguration) layerConf);
				this.layers.add(layer);
			} else {
				BasicLayer layer = new BasicLayer();
				this.layers.add(layer);
			}

		}
	}

	private void loadBasicConfiguration(String configuration) {
		startIndex = 0;
		endIndex = configuration.indexOf(";");
		BasicLayer basic = new BasicLayer();

		String subString = configuration.substring(startIndex, endIndex);
		basic.setName(subString);

		startIndex = endIndex + 1;
		subString = configuration.substring(startIndex);
		basic.setActive(Boolean.valueOf(subString));

		layers.add(basic);
	}

	// This could also be handled by a string array
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
		rlc.setUrlNumber(Integer.valueOf(getSubString(configuration, ";")));
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
		ownProperties = new OwnProperties();

		ownProperties.getTileServer(rlc.getUrlNumber(), rlc);
		rasterLayer.setActive(Boolean.valueOf(getSubString(configuration, ";")));
		layers.add(rasterLayer);
	}

	private void loadHeatMapLayerConfiguration(String configuration) {
		startIndex = 0;
		endIndex = 0;
		endIndex = configuration.indexOf(";");
		int r, g, b;
		double latSW, lngSW, latNE, lngNE;

		String substring = configuration.substring(startIndex, endIndex);
		HeatmapLayerConfiguration hlc = new HeatmapLayerConfiguration(substring);

		hlc.setSrid(Integer.valueOf(getSubString(configuration, ";")));
		hlc.setGeometricAttributePosition(Integer.valueOf(getSubString(configuration, ";")));
		hlc.setLatAttribute(Integer.valueOf(getSubString(configuration, ";")));
		hlc.setLngAttribute(Integer.valueOf(getSubString(configuration, ";")));
		hlc.setUsePoint(Boolean.valueOf(getSubString(configuration, ";")));
		hlc.setValueAttributePosition(Integer.valueOf(getSubString(configuration, ";")));

		r = Integer.valueOf(getSubString(configuration, ","));
		endIndex++;
		g = Integer.valueOf(getSubString(configuration, ","));
		endIndex++;
		b = Integer.valueOf(getSubString(configuration, ";"));
		hlc.setMinColor(new Color(Display.getDefault(), r, g, b));

		r = Integer.valueOf(getSubString(configuration, ","));
		endIndex++;
		g = Integer.valueOf(getSubString(configuration, ","));
		endIndex++;
		b = Integer.valueOf(getSubString(configuration, ";"));
		hlc.setMaxColor(new Color(Display.getDefault(), r, g, b));

		hlc.setAlpha(Integer.valueOf(getSubString(configuration, ";")));
		hlc.setInterpolation(Boolean.valueOf(getSubString(configuration, ";")));
		hlc.setAutoPosition(Boolean.valueOf(getSubString(configuration, ";")));
		hlc.setHideWithoutInformation(Boolean.valueOf(getSubString(configuration, ";")));
		hlc.setNumTilesWidth(Integer.valueOf(getSubString(configuration, ";")));
		hlc.setNumTilesHeight(Integer.valueOf(getSubString(configuration, ";")));

		latNE = Double.valueOf(getSubString(configuration, ";"));
		hlc.setLatNE(latNE);
		lngNE = Double.valueOf(getSubString(configuration, ";"));
		hlc.setLngNE(lngNE);
		latSW = Double.valueOf(getSubString(configuration, ";"));
		hlc.setLatSW(latSW);
		lngSW = Double.valueOf(getSubString(configuration, ";"));
		hlc.setLngSW(lngSW);
		hlc.setCoverageGeographic(lngSW, lngNE, latSW, latNE);

		Heatmap heatmap = new Heatmap(hlc);
		heatmap.setActive(Boolean.valueOf(getSubString(configuration, ";")));
		heatmap.setPuffer(puffer);
		layers.add(heatmap);
	}

	/**
	 * Return the next setting value
	 * 
	 * @param string
	 *            the given string
	 * @param separator
	 *            used separator
	 * @return
	 */
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

		if (layerConfiguration instanceof HeatmapLayerConfiguration) {
			layer = addLayer((HeatmapLayerConfiguration) layerConfiguration);
		} else if (layerConfiguration instanceof TracemapLayerConfiguration) {
			layer = addLayer((TracemapLayerConfiguration) layerConfiguration);
		} else if (layerConfiguration instanceof RasterLayerConfiguration) {
			layer = addLayer((RasterLayerConfiguration) layerConfiguration);
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
		layer.setPuffer(puffer);
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
		layer.setPuffer(puffer);
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

	public void rename(ILayer layer, String name) {
		layer.setName(name);
	}

	// TODO
	/**
	 * Generates a puffer connection to the ScreenManager
	 * 
	 * @param mapDashboardPart
	 */
	public Puffer addConnection(MapDashboardPart mapDashboardPart) {
		this.screenManager = mapDashboardPart.getScreenManager();
		puffer = new Puffer(mapDashboardPart);
		if (screenManager != null) {
			// If this Model is not opened by a file
			screenManager.addPropertyChangeListener(puffer);
			screenManager.addConnection(puffer);
		}
		return puffer;
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

	public OwnProperties getOwnProperties() {
		return ownProperties;
	}
}
