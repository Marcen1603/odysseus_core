package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ExtractSegments extends AbstractFunction<List<Geometry>> {
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public static final SDFDatatype[] accTypes0 = new SDFDatatype[] {
        SDFDatatype.SPATIAL_MULTI_POINT
    };
    public static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
        SDFDatatype.DOUBLE
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
            switch (argPos) {
                case 0:
                    return accTypes0; // Coordinate[].class;
                case 1:
                default:
                    return accTypes1;
            }
        }
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype getReturnType() {
        return new SDFDatatype(null, SDFDatatype.KindOfDatatype.MULTI_VALUE, SDFDatatype.SPATIAL);
    }

    @Override
    public String getSymbol() {
        return "ExtractSegments";
    }

    @Override
    public List<Geometry> getValue() {
        final Geometry geometry = (Geometry) this.getInputValue(0);
        final Double threshold = (Double) this.getInputValue(1);
        final Coordinate[] coordinates = geometry.getCoordinates();
        final List<Geometry> segments = new ArrayList<Geometry>();
        Coordinate tmp = new Coordinate(Double.MAX_VALUE, Double.MAX_VALUE);
        boolean isSegment = false;
        int start = 0;
        for (int i = 0; i < coordinates.length; i++) {
            final Coordinate coordinate = coordinates[i];

            final Double distance = tmp.distance(coordinate);
            tmp = coordinate;
            if (distance >= threshold) {
                if (isSegment) {
                    final List<Coordinate> segment = new ArrayList<Coordinate>();
                    for (int j = start; j < i; j++) {
                        segment.add(coordinates[j]);
                    }
                    if (segment.size() > 1) {
                        segments.add(geometryFactory.createLineString(segment
                                .toArray(new Coordinate[] {})));
                    }
                    else {
                        segments.add(geometryFactory.createPoint(segment.get(0)));

                    }
                    isSegment = (tmp.x < Double.MAX_VALUE) && (tmp.y < Double.MAX_VALUE);
                }
                else {
                    isSegment = true;
                }
                start = i;
            }
        }
        if (isSegment) {
            final List<Coordinate> segment;
            if (coordinates[coordinates.length - 1].distance(coordinates[0]) > threshold) {
                segment = new ArrayList<Coordinate>();
            }
            else {
                segment = Arrays.asList(segments.get(0).getCoordinates());
                segments.remove(0);
            }
            for (int j = start; j < coordinates.length; j++) {
                segment.add(coordinates[j]);
            }
            if (segment.size() > 1) {
                segments.add(geometryFactory.createLineString(segment.toArray(new Coordinate[] {})));
            }
            else {
                segments.add(geometryFactory.createPoint(segment.get(0)));

            }
        }
        return segments;
    }
}
