package de.uniol.inf.is.odysseus.rcp.dashboard.part.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;

public class OwnProperties extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 607964719113040993L;
	String[] defaults = null;
	ArrayList<RasterLayerConfiguration> serverList = null;

	public OwnProperties() {
		try {
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("resources/mapstreamclient.properties");

			load(is);
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<RasterLayerConfiguration> getServerList() {
		if (serverList == null)
			createList();
		return serverList;
	}

	private void createList() {
		String[] list = listTileServer();
		for (int i = 0; i < list.length; i++) {
			this.serverList.add(i, new RasterLayerConfiguration("tileServer"));
		}
	}

	public String[] listTileServer() {
		int size = Integer.parseInt(this.getProperty("tileserver.size"));
		String[] tileServer = new String[size];
		for (int i = 0; i < size; i++) {
			tileServer[i] = this.getProperty("tileserver." + i + ".url");
		}
		return tileServer;
	}

	private String[] getTileServerDefaults() {
		if (defaults == null) {
			defaults = new String[11];
			defaults[0] = this.getProperty("tileserver.default.tile.format");
			defaults[1] = this.getProperty("tileserver.default.tile.width");
			defaults[2] = this.getProperty("tileserver.default.tile.height");
			defaults[3] = this.getProperty("tileserver.default.srid");
			defaults[4] = this.getProperty("tileserver.default.coverage.type");
			defaults[5] = this.getProperty("tileserver.default.coverage.minX");
			defaults[6] = this.getProperty("tileserver.default.coverage.maxX");
			defaults[7] = this.getProperty("tileserver.default.coverage.minY");
			defaults[8] = this.getProperty("tileserver.default.coverage.maxY");
			defaults[9] = this.getProperty("tileserver.default.minZoom");
			defaults[10] = this.getProperty("tileserver.default.maxZoom");
		}
		return defaults;
	};

	// TODO A get method should return something and not set it directly to the config
	public void getTileServer(int i, RasterLayerConfiguration config) {
		this.getTileServerDefaults();
		config.setFormat(this.getProperty("tileserver." + i + ".tile.format", defaults[0]));
		int width = Integer.parseInt(this.getProperty("tileserver." + i + ".tile.width", defaults[1]));
		int height = Integer.parseInt(this.getProperty("tileserver." + i + ".tile.height", defaults[2]));
		config.setTileSize(width, height);
		config.setSrid(Integer.parseInt(this.getProperty("tileserver." + i + ".srid", defaults[3])));
		String type = this.getProperty("tileserver." + i + ".coverage.type", defaults[4]).trim();
		double minX = Double.parseDouble(this.getProperty("tileserver." + i + ".coverage.minX", defaults[5]));
		double maxX = Double.parseDouble(this.getProperty("tileserver." + i + ".coverage.maxX", defaults[6]));
		double minY = Double.parseDouble(this.getProperty("tileserver." + i + ".coverage.minY", defaults[7]));
		double maxY = Double.parseDouble(this.getProperty("tileserver." + i + ".coverage.maxY", defaults[8]));
		if (type.equals("geographic")) {
			config.setCoverageGeographic(minX, maxX, minY, maxY);
		} else if (type.equals("projected")) {
			config.setCoverageProjected(minX, maxX, minY, maxY);
		}
		config.setMinZoom(Integer.parseInt(this.getProperty("tileserver." + i + ".minZoom", defaults[9])));
		config.setMaxZoom(Integer.parseInt(this.getProperty("tileserver." + i + ".maxZoom", defaults[10])));
	}
}
