package de.uniol.inf.is.odysseus.salsa.physicaloperator;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.ui.Screen;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class VisualSinkPO<T extends IMetaAttribute> extends
        AbstractSink<RelationalTuple<TimeInterval>> {
    private final Queue<List<Coordinate>> segments = new ConcurrentLinkedQueue<List<Coordinate>>();
    Screen screen = new Screen();
    private final SDFAttributeList schema;

    private final Thread painter = new Thread() {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                List<Coordinate> segment;
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

    public VisualSinkPO(final VisualSinkPO<T> po) {
        this.schema = po.schema;
        this.screen.repaint();
        this.painter.start();
    }

    @Override
    public VisualSinkPO<T> clone() {
        return new VisualSinkPO<T>(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final RelationalTuple<TimeInterval> object, final int port,
            final boolean isReadOnly) {
        this.segments.offer((List<Coordinate>) object.getAttribute(0));
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {

    }

}
