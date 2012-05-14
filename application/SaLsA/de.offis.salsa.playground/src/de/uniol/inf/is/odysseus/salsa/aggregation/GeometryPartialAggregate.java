package de.uniol.inf.is.odysseus.salsa.aggregation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GeometryPartialAggregate<T> implements IPartialAggregate<T>, Iterable<T> {
    private static final double BUFFER = 100.0;
    // final List<T> notMElems;
    final List<T> elems;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    protected volatile static Logger LOGGER = LoggerFactory
            .getLogger(GeometryPartialAggregate.class);

    public GeometryPartialAggregate() {
        this.elems = new LinkedList<T>();
    }

    public GeometryPartialAggregate(GeometryPartialAggregate<T> p) {
        this.elems = new LinkedList<T>(p.elems);
    }

    public List<T> getElems() {
        return elems;
    }

    public GeometryPartialAggregate<T> addElem(T elem) {
        boolean merged = false;
        Geometry geometry = (Geometry) ((Tuple<?>) elem).getAttribute(0);
        Coordinate[] coordinates = geometry.getCoordinates();
        if (coordinates.length > 1) {
            Coordinate from = coordinates[0];
            Coordinate to = coordinates[coordinates.length - 1];
            for (int i = 0; i < elems.size(); i++) {
                Tuple<?> tuple = (Tuple<?>) elems.get(i);
                Geometry envelope = (Geometry) tuple.getAttribute(0);
                Coordinate[] envelopeCoordinates = envelope.getCoordinates();
                double minX = envelopeCoordinates[0].x;
                double minY = envelopeCoordinates[0].y;
                double maxX = envelopeCoordinates[2].x;
                double maxY = envelopeCoordinates[2].y;
                if (isInBoundingBox(minX, minY, maxX, maxY, from)) {
                    if (!isInBoundingBox(minX, minY, maxX, maxY, to)) {
                        envelopeCoordinates = extendBoundingBox(envelopeCoordinates, from, to);
                        tuple.setAttribute(0, geometryFactory.createLineString(envelopeCoordinates));
                    }
                    merged = true;
                }
                else if (isInBoundingBox(minX, minY, maxX, maxY, to)) {
                    if (!isInBoundingBox(minX, minY, maxX, maxY, from)) {
                        envelopeCoordinates = extendBoundingBox(envelopeCoordinates, from, to);
                        tuple.setAttribute(0, geometryFactory.createLineString(envelopeCoordinates));
                    }
                    merged = true;
                }
                else if (intersects(minX, minY, maxX, maxY, from, to)) {
                    envelopeCoordinates = extendBoundingBox(envelopeCoordinates, from, to);
                    tuple.setAttribute(0, geometryFactory.createLineString(envelopeCoordinates));
                    merged = true;
                }
            }
            if (!merged) {
                Tuple<?> tuple = (Tuple<?>) elem;
                tuple.setAttribute(
                        0,
                        geometryFactory.createLineString(new Coordinate[] {
                                new Coordinate(from.x - BUFFER, from.y - BUFFER),
                                new Coordinate(from.x - BUFFER, to.y + BUFFER),
                                new Coordinate(to.x + BUFFER, to.y + BUFFER),
                                new Coordinate(to.x + BUFFER, from.y - BUFFER),
                                new Coordinate(from.x - BUFFER, from.y - BUFFER)
                        }));
                this.elems.add(elem);
            }
        }
        return this;
    }

    public GeometryPartialAggregate<T> compress() {
        boolean merge = true;
        while (merge) {
            merge = false;
            List<T> tmp = new ArrayList<T>(elems);
            int offset = 0;
            for (int i = 0; i < tmp.size(); i++) {
                Geometry geometry = (Geometry) ((Tuple<?>) tmp.get(i)).getAttribute(0);
                Coordinate[] coordinates = geometry.getCoordinates();
                Coordinate bottomLeft = coordinates[0];
                Coordinate topLeft = coordinates[1];
                Coordinate topRight = coordinates[2];
                Coordinate bottomRight = coordinates[3];
                int index = -1;
                for (int j = 0; j < elems.size(); j++) {
                    if ((i - offset) != j) {
                        Tuple<?> tuple = (Tuple<?>) elems.get(j);
                        Geometry envelope = (Geometry) tuple.getAttribute(0);
                        Coordinate[] envelopeCoordinates = envelope.getCoordinates();
                        double minX = envelopeCoordinates[0].x;
                        double minY = envelopeCoordinates[0].y;
                        double maxX = envelopeCoordinates[2].x;
                        double maxY = envelopeCoordinates[2].y;
                        if (isInBoundingBox(minX, minY, maxX, maxY, bottomLeft)) {
                            if (!isInBoundingBox(minX, minY, maxX, maxY, topRight)) {
                                envelopeCoordinates = extendBoundingBox(envelopeCoordinates,
                                        bottomLeft, topRight);
                                tuple.setAttribute(0,
                                        geometryFactory.createLinearRing(envelopeCoordinates));
                            }
                            index = i;
                            break;
                        }
                        else if (isInBoundingBox(minX, minY, maxX, maxY, topRight)) {
                            if (!isInBoundingBox(minX, minY, maxX, maxY, bottomLeft)) {
                                envelopeCoordinates = extendBoundingBox(envelopeCoordinates,
                                        bottomLeft, topRight);
                                tuple.setAttribute(0,
                                        geometryFactory.createLinearRing(envelopeCoordinates));
                            }
                            index = i;
                            break;
                        }
                        else if (isInBoundingBox(minX, minY, maxX, maxY, bottomRight)) {
                            if (!isInBoundingBox(minX, minY, maxX, maxY, topLeft)) {
                                envelopeCoordinates = extendBoundingBox(envelopeCoordinates,
                                        topLeft, bottomRight);
                                tuple.setAttribute(0,
                                        geometryFactory.createLinearRing(envelopeCoordinates));
                            }
                            index = i;
                            break;
                        }
                        else if (isInBoundingBox(minX, minY, maxX, maxY, topLeft)) {
                            if (!isInBoundingBox(minX, minY, maxX, maxY, bottomRight)) {
                                envelopeCoordinates = extendBoundingBox(envelopeCoordinates,
                                        topLeft, bottomRight);
                                tuple.setAttribute(0,
                                        geometryFactory.createLinearRing(envelopeCoordinates));
                            }
                            index = i;
                            break;
                        }
                    }
                }
                if (index >= 0) {
                    elems.remove(index - offset);
                    offset++;
                    merge = true;
                }
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "" + elems;
    }

    @Override
    public ElementPartialAggregate<T> clone() {
        return new ElementPartialAggregate<T>(this);
    }

    @Override
    public Iterator<T> iterator() {
        return elems.iterator();
    }

    public int size() {
        return elems.size();
    }

    private static Coordinate[] extendBoundingBox(Coordinate[] boundingBox, Coordinate from, Coordinate to) {
        Coordinate leftBottom = boundingBox[0];
        Coordinate leftTop = boundingBox[1];
        Coordinate rightTop = boundingBox[2];
        Coordinate rightBottom = boundingBox[3];
        if (from.x < leftBottom.x) {
            leftBottom.x = from.x;
            leftTop.x = from.x;
        }
        if (to.x < leftBottom.x) {
            leftBottom.x = to.x;
            leftTop.x = to.x;
        }
        if (from.y < leftBottom.y) {
            leftBottom.y = from.y;
            rightBottom.y = from.y;
        }
        if (to.y < leftBottom.y) {
            leftBottom.y = to.y;
            rightBottom.y = to.y;
        }
        if (from.x > rightTop.x) {
            rightTop.x = from.x;
            rightBottom.x = from.x;
        }
        if (to.x > rightTop.x) {
            rightTop.x = to.x;
            rightBottom.x = to.x;
        }
        if (from.y > rightTop.y) {
            rightTop.y = from.y;
            leftTop.y = from.y;
        }
        if (to.y > rightTop.y) {
            rightTop.y = to.y;
            leftTop.y = to.y;
        }
        return new Coordinate[] {
                leftBottom, leftTop, rightTop, rightBottom, leftBottom
        };
    }

    /**
     * Checks weather the given {@link Coordinate} is in the given bounding box
     * 
     * @param x1
     *            The x-coordinate of the grid
     * @param y1
     *            The y-coordinate of the grid
     * @param x2
     *            The height of the grid
     * @param y2
     *            The width of the grid
     * @param coordinate
     *            The coordinate to check
     * @return true if the coordinate is in the grid else false
     */
    private static boolean isInBoundingBox(double x1, double y1, double x2, double y2,
            Coordinate coordinate) {
        return ((coordinate.x >= x1) && (coordinate.x <= x2) && (coordinate.y >= y1) && (coordinate.y <= y2));
    }

    /**
     * Calculate the denominator for line line intersection
     * 
     * @param x1
     *            The x-coordinate of the first corner of the bounding box
     * @param y1
     *            The y-coordinate of the first corner of the bounding box
     * @param x2
     *            The x-coordinate of the second corner of the bounding box
     * @param y2
     *            The y-coordinate of the second corner of the bounding box
     * @param from
     *            The first {@link Coordinate} of the line
     * @param to
     *            The second {@link Coordinate} of the line
     * @return The denominator
     */
    private static double getDenominator(double x1, double y1, double x2, double y2, Coordinate from,
            Coordinate to) {
        return ((to.y - from.y) * (x2 - x1)) - ((to.x - from.x) * (y2 - y1));
    }

    /**
     * Calculate the first numerator for line line intersection
     * 
     * @param x1
     *            The x-coordinate of the first corner of the bounding box
     * @param y1
     *            The y-coordinate of the first corner of the bounding box
     * @param x2
     *            The x-coordinate of the second corner of the bounding box
     * @param y2
     *            The y-coordinate of the second corner of the bounding box
     * @param x3
     *            The x-coordinate of the first point of the line
     * @param y3
     *            The y-coordinate of the first point of the line
     * @param x4
     *            The x-coordinate of the second point of the line
     * @param y4
     *            The y-coordinate of the second point of the line
     * @return The first numerator
     */
    private static double getNumeratorA(double x1, double y1, double x2, double y2, double x3, double y3,
            double x4, double y4) {
        return ((x4 - x3) * (y1 - y3)) - ((y4 - y3) * (x1 - x3));
    }

    /**
     * Calculate the second numerator for line line intersection
     * 
     * @param x1
     *            The x-coordinate of the first corner of the bounding box
     * @param y1
     *            The y-coordinate of the first corner of the bounding box
     * @param x2
     *            The x-coordinate of the second corner of the bounding box
     * @param y2
     *            The y-coordinate of the second corner of the bounding box
     * @param x3
     *            The x-coordinate of the first point of the line
     * @param y3
     *            The y-coordinate of the first point of the line
     * @return The second numerator
     */
    private static double getNumeratorB(double x1, double y1, double x2, double y2, double x3, double y3) {
        return ((x2 - x1) * (y1 - y3)) - ((y2 - y1) * (x1 - x3));
    }

    /**
     * Checks weather the given grid cell intersects with a line of the polygon given by two
     * coordinates
     * 
     * @param x1
     *            The x-coordinate of the bottom left coordinate
     * @param y1
     *            The y-coordinate of the bottom left coordinate
     * @param x2
     *            The x-coordinate of the top right coordinate
     * @param y2
     *            The y-coordinate of the top right coordinate
     * @param from
     *            The first {@link Coordinate} of the polygon
     * @param to
     *            The second {@link Coordinate} of the polygon
     * @return true if the line formed by the coordinates intersects with the bounding box else
     *         false
     */
    private static boolean intersects(double x1, double y1, double x2, double y2, Coordinate from,
            Coordinate to) {
        // Check bottom left to top left segment for intersection
        double denominator = getDenominator(x1, y1, x1, y2, from, to);
        double numeratorA = getNumeratorA(x1, y1, x1, y2, from.x, from.y, to.x, to.y);
        double numeratorB = getNumeratorB(x1, y1, x1, y2, from.x, from.y);
        if ((denominator != 0.0) && (isIntersection(denominator, numeratorA, numeratorB))) {
            return true;
        }

        // Check top left to top right segment for intersection
        denominator = getDenominator(x1, y2, x2, y2, from, to);
        numeratorA = getNumeratorA(x1, y2, x2, y2, from.x, from.y, to.x, to.y);
        numeratorB = getNumeratorB(x1, y2, x2, y2, from.x, from.y);
        if (isIntersection(denominator, numeratorA, numeratorB)) {
            return true;
        }

        // Check top right to bottom right segment for intersection
        denominator = getDenominator(x2, y2, x2, y1, from, to);
        numeratorA = getNumeratorA(x2, y2, x2, y1, from.x, from.y, to.x, to.y);
        numeratorB = getNumeratorB(x2, y2, x2, y1, from.x, from.y);
        if (isIntersection(denominator, numeratorA, numeratorB)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the given numerators and the denominator allows an intersection between the two
     * line segments
     * which is true if A/d and B/d are both between the values 0 and 1
     * 
     * @param denominator
     *            The denominator
     * @param numeratorA
     *            The first numerator
     * @param numeratorB
     *            The second numerator
     * @return
     */
    private static boolean isIntersection(double denominator, double numeratorA, double numeratorB) {
        double factorA = Math.abs(numeratorA / denominator);
        double factorB = Math.abs(numeratorB / denominator);
        if (((factorA >= 0) && (factorA <= 1)) && ((factorB >= 0) && (factorB <= 1))) {
            return true;
        }
        return false;
    }

}
