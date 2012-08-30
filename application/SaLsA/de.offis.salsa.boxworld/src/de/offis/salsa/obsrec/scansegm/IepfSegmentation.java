package de.offis.salsa.obsrec.scansegm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.obsrec.annotations.ScanSegmentation;
import de.offis.salsa.obsrec.util.Util;

@ScanSegmentation(name = "IepfSegmentation")
public class IepfSegmentation implements IScanSegmentation {
	
	private static final int MIN_OBJECTS_PER_SEGMENT = 5;
	private static final int TRESHOLD = 100;
	
	@Override
	public MultiLineString segmentScan(Measurement measurement) {
		Coordinate[] coordinates = Util.convert(measurement.getSamples());
        List<LineString> segments = this.segmentize(coordinates, TRESHOLD, MIN_OBJECTS_PER_SEGMENT);
        LineString[] lineStrings =  segments.toArray(new LineString[segments.size()]);        
        return new GeometryFactory().createMultiLineString(lineStrings);
	}

	private List<LineString> segmentize(final Coordinate[] measurement,
			final double threshold, final int minNumberOfPoints) {
		final List<LineString> segments = new ArrayList<LineString>();

		if (measurement.length >= 2) {

			final Coordinate start = new Coordinate(measurement[0].x,
					measurement[0].y);
			final Coordinate end = new Coordinate(
					measurement[measurement.length - 1].x,
					measurement[measurement.length - 1].y);
			double maxDistance = 0.0;
			int splitIndex = 0;

			for (int i = 1; i < measurement.length - 1; i++) {
				final double distance = getDistanceToLine(new Coordinate(
						measurement[i].x, measurement[i].y), start, end);
				if (distance > maxDistance) {
					maxDistance = distance;
					splitIndex = i;
				}
			}

			if (maxDistance > threshold) {
				if (splitIndex > minNumberOfPoints) {
					segments.addAll(this.segmentize(
							Arrays.copyOfRange(measurement, 0, splitIndex),
							threshold, minNumberOfPoints));
				}
				if (splitIndex < measurement.length - minNumberOfPoints - 1) {
					segments.addAll(this.segmentize(Arrays.copyOfRange(
							measurement, splitIndex, measurement.length),
							threshold, minNumberOfPoints));
				}
			} else {
				if (measurement.length > minNumberOfPoints) {
					segments.add(new GeometryFactory()
							.createLineString(measurement));
				}

			}
		}
		return segments;
	}

	private static double getDistanceToLine(final Coordinate point,
			final Coordinate start, final Coordinate end) {
		double x1 = start.x;
		double y1 = start.y;

		double x2 = end.x;
		double y2 = end.y;

		double x3 = point.x;
		double y3 = point.y;

		return Math.abs((x1 - x2) * (y1 - y3) - (x1 - x3) * (y1 - y2))
				/ Math.sqrt(Math.pow(x1 - x2, 2) - Math.pow(y1 - y2, 2));
	}
}
