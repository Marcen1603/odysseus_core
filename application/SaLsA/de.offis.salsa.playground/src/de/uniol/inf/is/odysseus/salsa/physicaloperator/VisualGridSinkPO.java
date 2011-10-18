package de.uniol.inf.is.odysseus.salsa.physicaloperator;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.ui.GridScreen;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class VisualGridSinkPO extends AbstractSink<Object> {
    private final Queue<Double[][]> grids = new ConcurrentLinkedQueue<Double[][]>();
    private GridScreen screen = new GridScreen();
    private final SDFAttributeList schema;
    private final Thread painter = new Thread() {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Double[][] grid;
                while ((grid = VisualGridSinkPO.this.grids.poll()) != null) {
                    VisualGridSinkPO.this.screen.onGrid(grid);
                }
            }
        }
    };

    public VisualGridSinkPO(final SDFAttributeList schema) {
        this.schema = schema;
        this.screen.repaint();
        this.painter.start();
    }

    public VisualGridSinkPO(final VisualGridSinkPO po) {
        this.schema = po.schema;
        this.screen.repaint();
        this.painter.start();
    }

    @Override
    public VisualGridSinkPO clone() {
        return new VisualGridSinkPO(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final Object object, final int port, final boolean isReadOnly) {
        this.grids.offer((Double[][]) ((RelationalTuple<TimeInterval>) object).getAttribute(0));
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {

    }

}
