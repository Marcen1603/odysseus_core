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
public class ConcaveObjRule implements IObjectRule {

    @Override
    public IObjectType getType() {
        return IObjectType.KONKAV;
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
            double v = Math.toDegrees(acos);

            if ((firstY < currentY) || (lastY < currentY)) {
                v = 360 - v;
            }

            stats.addValue(v);
        }

        if (stats.getMin() > 180) {
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

        Integer maxX = null;
        Integer maxY = null;

        for (final Coordinate s : segment.getCoordinates()) {
            if (maxX == null) {
                maxX = (int) s.x;
            }

            if (maxY == null) {
                maxY = (int) s.y;
            }

            if (maxX < s.x) {
                maxX = (int) s.x;
            }

            if (maxY < s.y) {
                maxY = (int) s.y;
            }

            result.add(new Coordinate((int) s.x, (int) s.y));
        }

        result.add(new Coordinate(maxX, maxY));
        result.add(result.get(0));

        LinearRing linearRing = segment.getFactory().createLinearRing(result.toArray(new Coordinate[result.size()]));
        return segment.getFactory().createPolygon(linearRing, null);
    }
}
