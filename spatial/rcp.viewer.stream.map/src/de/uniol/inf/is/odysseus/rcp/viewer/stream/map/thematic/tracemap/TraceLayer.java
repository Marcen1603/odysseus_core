package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.tracemap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.DataSet;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.TracemapLayerConfiguration;

public class TraceLayer extends RasterLayer {

	private static final long serialVersionUID = -6639695562970893314L;
	private LayerUpdater layerUpdater;
	private ScreenManager screenManager;
	private TracemapLayerConfiguration config;

	public TraceLayer(TracemapLayerConfiguration config,
			ScreenManager screenManager) {
		super(config);
		this.screenManager = screenManager;
		this.config = config;
	}

	@Override
	public void draw(GC gc) {
		// Get all the lines from the data and draw them
		HashMap<Integer, LineString> linesToDraw = getLines();
		if (linesToDraw != null) {
			Set<Integer> keySet = linesToDraw.keySet();
			for (Integer key : keySet) {
				// Draw with the right color
				drawLineString(linesToDraw.get(key), gc,
						config.getColorForId(key));
			}
		}
	}

	private HashMap<Integer, LineString> getLines() {
		Envelope searchEnv = new Envelope();
		searchEnv.init(new Coordinate(49.7, 11.7)); // Maybe somewhere in
													// Germany
		// Get everything
		searchEnv.expandToInclude(new Coordinate(Integer.MIN_VALUE,
				Integer.MAX_VALUE)); // Top left
		searchEnv.expandToInclude(new Coordinate(Integer.MAX_VALUE,
				Integer.MIN_VALUE)); // Bottom right

		// Envelope world = screenManager.getViewportWorldCoord();
		List<?> data = layerUpdater.query(searchEnv,
				config.getGeometricAttributePosition());
		GeometryFactory factory = new GeometryFactory();

		// List with all lists (for every id one list) of the coordinates
		HashMap<Integer, ArrayList<TraceElement>> lineList = new HashMap<Integer, ArrayList<TraceElement>>();

		for (Object dataSet : data) {
			// Get the data from the Tuple (point, id and starttime)
			Tuple<?> tuple = ((DataSet) dataSet).getTuple();
			GeometryCollection geoColl = (GeometryCollection) tuple
					.getAttribute(0);
			Point point = geoColl.getCentroid();
			TimeInterval timeInterval = (TimeInterval) tuple.getMetadata();
			PointInTime startTime = timeInterval.getStart();

			// Create new LineElement
			TraceElement lineElement = new TraceElement(point.getCoordinate(),
					startTime);

			int id = (int) tuple.getAttribute(1);

			// If this is the first coordinate for this key,
			// create a new ArrayList
			if (lineList.get(id) == null) {
				lineList.put(new Integer(id), new ArrayList<TraceElement>());
			}

			// Add a new color for this line
			// (Random)
			if (config.getColorForId(id) == null) {
				int r = (int) (Math.random() * (256));
				int g = (int) (Math.random() * (256));
				int b = (int) (Math.random() * (256));
				config.setColorForId(id, new Color(Display.getDefault(), r, g,
						b));
			}

			// Put the coordinate into the right ArrayList
			lineList.get(id).add(lineElement);
		}

		// The lists should be full, now we need to sort them, so
		// the coordinates are in the order of their starttimestamps ...
		// ... and then create all the lines and put them into a new HashMap
		HashMap<Integer, LineString> lines = new HashMap<Integer, LineString>();
		Set<Integer> keySet = lineList.keySet();
		for (Integer key : keySet) {

			// Get the next list of coordinates for new line
			ArrayList<TraceElement> tempList = lineList.get(key);

			// Sort the list (hopefully it works)
			Collections.sort(tempList);

			TraceElement[] lineElements = tempList.toArray(new TraceElement[1]);
			Coordinate[] coordsForLineString = new Coordinate[lineElements.length];

			// Copy all coords from the (sorted) lineElements to the new array
			for (int i = 0; i < coordsForLineString.length; i++) {
				coordsForLineString[i] = lineElements[i].getCoordinate();
			}
			if (coordsForLineString.length > 1) {
				// Lines are not allowed to have just 1 coordinate
				LineString line = factory.createLineString(coordsForLineString);
				line.setSRID(4326); // ???
				lines.put(key, line);
			}
		}
		
		return lines;
	}

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
					coordinate2 = transformation.transformCoord(coord,
							line.getSRID());
					// Circle around should be red (red border)
					gc.setForeground(new Color(Display.getDefault(), 255, 0, 0)); 
					// Background (for circle around)
					gc.drawOval(coordinate2[0] - 2, coordinate2[1] - 2,
							circleWidth + 4, circleWidth + 4); 
					gc.setForeground(color);
					gc.drawOval(coordinate2[0], coordinate2[1], circleWidth,
							circleWidth); // Foreground (Point)
				}
			}
			coordinate2 = transformation.transformCoord(coord, line.getSRID());

			if (coordinate1 != null) {
				// We have more than one point
				gc.drawLine(coordinate1[0], coordinate1[1], coordinate2[0],
						coordinate2[1]);

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
	 * 
	 * @return length in km
	 */
	private double getDistance(double lat1, double long1, double lat2,
			double long2) {
		double degreeRadians = Math.PI / 180.0;
		double earthRadius = 6371; // in km

		double degreeLong = (long2 - long1) * degreeRadians;
		double degreeLat = (lat2 - lat1) * degreeRadians;
		double a = Math.pow(Math.sin(degreeLat / 2.0), 2.0)
				+ Math.cos(lat1 * degreeRadians)
				* Math.cos(lat2 * degreeRadians)
				* Math.pow(Math.sin(degreeLong / 2.0), 2.0);
		double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
		double d = earthRadius * c;

		return d;
	}
	
	/**
	 * 
	 * @param id of the LineString
	 * @return Distance of the lineString in km
	 */
	public double getDistanceOfLineString(LineString line) {
		Coordinate tempCoord1 = null;
		Coordinate tempCoord2 = null;
		double distanceInKm = 0;
		
		for(Coordinate coord : line.getCoordinates()) {
			if(tempCoord2 != null) {
				tempCoord1 = (Coordinate) tempCoord2.clone();
			}
			tempCoord2 = coord;
			if(tempCoord1 != null && tempCoord2 != null)
				distanceInKm += getDistance(tempCoord1.y, tempCoord1.x, tempCoord2.y, tempCoord2.x);
		}
		return distanceInKm;
	}
	
	public HashMap<Integer, Double> getAllLineDistances() {
		// Get all lines
		HashMap<Integer, LineString> lines = getLines();
		HashMap<Integer, Double> distances = new HashMap<Integer, Double>();
		
		// Calculate distance for each line
		for(Integer key : lines.keySet()) {
			distances.put(key, getDistanceOfLineString(lines.get(key)));
		}
		
		return distances;
	}

	@Override
	public void setLayerUpdater(LayerUpdater layerUpdater) {
		this.layerUpdater = layerUpdater;
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
