/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.voronoi;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.AbstractCanvasDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.Coordinate;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * Voronoi tessellation based on
 * http://philogb.github.io/blog/2010/02/12/voronoi-tessellation/
 * JavaScript by Nicolas Garcia Belmonte.
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: VoronoiDashboardPart.java | Thu Apr 16 16:55:17 2015 +0000 |
 *          ckuka $
 *
 */
public class VoronoiDashboardPart extends AbstractCanvasDashboardPart {
    private final static String X_POS = "xPos";
    private final static String Y_POS = "yPos";
    private final static String MIN_X = "minX";
    private final static String MAX_X = "maxX";
    private final static String MIN_Y = "minY";
    private final static String MAX_Y = "maxY";
    private final static String AUTOADJUST = "autoadjust";
    private final static String BACKGROUND_COLOR = "backgroundColor";
    private final static String BACKGROUND_ALPHA = "backgroundAlpha";
    private final static String COLOR = "color";

    /** Background alpha. */
    private int backgroundAlpha = 255;
    /** Min Y value. */
    private double minY = 0.0;
    /** Max Y value. */
    private double maxY = 1.0;
    /** Min X value. */
    private double minX = 0.0;
    /** Max X value. */
    private double maxX = 1.0;
    /** Auto adjust min and max value. */
    private boolean autoadjust = true;
    private int xPos = 0;
    private int yPos = 1;
    private RGB backgroundColor = new RGB(255, 255, 255);
    private RGB color = new RGB(255, 0, 0);

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPaint(final PaintEvent event) {
        if (this.isAutoadjust()) {
            this.adjust();
        }
        final RGB foreground = this.getForegroundColor();
        final RGB background = this.getBackgroundColor();
        this.setAlpha(getBackgroundAlpha());
        this.fill(background);
        this.setAlpha(255);

        final EdgeList edgeList = new EdgeList();
        final EventQueue eventQueue = new EventQueue();

        final int initialSize = this.getObjects().size();
        final Queue<Coordinate> coordinates = new PriorityQueue<>(initialSize > 0 ? this.getObjects().size() : 10, new Comparator<Coordinate>() {

            @Override
            public int compare(final Coordinate a, final Coordinate b) {
                if (a.y < b.y) {
                    return -1;
                }
                else if (a.y > b.y) {
                    return 1;
                }
                else if (a.x < b.x) {
                    return -1;
                }
                else if (a.x > b.x) {
                    return 1;
                }
                return 0;
            }
        });

        final Iterator<IStreamObject<?>> iter = this.getObjects().iterator();
        IStreamObject<?> element = null;
        while (iter.hasNext()) {
            element = iter.next();
            coordinates.offer(this.getCoordinate(element));
        }
        if (coordinates.size() == 0) {
            return;
        }

        Coordinate bottomSite = null;
        if (coordinates.size() > 0) {
            bottomSite = coordinates.poll();
        }

        this.plotSite(bottomSite, foreground.getComplement());
        Coordinate newSite = null;
        if (coordinates.size() > 0) {
            newSite = coordinates.poll();
        }
        Coordinate newIntStar = null;
        HalfEdge lbnd;
        HalfEdge rbnd;
        HalfEdge llbnd;
        HalfEdge rrbnd;
        HalfEdge bisector;
        Coordinate top;
        Coordinate temp;
        Coordinate v;
        Edge e;
        Side pm;
        Coordinate p;
        Coordinate bot;
        while (true) {
            if (!eventQueue.isEmpty()) {
                newIntStar = eventQueue.min();
            }
            if ((newSite != null)
                    && (eventQueue.isEmpty() || ((newIntStar != null) && (newSite.y < newIntStar.y)) || ((newIntStar != null) && (newSite.y == newIntStar.y) && (newSite.x < newIntStar.x)))) {
                this.plotSite(newSite, foreground.getComplement());
                lbnd = edgeList.leftBound(newSite);
                rbnd = edgeList.right(lbnd);
                bot = edgeList.rightRegion(lbnd, bottomSite);
                e = Geom.bisect(bot, newSite);

                bisector = edgeList.createHalfEdge(e, Side.LEFT);
                edgeList.insert(lbnd, bisector);
                p = Geom.intersect(lbnd, bisector);
                if (p != null) {
                    eventQueue.remove(lbnd);
                    eventQueue.offer(lbnd, p, p.distance(newSite));
                }
                lbnd = bisector;
                bisector = edgeList.createHalfEdge(e, Side.RIGHT);
                edgeList.insert(lbnd, bisector);
                p = Geom.intersect(bisector, rbnd);
                if (p != null) {
                    eventQueue.offer(bisector, p, p.distance(newSite));
                }
                newSite = coordinates.poll();
            }
            else if (!eventQueue.isEmpty()) { // intersection is smallest
                lbnd = eventQueue.extractMin();
                llbnd = edgeList.left(lbnd);
                rbnd = edgeList.right(lbnd);
                rrbnd = edgeList.right(rbnd);
                bot = edgeList.leftRegion(lbnd, bottomSite);
                top = edgeList.rightRegion(rbnd, bottomSite);
                v = lbnd.vertex;
                this.plotVertex(v, foreground);

                if (Geom.endPoint(lbnd.edge, lbnd.side, v)) {
                    this.clipLine(lbnd.edge, foreground);
                }

                if (Geom.endPoint(rbnd.edge, rbnd.side, v)) {
                    this.clipLine(rbnd.edge, foreground);
                }
                edgeList.remove(lbnd);
                eventQueue.remove(rbnd);
                edgeList.remove(rbnd);
                pm = Side.LEFT;
                if (bot.y > top.y) {
                    temp = bot;
                    bot = top;
                    top = temp;
                    pm = Side.RIGHT;
                }
                e = Geom.bisect(bot, top);
                bisector = edgeList.createHalfEdge(e, pm);
                edgeList.insert(llbnd, bisector);
                if (Geom.endPoint(e, pm == Side.LEFT ? Side.RIGHT : Side.LEFT, v)) {
                    this.clipLine(e, foreground);
                }
                p = Geom.intersect(llbnd, bisector);
                if (p != null) {
                    eventQueue.remove(llbnd);
                    eventQueue.offer(llbnd, p, p.distance(bot));
                }
                p = Geom.intersect(bisector, rrbnd);
                if (p != null) {
                    eventQueue.offer(bisector, p, p.distance(bot));
                }
            }
            else {
                break;
            }
        }
        for (lbnd = edgeList.right(edgeList.leftEnd); !lbnd.equals(edgeList.rightEnd); lbnd = edgeList.right(lbnd)) {
            e = lbnd.edge;
            this.clipLine(e, foreground);
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void onLoad(Map<String, String> saved) {
        super.onLoad(saved);
        xPos = Integer.valueOf(saved.get(X_POS) != null ? saved.get(X_POS) : "0");
        yPos = Integer.valueOf(saved.get(Y_POS) != null ? saved.get(Y_POS) : "0");
        minX = Double.valueOf(saved.get(MIN_X) != null ? saved.get(MIN_X) : "0");
        maxX = Double.valueOf(saved.get(MAX_X) != null ? saved.get(MAX_X) : "1");
        minY = Double.valueOf(saved.get(MIN_Y) != null ? saved.get(MIN_Y) : "0");
        maxY = Double.valueOf(saved.get(MAX_Y) != null ? saved.get(MAX_Y) : "1");
        autoadjust = Boolean.valueOf(saved.get(AUTOADJUST) != null ? saved.get(AUTOADJUST) : "true");
        backgroundColor = RGB.valueOf(saved.get(BACKGROUND_COLOR) != null ? saved.get(BACKGROUND_COLOR) : "255,0,0");
        backgroundAlpha = Integer.valueOf(saved.get(BACKGROUND_ALPHA) != null ? saved.get(BACKGROUND_ALPHA) : "255");
        color = RGB.valueOf(saved.get(COLOR) != null ? saved.get(COLOR) : "0,0,0");
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> onSave() {
        Map<String, String> toSaveMap = super.onSave();
        toSaveMap.put(X_POS, String.valueOf(xPos));
        toSaveMap.put(Y_POS, String.valueOf(yPos));
        toSaveMap.put(MIN_X, String.valueOf(minX));
        toSaveMap.put(MAX_X, String.valueOf(maxX));
        toSaveMap.put(MIN_Y, String.valueOf(minY));
        toSaveMap.put(MAX_Y, String.valueOf(maxY));
        toSaveMap.put(AUTOADJUST, String.valueOf(autoadjust));
        toSaveMap.put(BACKGROUND_COLOR, String.valueOf(backgroundColor));
        toSaveMap.put(BACKGROUND_ALPHA, String.valueOf(backgroundAlpha));
        toSaveMap.put(COLOR, String.valueOf(color));
        return toSaveMap;
    }

    private void plotSite(final Coordinate site, final RGB color) {
        this.plotCoord(site.x, site.y, color);

    }

    private void plotVertex(final Coordinate site, final RGB color) {
        this.plotCoord(site.x, site.y, color);
    }

    private void plotCoord(final double x, final double y, final RGB color) {
        final GC gc = this.getGC();
        final Path path = new Path(gc.getDevice());
        path.addArc((float) (x - 1.0), (float) (y - 1.0), 2f, 2f, 0f, 360f);
        this.fillPath(path, color);
    }

    private void clipLine(final Edge e, final RGB color) {
        final double dy = this.getClipping().height;
        final double dx = this.getClipping().width;
        final double d = (dx > dy) ? dx : dy;
        final double pxmin = -(d - dx) / 2.0;
        final double pxmax = this.getClipping().width + ((d - dx) / 2.0);
        final double pymin = -(d - dy) / 2.0;
        final double pymax = this.getClipping().height + ((d - dy) / 2.0);
        Coordinate s2;
        Coordinate s1;
        double x1, x2, y1, y2;

        if ((e.a == 1.0) && (e.b >= 0.0)) {
            s1 = e.ep.right;
            s2 = e.ep.left;
        }
        else {
            s1 = e.ep.left;
            s2 = e.ep.right;
        }
        if (e.a == 1.0) {
            y1 = pymin;
            if ((s1 != null) && (s1.y > pymin)) {
                y1 = s1.y;
            }
            if (y1 > pymax) {
                return;
            }
            x1 = e.c - (e.b * y1);
            y2 = pymax;
            if ((s2 != null) && (s2.y < pymax)) {
                y2 = s2.y;
            }
            if (y2 < pymin) {
                return;
            }
            x2 = e.c - (e.b * y2);
            if (((x1 > pxmax) && (x2 > pxmax)) || ((x1 < pxmin) && (x2 < pxmin))) {
                return;
            }
            if (x1 > pxmax) {
                x1 = pxmax;
                y1 = (e.c - x1) / e.b;
            }
            if (x1 < pxmin) {
                x1 = pxmin;
                y1 = (e.c - x1) / e.b;
            }
            if (x2 > pxmax) {
                x2 = pxmax;
                y2 = (e.c - x2) / e.b;
            }
            if (x2 < pxmin) {
                x2 = pxmin;
                y2 = (e.c - x2) / e.b;
            }
        }
        else {
            x1 = pxmin;
            if ((s1 != null) && (s1.x > pxmin)) {
                x1 = s1.x;
            }
            if (x1 > pxmax) {
                return;
            }
            y1 = e.c - (e.a * x1);
            x2 = pxmax;
            if ((s2 != null) && (s2.x < pxmax)) {
                x2 = s2.x;
            }
            if (x2 < pxmin) {
                return;
            }
            y2 = e.c - (e.a * x2);
            if (((y1 > pymax) && (y2 > pymax)) || ((y1 < pymin) && (y2 < pymin))) {
                return;
            }
            if (y1 > pymax) {
                y1 = pymax;
                x1 = (e.c - y1) / e.a;
            }
            if (y1 < pymin) {
                y1 = pymin;
                x1 = (e.c - y1) / e.a;
            }
            if (y2 > pymax) {
                y2 = pymax;
                x2 = (e.c - y2) / e.a;
            }
            if (y2 < pymin) {
                y2 = pymin;
                x2 = (e.c - y2) / e.a;
            }
        }
        final GC gc = this.getGC();
        final Path path = new Path(gc.getDevice());
        path.moveTo((float) x1, (float) y1);
        path.lineTo((float) x2, (float) y2);
        this.drawPath(path, color);
    }

    private static class Region {
        Coordinate left;
        Coordinate right;

        /**
         * Class constructor.
         *
         * @param left
         * @param right
         */
        public Region(final Coordinate left, final Coordinate right) {
            super();
            this.left = left;
            this.right = right;
        }

    }

    private static class Edge {
        Region region;
        Region ep;
        double a;
        double b;
        double c;

        /**
         * Class constructor.
         *
         * @param region
         * @param ep
         */
        public Edge(final Region region, final Region ep) {
            super();
            this.region = region;
            this.ep = ep;
        }

    }

    private class HalfEdge {
        Edge edge;
        Side side;
        Coordinate vertex = null;
        HalfEdge left = null;
        HalfEdge right = null;
        double ystar;

        /**
         * Class constructor.
         *
         * @param edge
         * @param side
         */
        public HalfEdge(final Edge edge, final Side side, final Coordinate vertex, final HalfEdge left, final HalfEdge right) {
            super();
            this.edge = edge;
            this.side = side;
            this.vertex = vertex;
            this.left = left;
            this.right = right;
            this.ystar = 0.0;
        }

    }

    private class EdgeList {
        List<HalfEdge> list = new LinkedList<>();

        HalfEdge leftEnd = null;
        HalfEdge rightEnd = null;

        public EdgeList() {
            this.leftEnd = this.createHalfEdge(null, Side.LEFT);
            this.rightEnd = this.createHalfEdge(null, Side.LEFT);

            this.leftEnd.right = this.rightEnd;
            this.rightEnd.left = this.leftEnd;

            this.list.add(0, this.rightEnd);
            this.list.add(0, this.leftEnd);
        }

        HalfEdge createHalfEdge(final Edge edge, final Side side) {
            return new HalfEdge(edge, side, null, null, null);
        }

        void insert(final HalfEdge lb, final HalfEdge he) {
            he.left = lb;
            he.right = lb.right;
            lb.right.left = he;
            lb.right = he;
        }

        HalfEdge leftBound(final Coordinate p) {
            HalfEdge he = this.leftEnd;
            do {
                he = he.right;
            }
            while ((!he.equals(this.rightEnd)) && Geom.rightOf(he, p));
            he = he.left;
            return he;
        }

        void remove(final HalfEdge he) {
            he.left.right = he.right;
            he.right.left = he.left;
            he.edge = null;
        }

        HalfEdge right(final HalfEdge he) {
            return he.right;
        }

        HalfEdge left(final HalfEdge he) {
            return he.left;
        }

        Coordinate leftRegion(final HalfEdge he, final Coordinate bottomSite) {
            if (he.edge == null) {
                return bottomSite;
            }
            return he.side == Side.LEFT ? he.edge.region.left : he.edge.region.right;
        }

        Coordinate rightRegion(final HalfEdge he, final Coordinate bottomSite) {
            if (he.edge == null) {
                return bottomSite;
            }
            return he.side == Side.LEFT ? he.edge.region.right : he.edge.region.left;
        }
    }

    enum Side {
        LEFT, RIGHT
    }

    private static class Geom {

        public static Edge bisect(final Coordinate s1, final Coordinate s2) {
            final Edge newEdge = new Edge(new Region(s1, s2), new Region(null, null));

            final double dx = s2.x - s1.x;
            final double dy = s2.y - s1.y;
            final double adx = dx > 0.0 ? dx : -dx;
            final double ady = dy > 0.0 ? dy : -dy;

            newEdge.c = ((s1.x * dx) + (s1.y * dy) + (((dx * dx) + (dy * dy)) * 0.5));

            if (adx > ady) {
                newEdge.a = 1.0;
                newEdge.b = (dy / dx);
                newEdge.c /= dx;
            }
            else {
                newEdge.b = 1.0;
                newEdge.a = (dx / dy);
                newEdge.c /= dy;
            }

            return newEdge;
        }

        public static Coordinate intersect(final HalfEdge el1, final HalfEdge el2) {
            final Edge e1 = el1.edge;
            final Edge e2 = el2.edge;
            if ((e1 == null) || (e2 == null)) {
                return null;
            }
            if (e1.region.right.equals(e2.region.right)) {
                return null;
            }
            final double d = (e1.a * e2.b) - (e1.b * e2.a);
            if (Math.abs(d) < 1e-10) {
                return null;
            }
            final double xint = ((e1.c * e2.b) - (e2.c * e1.b)) / d;
            final double yint = ((e2.c * e1.a) - (e1.c * e2.a)) / d;
            final Coordinate e1r = e1.region.right;
            final Coordinate e2r = e2.region.right;
            HalfEdge el;
            Edge e;
            if ((e1r.y < e2r.y) || ((e1r.y == e2r.y) && (e1r.x < e2r.x))) {
                el = el1;
                e = e1;
            }
            else {
                el = el2;
                e = e2;
            }
            final boolean rightOfSite = (xint >= e.region.right.x);
            if ((rightOfSite && (el.side == Side.LEFT)) || (!rightOfSite && (el.side == Side.RIGHT))) {
                return null;
            }
            return new Coordinate(xint, yint);

        }

        public static boolean rightOf(final HalfEdge he, final Coordinate p) {
            final Edge e = he.edge;
            final Coordinate topsite = e.region.right;
            final boolean rightOfSite = (p.x > topsite.x);

            if (rightOfSite && (he.side == Side.LEFT)) {
                return true;
            }
            if (!rightOfSite && (he.side == Side.RIGHT)) {
                return false;
            }
            boolean above;
            if (e.a == 1.0) {
                final double dyp = p.y - topsite.y;
                final double dxp = p.x - topsite.x;
                boolean fast = false;
                above = false;

                if ((!rightOfSite && (e.b < 0.0)) || (rightOfSite && (e.b >= 0.0))) {
                    above = fast = (dyp >= (e.b * dxp));
                }
                else {
                    above = ((p.x + (p.y * e.b)) > e.c);
                    if (e.b < 0.0) {
                        above = !above;
                    }
                    if (!above) {
                        fast = true;
                    }
                }
                if (!fast) {
                    final double dxs = topsite.x - e.region.left.x;
                    above = (e.b * ((dxp * dxp) - (dyp * dyp))) < (dxs * dyp * (1.0 + ((2.0 * dxp) / dxs) + (e.b * e.b)));

                    if (e.b < 0.0) {
                        above = !above;
                    }
                }
            }
            else {
                final double yl = e.c - (e.a * p.x);
                final double t1 = p.y - yl;
                final double t2 = p.x - topsite.x;
                final double t3 = yl - topsite.y;

                above = (t1 * t1) > ((t2 * t2) + (t3 * t3));
            }
            return he.side == Side.LEFT ? above : !above;
        }

        public static boolean endPoint(final Edge edge, final Side side, final Coordinate site) {
            if (side == Side.LEFT) {
                edge.ep.left = site;
            }
            else {
                edge.ep.right = site;
            }

            final Side opSide = (side == Side.LEFT ? Side.RIGHT : Side.LEFT);
            if ((opSide == Side.LEFT) && (edge.ep.left == null)) {
                return false;
            }
            else if (edge.ep.right == null) {
                return false;
            }
            return true;
        }

    }

    private static class EventQueue {
        final List<HalfEdge> list;

        /**
         * Class constructor.
         *
         */
        public EventQueue() {
            this.list = new LinkedList<>();
        }

        public void offer(final HalfEdge he, final Coordinate site, final double offset) {
            he.vertex = site;
            he.ystar = site.y + offset;
            int i = 0;
            for (i = 0; i < this.list.size(); i++) {
                final HalfEdge next = this.list.get(i);
                if ((he.ystar > next.ystar) || ((he.ystar == next.ystar) && (site.x > next.vertex.x))) {
                    continue;
                }
                break;
            }
            this.list.add(i, he);
        }

        public void remove(final HalfEdge he) {
            this.list.remove(he);
        }

        public boolean isEmpty() {
            return this.list.isEmpty();
        }

        public Coordinate min() {
            final HalfEdge elem = this.list.get(0);
            return new Coordinate(elem.vertex.x, elem.ystar);
        }

        public HalfEdge extractMin() {
            return this.list.remove(0);
        }

    }

    private void adjust() {
        double minY = Double.POSITIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        final Iterator<IStreamObject<?>> iter = this.getObjects().iterator();
        while (iter.hasNext()) {
            final IStreamObject<?> element = iter.next();
            final double x = this.getX(element).doubleValue();
            final double y = this.getY(element).doubleValue();
            if (y > maxY) {
                maxY = y;
            }
            if (y < minY) {
                minY = y;
            }

            if (x > maxX) {
                maxX = x;
            }
            if (x < minX) {
                minX = x;
            }

        }
        this.setMinY(minY);
        this.setMaxY(maxY);
        this.setMinX(minX);
        this.setMaxX(maxX);
    }

    //@SuppressWarnings("static-method")
    private Tuple<?> getTuple(final IStreamObject<?> element) {
        return (Tuple<?>) element;
    }

    private Number getX(final IStreamObject<?> element) {
        final Tuple<?> tuple = this.getTuple(element);
        if ((tuple == null) || (tuple.getAttributes().length <= this.getXPos())) {
            return new Double(0.0);
        }
        return (Number) tuple.getAttribute(this.getXPos());
    }

    private Number getY(final IStreamObject<?> element) {
        final Tuple<?> tuple = this.getTuple(element);
        if ((tuple == null) || (tuple.getAttributes().length <= this.getYPos())) {
            return new Double(0.0);
        }
        return (Number) tuple.getAttribute(this.getYPos());
    }

    private Coordinate getCoordinate(final IStreamObject<?> element) {
        return new Coordinate(this.normalizeX(this.getX(element)).doubleValue(), this.normalizeY(this.getY(element)).doubleValue());
    }

    private Number normalizeX(final Number value) {
        if (value.doubleValue() > this.getMaxX()) {
            return new Double(this.getClipping().width);
        }
        if (value.doubleValue() < this.getMinX()) {
            return new Double(0.0);
        }
        return new Double(((value.doubleValue() - this.getMinX()) / Math.abs(this.getMaxX() - this.getMinX())) * this.getClipping().width);
    }

    private Number normalizeY(final Number value) {
        if (value.doubleValue() > this.getMaxY()) {
            return new Double(this.getClipping().height);
        }
        if (value.doubleValue() < this.getMinY()) {
            return new Double(0);
        }
        return new Double((((value.doubleValue() - this.getMinY()) / Math.abs(this.getMaxY() - this.getMinY()))) * this.getClipping().height);
    }

    /**
     * @return the minY
     */
    public double getMinY() {
        return this.minY;
    }

    /**
     * @param minY
     *            the minY to set
     */
    public void setMinY(final double minY) {
        this.minY = minY;
    }

    /**
     * @return the maxY
     */
    public double getMaxY() {
        return this.maxY;
    }

    /**
     * @param maxY
     *            the maxY to set
     */
    public void setMaxY(final double maxY) {
        this.maxY = maxY;
    }

    /**
     * @return the minX
     */
    public double getMinX() {
        return this.minX;
    }

    /**
     * @param minX
     *            the minX to set
     */
    public void setMinX(final double minX) {
        this.minX = minX;
    }

    /**
     * @return the maxX
     */
    public double getMaxX() {
        return this.maxX;
    }

    /**
     * @param maxX
     *            the maxX to set
     */
    public void setMaxX(final double maxX) {
        this.maxX = maxX;
    }

    /**
     * @return the auto adjust
     */
    public boolean isAutoadjust() {
        return this.autoadjust;
    }

    /**
     * @param autoadjust
     *            the auto adjust to set
     */
    public void setAutoadjust(final boolean autoadjust) {
        this.autoadjust = autoadjust;
    }

    /**
     * @return the xPos
     */
    public int getXPos() {
        return this.xPos;
    }

    /**
     * @param xPos
     *            the xPos to set
     */
    public void setXPos(final int xPos) {
        this.xPos = xPos;
    }

    /**
     * @return the yPos
     */
    public int getYPos() {
        return this.yPos;
    }

    /**
     * @param yPos
     *            the yPos to set
     */
    public void setYPos(final int yPos) {
        this.yPos = yPos;
    }

    /**
     * @return the backgroundColor
     */
    public RGB getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * @param backgroundColor
     *            the backgroundColor to set
     */
    public void setBackgroundColor(final RGB backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the backgroundAlpha
     */
    public int getBackgroundAlpha() {
        return backgroundAlpha;
    }

    /**
     * @param backgroundAlpha
     *            the backgroundAlpha to set
     */
    public void setBackgroundAlpha(int backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha;
    }

    /**
     * @return the foregroundColor
     */
    public RGB getForegroundColor() {
        return this.color;
    }

    /**
     * @param foregroundColor
     *            the foregroundColor to set
     */
    public void setForegroundColor(final RGB foregroundColor) {
        this.color = foregroundColor;
    }

    public static void main(final String[] args) {

        final Display display = new Display();
        final Shell shell = new Shell(display);

        shell.setSize(500, 500);

        shell.open();
        final VoronoiDashboardPart voronoi = new VoronoiDashboardPart();
        final Thread generator = new Thread() {
            /**
             * {@inheritDoc}
             */
            //@SuppressWarnings("boxing")
            @Override
            public void run() {
                final Random rnd = new Random();
                while (!this.isInterrupted()) {
                    @SuppressWarnings("rawtypes")
                    final Tuple tuple = new Tuple(3, false);
                    tuple.setAttribute(0, rnd.nextDouble() * 100);
                    tuple.setAttribute(1, rnd.nextDouble() * 100);
                    tuple.setAttribute(2, rnd.nextDouble() * 100);
                    voronoi.streamElementReceived(null, tuple, 0);

                    try {
                        Thread.sleep(1000);
                    }
                    catch (final InterruptedException e) {
                        // Empty block
                    }
                }
            }
        };
        voronoi.createPartControl(shell, null);
        generator.start();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        generator.interrupt();
        display.dispose();
    }

}
