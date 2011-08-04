package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * Implementation of the IEPF algorithm to extract segments of a laser scan
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 * @param <IMetadata>
 */
public class IEPF extends AbstractFunction<List<RelationalTuple<TimeInterval>>> {
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private static Logger LOG = LoggerFactory.getLogger(IEPF.class);

    public static final SDFDatatype[] accTypes0 = new SDFDatatype[] {
        SDFDatatype.SPATIAL_MULTI_POINT
    };
    public static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
        SDFDatatype.DOUBLE
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
            switch (argPos) {
                case 0:
                    return ExtractSegments.accTypes0; // Coordinate[].class;
                case 1:
                default:
                    return ExtractSegments.accTypes1;
            }
        }
    }

    @Override
    public String getSymbol() {
        return "IEPF";
    }

    @Override
    public List<RelationalTuple<TimeInterval>> getValue() {
        PointInTime startTimestamp = new PointInTime();
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
        return new SDFDatatype(null, SDFDatatype.KindOfDatatype.TUPLE, SDFDatatype.SPATIAL);
    }

    private List<Geometry> segmentize(final Coordinate[] coordinates, final double threshold) {
        final List<Geometry> segments = new ArrayList<Geometry>();
        if (coordinates.length < 2) {
            if (coordinates.length > 0) {
                segments.add(this.geometryFactory.createPoint(coordinates[0]));
            }
        }
        else {
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
                if (coordinates.length == 2) {
                    if (start.distance(end) > threshold) {
                        segments.add(this.geometryFactory.createPoint(start));
                        segments.add(this.geometryFactory.createPoint(end));
                    }
                }
                else {
                    segments.add(this.geometryFactory.createLineString(coordinates));
                }
            }
        }
        return segments;
    }

    private double getDistanceToLine(final Coordinate point, final Coordinate from,
            final Coordinate to) {
        final Coordinate line = new Coordinate(to.x - from.x, to.y - from.y);
        final double length = Math.sqrt(line.x * line.x + line.y * line.y);
        line.x /= length;
        line.y /= length;
        final double lambda = point.x - from.x * line.x + (point.y - from.y) * line.y;
        final Coordinate foot = new Coordinate(from.x + lambda * line.x, from.y + lambda * line.y);
        return Math.sqrt((foot.x - point.x) * (foot.x - point.x) + (foot.y - point.y)
                * (foot.y - point.y));
    }
}
