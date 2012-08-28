package de.uniol.inf.is.odysseus.classification.objects.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.classification.objects.IObjectRule;
import de.uniol.inf.is.odysseus.classification.objects.IObjectType;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class StraightObjRule implements IObjectRule {

    private static final int TOLERANCE = 25;

    @Override
    public IObjectType getType() {
        return IObjectType.GERADE;
    }

    @SuppressWarnings("unused")
    @Override
    public double getTypeAffinity(final Geometry segment) {
        final Coordinate first = segment.getCoordinates()[0];
        final double firstX = first.x;
        final double firstY = first.y;
        final Coordinate last = segment.getCoordinates()[segment.getCoordinates().length - 1];
        final double lastX = last.x;
        final double lastY = last.y;

        // das erste und letzte element nicht iterieren ...
        final DescriptiveStatistics stats = new DescriptiveStatistics();
        for (int i = 1; i < (segment.getCoordinates().length - 1); i += 2) {
            final Coordinate current = segment.getCoordinates()[i];
            final double currentX = current.x;
            final double currentY = current.y;

            final double b = first.distance(current);
            final double c = current.distance(last);
            final double a = first.distance(last);

            final double cos_v = (Math.pow(a, 2) - Math.pow(b, 2) - Math.pow(c, 2)) / (-2 * b * c);
            final double acos = Math.acos(cos_v);
            final double v = Math.toDegrees(acos);

            stats.addValue(v);
        }

        final int upperBound = 180 + StraightObjRule.TOLERANCE;
        final int lowerBound = 180 - StraightObjRule.TOLERANCE;
        final double mean = stats.getMean();
        if ((mean < upperBound) && (mean > lowerBound)) {
            // TODO feingranulare wahrscheinlichkeiten
            return 1.0;
        }
        else {
            return 0.0;
        }
    }

    @Override
    public Geometry getPredictedPolygon(final Geometry segment) {
        // TODO ausgleichsgrade benutzen!

        final List<Coordinate> result = new ArrayList<Coordinate>();

        // var x1 = ..., x2 = ..., y1 = ..., y2 = ... // The original line
        // var L = Math.Sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))
        //
        // var offsetPixels = 10.0
        //
        // // This is the second line
        // var x1p = x1 + offsetPixels * (y2-y1) / L
        // var x2p = x2 + offsetPixels * (y2-y1) / L
        // var y1p = y1 + offsetPixels * (x1-x2) / L
        // var y2p = y2 + offsetPixels * (x1-x2) / L

        final int x1 = (int) segment.getCoordinates()[0].x;
        final int y1 = (int) segment.getCoordinates()[0].y;
        final int x2 = (int) segment.getCoordinates()[segment.getCoordinates().length - 1].x;
        final int y2 = (int) segment.getCoordinates()[segment.getCoordinates().length - 1].y;

        final double L = Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
        final double offsetPixels = 10.0;

        final int x1p = (int) (x1 + ((offsetPixels * (y2 - y1)) / L));
        final int x2p = (int) (x2 + ((offsetPixels * (y2 - y1)) / L));
        final int y1p = (int) (y1 + ((offsetPixels * (x1 - x2)) / L));
        final int y2p = (int) (y2 + ((offsetPixels * (x1 - x2)) / L));

        result.add(new Coordinate(x1, y1));
        result.add(new Coordinate(x2, y2));
        // p.addPoint(0, 0);

        result.add(new Coordinate(x2p, y2p));
        result.add(new Coordinate(x1p, y1p));
        return segment.getFactory().createLineString(result.toArray(new Coordinate[result.size()]));
    }
}
