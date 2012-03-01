package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Implementation of the IEPF algorithm to extract segments of a laser scan
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 * @param <IMetadata>
 */
public class IEPF extends AbstractFunction<List<RelationalTuple<TimeInterval>>> {
    /**
     * 
     */
    private static final long serialVersionUID = 4973220087210280849L;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                    SDFSpatialDatatype.SPATIAL_POINT, SDFSpatialDatatype.SPATIAL_LINE_STRING,
                    SDFSpatialDatatype.SPATIAL_POLYGON, SDFSpatialDatatype.SPATIAL_MULTI_POINT,
                    SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
                    SDFSpatialDatatype.SPATIAL_MULTI_POLYGON,
                    SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
                    SDFSpatialDatatype.SPATIAL_GEOMETRY

            }, {
                SDFDatatype.DOUBLE
            }
    };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A matrix and a threashold.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "IEPF";
    }

    @Override
    public List<RelationalTuple<TimeInterval>> getValue() {
        PointInTime startTimestamp = PointInTime.getInfinityTime();;
        for (final IExpression<?> expr : this.getArguments()) {
            if (expr.getReturnType().isStartTimestamp()) {
                startTimestamp = (PointInTime) expr.getValue();
            }
            else {
                startTimestamp = new PointInTime(System.currentTimeMillis());
            }
        }
        final TimeInterval time = new TimeInterval(startTimestamp);

        final Geometry geometry = (Geometry) this.getInputValue(0);
        final Double threshold = (Double) this.getInputValue(1);
        final Coordinate[] coordinates = geometry.getCoordinates();
        final List<RelationalTuple<TimeInterval>> segmentTuples = new LinkedList<RelationalTuple<TimeInterval>>();
        final List<Geometry> segments = this.segmentize(coordinates, threshold);
        for (final Geometry segment : segments) {
            final RelationalTuple<TimeInterval> tuple = new RelationalTuple<TimeInterval>(1);
            tuple.setMetadata(time);
            tuple.setAttribute(0, segment);
            segmentTuples.add(tuple);
        }
        return segmentTuples;
    }

    @Override
    public SDFDatatype getReturnType() {
        return new SDFDatatype(SDFDatatype.TUPLE.getURI(), SDFDatatype.KindOfDatatype.TUPLE,
                SDFSpatialDatatype.SPATIAL_GEOMETRY);
    }

    private List<Geometry> segmentize(final Coordinate[] coordinates, final double threshold) {
        final List<Geometry> segments = new ArrayList<Geometry>();

        if (coordinates.length >= 2) {

            final Coordinate start = coordinates[0];
            final Coordinate end = coordinates[coordinates.length - 1];

            double maxDistance = 0;
            int splitIndex = 0;

            for (int i = 1; i < coordinates.length - 1; i++) {
                final double distance = this.getDistanceToLine(coordinates[i], start, end);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    splitIndex = i;
                }
            }

            if ((splitIndex > 0) && (splitIndex < coordinates.length - 1)
                    && (maxDistance > threshold)) {

                segments.addAll(this.segmentize(Arrays.copyOfRange(coordinates, 0, splitIndex),
                        threshold));
                segments.addAll(this.segmentize(
                        Arrays.copyOfRange(coordinates, splitIndex, coordinates.length), threshold));
            }
            else {

                if ((start.distance(end) <= threshold)) {
                    segments.add(this.geometryFactory.createLineString(coordinates));
                }

            }
        }
        return segments;
    }

    private double getDistanceToLine(final Coordinate point, final Coordinate from,
            final Coordinate to) {

        final Coordinate fromTo = new Coordinate(to.x - from.x, to.y - from.y);
        final Coordinate fromPoint = new Coordinate(point.x - from.x, point.y - from.y);
        final Coordinate toPoint = new Coordinate(point.x - to.x, point.y - to.y);

        final double dot = fromPoint.x * fromTo.x + fromPoint.y * fromTo.y;
        if (dot <= 0.0) {
            return Math.sqrt(fromPoint.x * fromPoint.x + fromPoint.y * fromPoint.y);
        }
        final double sqrtLength = fromTo.x * fromTo.x + fromTo.y * fromTo.y;
        if (dot >= sqrtLength) {
            return Math.sqrt(toPoint.x * toPoint.x + toPoint.y * toPoint.y);
        }

        return Math.sqrt((fromPoint.x * fromPoint.x + fromPoint.y * fromPoint.y) - dot * dot
                / sqrtLength);
    }
}
