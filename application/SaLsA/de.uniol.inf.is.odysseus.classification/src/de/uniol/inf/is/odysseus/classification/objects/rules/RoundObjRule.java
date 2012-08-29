package de.uniol.inf.is.odysseus.classification.objects.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.classification.objects.IObjectRule;
import de.uniol.inf.is.odysseus.classification.objects.IObjectType;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RoundObjRule implements IObjectRule {

    private static final int TOLERANCE = 30;

    @Override
    public IObjectType getType() {
        return IObjectType.RUND;
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

        final int upperBound = 90 + RoundObjRule.TOLERANCE;
        final int lowerBound = 90 - RoundObjRule.TOLERANCE;
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
    public Polygon getPredictedPolygon(final Geometry segment) {
        final List<Coordinate> result = new ArrayList<Coordinate>();
        LinearRing linearRing = segment.getFactory().createLinearRing(result.toArray(new Coordinate[result.size()]));
        return segment.getFactory().createPolygon(linearRing, null);
    }

}
