package de.uniol.inf.is.odysseus.classification.segmentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class IEPFSegmentation extends AbstractFunction<Geometry> {
    /**
     * 
     */
    private static final long           serialVersionUID = 4973220087210280849L;

    public static final SDFDatatype[][] accTypes         = new SDFDatatype[][] {
            { SDFSpatialDatatype.SPATIAL_MULTI_POINT }, { SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE } };
    private final GeometryFactory       geometryFactory  = new GeometryFactory();

    @Override
    public int getArity() {
        return 3;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A point cloud, a threashold, and the min. number of points per segment.");
        }

        return IEPFSegmentation.accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "IEPFSegmentation";
    }

    @Override
    public MultiLineString getValue() {
        final PolarCoordinate[] points = (PolarCoordinate[]) this.getInputValue(0);
        final double threshold = this.getNumericalInputValue(1).doubleValue();
        final int minNumberOfPoints = this.getNumericalInputValue(2).intValue();

        final LineString[] segments = this.segmentScan(points, threshold, minNumberOfPoints);
        return this.geometryFactory.createMultiLineString(segments);
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING;
    }

    private LineString[] segmentScan(final PolarCoordinate[] measurement, final double threshold,
            final int minNumberOfPoints) {
        Coordinate[] coordinates = new Coordinate[measurement.length];
        for (int i = 0; i < measurement.length; i++) {
            final double x = measurement[i].r * Math.cos(Math.toRadians(measurement[i].a));
            final double y = measurement[i].r * Math.sin(Math.toRadians(measurement[i].a));
            coordinates[i] = new Coordinate(x, y);
        }
        List<LineString> segments = this.segmentize(coordinates, threshold, minNumberOfPoints);
        return segments.toArray(new LineString[segments.size()]);
    }

    private List<LineString> segmentize(final Coordinate[] measurement, final double threshold,
            final int minNumberOfPoints) {
        final List<LineString> segments = new ArrayList<LineString>();

        if (measurement.length >= 2) {

            final Coordinate start = new Coordinate(measurement[0].x, measurement[0].y);
            final Coordinate end = new Coordinate(measurement[measurement.length - 1].x,
                    measurement[measurement.length - 1].y);
            double maxDistance = 0.0;
            int splitIndex = 0;

            for (int i = 1; i < measurement.length - 1; i++) {
                final double distance = getDistanceToLine(new Coordinate(measurement[i].x, measurement[i].y), start,
                        end);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    splitIndex = i;
                }
            }

            if (maxDistance > threshold) {
                if (splitIndex > minNumberOfPoints) {
                    segments.addAll(this.segmentize(Arrays.copyOfRange(measurement, 0, splitIndex), threshold,
                            minNumberOfPoints));
                }
                if (splitIndex < measurement.length - minNumberOfPoints - 1) {
                    segments.addAll(this.segmentize(Arrays.copyOfRange(measurement, splitIndex, measurement.length),
                            threshold, minNumberOfPoints));
                }
            }
            else {
                if (measurement.length > minNumberOfPoints) {
                    segments.add(this.geometryFactory.createLineString(measurement));
                }

            }
        }
        return segments;
    }

    private static double getDistanceToLine(final Coordinate point, final Coordinate start, final Coordinate end) {
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
