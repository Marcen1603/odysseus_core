package de.uniol.inf.is.odysseus.classification.segmentation;

import java.util.ArrayList;
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
public class SEFSegmentation extends AbstractFunction<Geometry> {
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

        return SEFSegmentation.accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "SEFSegmentation";
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
        final List<LineString> segments = new ArrayList<LineString>();
        PolarCoordinate last = null;

        List<PolarCoordinate> segment = this.beginSegment();
        for (final PolarCoordinate s : measurement) {
            if (this.checkNewObjectConditions(s, last, threshold) && (last != null)) {
                final LineString line = this.closeSegment(segment, minNumberOfPoints);
                if (line != null) {
                    segments.add(line);
                }
                segment = this.beginSegment();
            }

            this.addSegmentSample(segment, s);

            last = s;
        }

        final LineString line = this.closeSegment(segment, minNumberOfPoints);
        if (line != null) {
            segments.add(line);
        }

        return segments.toArray(new LineString[segments.size()]);
    }

    private List<PolarCoordinate> beginSegment() {
        return new ArrayList<PolarCoordinate>();
    }

    private void addSegmentSample(final List<PolarCoordinate> segment, final PolarCoordinate s) {
        if (segment != null) {
            segment.add(s);
        }
    }

    private LineString closeSegment(final List<PolarCoordinate> segment, final int minNumberOfPoints) {
        if (segment.size() > minNumberOfPoints) {
            final Coordinate[] coordinates = new Coordinate[segment.size()];
            for (int i = 0; i < segment.size(); i++) {
                final double x = segment.get(i).r * Math.cos(Math.toRadians(segment.get(i).a));
                final double y = segment.get(i).r * Math.sin(Math.toRadians(segment.get(i).a));
                coordinates[i] = new Coordinate(x, y);
            }
            return this.geometryFactory.createLineString(coordinates);
        }
        else {
            return null;
        }
    }

    private boolean checkNewObjectConditions(final PolarCoordinate current, final PolarCoordinate last,
            final double threshold) {
        if (last == null) {
            return false;
        }

        // TODO emulate den infinite wert ...
        if (current.r < 3) {
            return true;
        }

        if (Math.abs(current.r - last.r) > threshold) {
            return true;
        }

        return false;
    }
}
