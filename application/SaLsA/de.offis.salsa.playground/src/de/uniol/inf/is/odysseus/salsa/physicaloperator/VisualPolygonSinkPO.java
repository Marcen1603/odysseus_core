package de.uniol.inf.is.odysseus.salsa.physicaloperator;


import java.util.concurrent.BlockingQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.ui.PolygonScreen;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class VisualPolygonSinkPO extends AbstractSink<Object> {

    private final BlockingQueue<Geometry> segments = new LinkedBlockingQueue<Geometry>();

    private PolygonScreen screen = new PolygonScreen();
    private final SDFAttributeList schema;

    private final Thread painter = new Thread() {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Geometry segment;
                try {
                    while ((segment = VisualPolygonSinkPO.this.segments.take()) != null) {
                        VisualPolygonSinkPO.this.screen.onFeature(segment);
                    }
                }
                catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }
    };

    public VisualPolygonSinkPO(final SDFAttributeList schema) {
        this.schema = schema;
        this.screen.repaint();
        this.painter.start();
    }

    public VisualPolygonSinkPO(final VisualPolygonSinkPO po) {
        this.schema = po.schema;
        this.screen.repaint();
        this.painter.start();
    }

    @Override
    public VisualPolygonSinkPO clone() {
        return new VisualPolygonSinkPO(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final Object object, final int port, final boolean isReadOnly) {
        this.segments.offer((Geometry) ((RelationalTuple<TimeInterval>) object).getAttribute(0));
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {

    }

}
