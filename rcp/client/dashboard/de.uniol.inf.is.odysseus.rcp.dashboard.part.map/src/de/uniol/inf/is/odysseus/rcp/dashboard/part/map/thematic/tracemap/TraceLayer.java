package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.tracemap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.ProjCoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Buffer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard.MapConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.DataSet;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.TracemapLayerConfiguration;

public class TraceLayer extends RasterLayer {

	private static final Logger LOG = LoggerFactory.getLogger(MapConfigurer.class);

	private static final long serialVersionUID = -6639695562970893314L;
	private Buffer buffer;
	private ScreenManager screenManager;
	private TracemapLayerConfiguration config;
	private HashMap<Integer, PointInTime[]> timeHashMap;
	private Envelope searchEnv;
	private Envelope zoomEnv;
	private double colorOffset;

	public TraceLayer(TracemapLayerConfiguration config) {
		super(config);
		this.config = config;
		timeHashMap = new HashMap<Integer, PointInTime[]>();
		zoomEnv = new Envelope();
	}

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute) {
		this.screenManager = screenManager;
		this.srid = screenManager.getSRID();
	}

	@Override
	public void draw(GC gc) {
		// Get all the lines from the data and draw them
		HashMap<Integer, LineString> linesToDraw = getLines();
		if (linesToDraw != null) {
			Set<Integer> keySet = linesToDraw.keySet();
			for (Integer key : keySet) {
				// Draw with the right color
				drawLineString(linesToDraw.get(key), gc, config.getColorForId(key));
			}
		}
	}

	/**
	 * Creates a list of lineStrings, which are the traces that should be drawn
	 * 
	 * @return
	 */
	private HashMap<Integer, LineString> getLines() {
		zoomEnv = new Envelope();
		searchEnv = new Envelope();
		searchEnv.init(new Coordinate(49.7, 11.7)); // Maybe somewhere in
													// Germany
		// Get everything
		
		// top left
		searchEnv.expandToInclude(new Coordinate(Integer.MIN_VALUE, Integer.MAX_VALUE));
		// bottom right
		searchEnv.expandToInclude(new Coordinate(Integer.MAX_VALUE, Integer.MIN_VALUE));

		List<?> data = new ArrayList<Object>();
		try {
			data = buffer.query(searchEnv, config.getGeometricAttributePosition());
		} catch (ClassCastException e) {
			// Do nothing. The setting, which past of the query was the
			// geometric attribute was wrong
		}

		GeometryFactory factory = new GeometryFactory();

		// Map with all lists (for every id one list) of the coordinates
		HashMap<Integer, ArrayList<TraceElement>> lineList = new HashMap<Integer, ArrayList<TraceElement>>();

		for (Object dataSet : data) {
			// Get the data from the Tuple (point, id and starttime)
			Tuple<?> tuple = ((DataSet) dataSet).getTuple();

			
			Geometry geometry = getGeometry(tuple, config.getGeometricAttributePosition());

			TimeInterval timeInterval = (TimeInterval) tuple.getMetadata();
			PointInTime startTime = timeInterval.getStart();

			// Build the zoomEnvironment for "zoom to layer"
			zoomEnv.expandToInclude(geometry.getCoordinate());

			// Create new LineElement
			TraceElement lineElement = new TraceElement(geometry.getCoordinate(), startTime);

			try {

				int id = 0;
				if (tuple.getAttribute(config.getValueAttributePosition()) instanceof Long) {
					// We need to cast
					id = java.lang.Math.toIntExact(tuple.getAttribute(config.getValueAttributePosition()));
				} else {
					// It is already an integer value
					id = tuple.getAttribute(config.getValueAttributePosition());
				}

				// If this is the first coordinate for this key,
				// create a new ArrayList
				if (lineList.get(id) == null) {
					lineList.put(new Integer(id), new ArrayList<TraceElement>());
				}

				// Add a new color for this line
				// (Random)
				if (config.getColorForId(id) == null) {
					config.setColorForId(id, getColorForNumber(id));
				}

				// Put the coordinate into the right ArrayList
				lineList.get(id).add(lineElement);
			} catch (ClassCastException e) {
				// User set wrong attribute - just don't draw anything
				LOG.error("Tracelayer: Wrong attribute?");
				e.printStackTrace();
			}

		}

		// The lists should be full, now we need to sort them, so
		// the coordinates are in the order of their starttimestamps ...
		// ... and then create all the lines and put them into a new HashMap
		HashMap<Integer, LineString> lines = new HashMap<Integer, LineString>();
		Set<Integer> keySet = lineList.keySet();
		for (Integer key : keySet) {

			// Get the next list of coordinates for new line
			ArrayList<TraceElement> tempList = lineList.get(key);

			// Sort the list (TraceElement sorts by starttime)
			Collections.sort(tempList);

			// Get the first and the last starttimestamp
			// To calculate the speed of a geo-object in a trace
			PointInTime[] startEndTime = new PointInTime[2];
			if (tempList.size() > 0) {
				startEndTime[0] = tempList.get(0).getStartTime();
				startEndTime[1] = tempList.get(tempList.size() - 1).getStartTime();
			}
			timeHashMap.put(key, startEndTime);

			TraceElement[] lineElements = tempList.toArray(new TraceElement[1]);
			Coordinate[] coordsForLineString = new Coordinate[lineElements.length];

			// Copy all coords from the (sorted) lineElements to the new array
			for (int i = 0; i < coordsForLineString.length; i++) {
				coordsForLineString[i] = lineElements[i].getCoordinate();
			}
			if (coordsForLineString.length > 1) {
				// Lines are not allowed to have just 1 coordinate
				LineString line = factory.createLineString(coordsForLineString);
				line.setSRID(config.getSrid());
				lines.put(key, line);
			}
		}

		return lines;
	}

	/**
	 * Draws the line with transparency (older parts are more transparent)
	 * 
	 * @param line
	 * @param gc
	 * @param color
	 */
	private void drawLineString(LineString line, GC gc, Color color) {
		ScreenTransformation transformation = screenManager.getTransformation();
		Coordinate[] coords = line.getCoordinates();

		gc.setForeground(color);
		int originalLineWidth = gc.getLineWidth();
		gc.setLineWidth(config.getLineWidth());

		// The line is drawn from
		// new to old
		int numberOfPoints = 0;
		if (config.isAutoTransparency()) {
			numberOfPoints = line.getCoordinates().length;
		} else {
			// The lines which are in the order after the maximum number will
			// be drawn full transparent
			numberOfPoints = config.getNumOfLineElements();
		}

		int alphaStep = 255 / numberOfPoints;
		int alpha = 255;

		int[] coordinate1 = null;
		int[] coordinate2 = null;

		for (Coordinate coord : coords) {

			// From the old point to the newer line
			if (coordinate2 != null) {
				coordinate1 = coordinate2.clone();
			} else {
				// Draw a marker at the end of the line (to show the actual
				// position)
				if (config.isMarkEndpoint()) {
					int circleWidth = config.getLineWidth() * 2;
					coordinate2 = transformation.transformCoord(coord, line.getSRID());
					// Circle around should be red (red border)
					gc.setForeground(new Color(Display.getDefault(), 255, 0, 0));
					// Background (for circle around)
					gc.drawOval(coordinate2[0] - 2 - (circleWidth / 2), coordinate2[1] - 2 - (circleWidth / 2),
							circleWidth + 4, circleWidth + 4);
					gc.setForeground(color);
					gc.drawOval(coordinate2[0] - (circleWidth / 2), coordinate2[1] - (circleWidth / 2), circleWidth,
							circleWidth); // Foreground (Point)
				}
			}
			coordinate2 = transformation.transformCoord(coord, line.getSRID());

			if (coordinate1 != null) {
				// We have more than one point
				gc.drawLine(coordinate1[0], coordinate1[1], coordinate2[0], coordinate2[1]);

				// Transparency (newer lines should be better to see)
				alpha -= alphaStep;

				// Max-value is 255
				// (should not get above this, this is just extra security)
				if (alpha <= 0)
					alpha = 0;

				gc.setAlpha(alpha);
			}

		}

		// Set gc back to original settings
		gc.setAlpha(255);
		gc.setLineWidth(originalLineWidth);
	}

	/**
	 * Creates a random color
	 * 
	 * @param n
	 * @return
	 */
	private Color getColorForNumber(double n) {
		if (colorOffset <= 0) {
			colorOffset = Math.random();
		}

		double goldenRatio = 0.618033988749895;
		double h = (colorOffset + (goldenRatio * n)) % 1;
		return hsvToRgb(h, 0.99, 0.95);
	}

	private Color hsvToRgb(double hue, double saturation, double value) {

		int h = (int) (hue * 6);
		double f = hue * 6 - h;
		double p = value * (1 - saturation);
		double q = value * (1 - f * saturation);
		double t = value * (1 - (1 - f) * saturation);

		switch (h) {
		case 0:
			return rgbToColor(value, t, p);
		case 1:
			return rgbToColor(q, value, p);
		case 2:
			return rgbToColor(p, value, t);
		case 3:
			return rgbToColor(p, q, value);
		case 4:
			return rgbToColor(t, p, value);
		case 5:
			return rgbToColor(value, p, q);
		default:
			// Error, but in this (normally not possible) case we just take the
			// last option
			return rgbToColor(value, p, q);
		}
	}

	private Color rgbToColor(double r, double g, double b) {
		Color color = new Color(Display.getDefault(), (int) (r * 256), (int) (g * 256), (int) (b * 256));
		return color;
	}

	/**
	 * 
	 * @return length in km
	 */
	private double getDistance(double lat1, double long1, double lat2, double long2) {
		double degreeRadians = Math.PI / 180.0;
		double earthRadius = 6371; // in km

		double degreeLong = (long2 - long1) * degreeRadians;
		double degreeLat = (lat2 - lat1) * degreeRadians;
		double a = Math.pow(Math.sin(degreeLat / 2.0), 2.0) + Math.cos(lat1 * degreeRadians)
				* Math.cos(lat2 * degreeRadians) * Math.pow(Math.sin(degreeLong / 2.0), 2.0);
		double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
		double d = earthRadius * c;

		return d;
	}

	/**
	 * 
	 * @param id
	 *            of the LineString
	 * @return Distance of the lineString in km
	 */
	public double getDistanceOfLineString(LineString line) {
		Coordinate tempCoord1 = null;
		Coordinate tempCoord2 = null;
		double distanceInKm = 0;

		for (Coordinate coord : line.getCoordinates()) {
			if (tempCoord2 != null) {
				tempCoord1 = (Coordinate) tempCoord2.clone();
			}
			tempCoord2 = coord;
			if (tempCoord1 != null && tempCoord2 != null)
				distanceInKm += getDistance(tempCoord1.y, tempCoord1.x, tempCoord2.y, tempCoord2.x);
		}
		return distanceInKm;
	}

	public HashMap<Integer, Double> getAllLineDistances() {
		// Get all lines
		HashMap<Integer, LineString> lines = getLines();
		HashMap<Integer, Double> distances = new HashMap<Integer, Double>();

		// Calculate distance for each line
		for (Integer key : lines.keySet()) {
			distances.put(key, getDistanceOfLineString(lines.get(key)));
		}

		return distances;
	}

	/**
	 * 
	 * @return Speed of all geo-objects (traces)
	 */
	public HashMap<Integer, Double> getAllLineSpeeds() {
		HashMap<Integer, Double> distances = getAllLineDistances();
		HashMap<Integer, Double> speeds = new HashMap<Integer, Double>();
		int msToHours = 1000 * 60 * 60;

		// Calculate speed for every trace
		for (Integer key : distances.keySet()) {
			double distance = distances.get(key);
			// We assume that the timestamps are in ms - so convert them to
			// hours
			long time1 = timeHashMap.get(key)[0].getMainPoint();
			long time2 = timeHashMap.get(key)[1].getMainPoint();
			double durationInHours = (double) (time1 - time2) / (double) msToHours;
			double speed = distance / durationInHours;
			speeds.put(key, speed);
		}

		return speeds;
	}

	/**
	 * For "zoomToLayer"
	 */
	@Override
	public Envelope getEnvelope() {
		CoordinateTransform ct = screenManager.getTransformation().getCoordinateTransform(config.getSrid(),
				screenManager.getSRID());
		Envelope geoEnv = config.getCoverage();

		if (zoomEnv != null && zoomEnv.getMaxX() != -1 && zoomEnv.getMaxY() != -1) {
			geoEnv = zoomEnv;
		}
		ProjCoordinate srcMax = new ProjCoordinate(geoEnv.getMaxX(), geoEnv.getMaxY());
		ProjCoordinate srcMin = new ProjCoordinate(geoEnv.getMinX(), geoEnv.getMinY());
		ProjCoordinate destMax = new ProjCoordinate();
		ProjCoordinate destMin = new ProjCoordinate();
		ct.transform(srcMax, destMax);
		ct.transform(srcMin, destMin);
		Envelope coverage = new Envelope(destMin.x, destMax.x, destMin.y, destMax.y);

		return coverage;
	}

	@Override
	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}

	public Buffer getBuffer() {
		return this.buffer;
	}

	/**
	 * Sets the configuration for this layer
	 * 
	 * @param config
	 */
	public void setConfiguration(TracemapLayerConfiguration config) {
		this.config = config;
	}

	public TracemapLayerConfiguration getConfig() {
		return config;
	}
}
