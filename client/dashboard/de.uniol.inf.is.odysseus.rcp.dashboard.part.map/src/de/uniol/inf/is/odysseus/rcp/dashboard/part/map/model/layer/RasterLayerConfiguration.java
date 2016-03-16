package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer;

import com.vividsolutions.jts.geom.Envelope;

public class RasterLayerConfiguration extends LayerConfiguration {

	private static final long serialVersionUID = -1186631268002859663L;

	private String url = null;
	private int urlNumber = 0;
	private String format = "RESTFUL Tile Server";
	private int minZoom = 0;
	private int maxZoom = 0;
	private int tileSizeX = 0;
	private int tileSizeY = 0;
	private int srid = 0;
	private Envelope coverageGeographic = null;
	private Envelope coverageProjected = null;

	public RasterLayerConfiguration() {

	}

	public RasterLayerConfiguration(String layerName) {
		super(layerName);
	}

	public RasterLayerConfiguration(RasterLayerConfiguration toCopy) {
		super(toCopy);
		this.url = toCopy.url;
		// this.format = toCopy.format;
		this.minZoom = toCopy.minZoom;
		this.maxZoom = toCopy.maxZoom;
		this.tileSizeX = toCopy.tileSizeX;
		this.tileSizeY = toCopy.tileSizeY;
		this.srid = toCopy.srid;
		this.coverageGeographic = new Envelope(toCopy.coverageGeographic);
		this.coverageProjected = new Envelope(toCopy.coverageProjected);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(int minZoom) {
		this.minZoom = minZoom;
	}

	public int getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		this.maxZoom = maxZoom;
	}

	public int getTileSizeX() {
		return tileSizeX;
	}

	public int getTileSizeY() {
		return tileSizeY;
	}

	public void setTileSize(int tileSizeX, int tileSizeY) {
		this.tileSizeX = tileSizeX;
		this.tileSizeY = tileSizeY;
	}

	public int getSrid() {
		return srid;
	}

	public void setSrid(int srid) {
		this.srid = srid;
	}

	public boolean isCoverageProjected() {
		return (this.coverageProjected != null);
	}

	public Envelope getCoverage() {
		if (this.isCoverageProjected())
			return this.coverageProjected;
		return this.coverageGeographic;
	}

	public void setCoverageProjected(double minX, double maxX, double minY, double maxY) {
		// As far as I know this is on the map, therefore geographic coordinated
		// need to be projected (EPSG:3857, which should be a Mercator
		// projection)
		this.coverageProjected = new Envelope(minX, maxX, minY, maxY);
		this.coverageGeographic = null;
	}

	public void setCoverageGeographic(double minX, double maxX, double minY, double maxY) {
		// As far as I know this is on the geoid and therefore uses WGS84
		// (EPSG:4326)
		this.coverageGeographic = new Envelope(minX, maxX, minY, maxY);
		this.coverageProjected = null;
	}

	public void setUrlNumber(int number) {
		urlNumber = number;
	}

	public int getUrlNumber() {
		return urlNumber;
	}

}
