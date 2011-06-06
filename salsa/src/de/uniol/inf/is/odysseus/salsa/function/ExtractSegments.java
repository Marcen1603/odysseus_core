package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ExtractSegments extends AbstractFunction<Object> {

    @Override
    public Class<?>[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > 1) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A matrix and a threashold.");
        }
        else {
            final Class<?>[] accTypes = new Class<?>[2];
            accTypes[0] = double[][].class;
            accTypes[1] = double.class;
            return accTypes;
        }
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public Class<? extends Object> getReturnType() {
        return List.class;
    }

    @Override
    public String getSymbol() {
        return "ExtractSegments";
    }

    @Override
    public Object getValue() {
        final Coordinate[] measurements = (Coordinate[]) this.getInputValue(0);
        final Double threshold = (Double) this.getInputValue(1);
        final List<List<Coordinate>> segments = new ArrayList<List<Coordinate>>();
        Coordinate tmp = new Coordinate(Double.MAX_VALUE, Double.MAX_VALUE);
        boolean isSegment = false;
        int start = 0;
        for (int i = 0; i < measurements.length; i++) {
            final Coordinate measurement = measurements[i];

            final Double distance = tmp.distance(measurement);
            tmp = measurement;
            if (distance > threshold) {
                if (isSegment) {
                    final List<Coordinate> segment = new ArrayList<Coordinate>();
                    for (int j = start; j < i; j++) {
                        segment.add(measurements[j]);
                    }
                    segments.add(segment);
                    isSegment = (tmp.x < Double.MAX_VALUE) && (tmp.y < Double.MAX_VALUE);
                }
                else {
                    isSegment = true;
                }
                start = i;
            }
        }
        if (isSegment) {
            final List<Coordinate> segment = new ArrayList<Coordinate>();
            for (int j = start; j < measurements.length; j++) {
                segment.add(measurements[j]);
            }
            segments.add(segment);
        }
        return segments;
    }
}
