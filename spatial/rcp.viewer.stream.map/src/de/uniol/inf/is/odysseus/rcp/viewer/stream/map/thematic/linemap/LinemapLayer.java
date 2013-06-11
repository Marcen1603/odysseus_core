package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.linemap;

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
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.VectorLayerConfiguration;

public class LinemapLayer extends VectorLayer {

	private static final long serialVersionUID = -6639695562970893314L;
	private LayerUpdater layerUpdater;
	ScreenManager screenManager;
	VectorLayerConfiguration config;
	HashMap<Integer, Color> colors;

	public LinemapLayer(VectorLayerConfiguration config,
			ScreenManager screenManager) {
		this.screenManager = screenManager;
		this.config = config;
		colors = new HashMap<Integer, Color>();
	}

	@Override
	public void draw(GC gc) {
		// Get all the lines from the data and draw them
		HashMap<Integer, LineString> linesToDraw = getLines();
		if (linesToDraw != null) {
			Set<Integer> keySet = linesToDraw.keySet();
			for (Integer key : keySet) {
				// Draw with the right color
				drawLineString(linesToDraw.get(key), gc, colors.get(key));
			}
		}
	}

	private HashMap<Integer, LineString> getLines() {
		int geometryAttributeIndex = 0; // Here should be a point
		Envelope searchEnv = new Envelope();
		searchEnv.init(new Coordinate(49.7, 11.7)); // Maybe somewhere in Germany
		// Get everything
		searchEnv.expandToInclude(new Coordinate(Integer.MIN_VALUE, Integer.MAX_VALUE));	// Top left
		searchEnv.expandToInclude(new Coordinate(Integer.MAX_VALUE, Integer.MIN_VALUE)); 	// Bottom right
		
		//Envelope world = screenManager.getViewportWorldCoord();
		List<?> data = layerUpdater.query(searchEnv, geometryAttributeIndex);
		GeometryFactory factory = new GeometryFactory();

		// List with all lists (for every id one list) of the coordinates
		HashMap<Integer, ArrayList<LineElement>> lineList = new HashMap<Integer, ArrayList<LineElement>>();

		for (Object dataSet : data) {
			// Get the data from the Tuple (point, id and starttime)
			Tuple<?> tuple = ((DataSet) dataSet).getTuple();
			GeometryCollection geoColl = (GeometryCollection) tuple
					.getAttribute(0);
			Point point = geoColl.getCentroid();
			TimeInterval timeInterval = (TimeInterval) tuple.getMetadata();
			PointInTime startTime = timeInterval.getStart();
			
			// Create new LineElement
			LineElement lineElement = new LineElement(point.getCoordinate(), startTime);
			
			int id = (int) tuple.getAttribute(1);

			// If this is the first coordinate for this key,
			// create a new ArrayList
			if (lineList.get(id) == null) {
				lineList.put(new Integer(id), new ArrayList<LineElement>());
			}
			
			// Add a new color for this line
			// (Random)
			if (colors.get(id) == null) {
				int r = (int) (Math.random() * (256));
				int g = (int) (Math.random() * (256));
				int b = (int) (Math.random() * (256));
				colors.put(new Integer(id), new Color(Display.getDefault(), r,
						g, b));
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
			ArrayList<LineElement> tempList = lineList.get(key);
			
			// Sort the list (hopefully it works)
			Collections.sort(tempList);
			
			LineElement[] lineElements = tempList.toArray(new LineElement[1]);
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
		gc.setLineWidth(3);
		
		// The line is drawn from
		// new to old
		int numberOfPoints = line.getCoordinates().length;
		int alphaStep = 255 / numberOfPoints;
		int alpha = 255;

		int[] coordinate1 = null;
		int[] coordinate2 = null;

		for (Coordinate coord : coords) {
			
			// From the old point to the newer line
			if (coordinate2 != null) {
				coordinate1 = coordinate2.clone();
			}				
			coordinate2 = transformation.transformCoord(coord, line.getSRID());

			// Transparency (newer lines should be better to see)
			gc.setAlpha(alpha);
			alpha -= alphaStep;
			
			// Max-value is 255 
			// (should not get above this, this is just extra security)
			if (alpha <= 0)
				alpha = 0;

			if (coordinate1 != null) {
				gc.drawLine(coordinate1[0], coordinate1[1], coordinate2[0],
						coordinate2[1]);
			}

		}

		// Set gc back to original settings
		gc.setAlpha(255);
		gc.setLineWidth(originalLineWidth);
	}

	@Override
	public void setLayerUpdater(LayerUpdater layerUpdater) {
		this.layerUpdater = layerUpdater;
	}
}
