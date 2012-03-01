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
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 * @param <IMetadata>
 */
public class ExtractSegments extends AbstractFunction<List<RelationalTuple<TimeInterval>>> {
    /**
     * 
     */
    private static final long serialVersionUID = -2639569519274108241L;
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
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype getReturnType() {
        return new SDFDatatype(SDFDatatype.TUPLE.getURI(), SDFDatatype.KindOfDatatype.TUPLE,
                SDFSpatialDatatype.SPATIAL_GEOMETRY);
    }

    @Override
    public String getSymbol() {
        return "ExtractSegments";
    }

    // FIXME need metadata to set timestamp
    @Override
    public List<RelationalTuple<TimeInterval>> getValue() {

        PointInTime startTimestamp = PointInTime.getInfinityTime();

        for (IExpression<?> expr : getArguments()) {

            if (expr.getReturnType().isStartTimestamp()) {
                // LOG.debug("Foundtime");
                startTimestamp = (PointInTime) expr.getValue();
            }
            else {
                // LOG.debug("Found no time");
                startTimestamp = new PointInTime(System.currentTimeMillis());
            }
        }

        final Geometry geometry = (Geometry) this.getInputValue(0);
        final Double threshold = (Double) this.getInputValue(1);
        final Coordinate[] coordinates = geometry.getCoordinates();
        final List<RelationalTuple<TimeInterval>> segments = new LinkedList<RelationalTuple<TimeInterval>>();
        Coordinate tmp = new Coordinate(Double.MAX_VALUE, Double.MAX_VALUE);
        boolean isSegment = false;
        int start = 0;

        TimeInterval time = new TimeInterval(startTimestamp);

        for (int i = 0; i < coordinates.length; i++) {
            final Coordinate coordinate = coordinates[i];

            final Double distance = tmp.distance(coordinate);
            tmp = coordinate;
            if (distance >= threshold) {
                if (isSegment) {

                    final List<Coordinate> segment = new ArrayList<Coordinate>();
                    for (int j = start; j < i; j++) {
                        if ((coordinates[j].x < Float.MAX_VALUE)
                                && (coordinates[j].y < Float.MAX_VALUE)) {
                            segment.add(coordinates[j]);
                        }
                    }
                    RelationalTuple<TimeInterval> tuple = new RelationalTuple<TimeInterval>(1);
                    tuple.setMetadata(time);
                    if (segment.size() > 1) {
                        tuple.setAttribute(0, this.geometryFactory.createLineString(segment
                                .toArray(new Coordinate[] {})));
                        segments.add(tuple);
                    }
                    else if (segment.size() == 1) {
                        tuple.setAttribute(0, this.geometryFactory.createPoint(segment.get(0)));
                        segments.add(tuple);

                    }
                    isSegment = (tmp.x < Double.MAX_VALUE) && (tmp.y < Double.MAX_VALUE);
                }
                else {
                    isSegment = true;
                }
                start = i;
            }
        }
        if ((isSegment) && (segments.size() > 0)) {
            final List<Coordinate> segment;
            if (coordinates[coordinates.length - 1].distance(coordinates[0]) > threshold) {
                segment = new ArrayList<Coordinate>();
            }
            else {
                segment = new ArrayList<Coordinate>(Arrays.asList(((Geometry) (segments.get(0)
                        .getAttribute(0))).getCoordinates()));
                segments.remove(0);
            }
            for (int j = start; j < coordinates.length; j++) {
                if ((coordinates[j].x < Float.MAX_VALUE) && (coordinates[j].y < Float.MAX_VALUE)) {
                    segment.add(coordinates[j]);
                }
            }

            RelationalTuple<TimeInterval> tuple = new RelationalTuple<TimeInterval>(1);
            if (segment.size() > 1) {
                tuple.setMetadata(time);
                tuple.setAttribute(0,
                        this.geometryFactory.createLineString(segment.toArray(new Coordinate[] {})));
                segments.add(tuple);
            }
            else if (segment.size() == 1) {
                tuple.setMetadata(time);
                tuple.setAttribute(0, this.geometryFactory.createPoint(segment.get(0)));
                segments.add(tuple);

            }
        }
        return segments;
    }
}
