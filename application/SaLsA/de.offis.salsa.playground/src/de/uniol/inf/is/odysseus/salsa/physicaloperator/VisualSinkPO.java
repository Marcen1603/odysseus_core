package de.uniol.inf.is.odysseus.salsa.physicaloperator;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.ui.Screen;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class VisualSinkPO extends AbstractSink<Object> {
    private final Queue<Geometry> segments = new ConcurrentLinkedQueue<Geometry>();
    Screen screen = new Screen();
    private final SDFAttributeList schema;

    private final Thread painter = new Thread() {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Geometry segment;
                while ((segment = VisualSinkPO.this.segments.poll()) != null) {
                    VisualSinkPO.this.screen.onFeature(segment);
                }
            }
        }
    };

    public VisualSinkPO(final SDFAttributeList schema) {
        this.schema = schema;
        this.screen.repaint();
        this.painter.start();
    }

    public VisualSinkPO(final VisualSinkPO po) {
        this.schema = po.schema;
        this.screen.repaint();
        this.painter.start();
    }

    @Override
    public VisualSinkPO clone() {
        return new VisualSinkPO(this);
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
